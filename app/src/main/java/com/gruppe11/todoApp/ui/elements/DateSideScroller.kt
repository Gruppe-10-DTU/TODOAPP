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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSideScroller(
    viewModel: CalendarViewModel,
    onTitleClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val uiState = viewModel.uiState.collectAsState()
    val dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextButton(
            onClick = {
                viewModel.onSelectedDayChange(uiState.value.currentDay)
                CoroutineScope(Dispatchers.Main).launch {
                    listState.scrollToItem(
                        index = viewModel.startDay.datesUntil(uiState.value.currentDay).count().toInt(),
                        scrollOffset = (getSystem().displayMetrics.widthPixels * (-0.65F)).toInt()
                    )
                    onTitleClick()
                }
            },
            colors = ButtonColors(containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text(
            text = uiState.value.currentDay
                .format(DateTimeFormatter.ofPattern("E d. MMMM")),
            fontSize = 18.sp,
            )
        }
        LazyRow(
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(1.dp),
        ) {
            items(items = dates.value, itemContent = { day ->
                Column {
                    FilterChip(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(width = 150.dp, height = 50.dp),
                        selected = uiState.value.selectedDay == day,
                        colors = SelectableChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.background,
                            selectedLeadingIconColor = Color.Transparent,
                            selectedTrailingIconColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            labelColor = MaterialTheme.colorScheme.onBackground,
                            leadingIconColor = Color.Transparent,
                            trailingIconColor = Color.Red,
                            disabledContainerColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent,
                            disabledLeadingIconColor = Color.Transparent,
                            disabledSelectedContainerColor = Color.Transparent,
                            disabledTrailingIconColor = Color.Transparent
                        ),
                        onClick = { viewModel.onSelectedDayChange(day) },
                        label = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = day.format(DateTimeFormatter.ofPattern("E")),
                                    fontSize = 13.sp)
                                Text(text = day.format(DateTimeFormatter.ofPattern("d. MMM")),
                                    fontSize = 18.sp)
                            }
                        },
                        enabled = true,
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                        )
                    )
                }
            }

            )
            CoroutineScope(Dispatchers.Main).launch {
                listState.scrollToItem(
                    index = viewModel.startDay.datesUntil(uiState.value.selectedDay).count().toInt(),
                    scrollOffset = (getSystem().displayMetrics.widthPixels * (-0.65F)).toInt()
                )
            }
        }
    }
}
