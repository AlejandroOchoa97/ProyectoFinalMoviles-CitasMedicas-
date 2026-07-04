package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCalendarCard(modifier: Modifier = Modifier){

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates{})

    /*
    * Contenedor visual del calendario
    * Asignamos el fondo blanco (AppWhite),
    * borde suave y esquinas redondas.
    */
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(width = 1.dp, color = BorderSoft)
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            title = null,
            headline = null,
            colors = DatePickerDefaults.colors(
                containerColor = AppWhite,

                selectedDayContainerColor = PrimaryBlue,
                selectedDayContentColor = AppWhite,

                todayDateBorderColor = PrimaryBlue,
                todayContentColor = PrimaryBlue,

                dayContentColor = TextPrimary,
                weekdayContentColor = TextSecondary,
                navigationContentColor = TextPrimary,

                yearContentColor = TextPrimary,
                selectedYearContainerColor = PrimaryBlue,
                selectedYearContentColor = AppWhite
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Schedule Calendar Card",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390
)
@Composable
fun ScheduleCalendarCardPreview(){
    MediCitasTheme {
        ScheduleCalendarCard()
    }
}

