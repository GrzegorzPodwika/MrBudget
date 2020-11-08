package pl.podwikagrzegorz.mrbudget.ui.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.mrbudget.databinding.ExpenseFragmentBinding
import timber.log.Timber

@AndroidEntryPoint
class ExpenseFragment : Fragment() {

    private lateinit var binding: ExpenseFragmentBinding
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseFragmentBinding.inflate(inflater, container, false)

        setupBindingWithViewModel()
        setOnTabSelectedListener()

        return binding.root
    }

    private fun setupBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            expenseViewModel = viewModel
        }
    }

    private fun setOnTabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                position?.let {
                    if (position == 0) {
                        binding.radioGroupExpenses.visibility = View.VISIBLE
                    } else {
                        binding.radioGroupExpenses.visibility = View.INVISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}