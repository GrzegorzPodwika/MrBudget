package pl.podwikagrzegorz.mrbudget.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.mrbudget.data.domain.Expense
import pl.podwikagrzegorz.mrbudget.databinding.EditExpenseDialogBinding
import timber.log.Timber

class EditExpenseDialog(
    private val expenseToEdit: Expense,
    private val listener: EditExpenseListener
) : DialogFragment() {
    private lateinit var binding: EditExpenseDialogBinding

    fun interface EditExpenseListener {
        fun onChangedExpense(updatedExpense: Expense)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditExpenseDialogBinding.inflate(inflater, container, false)

        setUpViewsWithCurrentValues()
        setUpButtonListeners()
        return binding.root
    }

    private fun setUpViewsWithCurrentValues() {
        binding.apply {
            textViewTypeName.text = expenseToEdit.type.name
            editTextExpenseNameEdit.setText(expenseToEdit.name)
            editTextAmountOfMoney.setText(expenseToEdit.value.toString())
        }
    }

    private fun setUpButtonListeners() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonConfirm.setOnClickListener {
            val newName: String = if (!binding.editTextExpenseNameEdit.text.isNullOrEmpty()) {
                binding.editTextExpenseNameEdit.text.toString()
            } else {
                expenseToEdit.name
            }

            val newAmount: Double = if (!binding.editTextAmountOfMoney.text.isNullOrEmpty()) {
                try {
                    binding.editTextAmountOfMoney.text.toString().toDouble()
                } catch (e: NumberFormatException) {
                    Timber.e(e)
                    expenseToEdit.value
                }
            } else {
                expenseToEdit.value
            }


            listener.onChangedExpense(
                Expense(
                    expenseToEdit.expenseId,
                    expenseToEdit.budgetOwnerId,
                    newName,
                    expenseToEdit.type,
                    newAmount
                )
            )

            dismiss()
        }
    }

}