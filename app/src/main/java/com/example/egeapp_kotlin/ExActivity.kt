package com.example.egeapp_kotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
    private fun getEditTextValues(): HashMap<String, String> {
        val editTextValues = HashMap<String, String>()
        for (editTextId in editTextIds) {
            val editText = findViewById<EditText>(editTextId)
            val value = editText.text.toString()
            editTextValues[resources.getResourceEntryName(editTextId)] = value
        }
        return editTextValues
    }
    private fun checkEditTextValues(editTextValues: HashMap<String, String>): Boolean {
        for ((_, value) in editTextValues) {
            val intValue = value.toIntOrNull()
            if (intValue != null && intValue >= 1 && intValue <= 10) {
                return true
            }
        }
        return false
    }
}