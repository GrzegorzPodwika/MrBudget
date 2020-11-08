package pl.podwikagrzegorz.mrbudget.ui.expense

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.other.Constants

class ExpenseViewModel @ViewModelInject constructor(
    val budgetRepository: BudgetRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val budgetId = stateHandle.get<Long>(Constants.BUDGET_ID)

    private val _currentMoneyValue = MutableLiveData<Double>(5.0)
    val currentMoneyValue: LiveData<Double>
        get() = _currentMoneyValue

    val currentMoneyAsString: LiveData<String> =
        Transformations.map(currentMoneyValue) { value ->
            if (value == 0.0) {
                ""
            } else {
                value.toString()
            }
        }



}