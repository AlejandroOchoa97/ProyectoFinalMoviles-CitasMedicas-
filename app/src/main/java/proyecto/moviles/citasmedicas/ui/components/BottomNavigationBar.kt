package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

private data class BottomItem(val label: String, val icon: ImageVector)

@Composable
fun BottomNavigationBar(
    selectedIndex: Int = 0,
    middleLabel: String = "Historial",
    onItemSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        BottomItem("Inicio", Icons.Filled.Home),
        BottomItem(middleLabel, Icons.Filled.DateRange),
        BottomItem("Perfil", Icons.Filled.Person)
    )

    NavigationBar(containerColor = AppWhite) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.bodySmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    indicatorColor = SecondaryBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

@Preview(name = "Navegación inferior", showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
private fun BottomNavigationBarPreview() {
    MediCitasTheme { BottomNavigationBar() }
}
