package proyecto.moviles.citasmedicas.ui.screens.patient

/* Historial del paciente: muestra citas completadas/canceladas desde Room. */

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
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
import proyecto.moviles.citasmedicas.ui.viewmodel.AppointmentHistoryViewModel
import proyecto.moviles.citasmedicas.ui.viewmodel.HistoryFilter
import proyecto.moviles.citasmedicas.ui.viewmodel.PatientHistoryAppointment

@Composable
fun AppointmentHistoryScreen(
    onNavigateHome: () -> Unit,
    onNavigateProfile: () -> Unit,
    onAppointmentDetails: (Int) -> Unit = {},
    appointmentRepository: AppointmentRepository? = null,
    doctorRepository: DoctorRepository? = null,
    patientId: Int = 1,
    modifier: Modifier = Modifier
) {
    val viewModel = remember(appointmentRepository, doctorRepository) {
        AppointmentHistoryViewModel(
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository
        )
    }
    val uiState = viewModel.uiState

    LaunchedEffect(patientId) {
        viewModel.loadHistory(patientId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("MediCitas", color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(20.dp))
                Text("Historial de citas", color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Revisa tus consultas pasadas y recetas disponibles.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)

                Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.padding(top = 12.dp)) {
                    HistoryChip("Todas", uiState.selectedFilter == HistoryFilter.ALL) {
                        viewModel.selectFilter(HistoryFilter.ALL)
                    }
                    HistoryChip("Recetas disponibles", uiState.selectedFilter == HistoryFilter.PRESCRIPTIONS) {
                        viewModel.selectFilter(HistoryFilter.PRESCRIPTIONS)
                    }
                    HistoryChip("Canceladas", uiState.selectedFilter == HistoryFilter.CANCELLED) {
                        viewModel.selectFilter(HistoryFilter.CANCELLED)
                    }
                }

                uiState.errorMessage?.let { message ->
                    Spacer(Modifier.size(12.dp))
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
            }

            if (uiState.visibleAppointments.isEmpty()) {
                item {
                    Text(
                        "No hay citas en el historial para este filtro.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(uiState.visibleAppointments, key = { it.id }) { appointment ->
                    HistoryAppointmentCard(appointment) {
                        onAppointmentDetails(appointment.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) PrimaryBlue else BorderSoft,
            labelColor = if (selected) AppWhite else TextSecondary
        ),
        border = null
    )
}

@Composable
private fun HistoryAppointmentCard(
    appointment: PatientHistoryAppointment,
    onDetails: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(SecondaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, null, tint = PrimaryBlue)
                }
                Spacer(Modifier.size(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(appointment.doctor, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text(appointment.specialty, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                StatusBadge(appointment.status)
            }

            Text(
                "${appointment.date.format(dateFormatter)}        ${appointment.time.format(timeFormatter)}",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

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
                ) {
                    Text("Ver detalles")
                }
            }
        }
    }
}

@Preview(name = "Historial de citas", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun AppointmentHistoryScreenPreview() {
    MediCitasTheme { AppointmentHistoryScreen({}, {}) }
}
