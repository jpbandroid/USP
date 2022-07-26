package com.jpb.scratchtappy.md3.utils

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cuneytayyildiz.onboarder.OnboarderActivity
import com.cuneytayyildiz.onboarder.model.*
import com.cuneytayyildiz.onboarder.utils.OnboarderPageChangeListener
import com.cuneytayyildiz.onboarder.utils.color
import com.jpb.scratchtappy.md3.BuildConfig
import com.jpb.scratchtappy.md3.MainActivity
import com.jpb.scratchtappy.md3.R


class UpdateIntroActivity : OnboarderActivity(), OnboarderPageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnboarderPageChangeListener(this)

        val pages: MutableList<OnboarderPage> = createOnboarderPages()

        initOnboardingPages(pages)
    }

    public override fun onSkipButtonPressed() {
        super.onSkipButtonPressed()
        Toast.makeText(this, "Skip button was pressed!", Toast.LENGTH_SHORT).show()
    }

    override fun onFinishButtonPressed() {
        // implement your logic, save induction has done to sharedPrefs
        Toast.makeText(this, "Finish button was pressed", Toast.LENGTH_SHORT).show()
    }

    override fun onPageChanged(position: Int) {
        Log.d(javaClass.simpleName, "onPageChanged: $position")
    }

    private fun createOnboarderPages(): MutableList<OnboarderPage> {
        return mutableListOf(
            onboarderPage {
                backgroundColor = color(R.color.purple_500)

                image {
                    imageResId = R.mipmap.ic_launcher
                }

                title {
                    text = "Thanks for installing ScratchTappy md3 v" + BuildConfig.VERSION_NAME + "!"
                    textColor = color(R.color.white)
                }

                description {
                    text = "All the new features and changes in ST md3 " + BuildConfig.VERSION_NAME + "\n" + getString(R.string.changelog)
                    textColor = color(R.color.white)
                    multilineCentered = true
                }
            },
            onboarderPage {
                backgroundColor = color(R.color.white)

                image {
                    imageResId = R.drawable.ic_round_system_update_24
                }

                title {
                    text = "Please install other updates to ST md3 to get more new features!"
                    textColor = color(R.color.black)
                }

                description {
                    text = "Like UI revamps, Library updates, bug fixes and other stability improvements!"
                    textColor = color(R.color.black)
                    multilineCentered = true
                }

                miscellaneousButton {
                    visibility = View.VISIBLE
                    text = "Go to the app!"
                    backgroundColor = Color.WHITE
                    textColor = color(R.color.purple_500)
                    clickListener = View.OnClickListener {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            })}}