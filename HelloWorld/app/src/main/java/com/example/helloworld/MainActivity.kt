package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "Hello World");
        var s: String?
        s=null
        try {
            val i=s!!.length
        } catch (e: NullPointerException)
        {
            Log.d("MainActivity", "Exception", e);
            Log.e("MainActivity", "Exception", e);
        }

    }
}