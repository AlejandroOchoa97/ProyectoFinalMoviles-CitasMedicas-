package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository

data class DoctorProfileUiState(
    val name: String = "Dra. Elena Ruiz",
    val email: String = "elena@medicitas.com",
    val specialty: String = "Cardiología",
    val professionalLicense: String = "CARD-001",
    val experienceYears: Int = 10,
    val clinicName: String = "Torre Médica Metropolitan",
    val clinicAddress: String = "Av. Insurgentes Sur 1582, CDMX",
    val consultationPrice: Double = 800.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel del perfil médico.
 *
 * Carga desde Room los datos del médico activo para evitar reutilizar
 * el perfil del paciente.
 */
class DoctorProfileViewModel(
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(DoctorProfileUiState())
        private set

    /**
     * Carga el médico por ID.
     *
     * Si no hay repositorio, mantiene datos demo para que el Preview funcione.
     */
    suspend fun loadDoctor(doctorId: Int) {
        if (doctorRepository == null) return

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val doctor = doctorRepository.getDoctorById(doctorId)

            uiState = if (doctor != null) {
                doctor.toUiState().copy(isLoading = false)
            } else {
                uiState.copy(
                    isLoading = false,
                    errorMessage = "No se encontró el perfil del médico."
                )
            }
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar el perfil médico."
            )
        }
    }

    private fun DoctorEntity.toUiState(): DoctorProfileUiState {
        return DoctorProfileUiState(
            name = name,
            email = email,
            specialty = specialty,
            professionalLicense = professionalLicense,
            experienceYears = experienceYears,
            clinicName = clinicName,
            clinicAddress = clinicAddress,
            consultationPrice = consultationPrice
        )
    }
}
