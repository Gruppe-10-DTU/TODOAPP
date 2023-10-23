package com.gruppe11.todoApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.gruppe11.todoApp.ui.screens.ShowTaskList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addTask: View = findViewById(R.id.floatingActionButton)
        addTask.setOnClickListener{
        }
            //Navigate to create task page
        setContent {
            ShowTaskList()
        }
    }

    private fun onAddButtonClicked() {

    }
}
