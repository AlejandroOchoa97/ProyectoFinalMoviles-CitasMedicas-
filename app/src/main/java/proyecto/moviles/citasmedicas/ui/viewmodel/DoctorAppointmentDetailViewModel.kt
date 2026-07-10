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
import java.time.format.DateTimeFormatter

data class DoctorAppointmentDetailUiState(
    val appointment: DoctorAppointment = SampleData.sampleDoctorAppointments.last(),
    val prescriptionText: String = "",
    val successMessage: String? = null,
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

    private var currentAppointmentEntity: AppointmentEntity? = null

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
            currentAppointmentEntity = appointment

            uiState = uiState.copy(
                appointment = appointment.toDoctorAppointment(patient),
                prescriptionText = appointment.prescription,
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

    fun updatePrescriptionText(text: String) {
        uiState = uiState.copy(prescriptionText = text)
    }

    suspend fun savePrescription() {
        val current = currentAppointmentEntity ?: return
        val updated = current.copy(prescription = uiState.prescriptionText.trim())
        appointmentRepository?.updateAppointment(updated)
        currentAppointmentEntity = updated
        uiState = uiState.copy(
            appointment = updated.toDoctorAppointment(patientRepository?.getPatientById(updated.patientId)),
            successMessage = "Receta guardada"
        )
    }

    suspend fun updateStatus(status: String, message: String) {
        val current = currentAppointmentEntity ?: return
        val updated = current.copy(status = status)
        appointmentRepository?.updateAppointment(updated)
        currentAppointmentEntity = updated
        uiState = uiState.copy(
            appointment = updated.toDoctorAppointment(patientRepository?.getPatientById(updated.patientId)),
            successMessage = message
        )
    }

    private fun AppointmentEntity.toDoctorAppointment(patient: PatientEntity?): DoctorAppointment {
        val appointmentDate = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
        val appointmentTime = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(9, 0))
        val isUrgentAppointment = AppointmentStatus.isUrgent(status)

        return DoctorAppointment(
            id = id,
            patientName = patient?.name?.toDisplayNameWithFallback() ?: "Paciente MediCitas",
            patientAge = patient?.birthDate?.toAge() ?: 0,
            patientGender = "Masculino",
            patientPhone = patient?.phone ?: "+52 55 0000 0000",
            date = appointmentDate,
            time = appointmentTime,
            reason = reason,
            detailedReason = reason,
            specialty = "Consulta médica",
            status = AppointmentStatus.toDisplayName(status),
            prescription = prescription,
            isUrgent = isUrgentAppointment,
            hasNotification = isUrgentAppointment
        )
    }

    private fun String.toAge(): Int {
        val birthDate = runCatching { LocalDate.parse(this) }.getOrNull()
            ?: runCatching { LocalDate.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy")) }.getOrNull()

        return birthDate?.let { Period.between(it, LocalDate.now()).years } ?: 0
    }

    private fun String.toDisplayNameWithFallback(): String {
        val words = trim()
            .lowercase()
            .split(" ")
            .filter { it.isNotBlank() }

        val normalized = words.joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }

        return when {
            normalized.isBlank() -> "Paciente MediCitas"
            else -> normalized
        }
    }
}
