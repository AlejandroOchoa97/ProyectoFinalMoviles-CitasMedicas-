package proyecto.moviles.citasmedicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import proyecto.moviles.citasmedicas.data.AppContainer
import proyecto.moviles.citasmedicas.data.local.LocalDataSeeder
import proyecto.moviles.citasmedicas.navigation.AppNavigation
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = AppContainer(this)

        lifecycleScope.launch {
            LocalDataSeeder(
                patientRepository = appContainer.patientRepository,
                doctorRepository = appContainer.doctorRepository,
                appointmentRepository = appContainer.appointmentRepository
            ).seedIfNeeded()
        }

        setContent {
            MediCitasTheme {
                AppNavigation(
                    appointmentRepository = appContainer.appointmentRepository,
                    patientRepository = appContainer.patientRepository
                )
            }
        }
    }
}

