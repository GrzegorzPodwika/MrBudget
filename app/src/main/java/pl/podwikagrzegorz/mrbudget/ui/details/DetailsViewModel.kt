package pl.podwikagrzegorz.mrbudget.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {
    private val latestBudget = MutableLiveData<Budget>()

    private val _listOfExpenses = MutableLiveData<List<Expense>>()
    val listOfExpenses : LiveData<List<Expense>>
        get() = _listOfExpenses

    fun fetchFreshData() {
        fetchDataFromDb()
    }

    init {
        fetchDataFromDb()
    }

    private fun fetchDataFromDb() =
        viewModelScope.launch {
            latestBudget.value = repository.getLatestBudget()

            fetchListOfExpensesFromDb()
        }

    fun deleteExpense(expenseToDelete: Expense) =
        viewModelScope.launch {
            repository.deleteExpense(expenseToDelete)

            fetchListOfExpensesFromDb()
        }

    fun updateExpense(updatedExpense: Expense) =
        viewModelScope.launch {
            repository.updateExpense(updatedExpense)

            fetchListOfExpensesFromDb()
        }

    private suspend fun fetchListOfExpensesFromDb() {
        _listOfExpenses.postValue(repository.getListOfExpenses(latestBudget.value!!.budgetId))
    }

}