package pl.podwikagrzegorz.mrbudget.ui.transactions

import android.graphics.Color
import androidx.lifecycle.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.database.BudgetWithExpenses
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseExpense
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseIncome
import pl.podwikagrzegorz.mrbudget.data.domain.Budget
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.data.repo.BudgetRepository
import pl.podwikagrzegorz.mrbudget.data.repo.DefaultBudgetRepository
import pl.podwikagrzegorz.mrbudget.other.MyPercentFormatter
import pl.podwikagrzegorz.mrbudget.other.asPLName
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: BudgetRepository,
) : ViewModel() {

    private val dtf =
        SimpleDateFormat("MMM yyyy", Locale("pl"))
    private val setOfColors = mutableListOf<Int>(
        Color.rgb(176, 0, 32),  // red - regular
        Color.rgb(55, 0, 179),  // blue - one-off
        Color.rgb(10, 127, 88), // green - savings
        Color.rgb(208, 115, 23), // brown - ret
        Color.rgb(255, 102, 0), // orange
        Color.rgb(245, 199, 0), // dark yellow
        Color.rgb(52, 183, 192), // turquoise
        Color.rgb(133, 54, 186) // purple
    )

    private val _latestBudget = MutableLiveData<Budget>()
    val latestBudget: LiveData<Budget>
        get() = _latestBudget

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
    val expensesPieData: LiveData<PieData>
        get() = _expensesPieData

    fun fetchFreshData() =
        viewModelScope.launch {
            Timber.i("Fetching fresh data")
            _latestBudget.value = repository.getLatestBudget()
            fetchDataFromDb()
        }

    init {
        viewModelScope.launch {
            checkIfNewBudgetShouldBeAdded()
            fetchLatestBudgetFromDb()
        }
    }

    private suspend fun checkIfNewBudgetShouldBeAdded() {

        val budgetsCount = repository.getBudgetsCount()

        Timber.i("BudgetsCount = $budgetsCount")

        if (budgetsCount == 0) {
            Timber.i("Inserting first budget...")
            repository.insertBudget(Budget(0, Date()))
        } else {
            val lastBudget = repository.getLatestBudget()
            Timber.i("Lastbudget = $lastBudget")
            Timber.i("Checking two budgets...")
            if (currentMonthDiffersFromLastBudget(Date(), lastBudget.date)) {
                repository.insertBudget(Budget(0, Date()))
            }
        }

    }

    private fun currentMonthDiffersFromLastBudget(now: Date, lastBudgetDate: Date): Boolean {
        val calendar = Calendar.getInstance()

        calendar.time = now
        val nowMonth = calendar.get(Calendar.MONTH)

        calendar.time = lastBudgetDate
        val lastBudgetMonth = calendar.get(Calendar.MONTH)

        return nowMonth != lastBudgetMonth
    }


    private suspend fun fetchLatestBudgetFromDb() {
        Timber.i("Method fetchLatestBudgetFromDb")

        _latestBudget.value = repository.getLatestBudget()
        Timber.i("latestbudget = ${latestBudget.value}")
        _budgetDate.postValue(dtf.format(_latestBudget.value!!.date))

        fetchDataFromDb()
    }

    private suspend fun fetchDataFromDb() {
        _budgetWithExpenses.value = repository.getBudgetWithExpenses(_latestBudget.value!!.budgetId)
        val totalExpenses = _budgetWithExpenses.value!!.expenses
            .stream()
            .mapToDouble(DatabaseExpense::value)
            .sum()

        _totalExpenses.postValue(totalExpenses)


        val budgetWithIncomes = repository.getBudgetWithIncomes(_latestBudget.value!!.budgetId)
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

            ExpenseType.values().forEach { expenseType ->
                val countSpecificExpenseType = budgetWithExpenses.expenses.stream()
                    .filter { it.type == expenseType.name }
                    .mapToDouble(DatabaseExpense::value)
                    .sum()

                addPieEntry(entries, countSpecificExpenseType, expenseType)
            }


            val dataSet = PieDataSet(entries, "")

            //colors = setOfColors
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