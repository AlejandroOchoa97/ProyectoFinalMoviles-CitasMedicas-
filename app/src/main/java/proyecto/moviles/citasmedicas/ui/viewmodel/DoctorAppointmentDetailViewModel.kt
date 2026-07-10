package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.model.AppointmentStatus
import proyecto.moviles.citasmedicas.model.DoctorAppointment
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

data class DoctorAppointmentDetailUiState(
    val appointment: DoctorAppointment = SampleData.sampleDoctorAppointments.last(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel del detalle de cita del médico.
 *
 * Carga una cita por ID desde Room y la convierte al modelo que usa la pantalla.
 */
class DoctorAppointmentDetailViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val patientRepository: PatientRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(DoctorAppointmentDetailUiState())
        private set

    /**
     * Carga la cita seleccionada. Si no hay repositorios, conserva datos demo para Preview.
     */
    suspend fun loadAppointment(appointmentId: Int) {
        if (appointmentRepository == null || patientRepository == null) {
            val fallback = SampleData.sampleDoctorAppointments
                .find { it.id == appointmentId }
                ?: SampleData.sampleDoctorAppointments.last()

            uiState = uiState.copy(appointment = fallback)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val appointment = appointmentRepository.getAppointmentById(appointmentId)
            if (appointment == null) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se encontró la cita seleccionada."
                )
                return
            }

            val patient = patientRepository.getPatientById(appointment.patientId)

            uiState = uiState.copy(
                appointment = appointment.toDoctorAppointment(patient),
                isLoading = false,
                errorMessage = null
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar el detalle de la cita."
            )
        }
    }

    private fun AppointmentEntity.toDoctorAppointment(patient: PatientEntity?): DoctorAppointment {
        val appointmentDate = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
        val appointmentTime = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(9, 0))
        val isUrgentAppointment = AppointmentStatus.isUrgent(status)

        return DoctorAppointment(
            id = id,
            patientName = patient?.name ?: "Paciente",
            patientAge = patient?.birthDate?.toAge() ?: 0,
            patientGender = "Masculino",
            patientPhone = patient?.phone ?: "+52 55 0000 0000",
            date = appointmentDate,
            time = appointmentTime,
            reason = reason,
            detailedReason = reason,
            specialty = "Consulta médica",
            status = AppointmentStatus.toDisplayName(status),
            isUrgent = isUrgentAppointment,
            hasNotification = isUrgentAppointment
        )
    }

    private fun String.toAge(): Int {
        return runCatching {
            Period.between(LocalDate.parse(this), LocalDate.now()).years
        }.getOrDefault(0)
    }

}
