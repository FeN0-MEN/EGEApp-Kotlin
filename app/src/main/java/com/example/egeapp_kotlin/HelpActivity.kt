package com.example.egeapp_kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
        imageBox?.setVisibility(View.GONE)
        imageBoxHelp?.setVisibility(View.GONE)
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
            Picasso.get().load(helpImg).into(imageBoxHelp)
        }
        if (textForTask != null) {
            TaskText!!.text = textForTask
        }

        if (imageUrl != null) {
            imageBox!!.visibility = View.VISIBLE
            Picasso.get().load(imageUrl).into(imageBox)
        }
        imageBoxHelp!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            Picasso.get().load(helpImg).into(imageView)
            builder.setView(imageView)
            val dialog = builder.create()
            dialog.show()
            // Установка размера диалогового окна в полный экран
            val window = dialog.window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
    fun onClickClose(view: View?) {
        finish()
    }
}