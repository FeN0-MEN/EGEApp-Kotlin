package com.example.egeapp_kotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class StatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_stat)

        // Получение ссылки на элемент LineChart из макета
        val lineChart = findViewById<LineChart>(R.id.lineChart1)
        val lineChart2 = findViewById<LineChart>(R.id.lineChart2)

        // Создание массива точек данных
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 50f))
        entries.add(Entry(2f, 70f))
        entries.add(Entry(3f, 60f))
        entries.add(Entry(4f, 80f))
        entries.add(Entry(5f, 75f))

        // Создание набора данных для графика
        val dataSet = LineDataSet(entries, "Пример графика")
        val greenColor = Color.parseColor("#606C38")
        // Настройка внешнего вида набора данных
        dataSet.color = greenColor
        dataSet.valueTextColor = greenColor

        // Создание списка наборов данных
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        // Создание объекта LineData, содержащего все наборы данных
        val lineData = LineData(dataSets)

        // Установка данных на график
        lineChart.data = lineData
        lineChart2.data = lineData

        // Опциональные настройки графика
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.text = "Описание графика"
        lineChart.animateY(1000)
    }
    fun onClickStatExit(view: View?) {
        finish()
    }
}