package com.example.dailiesandroidapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//This class handles the display of a notification message
class NotificationMessage : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_message)
        textView = findViewById(R.id.tv_message)

        val bundle: Bundle? = intent.extras // Call data
        textView.text = bundle?.getString("message")
    }
}
