package pl.podwikagrzegorz.mrbudget.other

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyPercentFormatter : ValueFormatter() {
    val format: DecimalFormat = DecimalFormat("###,###,##0.0")

    override fun getFormattedValue(value: Float): String {
        return format.format(value.toDouble()) + " %"
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String =
        getFormattedValue(value)
}