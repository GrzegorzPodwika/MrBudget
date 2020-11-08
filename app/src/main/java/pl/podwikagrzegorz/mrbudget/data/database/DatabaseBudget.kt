package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "budget_table")
data class DatabaseBudget constructor(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Long,
    val date: Date
)

