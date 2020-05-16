package com.teamisland.zzazz.utils

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.teamisland.zzazz.R

class IntroAlertDialog(context: Context?, val run_function: () -> Unit) : AlertDialog(context) {

    private lateinit var textView: TextView
    private lateinit var checkBox: CheckBox
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_alertdialog)

        textView = findViewById(R.id.intro_alert_text)
        checkBox = findViewById(R.id.checkBox)
        button = findViewById(R.id.intro_alert_button)

        textView.text = context.getString(R.string.video_restrictions)
        checkBox.text = context.getString(R.string.alert_2)
        button.setOnClickListener { run_function() }
    }
}