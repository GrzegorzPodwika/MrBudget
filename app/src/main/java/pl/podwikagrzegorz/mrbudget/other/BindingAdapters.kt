package pl.podwikagrzegorz.mrbudget.other

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import android.view.View
import android.widget.TextView
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import timber.log.Timber
import java.util.*
import kotlin.math.exp
import kotlin.math.round

@BindingAdapter("bindTabItem")
fun bindTabItemText(tabLayout: TabLayout, formattedDate: String?) {
    tabLayout.getTabAt(0)?.text = formattedDate
}

@BindingAdapter("fabVisibility")
fun fabVisibility(fab: FloatingActionButton, moneyValue: String?) {
    if (moneyValue.isNullOrEmpty()) {
        fab.visibility = View.INVISIBLE
    } else {
        fab.visibility = View.VISIBLE
    }
}

@BindingAdapter("textExpense")
fun textExpense(textView: TextView, expense: Double) {
    val formattedExpense = String.format("- PLN %.2f", expense)
    textView.text = formattedExpense
}

@BindingAdapter("textIncome")
fun textIncome(textView: TextView, income: Double) {
    val formattedIncome = String.format("+ PLN %.2f", income)
    textView.text = formattedIncome
}

@BindingAdapter("textWithPLNSuffix")
fun textWithPLNSuffix(textView: TextView, expense: Double) {
    val formattedText = String.format("- %.2f PLN", expense)
    textView.text = formattedText
}

@BindingAdapter("textExpenseType")
fun textExpenseType(textView: TextView, expenseType: ExpenseType) {
    val country = Locale.getDefault().country

    if (country == "PL") {
        textView.text = expenseType.asPLName()
    } else {
        textView.text = expenseType.name
    }
}