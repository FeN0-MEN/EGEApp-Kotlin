package com.example.egeapp_kotlin

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class TheoryActivity : AppCompatActivity() {
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_theory)
        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        imageView1?.setVisibility(View.GONE)
        imageView2?.setVisibility(View.GONE)
        val imageURL1 = intent.getStringExtra("image")
        val imageURL2 = intent.getStringExtra("image2")
        if (imageURL1 != null) {
            imageView1?.setVisibility(View.VISIBLE)
            Picasso.get().load(imageURL1).into(imageView1)
        }
        if (imageURL2 != null) {
            imageView2?.setVisibility(View.VISIBLE)
            Picasso.get().load(imageURL2).into(imageView2)
        }
        val clickListener = View.OnClickListener { v ->
            imageUrl = if (v === imageView1) {
                imageURL1
            } else if (v === imageView2) {
                imageURL2
            } else {
                return@OnClickListener
            }
            // Создание диалогового окна с настраиваемым макетом
            val dialog = Dialog(this@TheoryActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.setContentView(R.layout.dialog_fullscreen_image)

            // Нахождение ImageView внутри диалогового окна
            val fullscreenImageView = dialog.findViewById<ImageView>(R.id.fullscreen_imageview)
            fullscreenImageView.scaleType = ImageView.ScaleType.FIT_XY

            // Загрузка изображения по ссылке с помощью Picasso и установка его в ImageView
            Picasso.get().load(imageUrl).into(fullscreenImageView)

            // Обработчик нажатия для закрытия диалогового окна
            fullscreenImageView.setOnClickListener { dialog.dismiss() }

            // Отображение диалогового окна
            dialog.show()
        }
        imageView1?.setOnClickListener(clickListener)
        imageView2?.setOnClickListener(clickListener)
    }

    fun backToChosing(view: View?) {
        finish()
    }
}