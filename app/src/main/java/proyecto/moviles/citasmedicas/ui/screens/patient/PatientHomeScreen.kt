package proyecto.moviles.citasmedicas.ui.screens.patient

/* Inicio del paciente: lista citas simuladas y conecta búsqueda, detalle, historial y perfil. */

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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.ui.components.AppointmentCard
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@Composable
fun PatientHomeScreen(
    onBack: () -> Unit,
    onScheduleAppointment: () -> Unit,
    onAppointmentDetails: (Int) -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Próximas") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val filters = listOf("Próximas", "Pendientes", "Pasadas")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { PatientTopBar(onBack) },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 92.dp),
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
                    filters.forEach { filter ->
                        val selected = selectedFilter == filter
                        AssistChip(
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selected) PrimaryBlue else BorderSoft,
                                labelColor = if (selected) AppWhite else TextSecondary
                            ),
                            border = null
                        )
                    }
                }
            }

            items(SampleData.sampleAppointments, key = { it.id }) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onDetailsClick = { onAppointmentDetails(appointment.id) }
                )
            }
        }
    }
}

@Composable
private fun PatientTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Regresar", tint = TextSecondary)
        }
        Text("MediCitas", color = PrimaryBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {}) {
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
