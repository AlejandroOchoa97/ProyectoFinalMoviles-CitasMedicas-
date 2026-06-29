package proyecto.moviles.citasmedicas.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MediCitasColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = AppWhite,
    secondary = SecondaryBlue,
    onSecondary = TextPrimary,
    tertiary = TertiaryGreen,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = AppWhite,
    onSurface = TextPrimary,
    error = ErrorRed,
    outline = OutlineGray
)

@Composable
fun MediCitasTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    val darkIcons = !isSystemInDarkTheme()

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AppBackground.toArgb()
            window.navigationBarColor = AppBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = darkIcons || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            }
        }
    }

    MaterialTheme(
        colorScheme = MediCitasColorScheme,
        typography = MediCitasTypography,
        content = content
    )
}

