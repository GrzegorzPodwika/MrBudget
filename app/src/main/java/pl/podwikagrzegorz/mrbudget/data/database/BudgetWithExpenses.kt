package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithExpenses(
    @Embedded val budget: DatabaseBudget,
    @Relation(
        parentColumn = "budgetId",
        entityColumn = "budgetOwnerId"
    )
    val expenses: List<DatabaseExpense>
)