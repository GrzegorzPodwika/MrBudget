package pl.podwikagrzegorz.mrbudget.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import pl.podwikagrzegorz.mrbudget.BudgetApp
import pl.podwikagrzegorz.mrbudget.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val darkThemeSwitch : SwitchMaterial = view.findViewById(R.id.dark_theme_switch)
        val preferenceRepository = (requireActivity().application as BudgetApp).preferenceRepository

        preferenceRepository.isDarkThemeLive.observe(viewLifecycleOwner, {isDarkTheme ->
            isDarkTheme?.let { darkThemeSwitch.isChecked = it }
        })

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            preferenceRepository.isDarkTheme = checked
        }
    }

}