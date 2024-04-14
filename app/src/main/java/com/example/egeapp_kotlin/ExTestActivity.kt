package com.example.egeapp_kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.widget.EditText
import android.view.View
import android.widget.*
import com.example.egeapp_kotlin.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*

class ExTestActivity : AppCompatActivity() {
    private var TaskText: TextView? = null
    private var imageUrl: String? = null
    private var downloadUrl: String? = null
    private var rightAnswer: String? = null
    private var imageBox: ImageView? = null
    private var buttonDownload: Button? = null
    private var textForTask: String? = null
    private var globalScore = 0
    private var scoreForTask: Int? = null
    private var answerEditText: EditText? = null
    private var number = 0
    private val answerArray = arrayOfNulls<String>(27)
    private val scoreArray = arrayOfNulls<Int>(27)
    private var index = 0
    private var sumOfScore = 0
    private var numberChoice: String? = null
    private var indexChoice = 0
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var seconds = 0
    private var isRunning = false
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_ex_test)

        timerTextView = findViewById(R.id.timerTextView)
        handler = Handler()

        startTimer()
    }

    private fun startTimer() {
        isRunning = true
        handler.post(object : Runnable {
            override fun run() {
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60

                val timeString = String.format("%02d:%02d", minutes, remainingSeconds)
                timerTextView.text = timeString

                if (isRunning) {
                    seconds++
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
    fun onClickExit(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun onClickHelp(view: View?) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }
}