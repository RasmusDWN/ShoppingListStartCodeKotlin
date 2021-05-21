package com.example.shoppinglistrasmus

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)
    }

}