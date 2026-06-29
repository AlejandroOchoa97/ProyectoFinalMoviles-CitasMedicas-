package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.model.Appointment
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@Composable
fun AppointmentCard(appointment: Appointment, onDetailsClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(48.dp).background(SecondaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(initials(appointment.doctorName), color = PrimaryBlue, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(appointment.doctorName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text(appointment.specialty, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                StatusBadge(appointment.status)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.DateRange, null, tint = TextSecondary, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(appointment.date, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Text("◷  ${appointment.time}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            HorizontalDivider(color = BorderSoft)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(appointment.price, color = PrimaryBlue, fontWeight = FontWeight.Medium)
                Button(
                    onClick = onDetailsClick,
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) { Text("Detalles") }
            }
        }
    }
}

private fun initials(name: String): String = name
    .replace("Dra. ", "").replace("Dr. ", "")
    .split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("")

@Preview(name = "Tarjeta de cita", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun AppointmentCardPreview() {
    MediCitasTheme {
        AppointmentCard(SampleData.sampleAppointments.first(), {}, Modifier.padding(16.dp))
    }
}
