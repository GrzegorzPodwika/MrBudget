package pl.podwikagrzegorz.mrbudget.other

import pl.podwikagrzegorz.mrbudget.BudgetApp
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import java.lang.StringBuilder
import java.util.*

fun StringBuilder.deleteLast() {
    if (length > 0) {
        deleteCharAt(length - 1)
    }
}

fun Double.asExpenseString() : String =
    String.format("- PLN %.2f", this)

fun Double.asIncomeString() : String =
    String.format("+ PLN %.2f", this)

fun Date.asMonthWithYear() : String {
    val res = BudgetApp.res
    val months = res.getStringArray(R.array.months)
    val calendar = Calendar.getInstance()
    calendar.time = this
    val receivedMonthAsNumb = calendar.get(Calendar.MONTH)

    return months[receivedMonthAsNumb] + " " +calendar.get(Calendar.YEAR)
}

fun ExpenseType.asPLName() : String {
    return when(this) {
        ExpenseType.REGULAR -> "Regularny"
        ExpenseType.ONE_OFF -> "Jednorazowy"
        ExpenseType.SAVINGS -> "Oszczędności"
        ExpenseType.RETIREMENT -> "Emerytura"
    }
}