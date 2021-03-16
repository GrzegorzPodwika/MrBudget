package pl.podwikagrzegorz.mrbudget.ui.adapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.mrbudget.data.domain.ExpenseType
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseTypeHolderBinding
import java.util.*

class ExpenseTypeAdapter(
    private val images: List<Drawable>,
    private val listener: OnSelectExpenseTypeListener
) : RecyclerView.Adapter<ExpenseTypeAdapter.ExpenseTypeHolder>() {

    private val listOfExpenseType = ExpenseType.values()
    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseTypeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExpenseTypeHolderBinding.inflate(layoutInflater, parent, false)

        return ExpenseTypeHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseTypeHolder, position: Int) {
        holder.bind(images[position], listOfExpenseType[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ExpenseTypeHolder(private val binding: ExpenseTypeHolderBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Drawable, expenseType: ExpenseType) {
            binding.imageViewExpenseType.setImageDrawable(image)

            if (checkedPosition == -1)
                binding.imageViewExpenseType.background = null
            else {
                if (checkedPosition == bindingAdapterPosition) {
                    binding.imageViewExpenseType.setBackgroundColor(GRAY)
                } else {
                    binding.imageViewExpenseType.background = null
                }
            }
            binding.imageViewExpenseType.setOnClickListener {
                listener.onTypeClick(expenseType)
                it.setBackgroundColor(GRAY)
                if (checkedPosition != bindingAdapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = bindingAdapterPosition
                }
            }
        }


    }


    companion object {
        private val GRAY = Color.rgb(228,228,228)
    }
}
