package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.graphics.Color
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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

            addPieEntry(entries, countRegularExpenses, ExpenseType.REGULAR)
            addPieEntry(entries, countOneOffExpenses, ExpenseType.ONE_OFF)
            addPieEntry(entries, countSavingExpenses, ExpenseType.SAVINGS)
            addPieEntry(entries, countRetirementExpenses, ExpenseType.RETIREMENT)

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

    private fun addPieEntry(
        entries: MutableList<PieEntry>,
        countExpenses: Double,
        expenseType: ExpenseType,
    ) {
        val country = Locale.getDefault().country

        if (countExpenses != 0.0) {
            if (country == "PL")
                entries.add(PieEntry(countExpenses.toFloat(), expenseType.asPLName()))
            else
                entries.add(PieEntry(countExpenses.toFloat(), expenseType.name))
        }
    }
}