package com.github.skytoph.simpleweather.presentation.settings

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.presentation.favorites.ConfirmationDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        setupLanguagePreference()
        setupTimeFormatPreference()
        setupClearHistoryPreference()
        setupAboutPreference()
    }

    private fun setupLanguagePreference() {
        val preference = findPreference<ListPreference>(getString(R.string.key_language))
        if (preference?.value == null) {
            val language = Locale.getDefault().language
            val indexOfValue = preference?.findIndexOfValue(language).takeIf { it != -1 } ?: 0
            preference?.setValueIndex(indexOfValue)
        }
    }

    private fun setupTimeFormatPreference() {
        val key = getString(R.string.key_time)
        val preference = findPreference<SwitchPreferenceCompat>(key)
        val default = DateFormat.is24HourFormat(context)
        val value = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }?.getBoolean(key, default)
        preference?.setDefaultValue(value)
        preference?.isChecked = value == true
    }

    private fun setupClearHistoryPreference() {
        preferenceScreen.findPreference<Preference>(getString(R.string.key_clear_search_history))
            ?.setOnPreferenceClickListener {
                ConfirmationDialogFragment
                    .newInstance(
                        { viewModel.clearSearchHistory() },
                        {},
                        R.string.clear_search_history_confirmation
                    )
                    .show(parentFragmentManager, DIALOG_TAG)
                true
            }
    }

    private fun setupAboutPreference() {
        preferenceScreen.findPreference<Preference>(getString(R.string.key_about))
            ?.setOnPreferenceClickListener {
                viewModel.showAbout(R.id.fragment_container)
                true
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout: View = inflater.inflate(R.layout.fragment_settings, container, false)
        val settingsContainerView = layout.findViewById<ViewGroup>(R.id.list_container)

        val toolbarTitle = layout.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = resources.getString(R.string.settings)

        val toolbar = layout.findViewById<Toolbar>(R.id.toolbar_with_title)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { viewModel.goBack() }

        val settingsView = super.onCreateView(inflater, settingsContainerView, savedInstanceState)
        settingsContainerView.addView(settingsView)
        return layout
    }

    private companion object {
        const val DIALOG_TAG = "ClearHistoryConfirmationDialog"
    }
}