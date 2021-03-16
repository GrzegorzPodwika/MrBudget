package pl.podwikagrzegorz.mrbudget.data.domain

data class Expense(
    val expenseId: Long,
    val budgetOwnerId: Long,
    val name: String,
    val type: ExpenseType,
    val value: Double
)