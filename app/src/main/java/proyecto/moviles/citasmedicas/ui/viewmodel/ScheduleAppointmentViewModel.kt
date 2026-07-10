package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import proyecto.moviles.citasmedicas.model.AppointmentStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class ScheduleAppointmentUiState(
    val selectedDate: LocalDate = LocalDate.of(2024, 10, 4),
    val visibleMonth: YearMonth = YearMonth.of(2024, 10),
    val selectedTime: LocalTime? = null,
    val reason: String = "",
    val isSaving: Boolean = false,
    val isLoadingTimes: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val availableTimes: List<LocalTime> = emptyList()
)

/**
 * ViewModel de agendamiento.
 *
 * Usa la disponibilidad real del médico guardada en Room,
 * oculta horarios ocupados y guarda la cita si el horario sigue libre.
 */
class ScheduleAppointmentViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val doctorAvailabilityRepository: DoctorAvailabilityRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(ScheduleAppointmentUiState())
        private set

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun loadAvailableTimes(doctorId: Int) {
        if (appointmentRepository == null || doctorAvailabilityRepository == null) {
            uiState = uiState.copy(
                availableTimes = previewTimes(),
                selectedTime = previewTimes().firstOrNull(),
                errorMessage = null
            )
            return
        }

        uiState = uiState.copy(
            isLoadingTimes = true,
            errorMessage = null,
            successMessage = null
        )

        try {
            val day = uiState.selectedDate.dayOfMonth.toString()
            val configuredTimes = doctorAvailabilityRepository
                .getAvailabilityByDoctorAndDay(doctorId = doctorId, day = day)
                .flatMap { availability -> availability.toTimeSlots() }
                .distinct()
                .sorted()

            val freeTimes = configuredTimes.filter { time ->
                appointmentRepository.getAppointmentByDoctorDateAndTime(
                    doctorId = doctorId,
                    date = uiState.selectedDate.toString(),
                    time = time.format(timeFormatter)
                ) == null
            }

            uiState = uiState.copy(
                availableTimes = freeTimes,
                selectedTime = freeTimes.firstOrNull(),
                isLoadingTimes = false,
                errorMessage = if (freeTimes.isEmpty()) {
                    "No hay horarios disponibles para esta fecha."
                } else {
                    null
                }
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                availableTimes = emptyList(),
                selectedTime = null,
                isLoadingTimes = false,
                errorMessage = "No se pudieron cargar los horarios disponibles."
            )
        }
    }

    fun selectDate(date: LocalDate) {
        uiState = uiState.copy(
            selectedDate = date,
            selectedTime = null,
            availableTimes = emptyList(),
            errorMessage = null,
            successMessage = null
        )
    }

    fun showPreviousMonth() {
        uiState = uiState.copy(visibleMonth = uiState.visibleMonth.minusMonths(1))
    }

    fun showNextMonth() {
        uiState = uiState.copy(visibleMonth = uiState.visibleMonth.plusMonths(1))
    }

    fun selectTime(time: LocalTime) {
        if (time !in uiState.availableTimes) {
            uiState = uiState.copy(errorMessage = "Ese horario ya no está disponible.")
            return
        }

        uiState = uiState.copy(
            selectedTime = time,
            errorMessage = null,
            successMessage = null
        )
    }

    fun updateReason(reason: String) {
        uiState = uiState.copy(
            reason = reason,
            errorMessage = null,
            successMessage = null
        )
    }

    suspend fun confirmAppointment(
        patientId: Int,
        doctorId: Int
    ): Boolean {
        val cleanReason = uiState.reason.trim()
        val selectedTime = uiState.selectedTime

        if (selectedTime == null) {
            uiState = uiState.copy(errorMessage = "Selecciona un horario disponible.")
            return false
        }

        if (cleanReason.isBlank()) {
            uiState = uiState.copy(errorMessage = "Ingresa el motivo de la consulta.")
            return false
        }

        if (appointmentRepository == null) {
            uiState = uiState.copy(successMessage = "Cita preparada para guardar.")
            return true
        }

        uiState = uiState.copy(
            isSaving = true,
            errorMessage = null,
            successMessage = null
        )

        return try {
            val existingAppointment = appointmentRepository.getAppointmentByDoctorDateAndTime(
                doctorId = doctorId,
                date = uiState.selectedDate.toString(),
                time = selectedTime.format(timeFormatter)
            )

            if (existingAppointment != null) {
                uiState = uiState.copy(
                    isSaving = false,
                    errorMessage = "Ese horario ya fue ocupado. Elige otro horario."
                )
                loadAvailableTimes(doctorId)
                return false
            }

            appointmentRepository.insertAppointment(
                AppointmentEntity(
                    patientId = patientId,
                    doctorId = doctorId,
                    date = uiState.selectedDate.toString(),
                    time = selectedTime.format(timeFormatter),
                    reason = cleanReason,
                    status = AppointmentStatus.PENDING,
                    createdAt = LocalDate.now().toString()
                )
            )

            uiState = uiState.copy(
                reason = cleanReason,
                isSaving = false,
                successMessage = "Cita guardada correctamente."
            )

            loadAvailableTimes(doctorId)
            true
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isSaving = false,
                errorMessage = "No se pudo guardar la cita. Verifica paciente, médico y horario."
            )
            false
        }
    }

    private fun DoctorAvailabilityEntity.toTimeSlots(): List<LocalTime> {
        val start = runCatching { LocalTime.parse(startTime, timeFormatter) }.getOrNull() ?: return emptyList()
        val end = runCatching { LocalTime.parse(endTime, timeFormatter) }.getOrNull() ?: return emptyList()

        if (!start.isBefore(end)) return emptyList()

        val slots = mutableListOf<LocalTime>()
        var current = start

        while (current.isBefore(end)) {
            slots.add(current)
            current = current.plusHours(1)
        }

        return slots
    }

    private fun previewTimes(): List<LocalTime> {
        return listOf(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 30),
            LocalTime.of(14, 0),
            LocalTime.of(15, 30),
            LocalTime.of(17, 0)
        )
    }
}
