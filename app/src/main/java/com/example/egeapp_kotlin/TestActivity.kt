package com.example.egeapp_kotlin

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Random

class TestActivity : AppCompatActivity() {
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
    var timeInMilleseconds: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_test)
        val textViewNum = findViewById<TextView>(R.id.textViewNum)
        timerTextView = findViewById(R.id.timerTextView)
        timerTextView.setTextColor(Color.parseColor("#606C38"))
        // Начинаем таймер с обратным отсчетом от 235 минут (235 * 60 * 1000 миллисекунд)
        startTimer(235 * 60 * 1000, this)
        answerEditText = findViewById(R.id.AnswerEditText)
        imageBox = findViewById(R.id.imageBox)
        TaskText = findViewById(R.id.TextTask)
        buttonDownload = findViewById(R.id.buttonDownload)
        buttonDownload?.isEnabled = false
        buttonDownload?.setBackgroundColor(Color.parseColor("#DAD6BC"))
        val buttonNext = findViewById<ImageButton>(R.id.buttonNext)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        buttonBack.isEnabled = false
        buttonBack.setBackgroundResource(R.drawable.image_button_left_blocked)
        val numberKeys = arrayOf("Number_1", "Number_2")
        val random = Random()
        indexChoice = random.nextInt(numberKeys.size)
        numberChoice = numberKeys[indexChoice]
        loadTaskFromFirebase("Task_1", numberChoice)
        buttonDownload?.setOnClickListener {
            startDownload()
        }
        // Обработчик событий для кнопки "далее"
        buttonNext.setOnClickListener { v ->
            if (number >= 0) {
                buttonBack.isEnabled = true
                buttonBack.setBackgroundResource(R.drawable.image_button_left)
            }
            val nice = v.context
            val userInput = answerEditText?.text.toString().trim { it <= ' ' }
            if (answerEditText != null) {
                answerArray[index] = userInput
                answerEditText!!.setText("")
                if (userInput == rightAnswer) {
                    scoreArray[index] = scoreForTask
                } else {
                    if (scoreArray[index] != null) {
                        if (scoreArray[index] == 1 || scoreArray[index] == 2) {
                            scoreArray[index] = 0
                        }
                    }
                }
            }
            index++
            if (TextUtils.isEmpty(userInput)) {
                Toast.makeText(nice, "Заполните ответ", Toast.LENGTH_SHORT).show()
            }
            val taskKeys = arrayOf("Task_1", "Task_2", "Task_3", "Task_4", "Task_5", "Task_6", "Task_7", "Task_8", "Task_9", "Task_10", "Task_11", "Task_12", "Task_13", "Task_14", "Task_15", "Task_16", "Task_17", "Task_18", "Task_19", "Task_20", "Task_21", "Task_22", "Task_23", "Task_24", "Task_25", "Task_26", "Task_27")
            number += 1
            textViewNum.text = (number+1).toString()
            if (number <= 26) {
                loadTaskFromFirebase(taskKeys[number], numberChoice)
                if (number == 26) {
                    buttonNext.setBackgroundResource(R.drawable.image_button_right_finish)
                }
            } else {
                for (i in scoreArray.indices) {
                    if (scoreArray[i] != null) {
                        scoreArray[i] = scoreArray[i]!! + scoreArray[i]!!
                    }
                }
                globalScore = sumOfScore
                timeInMilleseconds = stopTimer()
                val context = v.context
                val intent = Intent(context, TestResult::class.java)
                intent.putExtra("Score", globalScore)
                intent.putExtra("timeInMilleseconds", timeInMilleseconds)
                context.startActivity(intent)
            }
        }
        buttonBack.setOnClickListener { v ->
            if (number > 0) {
                if (number <= 26) {
                    buttonNext.setBackgroundResource(R.drawable.image_button_right)
                }
                if (number == 1)
                {
                    buttonBack.setBackgroundResource(R.drawable.image_button_left_blocked)
                }
                val nice = v.context
                val userInput = answerEditText?.text.toString().trim { it <= ' ' }
                if (answerEditText != null) {
                    answerArray[index] = userInput
                    answerEditText!!.setText("")
                    if (userInput == rightAnswer) {
                        scoreArray[index] = scoreForTask
                    } else {
                        if (scoreArray[index] != null) {
                            if (scoreArray[index] == 1 || scoreArray[index] == 2) {
                                scoreArray[index] = 0
                            }
                        }
                    }
                }
                index--
                if (TextUtils.isEmpty(userInput)) {
                    Toast.makeText(nice, "Заполните ответ", Toast.LENGTH_SHORT).show()
                }
                val taskKeys = arrayOf("Task_1", "Task_2", "Task_3", "Task_4", "Task_5", "Task_6", "Task_7", "Task_8", "Task_9", "Task_10", "Task_11", "Task_12", "Task_13", "Task_14", "Task_15", "Task_16", "Task_17", "Task_18", "Task_19", "Task_20", "Task_21", "Task_22", "Task_23", "Task_24", "Task_25", "Task_26", "Task_27")
                number -= 1
                textViewNum.text = (number+1).toString()
                if (number <= 26) {
                    loadTaskFromFirebase(taskKeys[number], numberChoice)
                    if (number == 26) {
                        buttonNext.setBackgroundResource(R.drawable.image_button_right_finish)
                    }
                } else {
                    for (i in scoreArray.indices) {
                        if (scoreArray[i] != null) {
                            scoreArray[i] = scoreArray[i]!! + scoreArray[i]!!
                        }
                    }
                    globalScore = sumOfScore
                    timeInMilleseconds = stopTimer()
                    val context = v.context
                    val intent = Intent(context, TestResult::class.java)
                    intent.putExtra("Score", globalScore)
                    intent.putExtra("timeInMilleseconds", timeInMilleseconds)
                    context.startActivity(intent)
                }
            }
        }
    }
    private var startTimeMillis: Long = 0 // Объявляем переменную startTimeMillis
    private fun startTimer(milliseconds: Long, context: Context) {
        startTimeMillis = System.currentTimeMillis() // Инициализируем startTimeMillis
        countDownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Показываем оставшееся время в формате минут:секунды
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)

                // Проверяем, осталось ли 15 минут и меняем цвет текста при необходимости
                if (minutes == 15L) {
                    timerTextView.setTextColor(Color.parseColor("#B34747"))
                }

                timerTextView.text = timeLeftFormatted
            }

            override fun onFinish() {
                // Выполняем действие по завершению таймера
                Toast.makeText(context, "Время экзамена окончено", Toast.LENGTH_SHORT).show()
                for (i in scoreArray.indices) {
                    if (scoreArray[i] != null) {
                        scoreArray[i] = scoreArray[i]!! + scoreArray[i]!!
                    }
                }
                globalScore = sumOfScore
                val intent = Intent(context, TestResult::class.java)
                intent.putExtra("Score", globalScore)
                context.startActivity(intent)
            }
        }.start()
    }

    // Функция для остановки таймера и получения прошедшего времени
    fun stopTimer(): Long {
        countDownTimer?.cancel() // Отменяем таймер, если он был запущен

        // Вычисляем прошедшее время в миллисекундах
        val elapsedTimeInMillis = System.currentTimeMillis() - startTimeMillis

        return elapsedTimeInMillis // Возвращаем прошедшее время в миллисекундах
    }

    fun onClickExit(view: View?) {
        finish()
    }

    private fun loadTaskFromFirebase(taskKey: String, numberKey: String?) {
        // Получение ссылки на узел задания в Firebase
        val taskRef = FirebaseDatabase.getInstance("https://appbase-66912-default-rtdb.europe-west1.firebasedatabase.app").reference.child(taskKey).child(numberKey!!)
        taskRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Получение значений из Firebase и присвоение их переменным
                    textForTask = dataSnapshot.child("Question").getValue(String::class.java)
                    imageUrl = dataSnapshot.child("Img").getValue(String::class.java)
                    downloadUrl = dataSnapshot.child("FileLink").getValue(String::class.java)
                    rightAnswer = dataSnapshot.child("Correct_Answer").getValue(String::class.java)
                    scoreForTask = dataSnapshot.child("Score").getValue(Int::class.java)

                    // Установка текста задания в TextView
                    TaskText!!.text = textForTask

                    // Если ссылка на картинку имеется, вы можете загрузить и отобразить ее в ImageView
                    if (imageUrl == null) {
                        imageBox!!.visibility = View.GONE
                    } else {
                        // Загрузка и отображение изображения
                        imageBox!!.visibility = View.VISIBLE
                        Picasso.get().load(imageUrl).into(imageBox)
                    }
                    // Если ссылка на файл для скачивания имеется, вы можете предоставить пользователю возможность скачать его
                    if (downloadUrl != null) {
                        // Отображение кнопки для скачивания файла
                        buttonDownload!!.isEnabled = true
                        buttonDownload?.setBackgroundColor(Color.parseColor("#F7F4E0"))
                    } else {
                        buttonDownload!!.isEnabled = false
                        buttonDownload?.setBackgroundColor(Color.parseColor("#DAD6BC"))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок чтения данных из Firebase
            }
        })
    }

    private fun startDownload() {
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Загрузка файла")
        request.setDescription("Загрузка файла...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file")
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}