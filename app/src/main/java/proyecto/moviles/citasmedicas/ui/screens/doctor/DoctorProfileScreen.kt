package proyecto.moviles.citasmedicas.ui.screens.doctor

/* Perfil médico: muestra datos profesionales y permite editar la ubicación del consultorio. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import proyecto.moviles.citasmedicas.BuildConfig
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
    var isEditingProfessionalInfo by remember { mutableStateOf(false) }
    var specialtyInput by remember { mutableStateOf("") }
    var licenseInput by remember { mutableStateOf("") }
    var experienceInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var isEditingLocation by remember { mutableStateOf(false) }
    var clinicNameInput by remember { mutableStateOf("") }
    var clinicAddressInput by remember { mutableStateOf("") }
    var latitudeInput by remember { mutableStateOf("") }
    var longitudeInput by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val viewModel = remember(doctorRepository) {
        DoctorProfileViewModel(doctorRepository)
    }
    val uiState = viewModel.uiState

    // Carga el médico activo desde Room.
    LaunchedEffect(doctorId) {
        viewModel.loadDoctor(doctorId)
    }

    // Muestra mensajes del ViewModel.
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it) }
        uiState.successMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                .imePadding()
                .navigationBarsPadding()
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

                TextButton(
                    onClick = {
                        specialtyInput = uiState.specialty
                        licenseInput = uiState.professionalLicense
                        experienceInput = uiState.experienceYears.toString()
                        priceInput = uiState.consultationPrice.toInt().toString()
                        isEditingProfessionalInfo = true
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Editar información", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }
            }

            if (isEditingProfessionalInfo) {
                EditProfessionalInfoCard(
                    specialty = specialtyInput,
                    onSpecialtyChange = { specialtyInput = it },
                    license = licenseInput,
                    onLicenseChange = { licenseInput = it },
                    experienceYears = experienceInput,
                    onExperienceYearsChange = { experienceInput = it.filter { char -> char.isDigit() } },
                    price = priceInput,
                    onPriceChange = { priceInput = it.filter { char -> char.isDigit() } },
                    isLoading = uiState.isLoading,
                    onCancel = { isEditingProfessionalInfo = false },
                    onSave = {
                        scope.launch {
                            val saved = viewModel.updateProfessionalInfo(
                                specialty = specialtyInput,
                                professionalLicense = licenseInput,
                                experienceYearsText = experienceInput,
                                consultationPriceText = priceInput
                            )

                            if (saved) {
                                isEditingProfessionalInfo = false
                            }
                        }
                    }
                )
            }

            DoctorProfileSection("CONSULTORIO") {
                DoctorProfileOption(Icons.Filled.Business, "Clínica", uiState.clinicName)
                DoctorProfileOption(Icons.Filled.LocationOn, "Dirección", uiState.clinicAddress)
                DoctorProfileOption(
                    icon = Icons.Filled.LocationOn,
                    title = "Coordenadas",
                    value = uiState.coordinatesText()
                )

                TextButton(
                    onClick = {
                        clinicNameInput = uiState.clinicName
                        clinicAddressInput = uiState.clinicAddress
                        latitudeInput = uiState.clinicLatitude?.toString().orEmpty()
                        longitudeInput = uiState.clinicLongitude?.toString().orEmpty()
                        isEditingLocation = true
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Editar ubicación", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }

                DoctorClinicMapPreview(
                    clinicName = uiState.clinicName,
                    latitude = uiState.clinicLatitude,
                    longitude = uiState.clinicLongitude
                )

                TextButton(
                    onClick = {
                        clinicNameInput = uiState.clinicName
                        clinicAddressInput = uiState.clinicAddress
                        latitudeInput = uiState.clinicLatitude?.toString().orEmpty()
                        longitudeInput = uiState.clinicLongitude?.toString().orEmpty()
                        isEditingLocation = true
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Editar ubicación", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }
            }

            if (isEditingLocation) {
                EditClinicLocationCard(
                    clinicName = clinicNameInput,
                    onClinicNameChange = { clinicNameInput = it },
                    clinicAddress = clinicAddressInput,
                    onClinicAddressChange = { clinicAddressInput = it },
                    latitude = latitudeInput,
                    onLatitudeChange = { latitudeInput = it },
                    longitude = longitudeInput,
                    onLongitudeChange = { longitudeInput = it },
                    isLoading = uiState.isLoading,
                    onCancel = { isEditingLocation = false },
                    onSave = {
                        scope.launch {
                            val saved = viewModel.updateClinicLocation(
                                clinicName = clinicNameInput,
                                clinicAddress = clinicAddressInput,
                                latitudeText = latitudeInput,
                                longitudeText = longitudeInput
                            )

                            if (saved) {
                                isEditingLocation = false
                            }
                        }
                    }
                )
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
                text = "Tip: copia latitud y longitud desde Google Maps con clic derecho en la clínica.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.size(120.dp))
        }
    }
}

@Composable
private fun DoctorClinicMapPreview(
    clinicName: String,
    latitude: Double?,
    longitude: Double?
) {
    val hasCoordinates = latitude != null && longitude != null
    val canShowMap = hasCoordinates && BuildConfig.MAPS_API_KEY_AVAILABLE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppBackground),
        border = BorderStroke(1.dp, BorderSoft),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                canShowMap -> DoctorClinicMap(
                    clinicName = clinicName,
                    latitude = latitude!!,
                    longitude = longitude!!
                )

                hasCoordinates -> DoctorMapPlaceholder(
                    title = "Mapa pendiente de API Key",
                    message = "Las coordenadas ya están guardadas, solo falta configurar MAPS_API_KEY."
                )

                else -> DoctorMapPlaceholder(
                    title = "Ubicación pendiente",
                    message = "Agrega latitud y longitud para mostrar el mapa del consultorio."
                )
            }
        }
    }
}

@Composable
private fun DoctorClinicMap(
    clinicName: String,
    latitude: Double,
    longitude: Double
) {
    val clinicPosition = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(clinicPosition, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = clinicPosition),
            title = clinicName.ifBlank { "Consultorio" }
        )
    }
}

@Composable
private fun DoctorMapPlaceholder(
    title: String,
    message: String
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = title,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = message,
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun EditProfessionalInfoCard(
    specialty: String,
    onSpecialtyChange: (String) -> Unit,
    license: String,
    onLicenseChange: (String) -> Unit,
    experienceYears: String,
    onExperienceYearsChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, PrimaryBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Editar información profesional",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            ProfileTextField(specialty, onSpecialtyChange, "Especialidad")
            ProfileTextField(license, onLicenseChange, "Cédula profesional")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileTextField(experienceYears, onExperienceYearsChange, "Años exp.", Modifier.weight(1f))
                ProfileTextField(price, onPriceChange, "Tarifa MXN", Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryBlue,
                        contentColor = PrimaryBlue
                    ),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar")
                }
            }
        }
    }
}

@Composable
private fun EditClinicLocationCard(
    clinicName: String,
    onClinicNameChange: (String) -> Unit,
    clinicAddress: String,
    onClinicAddressChange: (String) -> Unit,
    latitude: String,
    onLatitudeChange: (String) -> Unit,
    longitude: String,
    onLongitudeChange: (String) -> Unit,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, PrimaryBlue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Editar ubicación del consultorio",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            ProfileTextField(clinicName, onClinicNameChange, "Nombre de clínica")
            ProfileTextField(clinicAddress, onClinicAddressChange, "Dirección")

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileTextField(latitude, onLatitudeChange, "Latitud", Modifier.weight(1f))
                ProfileTextField(longitude, onLongitudeChange, "Longitud", Modifier.weight(1f))
            }

            Text(
                text = "Ejemplo: 19.3763, -99.1770. Puedes copiarlo desde Google Maps.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryBlue,
                        contentColor = PrimaryBlue
                    ),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar")
                }
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderSoft
        )
    )
}

@Composable
private fun DoctorProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
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

private fun proyecto.moviles.citasmedicas.ui.viewmodel.DoctorProfileUiState.coordinatesText(): String {
    val latitude = clinicLatitude
    val longitude = clinicLongitude

    return if (latitude != null && longitude != null) {
        "$latitude, $longitude"
    } else {
        "Pendientes"
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
