package pl.podwikagrzegorz.mrbudget.data.domain

import pl.podwikagrzegorz.mrbudget.data.database.DatabaseBudget
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseExpense
import pl.podwikagrzegorz.mrbudget.data.database.DatabaseIncome

fun Budget.asDatabaseModel() : DatabaseBudget =
    DatabaseBudget(budgetId, date)

fun Expense.asDatabaseModel() : DatabaseExpense =
    DatabaseExpense(expenseId, budgetOwnerId, name, type.name, value)

fun Income.asDatabaseModel() : DatabaseIncome =
    DatabaseIncome(incomeId, budgetOwnerId, name,  value)

