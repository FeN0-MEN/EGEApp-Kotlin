package com.example.egeapp_kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StatActivity : AppCompatActivity() {
    val entriesTime = ArrayList<Entry>()
    val entriesScore = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_stat)

        // Получаем уникальный идентификатор текущего пользователя
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Проверяем, что уникальный идентификатор пользователя не равен null
        if (userId != null) {
            // Ссылка на базу данных "Users"
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("Users")

            // Ссылка на подраздел "Stats" текущего пользователя
            val userStatsRef = usersRef.child(userId).child("Stats")

            // Получаем статистику по времени
            userStatsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(statsSnapshot: DataSnapshot) {
                    val times = mutableListOf<Long>()
                    var attemptNumberTime = 1f
                    // Перебираем все дочерние узлы узла "Stats" (каждая попытка теста)
                    statsSnapshot.children.forEach { attemptSnapshot ->
                        // Получаем ссылку на узел времени для текущей попытки
                        val timeStatsRef = attemptSnapshot.child("Time")

                        // Обработка данных о времени

                        // Получение значения времени из узла "Time"
                        val timeSnapshot = timeStatsRef.value as Long?
                        if (timeSnapshot != null) {
                            entriesTime.add(Entry(attemptNumberTime, timeSnapshot.toFloat()/60000))
                            attemptNumberTime++
                            times.add(timeSnapshot)
                        }
                    }
                        // Вычисляем среднее время
                        val averageTime = times.average().toLong()
                        val averageTimeFormatted = formatTime(averageTime)

                        // Находим наибольшее и наименьшее время
                        val maxTime = times.maxOrNull() ?: 0
                        val minTime = times.minOrNull() ?: 0
                        val maxTimeFormatted = formatTime(maxTime)
                        val minTimeFormatted = formatTime(minTime)

                        // Вычисляем сумму времени
                        val totalTime = times.sum()
                        val totalTimeFormatted = formatTime(totalTime)

                        // Показываем статистику по времени на экране
                        showStatisticsTime(averageTimeFormatted, maxTimeFormatted, minTimeFormatted, totalTimeFormatted)

                    val lineChartTime = findViewById<LineChart>(R.id.lineChart1)
                    val dataSet = LineDataSet(entriesTime, "Время попыток")
                    val greenColor = Color.parseColor("#606C38")

                    dataSet.color = greenColor
                    dataSet.valueTextColor = greenColor

                    val dataSets: ArrayList<ILineDataSet> = ArrayList()
                    dataSets.add(dataSet)

                    val lineData = LineData(dataSets)
                    lineChartTime.data = lineData

                    lineChartTime.setTouchEnabled(true)
                    lineChartTime.setPinchZoom(true)
                    lineChartTime.description.text = "Время, затраченное на попытки"
                    lineChartTime.animateY(1000)
                }

                override fun onCancelled(statsError: DatabaseError) {
                    // Обработка ошибок при получении данных о попытках тестов
                }
            })

            // Получаем статистику по баллам
            userStatsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                val scores = mutableListOf<Long>()
                var attemptNumberScore = 1f
                override fun onDataChange(statsSnapshot: DataSnapshot) {
                    // Перебираем все дочерние узлы узла "Stats" (каждая попытка теста)
                    statsSnapshot.children.forEach { attemptSnapshot ->
                        // Получаем ссылку на узел баллов для текущей попытки
                        val scoreStatsRef = attemptSnapshot.child("Score")

                        // Обработка данных о баллах
                        val scoreSnapshot = scoreStatsRef.value as Long?
                        if (scoreSnapshot != null) {
                            entriesScore.add(Entry(attemptNumberScore, scoreSnapshot.toFloat()))
                            attemptNumberScore++
                            scores.add(scoreSnapshot)
                        }

                        // Вычисляем средний балл
                        val averageScore = scores.average().toInt()

                        // Находим наибольший и наименьший балл
                        val maxScore = scores.maxOrNull()?.toInt() ?: 0
                        val minScore = scores.minOrNull()?.toInt() ?: 0


                        // Показываем статистику по баллам на экране
                        showStatisticsScore(averageScore, maxScore, minScore)

                        val lineChartScore = findViewById<LineChart>(R.id.lineChart2)
                        val dataSetScore = LineDataSet(entriesScore, "Баллы за попытки")
                        val greenColor = Color.parseColor("#606C38")
                        dataSetScore.color = greenColor
                        dataSetScore.valueTextColor = greenColor

                        val dataSetsScore: ArrayList<ILineDataSet> = ArrayList()
                        dataSetsScore.add(dataSetScore)

                        val lineDataScore = LineData(dataSetsScore)
                        lineChartScore.data = lineDataScore



                        lineChartScore.setTouchEnabled(true)
                        lineChartScore.setPinchZoom(true)
                        lineChartScore.description.text = "Баллы"
                        lineChartScore.animateY(1000)
                    }
                }

                override fun onCancelled(scoreError: DatabaseError) {
                    // Обработка ошибок при получении данных о баллах из базы данных
                }
            })
        }




        // Получение ссылки на элемент LineChart из макета



    }
    private fun showStatisticsTime(averageTime: String, maxTime: String, minTime: String, totalTime: String) {

        // Отображаем статистику о времени на экране
        val averageTimeText = findViewById<TextView>(R.id.exNum1)
        val maxTimeText = findViewById<TextView>(R.id.exNum2)
        val minTimeText = findViewById<TextView>(R.id.exNum3)
        val totalTimeText = findViewById<TextView>(R.id.exNum4)

        maxTimeText.text = maxTime
        averageTimeText.text = averageTime
        minTimeText.text = minTime
        totalTimeText.text = totalTime
    }

    private fun showStatisticsScore(averageScore: Int, maxScore: Int, minScore: Int) {

        // Отображаем статистику о времени на экране
        val averageScoreText = findViewById<TextView>(R.id.exNum6)
        val maxScoreText = findViewById<TextView>(R.id.exNum7)
        val minScoreText = findViewById<TextView>(R.id.exNum8)

        maxScoreText.text = maxScore.toString()
        averageScoreText.text = averageScore.toString()
        minScoreText.text = minScore.toString()
    }

    // Функция для форматирования времени в минуты и секунды
    private fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hours = minutes / 60
        val days = hours / 24

        return if (days > 0) {
            String.format("%d д. %02d ч.", days, hours % 24) }
        else if(hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes % 60, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
    fun onClickStatExit(view: View?) {
        finish()
    }
}