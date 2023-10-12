package com.gruppe11.todoApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addTask: View = findViewById(R.id.floatingActionButton)
        addTask.setOnClickListener{
            //Navigate to create task page
        }
    }

    private fun onAddButtonClicked() {

    }
}
