package proyecto.moviles.citasmedicas.ui.screens.patient

/* Agendamiento: permite elegir fecha, horario y motivo antes de confirmar la cita simulada. */

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
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
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ScheduleDetailsScreen(
    onBack: () -> Unit,
    onConfirm: (LocalDate, LocalTime, String) -> Unit,
    modifier: Modifier = Modifier,
    appointmentRepository: AppointmentRepository? = null,
    patientId: Int = 1,
    doctorId: Int = 1
) {
    // ViewModel temporal de la pantalla. Después se podrá inyectar desde navegación.
    val viewModel = remember(appointmentRepository) {
        ScheduleAppointmentViewModel(appointmentRepository)
    }

    // Estado actual que usa la interfaz.
    val uiState = viewModel.uiState

    // Scope para ejecutar operaciones suspend, como guardar en Room.
    val coroutineScope = rememberCoroutineScope()

    /**
     * Esta es la estructura base de la pantalla.
     */
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppBackground,

        /**
         * Barra superior de la pantalla.
         *
         * Fue detallada en una función por separado.
         */
        topBar = {
            ScheduleDetailsTopBar(
                onBack = onBack
            )
        },

        /**
         * Barra inferior de la pantalla.
         *
         * Se coloca el botón para confirmar cita.
         */
        bottomBar = {
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
                    enabled = !uiState.isSaving,
                    onClick = {
                        coroutineScope.launch {
                            val saved = viewModel.confirmAppointment(
                                patientId = patientId,
                                doctorId = doctorId
                            )

                            if (saved) {
                                val confirmedState = viewModel.uiState
                                onConfirm(
                                    confirmedState.selectedDate,
                                    confirmedState.selectedTime,
                                    confirmedState.reason
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        /**
         * Contenido principal de la pantalla.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

                // Permite hacer scroll si el contenido no cabe
                .verticalScroll(rememberScrollState())

                // Margen horizontal
                .padding(horizontal = 20.dp)

                // Espacio inferior para que el contenido no sea tapado por el botón
                .padding(bottom = 96.dp)
        ) {
            // Título principal de la pantalla
            Text(
                text = "Seleccionar fecha y hora",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Subtítulo de apoyo
            Text(
                text = "Confirma tu próxima consulta médica",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Calendario. Por ahora maneja el estado localmente; después el ViewModel podrá recibirlo.
            ScheduleCalendarCard(
                visibleMonth = uiState.visibleMonth,
                selectedDate = uiState.selectedDate,
                onPreviousMonthClick = viewModel::showPreviousMonth,
                onNextMonthClick = viewModel::showNextMonth,
                onDateSelected = viewModel::selectDate
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título para la selección de horarios
            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contenedor de los horarios
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // Esto divide la lista de horarios en grupos de 3
                uiState.availableTimes.chunked(3).forEach { rowTimes ->

                    // Cada row es una fila de horarios
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // Se crea un botón por cada horario
                        rowTimes.forEach { time ->
                            TimeSlotButton(
                                time = time,

                                // Se marca como seleccionado si coincide con selectedTime
                                selected = uiState.selectedTime == time,

                                // Al presionar un horario se actualiza el seleccionado
                                onClick = viewModel::selectTime,

                                // Hace que los botones tengan el mismo ancho.
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Campo de texto para escribir el motivo de la consulta.
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

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

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

/**
 * Barra superior de la pantalla.
 *
 * Contiene el botón para regresar, el nombre de la app y el ícono de perfil.
 */
@Composable
private fun ScheduleDetailsTopBar(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack
        ) {
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

        // Acción visual reservada para conectar el perfil posteriormente.
        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = PrimaryBlue
            )
        }
    }
}

/**
 * Preview de la pantalla.
 */
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
        ScheduleDetailsScreen(
            onBack = {},
            onConfirm = { _, _, _ -> }
        )
    }
}
