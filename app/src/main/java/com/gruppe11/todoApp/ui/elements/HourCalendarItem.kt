package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HourCalendarItem(time: String){
    Column(modifier = Modifier.height(60.dp),
        verticalArrangement = Arrangement.SpaceEvenly) {
        HorizontalDivider()
        Text(text = time, fontSize = 18.sp)

    }
}