package com.gruppe11.todoApp.ui.elements

import android.app.PictureInPictureUiState
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.util.LocalePreferences.FirstDayOfWeek.Days
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.model.CalendarScreenState
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSideScroller(
    viewModel: CalendarViewModel,
) {
    val listState = rememberLazyListState()
    val uiState = viewModel.uiState.collectAsState()
    val dates = viewModel.dates.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            text = uiState.value.currentDay
                .format(DateTimeFormatter.ofPattern("E d. MMMM")),
            fontSize = 22.sp
        )
        LazyRow(
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(1.dp),
        ) {
            items(items = dates.value, itemContent = { day ->
                Column {
                    FilterChip(
                        modifier = Modifier.padding(1.dp),
                        selected = uiState.value.selectedDay == day,
                        colors = SelectableChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                            selectedLeadingIconColor = Color.Transparent,
                            selectedTrailingIconColor = Color.Transparent,
                            containerColor = MaterialTheme.colorScheme.surface,
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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = day.dayOfMonth.toString())
                                Text(text = day.month.name.removeRange(3, day.month.name.length))
                            }
                        },
                        enabled = true
                    )
                }
            }

            )
            CoroutineScope(Dispatchers.Main).launch {
                listState.scrollToItem(viewModel.startDay.datesUntil(uiState.value.selectedDay,
                    Period.ofDays(1)).count().toInt(), -300)
            }

        }

    }
}
