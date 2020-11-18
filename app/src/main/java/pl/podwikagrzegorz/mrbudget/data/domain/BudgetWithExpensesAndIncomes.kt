package pl.podwikagrzegorz.mrbudget.data.domain

data class BudgetWithExpensesAndIncomes(
    val budget: Budget,
    val expenses: List<Expense>,
    val incomes: List<Income>
)