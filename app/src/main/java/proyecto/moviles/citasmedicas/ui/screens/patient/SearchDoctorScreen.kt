package proyecto.moviles.citasmedicas.ui.screens.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.components.DoctorCard
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary

@Composable
fun SearchDoctorScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf("Todos") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val specialties = listOf("Todos", "Medicina General", "Cardiología", "Pediatría", "Dermatología")
    val doctors = SampleData.sampleDoctors.filter { doctor ->
        (selectedSpecialty == "Todos" || doctor.specialty == selectedSpecialty) &&
            (query.isBlank() || doctor.name.contains(query, true) || doctor.specialty.contains(query, true))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { SearchDoctorTopBar(onBack) },
        bottomBar = { BottomNavigationBar(selectedIndex = 0) }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por nombre o especialidad") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppWhite,
                    unfocusedContainerColor = AppWhite,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderSoft
                )
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(specialties) { specialty ->
                    FilterChip(
                        selected = selectedSpecialty == specialty,
                        onClick = { selectedSpecialty = specialty },
                        label = { Text(specialty) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryBlue,
                            selectedLabelColor = AppWhite,
                            containerColor = SecondaryBlue,
                            labelColor = TextSecondary
                        ),
                        border = null
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(doctors, key = { it.id }) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onProfileClick = {
                            scope.launch { snackbarHostState.showSnackbar("Perfil del médico pendiente") }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchDoctorTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Regresar", tint = PrimaryBlue)
        }
        Text(
            "Buscar Médicos",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {}) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil", tint = TextSecondary, modifier = Modifier.size(25.dp))
        }
    }
}

@Preview(
    name = "Buscar Médicos",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun SearchDoctorScreenPreview() {
    MediCitasTheme { SearchDoctorScreen(onBack = {}) }
}
