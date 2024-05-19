package com.example.egeapp_kotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ExTestResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_ex_test_result)

        val Congratulation = findViewById<TextView>(R.id.CongratulationTextView)
        // полученные баллы
        val globalScore = intent.getIntExtra("scoreForEx", 0)
        val answerText: String
        val resultText: String
        val allText: Int = intent.getIntExtra("allForEx", 0)
        // потраченное время
        val timeInMilleseconds: Long = intent.getLongExtra("timeForEx", 0)
        if (globalScore % 10 == 1 && globalScore % 100 != 11) {
            answerText = "ответ из"
            resultText = "точный"
        } else if ((globalScore % 10 in 2..4) && (globalScore % 100 !in 12..14)) {
            answerText = "ответа из"
            resultText = "точных"
        } else {
            answerText = "ответов из"
            resultText = "точных"
        }
        val congratulationText = "У вас $globalScore $resultText  $answerText $allText"
        Congratulation.setText(congratulationText)
    }

    fun Exit(view: View?) {
        // Получаем баллы и время теста из Intent
        val globalScore = intent.getIntExtra("scoreForEx", 0)
        val timeInMilleseconds = intent.getLongExtra("allForEx", 0)

        // Получаем уникальный идентификатор текущего пользователя
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Проверяем, что уникальный идентификатор пользователя не равен null
        if (userId != null) {
            // Ссылка на базу данных "Users"
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("Users")

            // Ссылка на подраздел "Stats" текущего пользователя
            val currentUserStatsRef = usersRef.child(userId).child("ExStats")

            // Создаем новый уникальный идентификатор для каждой попытки теста
            val attemptId = currentUserStatsRef.push().key

            // Создаем объект для сохранения данных о текущей попытке теста
            val exTestAttemptData = HashMap<String, Any>()
            exTestAttemptData["Score"] = globalScore
            exTestAttemptData["Time"] = timeInMilleseconds

            // Добавляем информацию о попытке теста в базу данных
            currentUserStatsRef.child(attemptId ?: "").setValue(exTestAttemptData)
        }

        // Переходим на главный экран
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}

// Класс для хранения информации о попытке теста
data class ExTestAttempt(val globalScore: Int, val timeInMilliseconds: Long)

// Функция для добавления баллов и времени теста в базу данных пользователя
private fun addTestAttemptToUserStats(userId: String, globalScore: Int, timeInMilliseconds: Long) {
    // Ссылка на базу данных "Users"
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("Users")

    // Ссылка на подраздел "Stats" текущего пользователя
    val currentUserStatsRef = usersRef.child(userId).child("ExStats")

    // Создаем новый уникальный идентификатор для каждой попытки теста
    val attemptId = currentUserStatsRef.push().key

    // Создаем объект TestAttempt для текущей попытки теста
    val exTestAttempt = ExTestAttempt(globalScore, timeInMilliseconds)

    // Добавляем информацию о попытке теста в базу данных
    currentUserStatsRef.child(attemptId ?: "").setValue(exTestAttempt)
}