package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.model.DoctorAppointment
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

enum class DoctorAppointmentFilter {
    ALL,
    URGENT,
    FOLLOW_UP
}

data class DoctorHomeUiState(
    val doctorName: String = "DR. ALEJANDRO V.",
    val appointments: List<DoctorAppointment> = emptyList(),
    val selectedFilter: DoctorAppointmentFilter = DoctorAppointmentFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val urgentCount: Int
        get() = appointments.count { it.isUrgent }

    val followUpCount: Int
        get() = appointments.count { !it.isUrgent }

    val visibleAppointments: List<DoctorAppointment>
        get() = when (selectedFilter) {
            DoctorAppointmentFilter.ALL -> appointments
            DoctorAppointmentFilter.URGENT -> appointments.filter { it.isUrgent }
            DoctorAppointmentFilter.FOLLOW_UP -> appointments.filter { !it.isUrgent }
        }
}

/**
 * ViewModel del inicio médico.
 *
 * Carga las citas del médico desde Room y las convierte al modelo visual
 * que ya utilizan las tarjetas de la pantalla.
 */
class DoctorHomeViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val patientRepository: PatientRepository? = null,
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(DoctorHomeUiState())
        private set

    /**
     * Carga las citas del médico.
     *
     * Cuando no hay repositorios, usa datos de muestra para mantener funcional el Preview.
     */
    suspend fun loadAppointments(doctorId: Int) {
        if (appointmentRepository == null || patientRepository == null) {
            uiState = uiState.copy(appointments = SampleData.sampleDoctorAppointments)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val doctor = doctorRepository?.getDoctorById(doctorId)
            val appointments = appointmentRepository
                .getAppointmentsByDoctorId(doctorId)
                .map { appointment ->
                    val patient = patientRepository.getPatientById(appointment.patientId)
                    appointment.toDoctorAppointment(patient)
                }

            uiState = uiState.copy(
                doctorName = doctor?.name?.toDoctorHeaderName() ?: uiState.doctorName,
                appointments = appointments,
                isLoading = false,
                errorMessage = null
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudieron cargar las citas del médico."
            )
        }
    }

    /**
     * Cambia el filtro seleccionado en la pantalla.
     */
    fun selectFilter(filter: DoctorAppointmentFilter) {
        uiState = uiState.copy(selectedFilter = filter)
    }

    private fun AppointmentEntity.toDoctorAppointment(patient: PatientEntity?): DoctorAppointment {
        val appointmentDate = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
        val appointmentTime = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(9, 0))
        val isUrgentAppointment = status.equals("URGENT", ignoreCase = true)

        return DoctorAppointment(
            id = id,
            patientName = patient?.name ?: "Paciente",
            patientAge = patient?.birthDate?.toAge() ?: 0,
            patientPhone = patient?.phone ?: "+52 55 0000 0000",
            date = appointmentDate,
            time = appointmentTime,
            reason = reason,
            detailedReason = reason,
            status = status.toDisplayStatus(),
            isUrgent = isUrgentAppointment,
            hasNotification = isUrgentAppointment
        )
    }

    private fun String.toAge(): Int {
        return runCatching {
            Period.between(LocalDate.parse(this), LocalDate.now()).years
        }.getOrDefault(0)
    }

    private fun String.toDisplayStatus(): String {
        return when (uppercase()) {
            "CONFIRMED" -> "Confirmada"
            "PENDING" -> "Pendiente"
            "COMPLETED" -> "Completada"
            "CANCELLED" -> "Cancelada"
            "URGENT" -> "Urgente"
            else -> this
        }
    }

    private fun String.toDoctorHeaderName(): String {
        return uppercase()
    }
}
