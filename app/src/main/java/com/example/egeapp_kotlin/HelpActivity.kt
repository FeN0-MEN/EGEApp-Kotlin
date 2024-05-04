package com.example.egeapp_kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class HelpActivity : AppCompatActivity() {
    private var TaskText: TextView? = null
    private var TextHelp: TextView? = null
    private var imageBox: ImageView? = null
    private var imageBoxHelp: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_help)

        TaskText = findViewById(R.id.TextTask)
        TextHelp = findViewById(R.id.TextHelp)
        imageBox = findViewById(R.id.imageBox)
        imageBoxHelp = findViewById(R.id.imageBoxHelp)
        val helpText = intent.getStringExtra("helpText")
        val helpImg = intent.getStringExtra("helpImg")
        val imageUrl = intent.getStringExtra("imageUrl")
        val textForTask = intent.getStringExtra("textForTask")

        // Использование полученных данных
        if (helpText != null) {
            TextHelp!!.text = helpText
        }

        if (helpImg != null) {
            imageBoxHelp!!.visibility = View.VISIBLE
            Picasso.get().load(imageUrl).into(imageBoxHelp)
        }
        if (textForTask != null) {
            TaskText!!.text = textForTask
        }

        if (imageUrl != null) {
            imageBox!!.visibility = View.VISIBLE
            Picasso.get().load(imageUrl).into(imageBox)
        }
    }
    fun onClickClose(view: View?) {
        finish()
    }
}