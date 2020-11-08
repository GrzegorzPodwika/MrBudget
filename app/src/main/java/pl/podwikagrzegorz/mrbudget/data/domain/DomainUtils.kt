package pl.podwikagrzegorz.mrbudget.data.domain

import pl.podwikagrzegorz.mrbudget.data.database.DatabaseBudget

fun Budget.asDatabaseModel() : DatabaseBudget =
    DatabaseBudget(budgetId, date)