package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.model.Appointment
import java.time.LocalDate
import java.time.LocalTime

enum class PatientAppointmentFilter {
    UPCOMING,
    PENDING,
    PAST
}

data class PatientAppointmentsUiState(
    val appointments: List<Appointment> = emptyList(),
    val selectedFilter: PatientAppointmentFilter = PatientAppointmentFilter.UPCOMING,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val visibleAppointments: List<Appointment>
        get() = when (selectedFilter) {
            PatientAppointmentFilter.UPCOMING -> appointments.filter {
                it.status in listOf("Confirmada", "En revisión", "Urgente")
            }
            PatientAppointmentFilter.PENDING -> appointments.filter {
                it.status == "En revisión"
            }
            PatientAppointmentFilter.PAST -> appointments.filter {
                it.status in listOf("Completada", "Cancelada")
            }
        }
}

/**
 * ViewModel para el inicio del paciente.
 *
 * Carga las citas del paciente desde Room y las filtra para la pantalla.
 */
class PatientAppointmentsViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(PatientAppointmentsUiState())
        private set

    suspend fun loadAppointments(patientId: Int) {
        if (appointmentRepository == null || doctorRepository == null) {
            uiState = uiState.copy(appointments = SampleData.sampleAppointments)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val appointments = appointmentRepository
                .getAppointmentsByPatientId(patientId)
                .map { appointment ->
                    val doctor = doctorRepository.getDoctorById(appointment.doctorId)
                    appointment.toAppointment(doctor)
                }

            uiState = uiState.copy(
                appointments = appointments,
                isLoading = false
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudieron cargar tus citas."
            )
        }
    }

    fun selectFilter(filter: PatientAppointmentFilter) {
        uiState = uiState.copy(selectedFilter = filter)
    }
}

internal fun AppointmentEntity.toAppointment(doctor: DoctorEntity?): Appointment {
    return Appointment(
        id = id,
        doctorName = doctor?.name ?: "Médico",
        specialty = doctor?.specialty ?: "Especialidad",
        date = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now()),
        time = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(9, 0)),
        price = "$${doctor?.consultationPrice?.toInt() ?: 0} MXN",
        status = status.toPatientStatus(),
        clinicName = doctor?.clinicName.orEmpty(),
        clinicAddress = doctor?.clinicAddress.orEmpty(),
        latitude = doctor?.clinicLatitude,
        longitude = doctor?.clinicLongitude
    )
}

internal fun String.toPatientStatus(): String {
    return when (uppercase()) {
        "CONFIRMED" -> "Confirmada"
        "PENDING" -> "En revisión"
        "COMPLETED" -> "Completada"
        "CANCELLED" -> "Cancelada"
        "URGENT" -> "Urgente"
        else -> this
    }
}
