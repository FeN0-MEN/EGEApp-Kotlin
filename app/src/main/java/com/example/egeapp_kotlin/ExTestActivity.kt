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
import android.os.Handler
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

class ExTestActivity : AppCompatActivity() {
    private var TaskText: TextView? = null
    private var imageUrl: String? = null
    private var downloadUrl: String? = null
    private var rightAnswer: String? = null
    private var imageBox: ImageView? = null
    private var buttonDownload: Button? = null
    private var textForTask: String? = null
    private var helpText: String? = null
    private var helpImg: String? = null
    private var globalExScore = 0
    private var scoreForTask: Int? = null
    private var answerEditText: EditText? = null
    private var number = 0
    private var index = 0
    private var sumOfScore = 0
    private var numberChoice: String? = null
    private var indexChoice = 0
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var seconds = 0
    private var isRunning = false
    private lateinit var handler: Handler
    private val numberKeys = arrayOf("Number_1", "Number_2") // , "Number_3", "Number_4", "Number_5", "Number_6", "Number_7", "Number_8", "Number_9", "Number_10"
    private val remainingKeys = numberKeys.toMutableList()
    private val loadedTasks = mutableListOf<Pair<String, String?>>()
    private var currentIndex = 0
    private var lastTaskKey = ""
    private var taskNumberCounter = "1"
    private var lastCurrentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_ex_test)

        val textViewNum = findViewById<TextView>(R.id.textViewNum)
        buttonDownload = findViewById(R.id.buttonDownload)
        answerEditText = findViewById(R.id.AnswerEditText)
        imageBox = findViewById(R.id.imageBox)
        TaskText = findViewById(R.id.TextTask)
        buttonDownload?.isEnabled = false
        buttonDownload?.setBackgroundColor(Color.parseColor("#DAD6BC"))
        val buttonNext = findViewById<ImageButton>(R.id.buttonNext)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        val buttonHelp = findViewById<Button>(R.id.buttonHelp)
        buttonBack.isEnabled = false
        buttonBack.setBackgroundResource(R.drawable.image_button_left_blocked)

        val editTextValues: HashMap<String, Int> =
            intent.getSerializableExtra("editTextValues") as HashMap<String, Int>
        val allTask = editTextValues.values.sum()
        val firstKey: String? =
            editTextValues.filterKeys { editTextValues[it]!! > 0 }.keys.firstOrNull()
        val answerArray = arrayOfNulls<String>(allTask)
        val scoreArray = arrayOfNulls<Int>(allTask)
        val firstValue = editTextValues[firstKey]
        val keysTasks =
            editTextValues.filterKeys { key -> editTextValues[key]!! > 0 }.keys.toMutableSet()
        val valuesTasks = editTextValues.filterValues { value -> value > 0 }.values
        // Получение пары списков заданий и номеров с использованием функции generateTaskLists
        val (taskNames, taskNumbers) = generateTaskLists(editTextValues, ::getRandomKey)


        buttonDownload?.setOnClickListener {
            startDownload()
        }

        timerTextView = findViewById(R.id.timerTextView)
        handler = Handler()
        startTimer()
        // Загрузка первого задания


        // Проверяем, что есть задания для загрузки
        if (currentIndex < taskNames.size && currentIndex < taskNumbers.size) {
            // Получаем ключи первого задания и номера задания
            val taskKey = taskNames[currentIndex]
            val numberKey = taskNumbers[currentIndex]
            // Устанавливаем счётчик задания
            textViewNum.text = generateCounter(taskKey, currentIndex, taskNames)
            // Загружаем первое задание из Firebase
            loadTaskFromFirebase(taskKey, numberKey)

            // Увеличиваем значение currentIndex на 1
            lastCurrentIndex = currentIndex
        } else {
            // Если нет заданий для загрузки, выполняем какие-то действия или выводим сообщение об этом
            // Например:
            // Toast.makeText(context, "Нет заданий для загрузки", Toast.LENGTH_SHORT).show()
        }

        // Обработчик события для кнопки buttonNext
        buttonNext.setOnClickListener { v ->
            buttonBack.isEnabled = true
            buttonBack.setBackgroundResource(R.drawable.image_button_left)
            val nice = v.context
            val userInput = answerEditText?.text.toString().trim { it <= ' ' }
            if (answerEditText != null) {
                answerArray[index] = userInput
                answerEditText!!.setText("")
                if (userInput == rightAnswer) {
                    scoreArray[index] = 1
                } else {
                    if (scoreArray[index] != null) {
                        if (scoreArray[index] == 1) { scoreArray[index] = 0 }
                    }
                }
            }
            index++
            if (TextUtils.isEmpty(userInput)) {
                Toast.makeText(nice, "Заполните ответ", Toast.LENGTH_SHORT).show()
            }
            // Проверяем, что есть еще задания для загрузки
            if (currentIndex < taskNames.size && currentIndex < taskNumbers.size) {
                lastCurrentIndex = currentIndex
                currentIndex++
                if (currentIndex >= taskNames.size)
                {
                    for (score in scoreArray) {
                        if (score != null) {
                            sumOfScore += score
                        }
                    }

                    val context = v.context
                    val startResult = Intent(context, ExTestResultActivity::class.java)
                    startResult.putExtra("scoreForEx", sumOfScore)
                    startResult.putExtra("timeForEx", seconds * 1000L)
                    startResult.putExtra("allForEx", taskNumbers.size)
                    context.startActivity(startResult)
                    return@setOnClickListener
                }
                if (currentIndex == taskNames.size-1) buttonNext.setBackgroundResource(R.drawable.image_button_right_finish)
                // Получаем ключи текущего задания и номера задания
                val taskKey = taskNames[currentIndex]
                val numberKey = taskNumbers[currentIndex]
                // Устанавливаем счётчик задания
                textViewNum.text = generateCounter(taskKey, currentIndex, taskNames)
                // Загружаем задание из Firebase
                loadTaskFromFirebase(taskKey, numberKey)

                // После загрузки, возможно, нужно обновить текущий индекс задания
                // Например, если это происходит в асинхронном блоке кода
                // Для примера, предположим, что после загрузки задания мы хотим увеличить индекс
                lastCurrentIndex = currentIndex
            }
        }

        buttonBack.setOnClickListener { v ->
            buttonNext.setBackgroundResource(R.drawable.image_button_right)
            val nice = v.context
            val userInput = answerEditText?.text.toString().trim { it <= ' ' }
            if (answerEditText != null) {
                answerArray[index] = userInput
                answerEditText!!.setText("")
                if (userInput == rightAnswer) {
                    scoreArray[index] = 1
                } else {
                    if (scoreArray[index] != null) {
                        if (scoreArray[index] == 1) {
                            scoreArray[index] = 0
                        }
                    }
                }
            }
            index--
            if (TextUtils.isEmpty(userInput)) {
                Toast.makeText(nice, "Заполните ответ", Toast.LENGTH_SHORT).show()
            }
            lastCurrentIndex = currentIndex
            // Уменьшаем индекс текущего задания
            currentIndex--
            // Проверяем, что есть еще задания для загрузки
            if (currentIndex >= 0) {
                if (currentIndex == 0)
                {
                    buttonBack.isEnabled = false
                    buttonBack.setBackgroundResource(R.drawable.image_button_left_blocked)
                }
                // Получаем ключи текущего задания и номера задания
                val taskKey = taskNames[currentIndex]
                val numberKey = taskNumbers[currentIndex]
                // Устанавливаем счётчик задания
                textViewNum.text = generateCounter(taskKey, currentIndex, taskNames)
                // Загружаем задание из Firebase
                loadTaskFromFirebase(taskKey, numberKey)

                // После загрузки, возможно, нужно обновить текущий индекс задания
                // Например, если это происходит в асинхронном блоке кода

            } else {
                // Если заданий больше нет, выполняем какие-то действия или выводим сообщение об этом
                // Например:
                // Toast.makeText(context, "Больше нет заданий", Toast.LENGTH_SHORT).show()
            }
        }

        buttonHelp.setOnClickListener { v ->
            val taskKey = taskNames[currentIndex]
            val numberKey = taskNumbers[currentIndex]
            loadHelpFromFirebase(this, taskKey, numberKey)
        }


    }
    // Функция создания номера для счётчика
    fun generateCounter(taskKey: String, currentIndex: Int, taskNames: List <String>): String {
        // Получаем предпоследний символ из taskKey
        val secondToLastCharTask = if (taskKey.length >= 2) taskKey[taskKey.length - 2] else null

        // Формируем слово в зависимости от предпоследних символов и изменений в taskKey
        val word = buildString {
            // Если предпоследний символ taskKey равен "1" или "2", включаем его в слово
            if (secondToLastCharTask == '1' || secondToLastCharTask == '2') {
                append(secondToLastCharTask)
                append(taskKey.last() + ".")
            } else {
                append(taskKey.last() + ".")
            }

            // Если taskKey изменилось, обновляем счетчик второй части слова
            if (taskKey != lastTaskKey) {
                if (currentIndex < lastCurrentIndex) {
                    if (taskNames.count { it.equals(taskKey) } > 0) {
                        // Подсчитываем, сколько раз текущий lastTaskKey встречается в taskNames
                        val occurrences = taskNames.filter { it.equals(taskKey) }.count()
                        // Приравниваем значение occurrences к переменной taskNumberCounter
                        taskNumberCounter = occurrences.toString()
                    }
                    if (taskKey == lastTaskKey) taskNumberCounter = (taskNumberCounter.toInt() - 1).toString()
                    lastTaskKey = taskKey
                }
                else
                {
                    lastTaskKey = taskKey
                    // Сбрасываем счетчик номеров заданий до "1"
                    taskNumberCounter = "1"
                }

            } else {
                // Если currentIndex уменьшился, уменьшаем счетчик номеров заданий на 1
                if (currentIndex < lastCurrentIndex) {
                    if (taskNames.count { it.equals(taskKey) } > 0) {
                        // Подсчитываем, сколько раз текущий lastTaskKey встречается в taskNames
                        val occurrences = taskNames.filter { it.equals(taskKey) }.count()
                        // Приравниваем значение occurrences к переменной taskNumberCounter
                        taskNumberCounter = occurrences.toString()
                    }
                        if (taskKey == lastTaskKey) taskNumberCounter = (taskNumberCounter.toInt() - 1).toString()
                } else {
                    // Увеличиваем счетчик номеров заданий на 1
                    taskNumberCounter = (taskNumberCounter.toInt() + 1).toString()
                }
            }

            // Добавляем значение счетчика номеров заданий к слову
            append(taskNumberCounter)
        }

        // Обновляем значение lastCurrentIndex
        lastCurrentIndex = currentIndex

        return word
    }
    // Функция создания списка заданий
    fun generateTaskLists(editTextValues: HashMap<String, Int>, getRandomKey: () -> String?): Pair<List<String>, List<String>> {
        val taskNames = mutableListOf<String>()
        val taskNumbers = mutableListOf<String>()
        for ((task, value) in editTextValues) {
            repeat(value) {
                taskNames.add(task)
                val randomNumber = getRandomKey() ?: ""
                taskNumbers.add(randomNumber)
            }
        }
        return Pair(taskNames, taskNumbers)
    }
    // Функция получения случайного ключа для номера задания
    fun getRandomKey(): String? {
        if (remainingKeys.isEmpty()) {
            // Если все ключи уже использованы, возвращаем первоначальное состояние множества
            remainingKeys.addAll(numberKeys)
        }
        val randomIndex = (0 until remainingKeys.size).random() // Генерация случайного индекса
        val randomKey = remainingKeys[randomIndex] // Получение случайного ключа
        remainingKeys.removeAt(randomIndex) // Удаление выбранного ключа из списка
        return randomKey
    }
    // функция загрузки заданий
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
                    //helpText = dataSnapshot.child("HelpText").getValue(String::class.java)
                    //helpImg = dataSnapshot.child("HelpImg").getValue(String::class.java)

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

    private fun loadHelpFromFirebase(context: Context, taskKey: String, numberKey: String?) {
        // Получение ссылки на узел подсказки в Firebase
        val helpRef = FirebaseDatabase.getInstance("https://appbase-66912-default-rtdb.europe-west1.firebasedatabase.app").reference.child(taskKey).child(numberKey!!)
        helpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Получение значений из Firebase и присвоение их переменным
                    helpText = dataSnapshot.child("HelpText").getValue(String::class.java)
                    helpImg = dataSnapshot.child("HelpImg").getValue(String::class.java)
                    textForTask = dataSnapshot.child("Question").getValue(String::class.java)
                    imageUrl = dataSnapshot.child("Img").getValue(String::class.java)

                    // Создание нового интента для запуска активности подсказки
                    val intent = Intent(context, HelpActivity::class.java)
                    // Передача данных подсказки в активность подсказки через экстра
                    intent.putExtra("helpText", helpText)
                    intent.putExtra("helpImg", helpImg)
                    intent.putExtra("textForTask", textForTask)
                    intent.putExtra("imageUrl", imageUrl)
                    // Запуск активности подсказки
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок чтения данных из Firebase
            }
        })
    }

    // Обработчик таймера
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
    // Функция для обработки события выхода
    fun onClickExit(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    // Функция для обработки события нажатия на кнопку подсказки
    fun onClickHelp(view: View?) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }
    // Функция обработки загрузки файлов
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