package com.example.egeapp_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.egeapp_kotlin.LogingActivity
import com.example.egeapp_kotlin.LogingActivity.Companion.signOut
import com.example.egeapp_kotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun logOut(view: View?) {
        signOut()
        val intent = Intent(this, LogingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun onClickTest(view: View?) {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
    }

    fun onClickChoiceTheory(view: View?) {
        val intent = Intent(this, ChoosingTheoryActivity::class.java)
        startActivity(intent)
    }
}