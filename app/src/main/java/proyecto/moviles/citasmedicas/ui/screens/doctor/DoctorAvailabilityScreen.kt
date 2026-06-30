package proyecto.moviles.citasmedicas.ui.screens.doctor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import proyecto.moviles.citasmedicas.ui.components.BottomNavigationBar
import proyecto.moviles.citasmedicas.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAvailabilityScreen(
    onBack: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    var fee by remember { mutableStateOf("850") }

    Scaffold(
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppWhite)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 1,
                onItemSelected = { index ->
                    if (index == 0) onNavigateToHome()
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

            // Month Header
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

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Strip
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AppWhite,
                border = BorderStroke(1.dp, BorderSoft.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val days = listOf("D", "L", "M", "M", "J", "V", "S")
                        days.forEach { day ->
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
                        CalendarDay("29", isSelected = false, isPrevMonth = true)
                        CalendarDay("30", isSelected = false, isPrevMonth = true)
                        CalendarDay("1", isSelected = false, isHighlighted = true)
                        CalendarDay("2", isSelected = false)
                        CalendarDay("3", isSelected = false)
                        CalendarDay("4", isSelected = true)
                        CalendarDay("5", isSelected = false)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Schedules Header
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
                TextButton(onClick = { }) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Añadir bloque", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Shift Blocks
            ShiftBlock(
                title = "Turno Mañana",
                icon = Icons.Default.LightMode,
                startTime = "08:00",
                endTime = "14:00",
                isSelected = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ShiftBlock(
                title = "Turno Tarde",
                icon = Icons.Default.DarkMode,
                startTime = "16:00",
                endTime = "20:00",
                isSelected = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fee Section
            Text(
                text = "Tarifa de consulta",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = fee,
                onValueChange = { fee = it },
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

            // Save Button
            Button(
                onClick = { },
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
private fun CalendarDay(
    day: String,
    isSelected: Boolean,
    isHighlighted: Boolean = false,
    isPrevMonth: Boolean = false
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
            .background(backgroundColor),
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
    title: String,
    icon: ImageVector,
    startTime: String,
    endTime: String,
    isSelected: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SecondaryBlue.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else BorderSoft.copy(alpha = 0.3f))
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
                    Text(title, color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }
                Icon(Icons.Default.Delete, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimePickerField(label = "Inicio", time = startTime, modifier = Modifier.weight(1f))
                
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    null,
                    tint = TextSecondary,
                    modifier = Modifier.padding(horizontal = 12.dp).size(20.dp)
                )
                
                TimePickerField(
                    label = "Fin",
                    time = endTime,
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
                Icon(Icons.Default.AccessTime, null, tint = if (isFocused) PrimaryBlue else TextSecondary, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
fun DoctorAvailabilityScreenPreview() {
    MediCitasTheme {
        DoctorAvailabilityScreen()
    }
}
