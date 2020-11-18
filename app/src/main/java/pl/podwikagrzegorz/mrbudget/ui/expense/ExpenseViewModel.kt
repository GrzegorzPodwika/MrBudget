package pl.podwikagrzegorz.mrbudget.ui.expense

import androidx.databinding.Bindable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.BR
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.data.domain.Income
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.other.Constants
import pl.podwikagrzegorz.mrbudget.other.deleteLast
import timber.log.Timber
import java.lang.StringBuilder

class ExpenseViewModel @ViewModelInject constructor(
    private val budgetRepository: BudgetRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ObservableViewModel() {
    private val budgetId = stateHandle.get<Long>(Constants.BUDGET_ID)!!

    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess : LiveData<Boolean>
        get() = _insertSuccess

    private val moneySB: StringBuilder = StringBuilder()

    @get:Bindable
    var moneyAmountAsString: String = moneySB.toString()
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.moneyAmountAsString)
            }
        }

    fun onZeroClick() {
        moneySB.append(0)
        refreshMoneyValue()
    }

    fun onOneClick() {
        moneySB.append(1)
        refreshMoneyValue()
    }

    fun onTwoClick() {
        moneySB.append(2)
        refreshMoneyValue()
    }


    fun onThreeClick() {
        moneySB.append(3)
        refreshMoneyValue()
    }


    fun onFourClick() {
        moneySB.append(4)
        refreshMoneyValue()
    }


    fun onFiveClick() {
        moneySB.append(5)
        refreshMoneyValue()
    }


    fun onSixClick() {
        moneySB.append(6)
        refreshMoneyValue()
    }

    fun onSevenClick() {
        moneySB.append(7)
        refreshMoneyValue()
    }

    fun onEightClick() {
        moneySB.append(8)
        refreshMoneyValue()
    }

    fun onNineClick() {
        moneySB.append(9)
        refreshMoneyValue()
    }

    fun onDotClick() {
        if(moneySB.isNotEmpty()) {
            if (moneySB.last() != '.' && !moneySB.contains('.')) {
                moneySB.append('.')
                refreshMoneyValue()
            }
        }
    }

    fun onClearClick() {
        moneySB.deleteLast()
        refreshMoneyValue()
    }

    fun clearCompletelyMoney() {
        moneySB.clear()
        refreshMoneyValue()
    }

    private fun refreshMoneyValue() {
        moneyAmountAsString = moneySB.toString()
    }

    fun onInsertComplete() {
        _insertSuccess.value = false
    }


    fun addExpense(expenseType: ExpenseType) : Boolean{
        return if(userAmountOfMoneyIsCorrect()) {
            val newExpense = Expense(0, budgetId, expenseType, moneyAmountAsString.toDouble())
            viewModelScope.launch{
                budgetRepository.insertExpense(newExpense)
                _insertSuccess.postValue(true)
            }
            true
        } else
            false
    }

    fun addIncome() : Boolean {
        return if(userAmountOfMoneyIsCorrect()) {
            val newIncome = Income(0, budgetId, moneyAmountAsString.toDouble())
            viewModelScope.launch{
                budgetRepository.insertIncome(newIncome)
                _insertSuccess.postValue(true)
            }
            true
        } else
            false
    }

    private fun userAmountOfMoneyIsCorrect(): Boolean {
        return try {
            val amount = moneyAmountAsString.toDouble()
            true
        } catch (e: NumberFormatException) {
            Timber.e(e, "Given sentence is not a value")
            false
        }
    }

}