package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class DatabaseExpense constructor(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long,
    val budgetOwnerId: Long,
    val name: String,
    val type: String,
    val value: Double
)

