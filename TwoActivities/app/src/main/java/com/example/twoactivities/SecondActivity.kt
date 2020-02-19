package com.example.twoactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.EditText
import android.widget.TextView

class SecondActivity : AppCompatActivity() {

    val EXTRA_REPLY = "com.example.android.twoactivities.extra.REPLY"
    private lateinit var reply: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        reply = findViewById(R.id.editText_second);
        val intent = intent
        val message = intent.getStringExtra(MainActivity().EXTRA_MESSAGE)
        val textView: TextView = findViewById(R.id.text_message);
        textView.setText(message);
    }

    fun returnReply(view: View) {
        val s = reply.getText().toString()
        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_REPLY, s)
        setResult(RESULT_OK, replyIntent)
        finish()
    }


}
