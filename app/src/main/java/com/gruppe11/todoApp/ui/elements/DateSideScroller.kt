package com.gruppe11.todoApp.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gruppe11.todoApp.viewModel.CalendarViewModel
import org.jetbrains.annotations.Nullable
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSideScroller(
    viewModel: CalendarViewModel,
    today: LocalDate,
    selection: LocalDate,
    dates: State<List<LocalDate>>,
    onClick: (LocalDate) -> Unit
) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(1.dp),
    ) {
        items(items = dates.value, itemContent = {day ->
            Column {

                FilterChip(
                    modifier = Modifier.padding(1.dp),
                    selected = selection == day,
                    colors = SelectableChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onBackground,
                        selectedLeadingIconColor = Color.Transparent,
                        selectedTrailingIconColor = Color.Transparent,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        labelColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = Color.Transparent,
                        trailingIconColor = Color.Red,
                        disabledContainerColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        disabledLeadingIconColor = Color.Transparent,
                        disabledSelectedContainerColor = Color.Transparent,
                        disabledTrailingIconColor = Color.Transparent
                    ),
                    onClick = { onClick(day) },
                    label = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(text = day.dayOfMonth.toString())
                            Text(text = day.month.name.removeRange(3,day.month.name.length))
                        }
                    },
                    enabled = true
                )
            }
        }
        )
    }
}
