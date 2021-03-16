package pl.podwikagrzegorz.mrbudget.ui.history

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
import pl.podwikagrzegorz.mrbudget.other.Constants
import pl.podwikagrzegorz.mrbudget.other.MyPercentFormatter
import pl.podwikagrzegorz.mrbudget.other.asPLName
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val budgetId = stateHandle.get<Long>(Constants.BUDGET_ID)!!
    private lateinit var budget: Budget
    private val dtf =
        SimpleDateFormat("MMM yyyy", Locale.getDefault())
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
    private lateinit var budgetWithExpenses: BudgetWithExpenses


    private val _budgetDate = MutableLiveData<String>()
    val budgetDate: LiveData<String>
        get() = _budgetDate

    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double>
        get() = _totalExpenses

    private val _totalIncomes = MutableLiveData<Double>()
    val totalIncomes: LiveData<Double>
        get() = _totalIncomes

    private val _expensesPieData = MutableLiveData<PieData>()
    val expensesPieData: LiveData<PieData>
        get() = _expensesPieData

    init {
        viewModelScope.launch {
            fetchBudget()
            fetchAllExpenses()
            fetchAllIncomes()

            transformRawDataIntoPieData()
        }
    }

    private suspend fun fetchBudget() {
        budget = repository.getBudgetById(budgetId)

        _budgetDate.postValue(dtf.format(budget.date))
    }

    private suspend fun fetchAllExpenses() {
        budgetWithExpenses = repository.getBudgetWithExpenses(budgetId)
        val totalExpenses = budgetWithExpenses.expenses
            .stream()
            .mapToDouble(DatabaseExpense::value)
            .sum()

        _totalExpenses.postValue(totalExpenses)

    }

    private suspend fun fetchAllIncomes() {
        val budgetWithIncomes = repository.getBudgetWithIncomes(budgetId)
        val totalIncomes = budgetWithIncomes.incomes
            .stream()
            .mapToDouble(DatabaseIncome::value)
            .sum()

        _totalIncomes.postValue(totalIncomes)
    }

    private suspend fun transformRawDataIntoPieData() {
        withContext(Dispatchers.Default) {
            val entries = mutableListOf<PieEntry>()


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

            _expensesPieData.postValue(PieData(dataSet))
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
