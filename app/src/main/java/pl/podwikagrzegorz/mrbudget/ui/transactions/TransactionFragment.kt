package pl.podwikagrzegorz.mrbudget.ui.transactions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.R
import pl.podwikagrzegorz.mrbudget.databinding.TransactionFragmentBinding
import pl.podwikagrzegorz.mrbudget.other.Constants
import timber.log.Timber

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionFragmentBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            transactionViewModel = viewModel
        }
    }

/*    companion object {
        fun newInstance(budgetId: Long) : TransactionFragment {
            val fragment = TransactionFragment()
            fragment.arguments = bundleOf(
                Constants.BUDGET_ID to budgetId
            )
            return fragment
        }
    }*/

}