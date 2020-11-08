package pl.podwikagrzegorz.mrbudget.ui.transactions

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel @ViewModelInject constructor(
    private val repository: BudgetRepository,
) : ViewModel() {

    private val _latestBudget = MutableLiveData<Budget>()
    val latestBudget: LiveData<Budget>
        get() = _latestBudget

    val budgetDate = Transformations.map(latestBudget) {
        val dtf = SimpleDateFormat("MMM yyyy", Locale("pl"))
        dtf.format(it.date)
    }
    val dat = "September 2020"

    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double>
        get() = _totalExpenses

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double>
        get() = _totalIncome

    init {
        fetchLatestBudgetFromDb()
    }

    private fun fetchLatestBudgetFromDb() =
        viewModelScope.launch {
            _latestBudget.postValue(repository.getLatestBudget())
            //_totalExpenses.postValue(repository.getSumOfExpenses(budgetId))
        }
}