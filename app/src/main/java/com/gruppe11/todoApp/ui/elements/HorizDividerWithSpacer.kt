package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizDividerWithSpacer (){
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(10.dp))
}