package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview

@Composable
fun AppLogoIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(88.dp).background(SecondaryBlue, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(42.dp).background(PrimaryBlue, RoundedCornerShape(7.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Logo de MediCitas",
                tint = AppWhite,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Preview(name = "Logo MediCitas", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun AppLogoIconPreview() {
    MediCitasTheme {
        AppLogoIcon(modifier = Modifier.padding(20.dp))
    }
}
