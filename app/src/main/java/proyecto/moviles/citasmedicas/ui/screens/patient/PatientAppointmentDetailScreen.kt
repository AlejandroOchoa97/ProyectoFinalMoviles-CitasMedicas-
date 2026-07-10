package proyecto.moviles.citasmedicas.ui.screens.patient

/* Detalle del paciente: muestra una cita real cargada desde Room. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.BuildConfig
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.model.Appointment
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.components.StatusBadge
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.viewmodel.PatientAppointmentDetailViewModel

@Composable
fun PatientAppointmentDetailScreen(
    appointmentId: Int,
    onBack: () -> Unit,
    onNavigateHome: () -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    appointmentRepository: AppointmentRepository? = null,
    doctorRepository: DoctorRepository? = null,
    modifier: Modifier = Modifier,
    showInteractiveMap: Boolean = true
) {
    val viewModel = remember(appointmentRepository, doctorRepository) {
        PatientAppointmentDetailViewModel(
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository
        )
    }
    val uiState = viewModel.uiState
    val appointment = uiState.appointment
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(appointmentId) {
        viewModel.loadAppointment(appointmentId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { DetailTopBar(onBack) },
        bottomBar = {
            BottomNavigationBar(selectedIndex = 1) { index ->
                when (index) {
                    0 -> onNavigateHome()
                    1 -> onNavigateHistory()
                    2 -> onNavigateProfile()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DoctorHeader(appointment)
            AppointmentSummary(appointment)
            LocationSection(appointment, showInteractiveMap)

            Card(
                colors = CardDefaults.cardColors(containerColor = AppWhite),
                border = BorderStroke(1.dp, BorderSoft),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("Motivo de consulta", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text(uiState.reason, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                }
            }

            uiState.errorMessage?.let { message ->
                Text(message, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Cancelación de cita pendiente")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppWhite,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                contentPadding = PaddingValues(vertical = 13.dp)
            ) {
                Text("Cancelar cita")
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Regresar")
        }
        Text("Detalle de Cita", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun DoctorHeader(appointment: Appointment) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .background(SecondaryBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(46.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(appointment.doctorName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        Text(appointment.specialty, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(6.dp))
        StatusBadge(appointment.status)
    }
}

@Composable
private fun AppointmentSummary(appointment: Appointment) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        DetailCard("Especialidad", appointment.specialty, Modifier.weight(1f))
        DetailCard("Costo", appointment.price, Modifier.weight(1f))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = PrimaryBlue)
            Spacer(Modifier.size(10.dp))
            Column {
                Text("Fecha y hora", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                Text(
                    "${appointment.date.format(dateFormatter)}, ${appointment.time.format(timeFormatter)}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DetailCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(13.dp)) {
            Text(label, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Text(value, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun LocationSection(appointment: Appointment, showInteractiveMap: Boolean) {
    val latitude = appointment.latitude
    val longitude = appointment.longitude
    val hasCoordinates = latitude != null && longitude != null
    val canShowMap = hasCoordinates && showInteractiveMap && BuildConfig.MAPS_API_KEY_AVAILABLE

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = PrimaryBlue)
                Spacer(Modifier.size(10.dp))
                Column {
                    Text("Consultorio", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    Text(
                        appointment.clinicName.ifBlank { "Ubicación pendiente" },
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        appointment.clinicAddress.ifBlank { "Dirección pendiente" },
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (canShowMap) {
                AppointmentMap(
                    latitude = latitude,
                    longitude = longitude,
                    title = appointment.clinicName.ifBlank { "Consultorio" }
                )
            } else {
                MapPlaceholder(hasCoordinates = hasCoordinates)
            }
        }
    }
}

@Composable
private fun MapPlaceholder(hasCoordinates: Boolean) {
    val message = when {
        !hasCoordinates -> "Mapa pendiente: faltan coordenadas reales del consultorio"
        !BuildConfig.MAPS_API_KEY_AVAILABLE -> "Mapa no configurado: agrega MAPS_API_KEY en local.properties"
        else -> "Mapa no disponible por el momento"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(SecondaryBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(38.dp)
            )
            Text(
                message,
                color = PrimaryBlue,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
private fun AppointmentMap(latitude: Double, longitude: Double, title: String) {
    val location = remember(latitude, longitude) { LatLng(latitude, longitude) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(state = MarkerState(position = location), title = title)
    }
}

@Preview(name = "Detalle de cita paciente", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun PatientAppointmentDetailScreenPreview() {
    MediCitasTheme {
        PatientAppointmentDetailScreen(appointmentId = 1, onBack = {}, showInteractiveMap = false)
    }
}
