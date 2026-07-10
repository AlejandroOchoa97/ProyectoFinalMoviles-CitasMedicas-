package proyecto.moviles.citasmedicas.ui.screens.patient

/* Inicio del paciente: muestra citas del paciente cargadas desde Room. */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.ui.components.AppointmentCard
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.viewmodel.PatientAppointmentFilter
import proyecto.moviles.citasmedicas.ui.viewmodel.PatientAppointmentsViewModel

@Composable
fun PatientHomeScreen(
    onBack: () -> Unit,
    onScheduleAppointment: () -> Unit,
    onAppointmentDetails: (Int) -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    appointmentRepository: AppointmentRepository? = null,
    doctorRepository: DoctorRepository? = null,
    patientId: Int = 1,
    modifier: Modifier = Modifier
) {
    val viewModel = remember(appointmentRepository, doctorRepository) {
        PatientAppointmentsViewModel(
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository
        )
    }
    val uiState = viewModel.uiState

    // Carga las citas del paciente activo desde Room.
    LaunchedEffect(patientId) {
        viewModel.loadAppointments(patientId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        topBar = { PatientTopBar(onBack, onNavigateProfile) },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 0) { index ->
                when (index) {
                    1 -> onNavigateHistory()
                    2 -> onNavigateProfile()
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onScheduleAppointment,
                containerColor = PrimaryBlue,
                contentColor = AppWhite,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Agendar cita") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 150.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Mis citas", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Text(
                    "Gestiona tus próximas consultas médicas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("Próximas", uiState.selectedFilter == PatientAppointmentFilter.UPCOMING) {
                        viewModel.selectFilter(PatientAppointmentFilter.UPCOMING)
                    }
                    FilterChip("Pendientes", uiState.selectedFilter == PatientAppointmentFilter.PENDING) {
                        viewModel.selectFilter(PatientAppointmentFilter.PENDING)
                    }
                    FilterChip("Pasadas", uiState.selectedFilter == PatientAppointmentFilter.PAST) {
                        viewModel.selectFilter(PatientAppointmentFilter.PAST)
                    }
                }

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = onScheduleAppointment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = AppWhite
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Buscar médico y agendar cita", fontWeight = FontWeight.SemiBold)
                }

                uiState.errorMessage?.let { message ->
                    Spacer(Modifier.height(12.dp))
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
            }

            if (uiState.visibleAppointments.isEmpty()) {
                item {
                    Text(
                        text = "No hay citas para este filtro.",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(uiState.visibleAppointments, key = { it.id }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onDetailsClick = { onAppointmentDetails(appointment.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) PrimaryBlue else BorderSoft,
            labelColor = if (selected) AppWhite else TextSecondary
        ),
        border = null
    )
}

@Composable
private fun PatientTopBar(
    onBack: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Regresar", tint = TextSecondary)
        }
        Text("MediCitas", color = PrimaryBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onProfileClick) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil", tint = PrimaryBlue, modifier = Modifier.size(25.dp))
        }
    }
}

@Preview(
    name = "Inicio Paciente",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun PatientHomeScreenPreview() {
    MediCitasTheme { PatientHomeScreen(onBack = {}, onScheduleAppointment = {}) }
}
