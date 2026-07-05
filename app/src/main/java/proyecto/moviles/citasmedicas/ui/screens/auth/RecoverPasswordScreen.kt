package proyecto.moviles.citasmedicas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.ui.components.AppButton
import proyecto.moviles.citasmedicas.ui.components.AppTextField
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@Composable
fun RecoverPasswordScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 24.dp)) {
            Box(Modifier.fillMaxWidth().height(64.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Regresar")
                }
                Text("MediCitas", color = PrimaryBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Center))
            }
            Spacer(Modifier.height(34.dp))
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally).background(SecondaryBlue, CircleShape).padding(30.dp),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.LockReset, contentDescription = null, tint = PrimaryBlue) }
            Spacer(Modifier.height(30.dp))
            Text("Recuperar contraseña", color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Ingresa tu correo para recibir un enlace de recuperación.", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(28.dp))
            AppTextField(email, { email = it }, "Correo electrónico", Icons.Outlined.Email)
            Spacer(Modifier.height(28.dp))
            AppButton(
                text = "Enviar enlace",
                icon = Icons.AutoMirrored.Outlined.Send,
                onClick = { scope.launch { snackbarHostState.showSnackbar("Enlace de recuperación simulado") } }
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("¿Recordaste tu contraseña? Inicia sesión", color = PrimaryBlue)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(name = "Recuperar contraseña", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun RecoverPasswordScreenPreview() {
    MediCitasTheme { RecoverPasswordScreen(onBack = {}) }
}
