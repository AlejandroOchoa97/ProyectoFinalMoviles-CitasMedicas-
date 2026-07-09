package proyecto.moviles.citasmedicas.ui.screens.doctor

/* Disponibilidad médica: permite configurar horarios y guardarlos en Room. */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.viewmodel.DoctorAvailabilityBlockUi
import proyecto.moviles.citasmedicas.ui.viewmodel.DoctorAvailabilityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAvailabilityScreen(
    onBack: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    availabilityRepository: DoctorAvailabilityRepository? = null,
    doctorId: Int = 1
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ViewModel que carga y guarda la disponibilidad médica en Room.
    val viewModel = remember(availabilityRepository) {
        DoctorAvailabilityViewModel(availabilityRepository)
    }
    val uiState = viewModel.uiState

    // Al abrir la pantalla se cargan los bloques guardados del médico activo.
    LaunchedEffect(doctorId) {
        viewModel.loadAvailability(doctorId)
    }

    // Muestra mensajes de éxito o error producidos por el ViewModel.
    LaunchedEffect(uiState.message, uiState.errorMessage) {
        val message = uiState.message ?: uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Disponibilidad",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = PrimaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppWhite)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 1,
                middleLabel = "Disponibilidad",
                onItemSelected = { index ->
                    if (index == 0) onNavigateToHome()
                    if (index == 2) onProfileClick()
                }
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MonthHeader()

            Spacer(modifier = Modifier.height(16.dp))

            CalendarStrip(
                selectedDay = uiState.selectedDay,
                onDaySelected = viewModel::selectDay
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Horarios configurados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(onClick = viewModel::addBlock) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Añadir bloque", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            uiState.blocks.forEachIndexed { index, block ->
                ShiftBlock(
                    block = block,
                    icon = block.iconForTitle(),
                    isSelected = uiState.selectedBlockTitle == block.title,
                    onClick = { viewModel.selectBlock(block.title) },
                    onDelete = { viewModel.deleteBlock(index) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tarifa de consulta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.consultationFee,
                onValueChange = viewModel::updateConsultationFee,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("$ ", color = TextPrimary) },
                suffix = { Text("MXN", color = TextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderSoft,
                    focusedBorderColor = PrimaryBlue
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                text = "Este precio se mostrará a los pacientes al reservar.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveAvailability(doctorId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Done, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Guardar cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun MonthHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Octubre 2023",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Row {
            Icon(Icons.Default.KeyboardArrowLeft, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.KeyboardArrowRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun CalendarStrip(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = AppWhite,
        border = BorderStroke(1.dp, BorderSoft.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("D", "L", "M", "M", "J", "V", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CalendarDay("29", isSelected = selectedDay == "29", isPrevMonth = true, onClick = { onDaySelected("29") })
                CalendarDay("30", isSelected = selectedDay == "30", isPrevMonth = true, onClick = { onDaySelected("30") })
                CalendarDay("1", isSelected = selectedDay == "1", isHighlighted = selectedDay != "1", onClick = { onDaySelected("1") })
                CalendarDay("2", isSelected = selectedDay == "2", onClick = { onDaySelected("2") })
                CalendarDay("3", isSelected = selectedDay == "3", onClick = { onDaySelected("3") })
                CalendarDay("4", isSelected = selectedDay == "4", onClick = { onDaySelected("4") })
                CalendarDay("5", isSelected = selectedDay == "5", onClick = { onDaySelected("5") })
            }
        }
    }
}

@Composable
private fun CalendarDay(
    day: String,
    isSelected: Boolean,
    isHighlighted: Boolean = false,
    isPrevMonth: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor = when {
        isSelected -> PrimaryBlue
        isHighlighted -> SecondaryBlue.copy(alpha = 0.5f)
        else -> Color.Transparent
    }
    val textColor = when {
        isSelected -> AppWhite
        isPrevMonth -> BorderSoft
        else -> TextPrimary
    }

    Box(
        modifier = Modifier
            .size(40.dp, 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected || isHighlighted) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

@Composable
private fun ShiftBlock(
    block: DoctorAvailabilityBlockUi,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = SecondaryBlue.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, if (isSelected) PrimaryBlue else BorderSoft.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(block.title, color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimePickerField(label = "Inicio", time = block.startTime, modifier = Modifier.weight(1f))

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = TextSecondary,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(20.dp)
                )

                TimePickerField(
                    label = "Fin",
                    time = block.endTime,
                    modifier = Modifier.weight(1f),
                    isFocused = isSelected
                )
            }
        }
    }
}

@Composable
private fun TimePickerField(
    label: String,
    time: String,
    modifier: Modifier = Modifier,
    isFocused: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isFocused) AppWhite else BorderSoft.copy(alpha = 0.3f),
            border = BorderStroke(1.dp, if (isFocused) PrimaryBlue else Color.Transparent)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = time, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                Icon(
                    Icons.Default.AccessTime,
                    null,
                    tint = if (isFocused) PrimaryBlue else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private fun DoctorAvailabilityBlockUi.iconForTitle(): ImageVector {
    return when {
        title.contains("mañana", ignoreCase = true) -> Icons.Default.LightMode
        title.contains("tarde", ignoreCase = true) -> Icons.Default.DarkMode
        else -> Icons.Default.AccessTime
    }
}

@Preview(showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
fun DoctorAvailabilityScreenPreview() {
    MediCitasTheme {
        DoctorAvailabilityScreen()
    }
}
