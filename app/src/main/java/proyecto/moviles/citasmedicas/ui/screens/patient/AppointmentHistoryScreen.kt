package proyecto.moviles.citasmedicas.ui.screens.patient

/* Historial: filtra consultas simuladas y conecta las secciones de la navegación inferior. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.components.StatusBadge
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SuccessText
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

private data class HistoryAppointment(
    val id: Int,
    val doctor: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: String,
    val hasPrescription: Boolean
)

private val historyAppointments = listOf(
    HistoryAppointment(1, "Dra. Elena Ramírez", "Cardiología", "15 May, 2024", "09:30 AM", "Completada", true),
    HistoryAppointment(2, "Dr. Ricardo Silva", "Pediatría", "02 May, 2024", "14:00 PM", "Cancelada", false),
    HistoryAppointment(3, "Dra. Sofía Martínez", "Dermatología", "20 Abr, 2024", "11:15 AM", "Completada", true),
    HistoryAppointment(4, "Dr. Manuel Torres", "Medicina General", "10 Abr, 2024", "16:45 PM", "Completada", false)
)

@Composable
fun AppointmentHistoryScreen(
    onNavigateHome: () -> Unit,
    onNavigateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filters = listOf("Todas", "Recetas disponibles", "Canceladas")
    val visibleAppointments = historyAppointments.filter {
        when (selectedFilter) {
            "Recetas disponibles" -> it.hasPrescription
            "Canceladas" -> it.status == "Cancelada"
            else -> true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1) { index ->
                when (index) {
                    0 -> onNavigateHome()
                    2 -> onNavigateProfile()
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("MediCitas", color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(20.dp))
                Text("Historial de citas", color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Revisa tus consultas pasadas y recetas disponibles.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.padding(top = 12.dp)) {
                    filters.forEach { filter ->
                        AssistChip(
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, style = MaterialTheme.typography.bodySmall) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selectedFilter == filter) PrimaryBlue else BorderSoft,
                                labelColor = if (selectedFilter == filter) AppWhite else TextSecondary
                            ),
                            border = null
                        )
                    }
                }
            }
            items(visibleAppointments, key = { it.id }) { appointment ->
                HistoryAppointmentCard(appointment) {
                    scope.launch { snackbarHostState.showSnackbar("Detalle histórico pendiente") }
                }
            }
        }
    }
}

@Composable
private fun HistoryAppointmentCard(appointment: HistoryAppointment, onDetails: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.size(44.dp).background(SecondaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Person, null, tint = PrimaryBlue) }
                Spacer(Modifier.size(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(appointment.doctor, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(appointment.specialty, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                StatusBadge(appointment.status)
            }
            Text("▣  ${appointment.date}        ◷  ${appointment.time}", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                if (appointment.hasPrescription) {
                    Icon(Icons.Filled.Description, null, tint = SuccessText, modifier = Modifier.size(17.dp))
                    Text(" Receta disponible", color = SuccessText, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("Ver detalles") }
            }
        }
    }
}

@Preview(name = "Historial de citas", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun AppointmentHistoryScreenPreview() {
    MediCitasTheme { AppointmentHistoryScreen({}, {}) }
}
