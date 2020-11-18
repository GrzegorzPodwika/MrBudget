package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithExpenses
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseExpense
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseIncome
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.other.MyPercentFormatter
import pl.podwikagrzegorz.mrbudget.other.asPLName
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
class TransactionViewModel @ViewModelInject constructor(
    private val repository: BudgetRepository,
) : ViewModel() {

    private val dtf =
        SimpleDateFormat("MMM yyyy", Locale("pl"))
    private val setOfColors = mutableListOf<Int>(
        Color.rgb(176, 0, 32),  // red - regular
        Color.rgb(55, 0 ,179),  // blue - one-off
        Color.rgb(10, 127, 88), // green - savings
        Color.rgb(208, 115, 23) // brown - ret
    )

    private lateinit var latestBudget: Budget

    private val _budgetDate = MutableLiveData<String>()
    val budgetDate: LiveData<String>
        get() = _budgetDate

    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double>
        get() = _totalExpenses

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double>
        get() = _totalIncome

    private val _budgetWithExpenses = MutableLiveData<BudgetWithExpenses>()


    private val _expensesPieData = MutableLiveData<PieData>()
    val expensesPieData : LiveData<PieData>
        get() = _expensesPieData

    fun fetchFreshData() =
        viewModelScope.launch {
            Timber.i("Fetching fresh data")
            fetchDataFromDb()
        }

    init {
        fetchLatestBudgetFromDb()
    }

    private fun fetchLatestBudgetFromDb() =
        viewModelScope.launch {
            latestBudget = repository.getLatestBudget()
            _budgetDate.postValue(dtf.format(latestBudget.date))

            fetchDataFromDb()
        }

    private suspend fun fetchDataFromDb() {
        _budgetWithExpenses.value = repository.getBudgetWithExpenses(latestBudget.budgetId)
        val totalExpenses = _budgetWithExpenses.value!!.expenses
            .stream()
            .mapToDouble(DatabaseExpense::value)
            .sum()

        _totalExpenses.postValue(totalExpenses)


        val budgetWithIncomes = repository.getBudgetWithIncomes(latestBudget.budgetId)
        val totalIncomes = budgetWithIncomes.incomes
            .stream()
            .mapToDouble(DatabaseIncome::value)
            .sum()

        _totalIncome.postValue(totalIncomes)

        _expensesPieData.postValue(transformIntoPieData())
    }

    private suspend fun transformIntoPieData(): PieData {
        return withContext(Dispatchers.Default) {

            val entries = mutableListOf<PieEntry>()
            val budgetWithExpenses = _budgetWithExpenses.value!!
            val country = Locale.getDefault().country

            val countRegularExpenses = budgetWithExpenses.expenses.stream()
                .filter {it.type == ExpenseType.REGULAR.name}
                .mapToDouble(DatabaseExpense::value)
                .sum()

            val countOneOffExpenses = budgetWithExpenses.expenses.stream()
                .filter {it.type == ExpenseType.ONE_OFF.name}
                .mapToDouble(DatabaseExpense::value)
                .sum()

            val countSavingExpenses = budgetWithExpenses.expenses.stream()
                .filter {it.type == ExpenseType.SAVINGS.name}
                .mapToDouble(DatabaseExpense::value)
                .sum()

            val countRetirementExpenses = budgetWithExpenses.expenses.stream()
                .filter {it.type == ExpenseType.RETIREMENT.name}
                .mapToDouble(DatabaseExpense::value)
                .sum()

            if (country == "PL"){
                entries.add(PieEntry(countRegularExpenses.toFloat(), ExpenseType.REGULAR.asPLName()))
                entries.add(PieEntry(countOneOffExpenses.toFloat(), ExpenseType.ONE_OFF.asPLName()))
                entries.add(PieEntry(countSavingExpenses.toFloat(), ExpenseType.SAVINGS.asPLName()))
                entries.add(PieEntry(countRetirementExpenses.toFloat(), ExpenseType.RETIREMENT.asPLName()))
            } else {
                entries.add(PieEntry(countRegularExpenses.toFloat(), ExpenseType.REGULAR.name))
                entries.add(PieEntry(countOneOffExpenses.toFloat(), ExpenseType.ONE_OFF))
                entries.add(PieEntry(countSavingExpenses.toFloat(), ExpenseType.SAVINGS))
                entries.add(PieEntry(countRetirementExpenses.toFloat(), ExpenseType.RETIREMENT))
            }

            val dataSet = PieDataSet(entries, "")

            dataSet.apply {
                colors = setOfColors
                sliceSpace = 2f
                valueTextColor = Color.WHITE
                valueTextSize = 14f
                valueFormatter = MyPercentFormatter()
            }

            PieData(dataSet)
        }
    }
}

/*
            val totalExpenses = budgetWithExpenses.expenses
                .stream()
                .mapToDouble(DatabaseExpense::value)
                .sum()

val regularExpensesPercentage = calculatePercentageOfExpense(countRegularExpenses, totalExpenses)
            val oneOffExpensesPercentage = calculatePercentageOfExpense(countOneOffExpenses, totalExpenses)
            val savingExpensesPercentage = calculatePercentageOfExpense(countSavingExpenses, totalExpenses)
            val retirementExpensesPercentage = calculatePercentageOfExpense(countRetirementExpenses, totalExpenses)

            entries.add(PieEntry(regularExpensesPercentage, ExpenseType.REGULAR.name))
            entries.add(PieEntry(oneOffExpensesPercentage, ExpenseType.ONE_OFF.name))
            entries.add(PieEntry(savingExpensesPercentage, ExpenseType.SAVINGS.name))
            entries.add(PieEntry(retirementExpensesPercentage, ExpenseType.RETIREMENT.name))


    private fun calculatePercentageOfExpense(expense: Double, totalExpense: Double) : Float =
        (expense * 100 / totalExpense).toFloat()
       */