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
import com.jpb.scratchtappy.md3.MainActivity
import com.jpb.scratchtappy.md3.R


class IntroActivity : OnboarderActivity(), OnboarderPageChangeListener {
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
                    text = "Welcome to ScratchTappy!"
                    textColor = color(R.color.white)
                }

                description {
                    text = "This app helps you relieve stress with a blue Button (formerly a FloatingActionButton), and also appeals to people that want to test out all of the Material Design components, and people who want ato find out detailed information about their Android device"
                    textColor = color(R.color.white)
                    multilineCentered = true
                }
            },
            onboarderPage {
                backgroundColor = color(R.color.white)

                image {
                    imageResId = R.mipmap.ic_banner
                }

                title {
                    text = "Also for TVs!"
                    textColor = color(R.color.black)
                }

                description {
                    text = "ScratchTappy md3 is also optimized for TVs!\nIt has a TV-friendly makeup in the majority, but unfortunately, some more recent UI surfaces (like the easter eggs) are not fit for TVs!"
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
            }
        )
    }
}