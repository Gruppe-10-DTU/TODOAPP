package com.gruppe11.todoApp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.gruppe11.todoApp.ui.screens.ShowTaskList
import com.gruppe11.todoApp.ui.theme.TODOAPPTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOAPPTheme {
                ShowTaskList()
            }
        }
    }
}