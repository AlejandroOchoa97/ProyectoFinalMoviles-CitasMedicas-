package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val TimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

/**
 * Botón visual para seleccionar un horario de cita.
 *
 * Este componente solo muestra el horario y avisa cuando se presiona.
 * La lógica de selección debe manejarse en la pantalla o ViewModel.
 */
@Composable
fun TimeSlotButton(
    time: LocalTime,
    selected: Boolean,
    onClick: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = { onClick(time) },
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) PrimaryBlue else BorderSoft
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) SecondaryBlue else AppWhite,
            contentColor = if (selected) PrimaryBlue else TextPrimary,
            disabledContainerColor = AppWhite,
            disabledContentColor = TextPrimary.copy(alpha = 0.35f)
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = time.format(TimeFormatter),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun TimeSlotButtonPreview() {
    MediCitasTheme {
        TimeSlotButton(
            time = LocalTime.of(11, 30),
            selected = true,
            onClick = {}
        )
    }
}