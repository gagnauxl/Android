package com.example.notificationchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTitel: EditText
    private lateinit var editTextMessage: EditText
    private lateinit var buttonChannel1: Button
    private lateinit var buttonChannel2: Button
    private lateinit var mNotificationHelper : NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextTitel = findViewById(R.id.edittext_title)
        editTextMessage = findViewById(R.id.edittext_message)
        buttonChannel1 = findViewById(R.id.button_channel1)
        buttonChannel2 = findViewById(R.id.button_channel2)
        mNotificationHelper = NotificationHelper(this)

        buttonChannel1.setOnClickListener {
            sendOnChannel1(editTextTitel.text.toString(), editTextMessage.text.toString())
        }
        buttonChannel2.setOnClickListener {
            sendOnChannel2(editTextTitel.text.toString(), editTextMessage.text.toString())
        }
    }

    fun sendOnChannel1(title: String, message: String) {
        var nb = mNotificationHelper.getChannel1Notification(title, message)
        mNotificationHelper.getManager()?.notify(1, nb.build())
    }

    fun sendOnChannel2(title: String, message: String) {
        var nb = mNotificationHelper.getChannel2Notification(title, message)
        mNotificationHelper.getManager()?.notify(2, nb.build())
    }
}
