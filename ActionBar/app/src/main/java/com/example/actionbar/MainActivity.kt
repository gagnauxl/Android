package com.example.actionbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Note use a theme in manifest without an action bar otherwise there 2 action bar
        // e.g.: android:theme="@style/Theme.AppCompat.Light.NoActionBar">
        setSupportActionBar(findViewById(R.id.toolbar))
        title = "Home"
    }
}
