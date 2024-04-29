package com.example.egeapp_kotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ExActivity : AppCompatActivity() {
    private val editTextIds = arrayOf(
        R.id.exInput1, R.id.exInput2, R.id.exInput3, R.id.exInput4, R.id.exInput5, R.id.exInput6,
        R.id.exInput7, R.id.exInput8, R.id.exInput9, R.id.exInput10, R.id.exInput11, R.id.exInput12,
        R.id.exInput13, R.id.exInput14, R.id.exInput15, R.id.exInput16, R.id.exInput17,
        R.id.exInput18, R.id.exInput19, R.id.exInput20, R.id.exInput21, R.id.exInput22,
        R.id.exInput23, R.id.exInput24, R.id.exInput25, R.id.exInput26, R.id.exInput27
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_ex)

        val buttonExTest = findViewById<Button>(R.id.buttonExTest)
        buttonExTest.setOnClickListener {
            val editTextValues = getEditTextValues()
            val hasValueGreaterThanOne = checkEditTextValues(editTextValues)
            if (hasValueGreaterThanOne) {
                val intent = Intent(this, ExTestActivity::class.java)
                intent.putExtra("editTextValues", editTextValues)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Вы выбрали некорректное количество заданий (1-10)", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getEditTextValues(): HashMap<String, Int> {
        val editTextValues = HashMap<String, Int>()
        for ((index, editTextId) in editTextIds.withIndex()) {
            val editText = findViewById<EditText>(editTextId)
            val value = editText.text.toString().toIntOrNull() ?: 0
            val taskKey = "Task_${index + 1}"
            editTextValues[taskKey] = value
        }
        return editTextValues
    }

    private fun checkEditTextValues(editTextValues: HashMap<String, Int>): Boolean {
        for ((_, value) in editTextValues) {
            if (value in 1..10) {
                return true
            }
        }
        return false
    }
    fun onClickExExit(view: View?) {
        finish()
    }
}