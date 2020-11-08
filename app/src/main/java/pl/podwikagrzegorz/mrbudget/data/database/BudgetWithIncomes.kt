package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithIncomes(
    @Embedded val budget: DatabaseBudget,
    @Relation(
        parentColumn = "budgetId",
        entityColumn = "budgetOwnerId"
    )
    val incomes: List<DatabaseIncome>
)