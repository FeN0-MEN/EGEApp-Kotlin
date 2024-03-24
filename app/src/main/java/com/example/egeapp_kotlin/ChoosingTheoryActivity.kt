package com.example.egeapp_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.R

class ChoosingTheoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choosing_theory)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val button10 = findViewById<Button>(R.id.button10)
        val button11 = findViewById<Button>(R.id.button11)
        button1.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/Yk9tTNX/Screenshot-20230524-213129-3.png") // Передача имени файла изображения
            intent.putExtra("image2", "https://i.ibb.co/jDS7qPp/Screenshot-20230524-213139-4.png")
            startActivity(intent)
        }
        button2.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/DV9xcGp/slide-21.jpg") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
        button3.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/w68qS0q/Screenshot-20230524-214727-3.png") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
        button4.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/f9rK8v0/Screenshot-20230524-214740-3.png") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
        button5.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/YdpqN29/Screenshot-20230524-214849-3.png") // Передача имени файла изображения
            intent.putExtra("image2", "https://i.ibb.co/ChBGCT5/Screenshot-20230524-214901-3.png")
            startActivity(intent)
        }
        button6.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/6XSc6tg/Screenshot-20230524-214930-3.png") // Передача имени файла изображения
            intent.putExtra("image2", "https://i.ibb.co/swZJwzm/Screenshot-20230524-215310-4.png")
            startActivity(intent)
        }
        button7.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/yW8WzZ3/Screenshot-20230524-215324-3.png") // Передача имени файла изображения
            intent.putExtra("image2", "https://i.ibb.co/gW4wMts/Screenshot-20230524-215335-3.png")
            startActivity(intent)
        }
        button8.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/NtS3q5X/Screenshot-20230524-215406-2.png") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
        button9.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/8PLTRz2/Screenshot-20230524-215415-2.png") // Передача имени файла изображения
            intent.putExtra("image2", "https://i.ibb.co/FwC1xDb/Screenshot-20230524-215424-3.png")
            startActivity(intent)
        }
        button10.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/K9YXBSh/Screenshot-20230524-215438-2.png") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
        button11.setOnClickListener { // Вызов активности с передачей входных данных
            val intent = Intent(this@ChoosingTheoryActivity, TheoryActivity::class.java)
            intent.putExtra("image", "https://i.ibb.co/0sgrjkM/Screenshot-20230524-215507-2.png") // Передача имени файла изображения
            intent.putExtra("image2", null as String?)
            startActivity(intent)
        }
    }

    fun backToMenu(view: View?) {
        finish()
    }
}