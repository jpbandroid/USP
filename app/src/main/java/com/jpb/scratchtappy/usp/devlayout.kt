package com.jpb.scratchtappy.usp

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class devlayout : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences2, rootKey)
    }
}