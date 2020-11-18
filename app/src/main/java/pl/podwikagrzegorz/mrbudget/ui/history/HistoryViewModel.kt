package pl.podwikagrzegorz.mrbudget.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.data.domain.BudgetWithExpensesAndIncomes
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository

class HistoryViewModel @ViewModelInject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _budgetsWithExpensesAndIncomes = MutableLiveData<List<BudgetWithExpensesAndIncomes>>()
    val budgetsWithExpensesAndIncomes: LiveData<List<BudgetWithExpensesAndIncomes>>
        get() = _budgetsWithExpensesAndIncomes


    init {
        fetchAllBudgetsFromDb()
    }

    private fun fetchAllBudgetsFromDb() =
        viewModelScope.launch {
            _budgetsWithExpensesAndIncomes.postValue(repository.getAllBudgets())
        }
}