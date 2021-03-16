package pl.podwikagrzegorz.mrbudget.ui.expense

import androidx.databinding.Bindable
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.mrbudget.BR
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.data.domain.Income
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.data.repo.DefaultBudgetRepository
import pl.podwikagrzegorz.mrbudget.other.Constants
import pl.podwikagrzegorz.mrbudget.other.deleteLast
import timber.log.Timber
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val stateHandle: SavedStateHandle
) : ObservableViewModel() {
    private val budgetId = stateHandle.get<Long>(Constants.BUDGET_ID)!!

    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean>
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
        if (isAppendingCorrect(0)) {
            moneySB.append(0)
            refreshMoneyValue()
        }
    }

    private fun isAppendingCorrect(value: Int): Boolean {
        val currentAmount = moneySB.toString()
        if (currentAmount.isNotEmpty()) {
            if (currentAmount.contains('.')) {
                val dotPosition = currentAmount.indexOf('.')
                val sub = currentAmount.substring(dotPosition) + value

                if (sub.length > 3)
                    return false
            }
        }

        return true
    }

    fun onOneClick() {
        if (isAppendingCorrect(1)) {
            moneySB.append(1)
            refreshMoneyValue()
        }
    }

    fun onTwoClick() {
        if (isAppendingCorrect(2)) {
            moneySB.append(2)
            refreshMoneyValue()
        }
    }


    fun onThreeClick() {
        if (isAppendingCorrect(3)) {
            moneySB.append(3)
            refreshMoneyValue()
        }
    }


    fun onFourClick() {
        if (isAppendingCorrect(4)) {
            moneySB.append(4)
            refreshMoneyValue()
        }
    }


    fun onFiveClick() {
        if (isAppendingCorrect(5)) {
            moneySB.append(5)
            refreshMoneyValue()
        }
    }


    fun onSixClick() {
        if (isAppendingCorrect(6)) {
            moneySB.append(6)
            refreshMoneyValue()
        }
    }

    fun onSevenClick() {
        if (isAppendingCorrect(7)) {
            moneySB.append(7)
            refreshMoneyValue()
        }
    }

    fun onEightClick() {
        if (isAppendingCorrect(8)) {
            moneySB.append(8)
            refreshMoneyValue()
        }
    }

    fun onNineClick() {
        if (isAppendingCorrect(9)) {
            moneySB.append(9)
            refreshMoneyValue()
        }
    }

    fun onDotClick() {
        if (moneySB.isNotEmpty()) {
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


    fun addExpense(expenseType: ExpenseType, name: String): Boolean {
        return if (userAmountOfMoneyIsCorrect()) {
            val newExpense = Expense(0, budgetId, name, expenseType, moneyAmountAsString.toDouble())
            viewModelScope.launch {
                budgetRepository.insertExpense(newExpense)
                _insertSuccess.postValue(true)
            }
            true
        } else
            false
    }

    fun addIncome(name: String): Boolean {
        return if (userAmountOfMoneyIsCorrect()) {
            val newIncome = Income(0, budgetId, name, moneyAmountAsString.toDouble())
            viewModelScope.launch {
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