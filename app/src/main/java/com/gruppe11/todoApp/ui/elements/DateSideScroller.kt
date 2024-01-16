package com.gruppe11.todoApp.ui.elements

import android.content.res.Resources.getSystem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateSideScroller(
    currentDate: LocalDate,
    dates: State<List<LocalDate>>,
    onDateChange: (LocalDate) -> Unit,
    onTitleClick: () -> Unit
) {
    val listState = rememberLazyListState()
    var selectedDate by remember { mutableStateOf(currentDate) }
    var selectedIndex by remember {
        mutableStateOf(dates.value
            .indexOfFirst { it.dayOfMonth == currentDate.dayOfMonth && it.month == currentDate.month })
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = {
                selectedDate = currentDate
                selectedIndex = dates.value.indexOfFirst { it.isEqual(selectedDate) }
                CoroutineScope(Dispatchers.Main).launch {
                    listState.scrollToItem(
                        index = selectedIndex - 1,
                        scrollOffset = (getSystem().displayMetrics.widthPixels * (0.05F)).toInt()
                    )
                    onDateChange(selectedDate)
                    onTitleClick()
                }
            },
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text(
                text = currentDate
                    .format(DateTimeFormatter.ofPattern("E d. MMMM")),
                fontSize = 18.sp,
            )
        }
        LazyRow(
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(1.dp),
        ) {
            items(items = dates.value, itemContent = { day: LocalDate ->
                Column {
                    FilterChip(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(width = 150.dp, height = 50.dp),
                        selected = selectedDate == day,
                        colors = SelectableChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.background,
                            selectedLeadingIconColor = Color.Transparent,
                            selectedTrailingIconColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            labelColor = MaterialTheme.colorScheme.onPrimary,
                            leadingIconColor = Color.Transparent,
                            trailingIconColor = Color.Red,
                            disabledContainerColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent,
                            disabledLeadingIconColor = Color.Transparent,
                            disabledSelectedContainerColor = Color.Transparent,
                            disabledTrailingIconColor = Color.Transparent
                        ),
                        onClick = {
                            selectedDate = day
                            selectedIndex = dates.value.indexOf(day)
                            onDateChange(day)
                        },
                        label = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = day.format(DateTimeFormatter.ofPattern("E")),
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = day.format(DateTimeFormatter.ofPattern("d. MMM")),
                                    fontSize = 18.sp
                                )
                            }
                        },
                        enabled = true,
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                        )
                    )
                }
            })
        }
        LaunchedEffect(key1 = selectedDate){
            CoroutineScope(Dispatchers.Main).launch {
                selectedIndex = dates.value.indexOfFirst { it.isEqual(selectedDate) }
                listState.scrollToItem(
                    index = selectedIndex - 1,
                    scrollOffset = (getSystem().displayMetrics.widthPixels * (0.05F)).toInt()
                )
            }
        }
    }
}
