package proyecto.moviles.citasmedicas.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import proyecto.moviles.citasmedicas.ui.components.AppLogoIcon
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@Composable
fun SplashScreen(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(1800)
        onFinished()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))
        AppLogoIcon()
        Spacer(Modifier.height(18.dp))
        Text("MediCitas", color = PrimaryBlue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Tu salud, una cita de distancia.", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.weight(1f))
        CircularProgressIndicator(color = PrimaryBlue)
        Spacer(Modifier.height(10.dp))
        Text("CARGANDO...", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(30.dp))
        Text("© 2026 MediCitas Healthcare", color = TextSecondary, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(28.dp))
    }
}

@Preview(name = "Pantalla de inicio", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    MediCitasTheme { SplashScreen(onFinished = {}) }
}
