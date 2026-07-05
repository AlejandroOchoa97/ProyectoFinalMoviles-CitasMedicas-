package proyecto.moviles.citasmedicas.ui.screens.onboarding

/* Plantilla de onboarding: cambia contenido por página y expone acciones para avanzar u omitir. */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

data class OnboardingPage(
    val number: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

val onboardingPages = listOf(
    OnboardingPage(1, "Agenda citas fácilmente", "Conecta con los mejores especialistas médicos en segundos desde la palma de tu mano.", Icons.Filled.CalendarMonth),
    OnboardingPage(2, "Encuentra médicos confiables", "Accede a una red exclusiva de profesionales certificados y consulta las opiniones de otros pacientes.", Icons.Filled.MedicalServices),
    OnboardingPage(3, "Consulta tus recetas desde cualquier lugar", "Toda tu información médica y recetas digitales sincronizadas en tiempo real para tu comodidad.", Icons.Filled.Description)
)

@Composable
fun OnboardingScreen(page: OnboardingPage, onNext: () -> Unit, onSkip: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(AppBackground).padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("MediCitas", color = PrimaryBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text("${page.number}/3", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(34.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(310.dp).background(SecondaryBlue, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(158.dp).background(AppWhite, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(page.icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(92.dp))
            }
        }
        Spacer(Modifier.height(34.dp))
        Text(page.title, color = TextPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(page.description, color = TextSecondary, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { index ->
                Box(
                    Modifier
                        .size(width = if (index + 1 == page.number) 22.dp else 7.dp, height = 7.dp)
                        .background(if (index + 1 == page.number) PrimaryBlue else BorderSoft, CircleShape)
                )
            }
        }
        Spacer(Modifier.weight(1f))
        AppButton(
            text = if (page.number == 3) "Comenzar" else "Siguiente",
            icon = Icons.AutoMirrored.Outlined.ArrowForward,
            onClick = onNext
        )
        TextButton(onClick = onSkip) { Text("Omitir", color = PrimaryBlue) }
    }
}

@Preview(name = "Onboarding 1", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun OnboardingOnePreview() {
    MediCitasTheme { OnboardingScreen(onboardingPages[0], {}, {}) }
}

@Preview(name = "Onboarding 2", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun OnboardingTwoPreview() {
    MediCitasTheme { OnboardingScreen(onboardingPages[1], {}, {}) }
}

@Preview(name = "Onboarding 3", showBackground = true, backgroundColor = AppBackgroundPreview, widthDp = 390, heightDp = 844)
@Composable
private fun OnboardingThreePreview() {
    MediCitasTheme { OnboardingScreen(onboardingPages[2], {}, {}) }
}
