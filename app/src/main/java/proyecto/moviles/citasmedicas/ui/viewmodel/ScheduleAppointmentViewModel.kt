package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

/**
 * Estado visual de la pantalla para agendar una cita.
 *
 * Aquí se agrupan los valores que el usuario selecciona antes de confirmar:
 * fecha, mes visible, horario, motivo y horarios disponibles.
 */
data class ScheduleAppointmentUiState(
    val selectedDate: LocalDate = LocalDate.of(2024, 10, 13),
    val visibleMonth: YearMonth = YearMonth.of(2024, 10),
    val selectedTime: LocalTime = LocalTime.of(11, 30),
    val reason: String = "",
    val availableTimes: List<LocalTime> = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        LocalTime.of(11, 30),
        LocalTime.of(14, 0),
        LocalTime.of(15, 30),
        LocalTime.of(17, 0),
    )
)

/**
 * ViewModel de agendamiento.
 *
 * Por ahora solo maneja el estado de la pantalla.
 * En el siguiente paso se conectará con el repositorio de citas para guardar en Room.
 */
class ScheduleAppointmentViewModel : ViewModel() {

    var uiState by mutableStateOf(ScheduleAppointmentUiState())
        private set

    /**
     * Actualiza la fecha seleccionada en el calendario.
     */
    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(selectedDate = date)
    }

    /**
     * Muestra el mes anterior en el calendario.
     */
    fun showPreviousMonth() {
        uiState = uiState.copy(
            visibleMonth = uiState.visibleMonth.minusMonths(1)
        )
    }

    /**
     * Muestra el siguiente mes en el calendario.
     */
    fun showNextMonth() {
        uiState = uiState.copy(
            visibleMonth = uiState.visibleMonth.plusMonths(1)
        )
    }

    /**
     * Actualiza el horario seleccionado.
     */
    fun selectTime(time: LocalTime) {
        uiState = uiState.copy(selectedTime = time)
    }

    /**
     * Actualiza el motivo escrito por el paciente.
     */
    fun updateReason(reason: String) {
        uiState = uiState.copy(reason = reason)
    }

    /**
     * Devuelve el estado actual para que la pantalla pueda continuar el flujo.
     *
     * Más adelante aquí se podrá validar información o guardar la cita.
     */
    fun confirmAppointment(): ScheduleAppointmentUiState {
        return uiState.copy(reason = uiState.reason.trim())
    }
}
