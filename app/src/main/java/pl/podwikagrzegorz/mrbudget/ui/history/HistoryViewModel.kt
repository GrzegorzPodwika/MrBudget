package pl.podwikagrzegorz.mrbudget.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.data.domain.BudgetWithExpensesAndIncomes
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.data.repo.DefaultBudgetRepository
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _budgetsWithExpensesAndIncomes = MutableLiveData<List<BudgetWithExpensesAndIncomes>>()
    val budgetsWithExpensesAndIncomes: LiveData<List<BudgetWithExpensesAndIncomes>>
        get() = _budgetsWithExpensesAndIncomes

    fun fetchFreshData() {
        fetchAllBudgetsFromDb()
    }

    init {
        fetchAllBudgetsFromDb()
    }

    private fun fetchAllBudgetsFromDb() =
        viewModelScope.launch {
            _budgetsWithExpensesAndIncomes.postValue(repository.getAllBudgets())
        }

}