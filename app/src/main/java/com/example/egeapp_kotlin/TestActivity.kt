package com.example.egeapp_kotlin

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.EditText
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        answerEditText = findViewById(R.id.AnswerEditText)
        imageBox = findViewById(R.id.imageBox)
        TaskText = findViewById(R.id.TextTask)
        buttonDownload = findViewById(R.id.buttonDownload)
        buttonDownload?.visibility = View.INVISIBLE
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.visibility = View.INVISIBLE
        val numberKeys = arrayOf("Number_1", "Number_2")
        val random = Random()
        indexChoice = random.nextInt(numberKeys.size)
        numberChoice = numberKeys[indexChoice]
        loadTaskFromFirebase("Task_1", numberChoice)
        buttonDownload?.setOnClickListener {
            startDownload()
        }
        buttonNext.setOnClickListener { v ->
            if (number >= 0) {
                buttonBack.visibility = View.VISIBLE
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
            if (number <= 26) {
                loadTaskFromFirebase(taskKeys[number], numberChoice)
                if (number == 26) {
                    buttonNext.text = "Завершить тест"
                }
            } else {
                for (i in scoreArray.indices) {
                    if (scoreArray[i] != null) {
                        scoreArray[i] = scoreArray[i]!! + scoreArray[i]!!
                    }
                }
                globalScore = sumOfScore
                val context = v.context
                val intent = Intent(context, TestResult::class.java)
                intent.putExtra("Score", globalScore)
                context.startActivity(intent)
            }
        }
        buttonBack.setOnClickListener { v ->
            if (number > 0) {
                if (number <= 26) {
                    buttonNext.text = "Перейти к следующему заданию"
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
                if (number <= 26) {
                    loadTaskFromFirebase(taskKeys[number], numberChoice)
                    if (number == 26) {
                        buttonNext.text = "Завершить тест"
                    }
                } else {
                    for (i in scoreArray.indices) {
                        if (scoreArray[i] != null) {
                            scoreArray[i] = scoreArray[i]!! + scoreArray[i]!!
                        }
                    }
                    globalScore = sumOfScore
                    val context = v.context
                    val intent = Intent(context, TestResult::class.java)
                    intent.putExtra("Score", globalScore)
                    context.startActivity(intent)
                }
            }
        }
    }

    fun onClickExit(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
                        buttonDownload!!.visibility = View.VISIBLE
                    } else {
                        buttonDownload!!.visibility = View.INVISIBLE
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