package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.ErrorBackground
import proyecto.moviles.citasmedicas.ui.theme.ErrorRed
import proyecto.moviles.citasmedicas.ui.theme.InfoBackground
import proyecto.moviles.citasmedicas.ui.theme.InfoText
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.SuccessBackground
import proyecto.moviles.citasmedicas.ui.theme.SuccessText
import proyecto.moviles.citasmedicas.ui.theme.WarningBackground
import proyecto.moviles.citasmedicas.ui.theme.WarningText

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (background, textColor) = statusColors(status)
    Text(
        text = status.uppercase(),
        color = textColor,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    )
}

private fun statusColors(status: String): Pair<Color, Color> = when (status.lowercase()) {
    "confirmada" -> SuccessBackground to SuccessText
    "pendiente" -> WarningBackground to WarningText
    "cancelada" -> ErrorBackground to ErrorRed
    "completada", "en revisión" -> InfoBackground to InfoText
    else -> InfoBackground to InfoText
}

@Preview(name = "Estados", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun StatusBadgePreview() {
    MediCitasTheme { StatusBadge("Confirmada", Modifier.padding(16.dp)) }
}
