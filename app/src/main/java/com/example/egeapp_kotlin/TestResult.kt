package com.example.egeapp_kotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TestResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_test_result)

        val ResultTextView = findViewById<TextView>(R.id.ResultTextView)
        val CongratulationsTextView = findViewById<TextView>(R.id.CongratulationTextView)
        val ScoreTextView = findViewById<TextView>(R.id.ScoreTextView)
        var secondaryScore: String? = null
        // полученные баллы
        val globalScore = intent.getIntExtra("Score", 0)
        secondaryScore = convertPrimaryToSecondary(globalScore)
        // потраченное время
        var timeInMilleseconds: Long = intent.getLongExtra("timeInMilleseconds", 0)
        if (globalScore == 1 || globalScore == 21) {
            ScoreTextView.text = "балл"
        } else if (globalScore > 1 && globalScore < 5) {
            ScoreTextView.text = "балла"
        } else if (globalScore > 21 && globalScore < 25) {
            ScoreTextView.text = "балла"
        } else {
            ScoreTextView.text = "баллов"
        }
        if (globalScore > 20) {
            CongratulationsTextView.text = "Поздравляем!"
        } else if (globalScore <= 20 && globalScore > 7) {
            CongratulationsTextView.text = "Неплохо!"
        } else {
            CongratulationsTextView.text = "К сожалению,"
        }
        ResultTextView.text = globalScore.toString()

    }

    fun Exit(view: View?) {
        // Получаем баллы и время теста из Intent
        val globalScore = intent.getIntExtra("Score", 0)
        val timeInMilleseconds = intent.getLongExtra("timeInMilleseconds", 0)

        // Получаем уникальный идентификатор текущего пользователя
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Проверяем, что уникальный идентификатор пользователя не равен null
        if (userId != null) {
            // Ссылка на базу данных "Users"
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("Users")

            // Ссылка на подраздел "Stats" текущего пользователя
            val currentUserStatsRef = usersRef.child(userId).child("Stats")

            // Создаем новый уникальный идентификатор для каждой попытки теста
            val attemptId = currentUserStatsRef.push().key

            // Создаем объект для сохранения данных о текущей попытке теста
            val testAttemptData = HashMap<String, Any>()
            testAttemptData["Score"] = globalScore
            testAttemptData["Time"] = timeInMilleseconds

            // Добавляем информацию о попытке теста в базу данных
            currentUserStatsRef.child(attemptId ?: "").setValue(testAttemptData)
        }

        // Переходим на главный экран
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
    fun convertPrimaryToSecondary(primaryScore: Int) : String? {
        val database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://appbase-66912-default-rtdb.europe-west1.firebasedatabase.app")
        val scoreConversionRef = database.child(primaryScore.toString())

        var secondaryScore: Int? = null

        scoreConversionRef.child(primaryScore.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                secondaryScore = snapshot.value as Int?
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })

        return secondaryScore.toString()
    }
}

// Класс для хранения информации о попытке теста
data class TestAttempt(val globalScore: Int, val timeInMilliseconds: Long)

// Функция для добавления баллов и времени теста в базу данных пользователя
private fun addTestAttemptToUserStats(userId: String, globalScore: Int, timeInMilliseconds: Long) {
    // Ссылка на базу данных "Users"
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("Users")

    // Ссылка на подраздел "Stats" текущего пользователя
    val currentUserStatsRef = usersRef.child(userId).child("Stats")

    // Создаем новый уникальный идентификатор для каждой попытки теста
    val attemptId = currentUserStatsRef.push().key

    // Создаем объект TestAttempt для текущей попытки теста
    val testAttempt = TestAttempt(globalScore, timeInMilliseconds)

    // Добавляем информацию о попытке теста в базу данных
    currentUserStatsRef.child(attemptId ?: "").setValue(testAttempt)
}