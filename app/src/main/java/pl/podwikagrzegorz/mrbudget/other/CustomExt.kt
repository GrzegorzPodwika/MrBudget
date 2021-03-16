package pl.podwikagrzegorz.mrbudget.other

import pl.podwikagrzegorz.mrbudget.BudgetApp
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import java.lang.StringBuilder
import java.util.*
import android.content.Context
import android.content.res.Resources

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
        ExpenseType.GROCERIES -> "Zakupy"
        ExpenseType.TRANSPORT -> "Transport"
        ExpenseType.HEALTH -> "Zdrowie"
        ExpenseType.FAMILY -> "Rodzina"
        ExpenseType.GIFTS -> "Prezenty"
        ExpenseType.EDUCATION -> "Edukacja"
        ExpenseType.HOME -> "Dom"
        ExpenseType.HOBBY -> "Hobby"
    }
}

fun Date.isTheSameMonth(that: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val thisMonth = calendar.get(Calendar.MONTH)

    calendar.time = that

    val thatMonth = calendar.get(Calendar.MONTH)

    return thisMonth == thatMonth
}



fun Context.resIdByName(resIdName: String?, resType: String): Int {
    resIdName?.let {
        return resources.getIdentifier(it, resType, packageName)
    }
    throw Resources.NotFoundException()
}