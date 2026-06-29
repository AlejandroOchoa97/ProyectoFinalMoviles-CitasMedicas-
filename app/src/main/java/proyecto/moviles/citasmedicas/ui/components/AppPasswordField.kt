package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.OutlineGray
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview

@Composable
fun AppPasswordField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Contraseña") },
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Canvas(Modifier.size(24.dp)) {
                    drawOval(
                        color = TextSecondary,
                        topLeft = Offset(2.dp.toPx(), 6.dp.toPx()),
                        size = Size(20.dp.toPx(), 12.dp.toPx()),
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                    drawCircle(TextSecondary, radius = 3.dp.toPx(), center = center)
                    if (passwordVisible) {
                        drawLine(
                            color = TextSecondary,
                            start = Offset(4.dp.toPx(), 4.dp.toPx()),
                            end = Offset(20.dp.toPx(), 20.dp.toPx()),
                            strokeWidth = 2.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = OutlineGray,
            focusedContainerColor = AppWhite,
            unfocusedContainerColor = AppWhite,
            focusedLeadingIconColor = PrimaryBlue,
            unfocusedLeadingIconColor = TextSecondary
        )
    )
}

@Preview(name = "Campo de contraseña", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun AppPasswordFieldPreview() {
    MediCitasTheme {
        AppPasswordField(
            value = "contraseña123",
            onValueChange = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
