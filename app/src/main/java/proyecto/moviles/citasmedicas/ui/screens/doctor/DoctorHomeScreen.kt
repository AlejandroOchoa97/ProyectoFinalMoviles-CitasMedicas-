package proyecto.moviles.citasmedicas.ui.screens.doctor

/* Inicio médico: filtra citas del día y abre detalle o configuración de disponibilidad. */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.components.DoctorAppointmentCard
import proyecto.moviles.citasmedicas.ui.theme.*
import proyecto.moviles.citasmedicas.ui.viewmodel.DoctorAppointmentFilter
import proyecto.moviles.citasmedicas.ui.viewmodel.DoctorHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    onBack: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAppointmentClick: (Int) -> Unit = {},
    onNavigateToAvailability: () -> Unit = {},
    appointmentRepository: AppointmentRepository? = null,
    patientRepository: PatientRepository? = null,
    doctorId: Int = 1
) {
    // ViewModel temporal para cargar las citas del médico desde Room.
    val viewModel = remember(appointmentRepository, patientRepository) {
        DoctorHomeViewModel(
            appointmentRepository = appointmentRepository,
            patientRepository = patientRepository
        )
    }

    val uiState = viewModel.uiState

    LaunchedEffect(doctorId) {
        viewModel.loadAppointments(doctorId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MediCitas",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = PrimaryBlue)
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppWhite)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 0,
                middleLabel = "Disponibilidad",
                onItemSelected = { index ->
                    if (index == 1) onNavigateToAvailability()
                    if (index == 2) onProfileClick()
                }
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = uiState.doctorName,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            
            Text(
                text = "Mis citas de hoy",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Filter Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = uiState.selectedFilter == DoctorAppointmentFilter.ALL,
                    label = "Todas (${uiState.appointments.size})",
                    onClick = { viewModel.selectFilter(DoctorAppointmentFilter.ALL) }
                )
                FilterChip(
                    selected = uiState.selectedFilter == DoctorAppointmentFilter.URGENT,
                    label = "Urgentes (${uiState.urgentCount})",
                    onClick = { viewModel.selectFilter(DoctorAppointmentFilter.URGENT) }
                )
                FilterChip(
                    selected = uiState.selectedFilter == DoctorAppointmentFilter.FOLLOW_UP,
                    label = "Seguimiento (${uiState.followUpCount})",
                    onClick = { viewModel.selectFilter(DoctorAppointmentFilter.FOLLOW_UP) }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(uiState.visibleAppointments) { appointment ->
                    DoctorAppointmentCard(
                        appointment = appointment,
                        onClick = { onAppointmentClick(appointment.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) PrimaryBlue else SecondaryBlue.copy(alpha = 0.5f),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) AppWhite else TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
fun DoctorHomeScreenPreview() {
    MediCitasTheme {
        DoctorHomeScreen()
    }
}
