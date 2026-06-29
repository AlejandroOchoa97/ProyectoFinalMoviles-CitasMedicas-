package proyecto.moviles.citasmedicas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.screens.auth.LoginScreen
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview


@Composable
fun AppNavigation(startDestination: String = Routes.LOGIN) {
    when (startDestination) {
        Routes.LOGIN -> LoginScreen()
        else -> LoginScreen()
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

