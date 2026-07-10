package proyecto.moviles.citasmedicas.ui.screens.auth

/* Login visual: mantiene campos en memoria y delega navegación mediante callbacks. */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AuthRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.ui.components.AppButton
import proyecto.moviles.citasmedicas.ui.components.AppButtonStyle
import proyecto.moviles.citasmedicas.ui.components.AppLogoIcon
import proyecto.moviles.citasmedicas.ui.components.AppPasswordField
import proyecto.moviles.citasmedicas.ui.components.AppTextField
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

private const val ROLE_PATIENT = "PATIENT"
private const val ROLE_DOCTOR = "DOCTOR"

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    authRepository: AuthRepository? = null,
    patientRepository: PatientRepository? = null,
    doctorRepository: DoctorRepository? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun handleLogin() {
        val cleanEmail = email.trim()

        if (cleanEmail.isBlank() || password.isBlank()) {
            scope.launch { snackbarHostState.showSnackbar("Por favor, completa todos los campos") }
            return
        }

        if (authRepository == null) {
            onLoginSuccess(ROLE_PATIENT)
            return
        }

        isLoading = true
        scope.launch {
            val result = authRepository.login(cleanEmail, password)

            if (result.isSuccess) {
                val firebaseDisplayName = result.getOrNull()?.displayName?.toDisplayName().orEmpty()
                // Primero busca médico para corregir correos que se hayan creado como paciente por error.
                val doctor = doctorRepository?.getDoctorByEmail(cleanEmail)
                val patient = if (doctor == null) patientRepository?.getPatientByEmail(cleanEmail) else null

                isLoading = false

                when {
                    doctor != null -> {
                        val updatedDoctor = doctor.withBetterName(firebaseDisplayName)
                        if (updatedDoctor.name != doctor.name) {
                            doctorRepository?.updateDoctor(updatedDoctor)
                        }

                        authRepository.activePatient = null
                        authRepository.activeDoctor = updatedDoctor
                        onLoginSuccess(ROLE_DOCTOR)
                    }

                    patient != null -> {
                        val updatedPatient = patient.withBetterName(firebaseDisplayName)
                        if (updatedPatient.name != patient.name) {
                            patientRepository?.updatePatient(updatedPatient)
                        }

                        authRepository.activeDoctor = null
                        authRepository.activePatient = updatedPatient
                        onLoginSuccess(ROLE_PATIENT)
                    }

                    else -> {
                        authRepository.logout()
                        snackbarHostState.showSnackbar(
                            "La cuenta existe en Firebase, pero no tiene perfil local. Regístrala otra vez desde esta app."
                        )
                    }
                }
            } else {
                isLoading = false
                val e = result.exceptionOrNull()
                snackbarHostState.showSnackbar("Error: ${e?.localizedMessage ?: "Credenciales inválidas"}")
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MediCitas",
                color = PrimaryBlue,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(44.dp))
            AppLogoIcon()
            Spacer(Modifier.height(28.dp))

            Text(text = "Iniciar sesión", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Accede a tu historial médico y gestiona tus citas con facilidad.",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.86f)
            )

            Spacer(Modifier.height(34.dp))
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                leadingIcon = Icons.Outlined.Email
            )
            Spacer(Modifier.height(14.dp))
            AppPasswordField(value = password, onValueChange = { password = it })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                    )
                    Text("Recordarme", style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(onClick = onForgotPasswordClick) {
                    Text("¿Olvidé mi contraseña?", color = PrimaryBlue, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(8.dp))
            AppButton(
                text = if (isLoading) "Iniciando sesión..." else "Iniciar sesión",
                icon = Icons.AutoMirrored.Outlined.ArrowForward,
                onClick = { handleLogin() },
                enabled = !isLoading
            )
            Spacer(Modifier.height(14.dp))
            AppButton(
                text = "Registrarse",
                style = AppButtonStyle.Outline,
                onClick = onRegisterClick
            )

            Spacer(Modifier.height(28.dp))
            Text(
                text = buildAnnotatedString {
                    append("Al continuar, aceptas nuestros ")
                    withStyle(SpanStyle(color = TextSecondary, fontWeight = FontWeight.SemiBold)) {
                        append("Términos de Servicio")
                    }
                    append(" y ")
                    withStyle(SpanStyle(color = TextSecondary, fontWeight = FontWeight.SemiBold)) {
                        append("Política de Privacidad")
                    }
                    append(".")
                },
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

private fun PatientEntity.withBetterName(firebaseDisplayName: String): PatientEntity {
    return if (firebaseDisplayName.isMoreCompleteThan(name)) copy(name = firebaseDisplayName) else this
}

private fun DoctorEntity.withBetterName(firebaseDisplayName: String): DoctorEntity {
    return if (firebaseDisplayName.isMoreCompleteThan(name)) copy(name = firebaseDisplayName) else this
}

private fun String.isMoreCompleteThan(currentName: String): Boolean {
    val newWords = trim().split(" ").filter { it.isNotBlank() }
    val currentWords = currentName.trim().split(" ").filter { it.isNotBlank() }

    return newWords.size > currentWords.size && newWords.joinToString(" ").isNotBlank()
}

private fun String.toDisplayName(): String {
    return trim()
        .lowercase()
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}

@Preview(
    name = "Login MediCitas",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun LoginScreenPreview() {
    MediCitasTheme {
        LoginScreen()
    }
}
