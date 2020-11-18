package pl.podwikagrzegorz.mrbudget.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _isAddedExpenseOrIncome = MutableLiveData<Boolean>()
    val isAddedExpenseOrIncome : LiveData<Boolean>
        get() = _isAddedExpenseOrIncome

    fun changeFlagToSuccess(){
        _isAddedExpenseOrIncome.value = true
    }

    fun refreshBarChartComplete(){
        _isAddedExpenseOrIncome.value = false
    }
}