package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.model.Appointment

data class PatientAppointmentDetailUiState(
    val appointment: Appointment = SampleData.sampleAppointments.first(),
    val reason: String = "Motivo de consulta pendiente",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel del detalle de cita del paciente.
 *
 * Carga la cita y los datos del médico desde Room.
 */
class PatientAppointmentDetailViewModel(
    private val appointmentRepository: AppointmentRepository? = null,
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(PatientAppointmentDetailUiState())
        private set

    suspend fun loadAppointment(appointmentId: Int) {
        if (appointmentRepository == null || doctorRepository == null) {
            val appointment = SampleData.sampleAppointments.firstOrNull { it.id == appointmentId }
                ?: SampleData.sampleAppointments.first()
            uiState = uiState.copy(appointment = appointment)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val appointmentEntity = appointmentRepository.getAppointmentById(appointmentId)

            if (appointmentEntity == null) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se encontró la cita."
                )
                return
            }

            val doctor = doctorRepository.getDoctorById(appointmentEntity.doctorId)

            uiState = uiState.copy(
                appointment = appointmentEntity.toAppointment(doctor),
                reason = appointmentEntity.reason,
                isLoading = false
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar el detalle de la cita."
            )
        }
    }
}
