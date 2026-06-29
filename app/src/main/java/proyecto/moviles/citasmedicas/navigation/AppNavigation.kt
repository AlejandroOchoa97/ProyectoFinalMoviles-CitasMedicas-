package proyecto.moviles.citasmedicas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.screens.auth.LoginScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.PatientHomeScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.SearchDoctorScreen
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview

// Punto de entrada de navegación. Por ahora el único destino es el inicio de sesión.
@Composable
fun AppNavigation(startDestination: String = Routes.LOGIN) {
    var currentRoute by rememberSaveable { mutableStateOf(startDestination) }

    when (currentRoute) {
        Routes.LOGIN -> LoginScreen(
            onLoginSuccess = { currentRoute = Routes.PATIENT_HOME }
        )
        Routes.PATIENT_HOME -> PatientHomeScreen(
            onBack = { currentRoute = Routes.LOGIN },
            onScheduleAppointment = { currentRoute = Routes.SEARCH_DOCTOR }
        )
        Routes.SEARCH_DOCTOR -> SearchDoctorScreen(
            onBack = { currentRoute = Routes.PATIENT_HOME }
        )
        else -> LoginScreen(onLoginSuccess = { currentRoute = Routes.PATIENT_HOME })
    }
}

@Preview(
    name = "Navegación principal",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun AppNavigationPreview() {
    MediCitasTheme {
        AppNavigation()
    }
}

