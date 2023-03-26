package com.github.skytoph.simpleweather.presentation.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.presentation.favorites.ConfirmationDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        setupClearHistoryPreference()
        setupLanguagePreference()
    }

    private fun setupClearHistoryPreference() {
        preferenceScreen.findPreference<Preference>(getString(R.string.key_clear_search_history))
            ?.setOnPreferenceClickListener {
                ConfirmationDialogFragment
                    .newInstance(
                        { viewModel.clearSearchHistory() },
                        R.string.clear_search_history_confirmation
                    )
                    .show(parentFragmentManager, DIALOG_TAG)
                true
            }
    }

    private fun setupLanguagePreference() {
        val preference =
            preferenceScreen.findPreference<ListPreference>(getString(R.string.key_language))
        val language = Locale.getDefault().language
        val indexOfValue = preference?.findIndexOfValue(language).takeIf { it != -1 } ?: 0
        preference?.setValueIndex(indexOfValue)
    }

    private companion object {
        const val DIALOG_TAG = "ClearHistoryConfirmationDialog"
    }
}