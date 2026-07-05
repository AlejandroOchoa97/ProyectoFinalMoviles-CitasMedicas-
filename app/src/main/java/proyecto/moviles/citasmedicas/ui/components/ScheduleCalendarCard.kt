package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

/**
 * Calendario ligero utilizado durante el agendamiento.
 *
 * Se implementa con componentes básicos de Compose para que también funcione dentro de Preview.
 * Por ahora representa octubre de 2024; posteriormente podrá recibir el mes desde un ViewModel.
 */
@Composable
fun ScheduleCalendarCard(modifier: Modifier = Modifier) {
    // Día seleccionado únicamente para la demostración visual.
    var selectedDay by remember { mutableIntStateOf(13) }
    val weekDays = listOf("D", "L", "M", "M", "J", "V", "S")

    // Los valores nulos representan espacios antes del primer día del mes.
    val monthDays: List<Int?> = listOf(null, null) + (1..31).toList()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Encabezado del mes y controles reservados para navegación entre meses.
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Octubre 2024",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, "Mes anterior", tint = TextSecondary)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, "Mes siguiente", tint = TextSecondary)
                }
            }

            // Iniciales de los días de la semana.
            Row(Modifier.fillMaxWidth()) {
                weekDays.forEach { day ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(day, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Cuadrícula mensual dividida en semanas de siete días.
            monthDays.chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth()) {
                    repeat(7) { index ->
                        val day = week.getOrNull(index)
                        CalendarDay(
                            day = day,
                            selected = day == selectedDay,
                            onClick = { if (day != null) selectedDay = day },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/** Celda reutilizable que dibuja un día normal o el día seleccionado. */
@Composable
private fun CalendarDay(day: Int?, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(vertical = 3.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(if (selected) PrimaryBlue else AppWhite, CircleShape)
                .clickable(enabled = day != null, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (day != null) {
                Text(
                    text = day.toString(),
                    color = if (selected) AppWhite else TextPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(
    name = "Schedule Calendar Card",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390
)
@Composable
fun ScheduleCalendarCardPreview() {
    MediCitasTheme { ScheduleCalendarCard(Modifier.padding(16.dp)) }
}
