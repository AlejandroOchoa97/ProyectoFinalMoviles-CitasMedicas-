package proyecto.moviles.citasmedicas.ui.screens.auth

/* Registro: captura datos y los envía a Firebase. */

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AuthRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.ui.components.AppButton
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
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistrationComplete: () -> Unit,
    modifier: Modifier = Modifier,
    authRepository: AuthRepository? = null,
    patientRepository: PatientRepository? = null,
    doctorRepository: DoctorRepository? = null
) {
    var role by remember { mutableStateOf("Paciente") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Masculino", "Femenino", "Otro")

    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun handleRegister() {
        val cleanEmail = email.trim()
        val cleanPassword = password.trim()
        val cleanConfirmation = confirmation.trim()

        if (cleanEmail.isBlank() || cleanPassword.isBlank() || fullName.isBlank()) {
            scope.launch { snackbarHostState.showSnackbar("Por favor, completa los campos obligatorios") }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            scope.launch { snackbarHostState.showSnackbar("Ingresa un correo electrónico válido") }
            return
        }

        if (cleanPassword.length < 6) {
            scope.launch { snackbarHostState.showSnackbar("La contraseña debe tener al menos 6 caracteres") }
            return
        }

        if (cleanPassword != cleanConfirmation) {
            scope.launch { snackbarHostState.showSnackbar("Las contraseñas no coinciden") }
            return
        }

        if (authRepository == null) {
            onRegistrationComplete()
            return
        }

        isLoading = true
        scope.launch {
            try {
                val result = authRepository.register(cleanEmail, cleanPassword)
                isLoading = false

                result.onSuccess {
                    // Firebase Auth solo guarda el acceso. Room guarda el perfil de MediCitas.
                    if (role == "Paciente") {
                        patientRepository?.insertPatient(
                            PatientEntity(
                                name = fullName.trim(),
                                email = cleanEmail,
                                password = cleanPassword,
                                phone = phone.trim(),
                                birthDate = birthDate.trim()
                            )
                        )
                    } else {
                        doctorRepository?.insertDoctor(
                            DoctorEntity(
                                name = fullName.trim(),
                                email = cleanEmail,
                                password = cleanPassword,
                                specialty = "Medicina General",
                                professionalLicense = "Pendiente",
                                experienceYears = 0,
                                clinicName = "Clínica pendiente",
                                clinicAddress = "Dirección pendiente",
                                consultationPrice = 800.0
                            )
                        )
                    }

                    // Al registrarse correctamente regresa de inmediato al Login.
                    // Si esperamos al Snackbar, la navegación tarda hasta que el mensaje desaparece.
                    onRegistrationComplete()
                }.onFailure { exception ->
                    snackbarHostState.showSnackbar(
                        "Error: ${exception.localizedMessage ?: "No se pudo registrar el usuario"}"
                    )
                }
            } catch (exception: Exception) {
                isLoading = false
                snackbarHostState.showSnackbar(
                    "Error inesperado: ${exception.localizedMessage ?: "Intenta nuevamente"}"
                )
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Regresar",
                        tint = PrimaryBlue
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "MediCitas",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = "Registrarse",
                color = TextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Crea tu cuenta para gestionar tus citas médicas de forma segura.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(22.dp))

            Text("¿Cuál es tu rol?", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RoleButton(
                    text = "Paciente",
                    selected = role == "Paciente",
                    icon = Icons.Filled.Person,
                    onClick = { role = "Paciente" },
                    modifier = Modifier.weight(1f)
                )
                RoleButton(
                    text = "Médico",
                    selected = role == "Médico",
                    icon = Icons.Filled.LocalHospital,
                    onClick = { role = "Médico" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(18.dp))

            RegisterField(fullName, { fullName = it }, "Nombre completo")
            RegisterField(email, { email = it }, "Correo electrónico")

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BirthDateField(
                    value = birthDate,
                    onValueChange = { birthDate = formatBirthDateInput(it) },
                    modifier = Modifier.weight(1f)
                )

                GenderDropdownField(
                    value = gender,
                    expanded = expanded,
                    options = genderOptions,
                    onExpandedChange = { expanded = it },
                    onOptionSelected = {
                        gender = it
                        expanded = false
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            RegisterField(phone, { phone = it }, "Teléfono")
            RegisterField(password, { password = it }, "Contraseña", visualPassword = true)
            RegisterField(confirmation, { confirmation = it }, "Confirmar contraseña", visualPassword = true)

            Spacer(Modifier.height(14.dp))

            AppButton(
                text = if (isLoading) "Registrando..." else "Registrarse",
                onClick = { handleRegister() },
                enabled = !isLoading
            )

            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("¿Ya tienes una cuenta? Inicia sesión", color = PrimaryBlue)
            }
        }
    }
}

@Composable
private fun GenderDropdownField(
    value: String,
    expanded: Boolean,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AppWhite,
                unfocusedContainerColor = AppWhite,
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderSoft
            )
        )

        OutlinedButton(
            onClick = { onExpandedChange(true) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.dp, Color.Transparent),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            )
        ) {}

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

@Composable
private fun RoleButton(
    text: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (selected) PrimaryBlue else BorderSoft),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) SecondaryBlue else AppWhite,
            contentColor = if (selected) PrimaryBlue else TextPrimary
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null)
            Text(text)
        }
    }
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (visualPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderSoft
        )
    )
}

@Composable
private fun BirthDateField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Fecha de nacimiento") },
        placeholder = { Text("DD/MM/AAAA") },
        supportingText = { Text("Ejemplo: 30/08/1997") },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderSoft
        )
    )
}

private fun formatBirthDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)

    return buildString {
        digits.forEachIndexed { index, char ->
            if (index == 2 || index == 4) append("/")
            append(char)
        }
    }
}

@Preview(
    name = "Registro",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun RegisterScreenPreview() {
    MediCitasTheme {
        RegisterScreen({}, {})
    }
}
