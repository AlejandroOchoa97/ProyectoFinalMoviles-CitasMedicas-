package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun ScheduleCalendarCard(
    modifier: Modifier = Modifier,
    visibleMonth: YearMonth = YearMonth.now(),
    selectedDate: LocalDate? = LocalDate.now(),
    disabledDates: Set<LocalDate> = emptySet(),
    canNavigateToPreviousMonth: Boolean = true,
    onPreviousMonthClick: () -> Unit = {},
    onNextMonthClick: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {}
) {
    // Esta es la tarjeta con bordes redondeados donde esta el calendario.
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft)
    ) {
        // Column ordena verticalmente: encabezado del mes, dias de semana y grilla.
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CalendarHeader(
                visibleMonth = visibleMonth,
                canNavigateToPreviousMonth = canNavigateToPreviousMonth,
                onPreviousMonthClick = onPreviousMonthClick,
                onNextMonthClick = onNextMonthClick
            )

            WeekdayHeader()

            CalendarGrid(
                visibleMonth = visibleMonth,
                selectedDate = selectedDate,
                disabledDates = disabledDates,
                onDateSelected = onDateSelected
            )
        }
    }
}

/**
 * Encabezado del calendario.
 *
 * Muestra el mes visible y los botones de navegacion entre meses.
 */
@Composable
private fun CalendarHeader(
    visibleMonth: YearMonth,
    canNavigateToPreviousMonth: Boolean,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Texto del mes visible, por ejemplo: "Octubre 2024".
        Text(
            text = visibleMonth.title(),
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        // Icono del proyecto para regresar al mes anterior.
        IconButton(
            onClick = onPreviousMonthClick,
            enabled = canNavigateToPreviousMonth
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "Mes anterior",
                tint = TextSecondary.copy(alpha = if (canNavigateToPreviousMonth) 1f else 0.35f)
            )
        }

        // Icono del proyecto para avanzar al mes siguiente.
        IconButton(onClick = onNextMonthClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "Mes siguiente",
                tint = TextSecondary
            )
        }
    }
}

/**
 * Encabezado fijo de dias de la semana.
 */
@Composable
private fun WeekdayHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("D", "L", "M", "M", "J", "V", "S").forEach { day ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Grilla mensual dividida en semanas de siete dias.
 */
@Composable
private fun CalendarGrid(
    visibleMonth: YearMonth,
    selectedDate: LocalDate?,
    disabledDates: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visibleMonth.calendarRows().forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    // El componente no calcula reglas; solo respeta disabledDates.
                    val enabled = date != null && date !in disabledDates

                    CalendarDayCell(
                        date = date,
                        selected = date != null && date == selectedDate,
                        enabled = enabled,
                        onDateSelected = onDateSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Celda reutilizable que dibuja un dia normal, seleccionado, vacio o deshabilitado.
 */
@Composable
private fun CalendarDayCell(
    date: LocalDate?,
    selected: Boolean,
    enabled: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    // Cada celda conserva el mismo alto para que la grilla no brinque.
    Box(
        modifier = modifier.height(36.dp),
        contentAlignment = Alignment.Center
    ) {
        if (date == null) {
            // Los null son espacios vacios para alinear el primer dia del mes.
            Spacer(modifier = Modifier.size(34.dp))
        } else {
            val textColor = when {
                selected -> AppWhite
                enabled -> TextPrimary
                else -> TextSecondary.copy(alpha = 0.45f)
            }

            val backgroundColor = when {
                selected -> PrimaryBlue
                enabled -> AppWhite
                else -> AppWhite
            }

            // Solo las fechas habilitadas se pueden tocar.
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable(enabled = enabled) { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Convierte el mes visible en filas de siete columnas.
 *
 * Las celdas null representan espacios vacios antes del dia 1 y despues del ultimo dia.
 */
private fun YearMonth.calendarRows(): List<List<LocalDate?>> {
    // dayOfWeek.value devuelve lunes=1 ... domingo=7; con modulo domingo queda en 0.
    val firstDayOffset = atDay(1).dayOfWeek.value % 7

    val cells = buildList {
        // Espacios antes del dia 1.
        repeat(firstDayOffset) { add(null) }

        // Dias reales del mes.
        for (day in 1..lengthOfMonth()) {
            add(atDay(day))
        }

        // Espacios al final para completar la ultima semana.
        while (size % 7 != 0) {
            add(null)
        }
    }

    return cells.chunked(7)
}

/**
 * Formatea el titulo del mes en espanol sin depender del idioma del dispositivo.
 */
private fun YearMonth.title(): String {
    val monthName = when (monthValue) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        else -> "Diciembre"
    }
    return "$monthName $year"
}

@Preview(
    name = "Schedule Calendar Card",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390
)
@Composable
fun ScheduleCalendarCardPreview() {
    MediCitasTheme {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        Box(
            modifier = Modifier
                .background(SecondaryBlue.copy(alpha = 0.15f))
                .padding(16.dp)
        ) {
            ScheduleCalendarCard(
                visibleMonth = YearMonth.now(),
                selectedDate = selectedDate,
                disabledDates = emptySet(),
                canNavigateToPreviousMonth = true,
                onPreviousMonthClick = {},
                onNextMonthClick = {},
                onDateSelected = { selectedDate = it }
            )
        }
    }
}