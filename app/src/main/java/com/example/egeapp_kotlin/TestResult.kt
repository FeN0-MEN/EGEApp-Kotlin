package com.example.egeapp_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.R

class TestResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        val ResultTextView = findViewById<TextView>(R.id.ResultTextView)
        val CongratulationsTextView = findViewById<TextView>(R.id.CongratulationsTextView)
        val ScoreTextView = findViewById<TextView>(R.id.ScoreTextView)
        val globalScore = intent.getIntExtra("Score", 0)
        if (globalScore == 1 || globalScore == 21) {
            ScoreTextView.text = "БАЛЛ"
        } else if (globalScore > 1 && globalScore < 5) {
            ScoreTextView.text = "БАЛЛА"
        } else if (globalScore > 21 && globalScore < 25) {
            ScoreTextView.text = "БАЛЛА"
        } else {
            ScoreTextView.text = "БАЛЛОВ"
        }
        if (globalScore > 20) {
            CongratulationsTextView.text = "ПОЗДРАВЛЯЕМ!"
        } else if (globalScore <= 20 && globalScore > 7) {
            CongratulationsTextView.text = "НЕПЛОХО!"
        } else {
            CongratulationsTextView.text = "К СОЖАЛЕНИЮ,"
        }
        ResultTextView.text = globalScore.toString()
    }

    fun Exit(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}