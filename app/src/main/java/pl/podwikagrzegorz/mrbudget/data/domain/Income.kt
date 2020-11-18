package pl.podwikagrzegorz.mrbudget.data.domain

data class Income(
    val incomeId: Long,
    val budgetOwnerId: Long,
    val value: Double
)