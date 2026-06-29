package proyecto.moviles.citasmedicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import proyecto.moviles.citasmedicas.navigation.AppNavigation
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediCitasTheme {
                AppNavigation()
            }
        }
    }
}

