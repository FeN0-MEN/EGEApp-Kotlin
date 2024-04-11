package com.example.egeapp_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LogingActivity : AppCompatActivity() {
    var message: TextView? = null


    fun StartRegisterActivity(v: View?) {
        val intent = Intent(this, RegActivity::class.java)
        startActivity(intent)
    }
    fun closeCurrentActivity(activity: Activity) {
        activity.finish()
    }

    // [START declare_auth]
    private var mAuth: FirebaseAuth? = null

    // [END declare_auth]
    private var mGoogleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FEFAE0")))
        // [START config_signin]
        // Конфигурация для авторизации через Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
        setContentView(R.layout.activity_loging)
        val currentUser = mAuth!!.currentUser
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Проверка авторизован ли пользователь
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    // [END on_start_check_user]
    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Результат, возвращенный при запуске Intent из Google Sign In Api.get Sign In Intent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Вход в Google прошел успешно, пройдите аутентификацию с помощью Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Не удалось войти в Google, обновите пользовательский интерфейс соответствующим образом
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    // [END onactivityresult]
    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешно вошли в систему, обновление пользовательского интерфейса информацией о вошедшем пользователе
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth!!.currentUser

                    // Получаем информацию о пользователе из объекта GoogleSignInAccount
                    val account = GoogleSignIn.getLastSignedInAccount(this)
                    val name = account?.displayName
                    val email = account?.email

                    // Создаем уникальный идентификатор пользователя
                    val userId = user?.uid

                    // Ссылка на базу данных "Users"
                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("Users")

                    // Создаем подузел для текущего пользователя с его уникальным идентификатором
                    val currentUserRef = usersRef.child(userId ?: "")

                    // Заполняем данные пользователя
                    currentUserRef.child("E-mail").setValue(email)
                    currentUserRef.child("Name").setValue(name)
                    currentUserRef.child("Stats").setValue("") // Пока оставляем пустым

                    updateUI(user)
                } else {
                    // Если вход систему прошёл не удачно
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    // [END auth_with_google]
    // [START signin]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun onClickSignIn(view: View?) {
        signIn()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Функция для кнопки авторизации пользователя
    var firebaseAuth = FirebaseAuth.getInstance()
    fun loginUser(view: View?) {
        val emailEditText = findViewById<EditText>(R.id.emailLog)
        val passwordEditText = findViewById<EditText>(R.id.passwordLog)
        val email = emailEditText.text.toString().trim { it <= ' ' }
        val password = passwordEditText.text.toString().trim { it <= ' ' }
        // Исправить баг
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Пожалуйста, введите e-mail для авторизации", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Пожалуйста, введите пароль для авторизации", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Авторизация успешна, выполняем дальнейшие действия
                    Toast.makeText(this@LogingActivity, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LogingActivity, MainActivity::class.java)
                    startActivity(intent)
                    // Переход на другой экран или выполнение других действий
                } else {
                    // Авторизация провалилась
                    Toast.makeText(this@LogingActivity, "Ошибка авторизации: " + task.exception!!.message, Toast.LENGTH_LONG).show()
                }
            }

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
        @JvmStatic
        fun signOut() {
            FirebaseAuth.getInstance().signOut()
        }
    }
}