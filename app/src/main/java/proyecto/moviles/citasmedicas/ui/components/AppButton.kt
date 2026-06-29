package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue

enum class AppButtonStyle { Primary, Outline }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val content: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text)
            icon?.let {
                Spacer(Modifier.width(8.dp))
                Icon(it, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    }

    if (style == AppButtonStyle.Primary) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue, contentColor = AppWhite),
            content = { content() }
        )
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, PrimaryBlue),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
            content = { content() }
        )
    }
}

@Preview(name = "Botones", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun AppButtonPreview() {
    MediCitasTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.background(AppBackground).padding(20.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            AppButton(text = "Iniciar sesión", onClick = {})
            AppButton(text = "Registrarse", style = AppButtonStyle.Outline, onClick = {})
        }
    }
}

