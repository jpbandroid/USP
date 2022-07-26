package com.jpb.scratchtappy.md3

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jpb.scratchtappy.md3.databinding.ActivityAboutBinding

@Deprecated(message = "Old AboutActivity. Deprecated since dev builds of ST md3 1.1.", replaceWith = ReplaceWith("com.jpb.scratchtappy.md3.ScrollingActivity"))
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Tap, Tap, Tap!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}