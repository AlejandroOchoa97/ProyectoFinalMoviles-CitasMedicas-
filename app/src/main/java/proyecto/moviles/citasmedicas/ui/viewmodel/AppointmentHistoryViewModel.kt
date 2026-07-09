package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import java.time.LocalDate
import java.time.LocalTime

enum class HistoryFilter {
    ALL,
    PRESCRIPTIONS,
    CANCELLED
}

data class PatientHistoryAppointment(
    val id: Int,
    val doctor: String,
    val specialty: String,
    val date: LocalDate,
    val time: LocalTime,
    val status: String,
    val hasPrescription: Boolean
)

data class AppointmentHistoryUiState(
    val appointments: List<PatientHistoryAppointment> = emptyList(),
    val selectedFilter: HistoryFilter = HistoryFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val visibleAppointments: List<PatientHistoryAppointment>
        get() = when (selectedFilter) {
            HistoryFilter.ALL -> appointments
            HistoryFilter.PRESCRIPTIONS -> appointments.filter { it.hasPrescription }
            HistoryFilter.CANCELLED -> appointments.filter { it.status == "Cancelada" }
        }
}

/**
 * ViewModel del historial del paciente.
 *
 * Carga citas completadas o canceladas desde Room.
 */
class AppointmentHistoryViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(AppointmentHistoryUiState())
        private set

    suspend fun loadHistory(patientId: Int) {
        if (appointmentRepository == null || doctorRepository == null) return

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val appointments = appointmentRepository
                .getAppointmentsByPatientId(patientId)
                .filter { it.status in listOf("COMPLETED", "CANCELLED") }
                .map { appointment ->
                    val doctor = doctorRepository.getDoctorById(appointment.doctorId)
                    appointment.toHistoryAppointment(doctor)
                }

            uiState = uiState.copy(
                appointments = appointments,
                isLoading = false
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar el historial."
            )
        }
    }

    fun selectFilter(filter: HistoryFilter) {
        uiState = uiState.copy(selectedFilter = filter)
    }

    private fun AppointmentEntity.toHistoryAppointment(doctor: DoctorEntity?): PatientHistoryAppointment {
        val completed = status.equals("COMPLETED", ignoreCase = true)

        return PatientHistoryAppointment(
            id = id,
            doctor = doctor?.name ?: "Médico",
            specialty = doctor?.specialty ?: "Especialidad",
            date = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now()),
            time = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(9, 0)),
            status = status.toPatientStatus(),
            hasPrescription = completed
        )
    }
}
