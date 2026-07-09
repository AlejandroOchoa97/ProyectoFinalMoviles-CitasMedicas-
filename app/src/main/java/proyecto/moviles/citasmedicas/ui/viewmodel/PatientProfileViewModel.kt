package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.PatientRepository

data class PatientProfileUiState(
    val name: String = "Juan Pérez",
    val email: String = "paciente@medicitas.com",
    val phone: String = "+52 55 0000 0000",
    val birthDate: String = "1998-05-12",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel del perfil del paciente.
 *
 * Lee los datos del paciente desde Room.
 */
class PatientProfileViewModel(
    private val patientRepository: PatientRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(PatientProfileUiState())
        private set

    suspend fun loadPatient(patientId: Int) {
        if (patientRepository == null) return

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val patient = patientRepository.getPatientById(patientId)

            uiState = if (patient != null) {
                patient.toUiState().copy(isLoading = false)
            } else {
                uiState.copy(
                    isLoading = false,
                    errorMessage = "No se encontró el perfil del paciente."
                )
            }
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar el perfil del paciente."
            )
        }
    }

    private fun PatientEntity.toUiState(): PatientProfileUiState {
        return PatientProfileUiState(
            name = name,
            email = email,
            phone = phone,
            birthDate = birthDate
        )
    }
}
