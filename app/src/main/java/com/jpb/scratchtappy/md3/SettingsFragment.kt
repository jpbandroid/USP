package com.jpb.scratchtappy.md3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import android.content.DialogInterface
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.color.DynamicColors

import com.google.android.material.dialog.MaterialAlertDialogBuilder




class SettingsFragment : PreferenceFragmentCompat() {
    var tap = 0
    var tapst = 0
    var tapee = 5
    var tapstee = 5

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences2, rootKey)

                val signaturePreference: Preference? = findPreference("aboutpref")
        signaturePreference?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), ScrollingActivity::class.java))
            true
        }
        val clockPreference: Preference? = findPreference("clock")
        clockPreference?.setOnPreferenceClickListener {
            tap++
            tapee--
            if (tap == 5) {
                startActivity(Intent(requireContext(), PlatLogoActivity::class.java))
            }
            else{
                val text = "Tap the clock " + tapee + " more times, and a surprise will unlock!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
            }
            true
        }
        val stPreference: Preference? = findPreference("stmd3")
        stPreference?.setOnPreferenceClickListener {
            tapst++
            tapstee--
            if (tapst == 5) {
                startActivity(Intent(requireContext(), PlatLogoActivity2::class.java))
            }
            else{
                val text = "Tap the ScratchTappy icon " + tapstee + " more times, and a surprise will unlock!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
            }
            true
        }
        val ratePreference: Preference? = findPreference("rate")
        ratePreference?.setOnPreferenceClickListener {
            val inflater = requireActivity().layoutInflater;
            getActivity()?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Rate ST!")
                    .setIcon(R.drawable.ic_round_star_rate_24)
                    .setMessage("Please rate ST using the 5 stars below.")
                    .setView(inflater.inflate(R.layout.ratelayout, null))
                    .setPositiveButton(
                        "Got It"
                    ) { dialogInterface, i -> }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialogInterface, i -> }
                    .show()
            }
            true
            }
        }
    }