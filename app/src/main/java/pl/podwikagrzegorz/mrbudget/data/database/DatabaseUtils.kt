package pl.podwikagrzegorz.mrbudget.data.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.mrbudget.data.domain.*

fun List<DatabaseExpense>.asListOfExpenses(): List<Expense> {
    return map {
        Expense(
            expenseId = it.expenseId,
            budgetOwnerId = it.budgetOwnerId,
            type = ExpenseType.valueOf(it.type),
            value = it.value
        )
    }
}

fun List<DatabaseIncome>.asListOfIncomes(): List<Income> {
    return map {
        Income(
            incomeId = it.incomeId,
            budgetOwnerId = it.budgetOwnerId,
            value = it.value
        )
    }
}

suspend fun List<DatabaseExpense>.reverseAndConvertToDomainModel(): List<Expense> {
    val listOfDatabaseExpense = this
    return withContext(Dispatchers.IO) {
        val result = mutableListOf<Expense>()
        for (index in (size-1) downTo 0) {
            result.add(Expense(listOfDatabaseExpense[index].expenseId,
                listOfDatabaseExpense[index].budgetOwnerId,
                ExpenseType.valueOf(listOfDatabaseExpense[index].type),
                listOfDatabaseExpense[index].value))
        }

        result
    }
}

fun DatabaseBudget.asDomainModel(): Budget =
    Budget(budgetId, date)


fun DatabaseExpense.asDomainModel(): Expense =
    Expense(expenseId, budgetOwnerId, ExpenseType.valueOf(type), value)


fun DatabaseIncome.asDomainModel(): Income =
    Income(incomeId, budgetOwnerId, value)