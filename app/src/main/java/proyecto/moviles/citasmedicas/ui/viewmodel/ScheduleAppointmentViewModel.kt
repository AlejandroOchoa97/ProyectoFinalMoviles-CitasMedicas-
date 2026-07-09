package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
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
 * Maneja el estado de la pantalla y, cuando recibe un repositorio,
 * permite guardar la cita en Room.
 */
class ScheduleAppointmentViewModel(
    private val appointmentRepository: AppointmentRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(ScheduleAppointmentUiState())
        private set

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    /**
     * Actualiza la fecha seleccionada en el calendario.
     */
    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(
            selectedDate = date,
            errorMessage = null,
            successMessage = null
        )
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
        uiState = uiState.copy(
            selectedTime = time,
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Actualiza el motivo escrito por el paciente.
     */
    fun updateReason(reason: String) {
        uiState = uiState.copy(
            reason = reason,
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Guarda la cita en Room usando el repositorio recibido.
     *
     * Los IDs son temporales mientras se conecta el inicio de sesión real:
     * - patientId vendrá del paciente autenticado.
     * - doctorId vendrá del médico seleccionado en la búsqueda.
     */
    suspend fun confirmAppointment(
        patientId: Int,
        doctorId: Int
    ): Boolean {
        val cleanReason = uiState.reason.trim()

        if (cleanReason.isBlank()) {
            uiState = uiState.copy(
                errorMessage = "Ingresa el motivo de la consulta."
            )
            return false
        }

        if (appointmentRepository == null) {
            uiState = uiState.copy(
                successMessage = "Cita preparada para guardar."
            )
            return true
        }

        uiState = uiState.copy(
            isSaving = true,
            errorMessage = null,
            successMessage = null
        )

        return try {
            appointmentRepository.insertAppointment(
                AppointmentEntity(
                    patientId = patientId,
                    doctorId = doctorId,
                    date = uiState.selectedDate.toString(),
                    time = uiState.selectedTime.format(timeFormatter),
                    reason = cleanReason,
                    status = "PENDING",
                    createdAt = LocalDate.now().toString()
                )
            )

            uiState = uiState.copy(
                reason = cleanReason,
                isSaving = false,
                successMessage = "Cita guardada correctamente."
            )
            true
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isSaving = false,
                errorMessage = "No se pudo guardar la cita. Verifica que exista el paciente y el médico."
            )
            false
        }
    }
}
