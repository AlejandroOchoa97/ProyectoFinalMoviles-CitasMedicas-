package proyecto.moviles.citasmedicas.ui.screens.doctor

/* Perfil médico: muestra datos profesionales del doctor cargados desde Room. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.ErrorBackground
import proyecto.moviles.citasmedicas.ui.theme.ErrorRed
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.viewmodel.DoctorProfileViewModel

@Composable
fun DoctorProfileScreen(
    onBack: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateAvailability: () -> Unit = {},
    onLogout: () -> Unit = {},
    doctorRepository: DoctorRepository? = null,
    doctorId: Int = 1,
    modifier: Modifier = Modifier
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }
    val viewModel = remember(doctorRepository) {
        DoctorProfileViewModel(doctorRepository)
    }
    val uiState = viewModel.uiState

    // Carga el médico activo desde Room.
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 2,
                middleLabel = "Disponibilidad",
                onItemSelected = { index ->
                    when (index) {
                        0 -> onNavigateHome()
                        1 -> onNavigateAvailability()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Regresar",
                        tint = PrimaryBlue
                    )
                }
                Text(
                    text = "MediCitas",
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.size(18.dp))

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(SecondaryBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MedicalServices,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(52.dp)
                )
            }

            Text(
                text = uiState.name,
                color = TextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = uiState.specialty,
                color = PrimaryBlue,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = uiState.email,
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            uiState.errorMessage?.let { message ->
                Spacer(Modifier.size(12.dp))
                Text(
                    text = message,
                    color = ErrorRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.size(22.dp))

            DoctorProfileSection("INFORMACIÓN PROFESIONAL") {
                DoctorProfileOption(Icons.Filled.HealthAndSafety, "Especialidad", uiState.specialty)
                DoctorProfileOption(Icons.Filled.Badge, "Cédula profesional", uiState.professionalLicense)
                DoctorProfileOption(Icons.Filled.WorkHistory, "Experiencia", "${uiState.experienceYears} años")
                DoctorProfileOption(
                    icon = Icons.Filled.Payments,
                    title = "Tarifa",
                    value = "$${uiState.consultationPrice.toInt()} MXN"
                )
            }

            DoctorProfileSection("CONSULTORIO") {
                DoctorProfileOption(Icons.Filled.Business, "Clínica", uiState.clinicName)
                DoctorProfileOption(Icons.Filled.LocationOn, "Dirección", uiState.clinicAddress)
            }

            DoctorProfileSection("PREFERENCIAS") {
                DoctorProfileOption(
                    icon = Icons.Filled.Notifications,
                    title = "Notificaciones",
                    trailing = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue)
                        )
                    }
                )
                DoctorProfileOption(
                    icon = Icons.Filled.DarkMode,
                    title = "Modo oscuro",
                    trailing = {
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue)
                        )
                    }
                )
            }

            DoctorProfileSection("INFORMACIÓN") {
                DoctorProfileOption(Icons.Filled.Info, "Acerca de", "MediCitas")
                DoctorProfileOption(Icons.Filled.PrivacyTip, "Política de privacidad", "Ver documento")
            }

            Card(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ErrorBackground),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, null, tint = ErrorRed)
                    Text(
                        text = "  Cerrar sesión",
                        color = ErrorRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.size(24.dp))
            Text(
                text = "Perfil médico conectado a Room",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.size(22.dp))
        }
    }
}

@Composable
private fun DoctorProfileSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title,
            color = PrimaryBlue,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = AppWhite),
            border = BorderStroke(1.dp, BorderSoft),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column { content() }
        }
    }
}

@Composable
private fun DoctorProfileOption(
    icon: ImageVector,
    title: String,
    value: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            if (value != null) {
                Text(
                    text = value,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

@Preview(
    name = "Perfil médico",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun DoctorProfileScreenPreview() {
    MediCitasTheme {
        DoctorProfileScreen()
    }
}
