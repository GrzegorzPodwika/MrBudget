package pl.podwikagrzegorz.mrbudget.other

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import android.view.View

@BindingAdapter("bindTabItem")
fun bindTabItemText(tabLayout: TabLayout, formattedDate: String?) {
    tabLayout.getTabAt(0)?.text = formattedDate
}

@BindingAdapter("fabVisibility")
fun fabVisibility(fab: FloatingActionButton, moneyValue: Double?) {
    if (moneyValue == null || moneyValue == 0.0) {
        fab.visibility = View.INVISIBLE
    } else {
        fab.visibility = View.VISIBLE
    }
}