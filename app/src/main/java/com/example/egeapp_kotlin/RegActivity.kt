package com.example.egeapp_kotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.LogingActivity
import com.example.egeapp_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        setContentView(R.layout.activity_reg)
    }

    fun StartLogingActivity(v: View?) {
        val intent = Intent(this, LogingActivity::class.java)
        startActivity(intent)
    }

    fun registerUser(view: View?) {
        val emailEditText = findViewById<EditText>(R.id.background)
        val nameEditText = findViewById<EditText>(R.id.Name)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val passwordEditText2 = findViewById<EditText>(R.id.passwordEditText2)
        val email = emailEditText.text.toString().trim { it <= ' ' }
        val password = passwordEditText.text.toString().trim { it <= ' ' }
        val password2 = passwordEditText2.text.toString().trim { it <= ' ' }
        val name = nameEditText.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Пожалуйста, введите почту", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Пожалуйста, введите пароль", Toast.LENGTH_SHORT).show()
            return
        }
        if ((TextUtils.isEmpty(password2)) || (password != password2)) {
            Toast.makeText(this, "Пожалуйста, повторите тот же пароль", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Пожалуйста, введите ваше имя", Toast.LENGTH_SHORT).show()
            return
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Получаем уникальный идентификатор пользователя
                    val currentUser = firebaseAuth.currentUser
                    val userId = currentUser?.uid

                    // Ссылка на базу данных "Users"
                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("Users")

                    // Создаем подузел для текущего пользователя с его уникальным идентификатором
                    val currentUserRef = usersRef.child(userId ?: "")

                    // Заполняем данные пользователя
                    currentUserRef.child("E-mail").setValue(email)
                    currentUserRef.child("Name").setValue(name)
                    currentUserRef.child("Stats").setValue("") // Пока оставляем пустым

                    Toast.makeText(this@RegActivity, "Регистрация завершена", Toast.LENGTH_SHORT).show()

                    // Пользователь успешно зарегистрирован, можно перейти на другой экран или выполнить другие действия
                } else {
                    Toast.makeText(this@RegActivity, "Ошибка регистрации данных: " + Objects.requireNonNull(task.exception)!!.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}