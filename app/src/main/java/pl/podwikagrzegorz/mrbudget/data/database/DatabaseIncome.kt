package pl.podwikagrzegorz.mrbudget.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income_table")
data class DatabaseIncome constructor(
    @PrimaryKey(autoGenerate = true)
    val incomeId: Long,
    val budgetOwnerId: Long,
    val name: String,
    val value: Double
)