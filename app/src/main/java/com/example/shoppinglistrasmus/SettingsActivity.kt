package com.example.shoppinglistrasmus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display the fragment as the main content
        // Note that the ID named "content" is defined by Android -
        // it is NOT an identifier we define in .xml

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, PreferenceFragment())
            .commit()

        // Note - there is no setContentView and no xml layout
        // for this activity. Because that is defined 100%
        // in the fragment (PreferenceFragment)
    }
}