package proyecto.moviles.citasmedicas.ui.screens.patient

/* Perfil del paciente: muestra información del paciente cargada desde Room. */

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
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
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
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
import proyecto.moviles.citasmedicas.ui.viewmodel.PatientProfileViewModel

@Composable
fun UserProfileScreen(
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateHistory: () -> Unit,
    onLogout: () -> Unit,
    patientRepository: PatientRepository? = null,
    patientId: Int = 1,
    modifier: Modifier = Modifier
) {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    val viewModel = remember(patientRepository) {
        PatientProfileViewModel(patientRepository)
    }
    val uiState = viewModel.uiState

    LaunchedEffect(patientId) {
        viewModel.loadPatient(patientId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        bottomBar = {
            BottomNavigationBar(selectedIndex = 2) { index ->
                when (index) {
                    0 -> onNavigateHome()
                    1 -> onNavigateHistory()
                }
            }
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
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Regresar", tint = PrimaryBlue)
                }
                Text("MediCitas", color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.size(18.dp))

            Box(
                modifier = Modifier
                    .size(84.dp)
                    .background(SecondaryBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.AccountCircle, null, tint = PrimaryBlue, modifier = Modifier.size(58.dp))
            }

            Text(uiState.name, color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(uiState.email, color = TextSecondary, style = MaterialTheme.typography.bodySmall)

            uiState.errorMessage?.let { message ->
                Spacer(Modifier.size(10.dp))
                Text(message, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.size(22.dp))

            ProfileSection("CUENTA") {
                ProfileOption(Icons.Filled.Person, "Nombre", uiState.name)
                ProfileOption(Icons.Filled.Email, "Correo electrónico", uiState.email)
                ProfileOption(Icons.Filled.Phone, "Teléfono", uiState.phone)
                ProfileOption(Icons.Filled.Cake, "Fecha de nacimiento", uiState.birthDate)
                ProfileOption(Icons.Filled.Edit, "Editar perfil", "Pendiente")
                ProfileOption(Icons.Filled.Lock, "Cambiar contraseña", "Pendiente")
            }

            ProfileSection("PREFERENCIAS") {
                ProfileOption(Icons.Filled.Notifications, "Notificaciones", trailing = {
                    Switch(
                        checked = notifications,
                        onCheckedChange = { notifications = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue)
                    )
                })
                ProfileOption(Icons.Filled.DarkMode, "Modo oscuro", trailing = {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { darkMode = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = PrimaryBlue)
                    )
                })
            }

            ProfileSection("INFORMACIÓN") {
                ProfileOption(Icons.Filled.Info, "Acerca de", "MediCitas")
                ProfileOption(Icons.Filled.PrivacyTip, "Política de privacidad", "Ver documento")
            }

            Card(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ErrorBackground),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, null, tint = ErrorRed)
                    Text("  Cerrar sesión", color = ErrorRed, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.size(24.dp))
            Text("Perfil paciente conectado a Room", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.size(80.dp))
        }
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(title, color = PrimaryBlue, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
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
private fun ProfileOption(
    icon: ImageVector,
    title: String,
    value: String? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        Modifier
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
            Text(title, color = TextPrimary, fontWeight = FontWeight.Medium)
            if (value != null) {
                Text(value, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
        if (trailing != null) trailing()
    }
}

@Preview(name = "Perfil de usuario", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun UserProfileScreenPreview() {
    MediCitasTheme { UserProfileScreen({}, {}, {}, {}) }
}
