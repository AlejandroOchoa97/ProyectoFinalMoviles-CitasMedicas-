package proyecto.moviles.citasmedicas.ui.screens.auth

/* Registro: captura datos y los envía a Firebase. Incluye selección de género con dropdown. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.repository.AuthRepository
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistrationComplete: () -> Unit,
    modifier: Modifier = Modifier,
    authRepository: AuthRepository? = null
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
        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
            scope.launch { snackbarHostState.showSnackbar("Por favor, completa los campos obligatorios") }
            return
        }
        if (password != confirmation) {
            scope.launch { snackbarHostState.showSnackbar("Las contraseñas no coinciden") }
            return
        }

        if (authRepository == null) {
            onRegistrationComplete()
            return
        }

        isLoading = true
        scope.launch {
            val result = authRepository.register(email, password)
            isLoading = false
            result.onSuccess {
                snackbarHostState.showSnackbar("Registro exitoso")
                onRegistrationComplete()
            }.onFailure { e ->
                snackbarHostState.showSnackbar("Error: ${e.localizedMessage}")
            }
        }
    }

    Scaffold(containerColor = AppBackground, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize().padding(innerPadding).imePadding().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Regresar", tint = PrimaryBlue) }
                Spacer(Modifier.weight(1f))
                Text("MediCitas", color = PrimaryBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(22.dp))
            Text("Registrarse", color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Crea tu cuenta para gestionar tus citas médicas de forma segura.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(22.dp))
            Text("¿Cuál es tu rol?", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoleButton("Paciente", role == "Paciente", Icons.Filled.Person, { role = "Paciente" }, Modifier.weight(1f))
                RoleButton("Médico", role == "Médico", Icons.Filled.LocalHospital, { role = "Médico" }, Modifier.weight(1f))
            }
            Spacer(Modifier.height(18.dp))
            RegisterField(fullName, { fullName = it }, "Nombre completo")
            RegisterField(email, { email = it }, "Correo electrónico")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RegisterField(birthDate, { birthDate = it }, "Fecha nac.", Modifier.weight(1f))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = AppWhite,
                            unfocusedContainerColor = AppWhite,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderSoft
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
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
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("¿Ya tienes una cuenta? Inicia sesión", color = PrimaryBlue)
            }
        }
    }
}

@Composable
private fun RoleButton(text: String, selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (selected) PrimaryBlue else BorderSoft),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (selected) SecondaryBlue else AppWhite, contentColor = if (selected) PrimaryBlue else TextPrimary)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null)
            Text(text)
        }
    }
}

@Composable
private fun RegisterField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier, visualPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth().padding(bottom = 10.dp),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (visualPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = AppWhite, unfocusedContainerColor = AppWhite, focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderSoft)
    )
}

@Preview(name = "Registro", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun RegisterScreenPreview() {
    MediCitasTheme { RegisterScreen({}, {}) }
}
