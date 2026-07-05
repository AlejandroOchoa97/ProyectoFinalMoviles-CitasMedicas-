package proyecto.moviles.citasmedicas.ui.screens.auth

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
fun RegisterScreen(onBack: () -> Unit, onRegistrationComplete: () -> Unit, modifier: Modifier = Modifier) {
    var role by remember { mutableStateOf("Paciente") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                RegisterField(gender, { gender = it }, "Género", Modifier.weight(1f))
            }
            RegisterField(phone, { phone = it }, "Teléfono")
            RegisterField(password, { password = it }, "Contraseña", visualPassword = true)
            RegisterField(confirmation, { confirmation = it }, "Confirmar contraseña", visualPassword = true)
            Spacer(Modifier.height(14.dp))
            AppButton(text = "Registrarse", onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Registro simulado correctamente")
                    onRegistrationComplete()
                }
            })
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
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(containerColor = if (selected) SecondaryBlue else AppWhite, contentColor = if (selected) PrimaryBlue else TextPrimary)
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
