package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gruppe11.todoApp.model.Task

@Composable
fun EditTaskDialog(
    task: Task,
    editTask: (Int) -> Unit,
    deleteTask: () -> Unit,
    dismissDialog: () -> Unit
) {
    Dialog(
        onDismissRequest = dismissDialog,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        ElevatedCard(
            modifier = Modifier.width(150.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp, 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = {
                        editTask(task.id)
                        dismissDialog()
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(text = "Edit Task")
                    Spacer(modifier = Modifier.width(30.dp))
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Task",
                        modifier = Modifier.scale(0.75F)
                    )
                }
                HorizontalDivider(modifier = Modifier.width(140.dp))
                TextButton(
                    onClick = {
                        deleteTask()
                        dismissDialog()
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.error,
                        disabledContentColor = MaterialTheme.colorScheme.error,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.width(150.dp),
                ) {
                    Text(text = "Delete Task", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(15.dp))
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = "Delete Task",
                        modifier = Modifier.scale(0.75F)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun EditTaskDialogPreview(){
    EditTaskDialog(Task(), {}, {}, {})
}