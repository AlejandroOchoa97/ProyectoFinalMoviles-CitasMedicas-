package proyecto.moviles.citasmedicas.ui.screens.patient

/* Agendamiento: fecha, horario disponible real y motivo antes de guardar en Room. */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import proyecto.moviles.citasmedicas.ui.components.AppButton
import proyecto.moviles.citasmedicas.ui.components.ScheduleCalendarCard
import proyecto.moviles.citasmedicas.ui.components.TimeSlotButton
import proyecto.moviles.citasmedicas.ui.theme.AppBackground
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.OutlineGray
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary
import proyecto.moviles.citasmedicas.ui.theme.TextSecondary
import proyecto.moviles.citasmedicas.ui.viewmodel.ScheduleAppointmentViewModel

@Composable
fun ScheduleDetailsScreen(
    onBack: () -> Unit,
    onConfirm: (LocalDate, LocalTime, String) -> Unit,
    modifier: Modifier = Modifier,
    appointmentRepository: AppointmentRepository? = null,
    doctorAvailabilityRepository: DoctorAvailabilityRepository? = null,
    patientId: Int = 1,
    doctorId: Int = 1
) {
    // Se crea el ViewModel de esta pantalla.
    // Aqui se controla la fecha, el horario, el motivo y el guardado de la cita.
    val viewModel = remember(appointmentRepository, doctorAvailabilityRepository) {
        ScheduleAppointmentViewModel(
            appointmentRepository = appointmentRepository,
            doctorAvailabilityRepository = doctorAvailabilityRepository
        )
    }

    // uiState guarda lo que se muestra en pantalla: horarios, fecha seleccionada,
    // mensajes de carga, errores y confirmacion.
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    // Al abrir la pantalla se cargan los horarios reales del medico seleccionado desde Room.
    LaunchedEffect(doctorId) {
        viewModel.loadAvailableTimes(doctorId)
    }

    // Scaffold nos ayuda a separar la barra superior, el contenido y el boton inferior.
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,
        topBar = { ScheduleDetailsTopBar(onBack = onBack) },
        bottomBar = {
            // El boton queda fijo abajo para confirmar la cita sin perderlo al hacer scroll.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppBackground)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                AppButton(
                    text = if (uiState.isSaving) "Guardando..." else "Confirmar",
                    // Se desactiva si esta guardando, cargando horarios o si no hay horarios disponibles.
                    enabled = !uiState.isSaving &&
                        !uiState.isLoadingTimes &&
                        uiState.availableTimes.isNotEmpty(),
                    onClick = {
                        coroutineScope.launch {
                            // Aqui se guarda la cita en Room con el paciente y medico seleccionados.
                            val saved = viewModel.confirmAppointment(
                                patientId = patientId,
                                doctorId = doctorId
                            )

                            // Si se guardo bien, se regresa la fecha, hora y motivo a la navegacion.
                            val confirmedTime = viewModel.uiState.selectedTime
                            if (saved && confirmedTime != null) {
                                onConfirm(
                                    viewModel.uiState.selectedDate,
                                    confirmedTime,
                                    viewModel.uiState.reason
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // Se agrega scroll para que no se corte en pantallas pequenas.
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                // Espacio extra para que el boton inferior no tape el campo del motivo.
                .padding(bottom = 96.dp)
        ) {
            Text(
                text = "Seleccionar fecha y hora",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Confirma tu prÃ³xima consulta mÃ©dica",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Calendario donde el paciente selecciona el dia de la consulta.
            ScheduleCalendarCard(
                visibleMonth = uiState.visibleMonth,
                selectedDate = uiState.selectedDate,
                onPreviousMonthClick = viewModel::showPreviousMonth,
                onNextMonthClick = viewModel::showNextMonth,
                onDateSelected = { date ->
                    coroutineScope.launch {
                        // Cuando cambia la fecha, se actualizan tambien los horarios disponibles.
                        viewModel.selectDate(date)
                        viewModel.loadAvailableTimes(doctorId)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (uiState.isLoadingTimes) "Cargando horarios..." else "Horarios disponibles",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Los horarios se acomodan en filas de tres botones para que se vea ordenado.
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (uiState.availableTimes.isEmpty() && !uiState.isLoadingTimes) {
                    Text(
                        text = "No hay horarios disponibles para esta fecha.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                uiState.availableTimes.chunked(3).forEach { rowTimes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTimes.forEach { time ->
                            // Cada boton representa un horario disponible del medico.
                            TimeSlotButton(
                                time = time,
                                selected = uiState.selectedTime == time,
                                onClick = viewModel::selectTime,
                                enabled = !uiState.isLoadingTimes && !uiState.isSaving,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Campo donde el paciente escribe el motivo; este dato se guarda con la cita.
            OutlinedTextField(
                value = uiState.reason,
                onValueChange = viewModel::updateReason,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        text = "Motivo de consulta",
                        color = TextSecondary
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppWhite,
                    unfocusedContainerColor = AppWhite,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = OutlineGray,
                    cursorColor = PrimaryBlue
                )
            )

            // Mensaje de error si no se pudo cargar o guardar la cita.
            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Mensaje de exito cuando la cita se guarda correctamente.
            uiState.successMessage?.let { message ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = PrimaryBlue
                )
            }
        }
    }
}

@Composable
private fun ScheduleDetailsTopBar(onBack: () -> Unit) {
    // Barra superior con regreso, nombre de la app e icono de perfil.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Regresar",
                tint = PrimaryBlue
            )
        }

        Text(
            text = "MediCitas",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = PrimaryBlue
            )
        }
    }
}

@Preview(
    name = "Schedule Details Screen",
    showBackground = true,
    showSystemUi = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun ScheduleDetailsScreenPreview() {
    MediCitasTheme {
        // Preview para revisar esta pantalla en Android Studio sin correr toda la app.
        ScheduleDetailsScreen(
            onBack = {},
            onConfirm = { _, _, _ -> }
        )
    }
}

