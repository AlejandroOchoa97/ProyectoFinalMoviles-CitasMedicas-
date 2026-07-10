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
    val clinicLatitude: Double? = 19.3763,
    val clinicLongitude: Double? = -99.1770,
    val consultationPrice: Double = 800.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

/**
 * ViewModel del perfil médico.
 *
 * Carga desde Room los datos del médico activo y permite actualizar
 * la ubicación exacta del consultorio.
 */
class DoctorProfileViewModel(
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(DoctorProfileUiState())
        private set

    private var currentDoctor: DoctorEntity? = null

    /**
     * Carga el médico por ID.
     *
     * Si no hay repositorio, mantiene datos demo para que el Preview funcione.
     */
    suspend fun loadDoctor(doctorId: Int) {
        if (doctorRepository == null) return

        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)

        try {
            val doctor = doctorRepository.getDoctorById(doctorId)

            uiState = if (doctor != null) {
                currentDoctor = doctor
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

    /**
     * Actualiza los datos de consultorio.
     *
     * Las coordenadas se copian manualmente desde Google Maps para mantener
     * esta entrega simple y estable.
     */
    suspend fun updateClinicLocation(
        clinicName: String,
        clinicAddress: String,
        latitudeText: String,
        longitudeText: String
    ): Boolean {
        val doctor = currentDoctor

        if (doctorRepository == null || doctor == null) {
            uiState = uiState.copy(errorMessage = "No se pudo editar el consultorio.")
            return false
        }

        val cleanClinicName = clinicName.trim()
        val cleanClinicAddress = clinicAddress.trim()
        val latitude = latitudeText.trim().replace(",", ".").toDoubleOrNull()
        val longitude = longitudeText.trim().replace(",", ".").toDoubleOrNull()

        if (cleanClinicName.isBlank() || cleanClinicAddress.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa el nombre y dirección del consultorio.")
            return false
        }

        if (latitude == null || latitude !in -90.0..90.0) {
            uiState = uiState.copy(errorMessage = "La latitud debe estar entre -90 y 90.")
            return false
        }

        if (longitude == null || longitude !in -180.0..180.0) {
            uiState = uiState.copy(errorMessage = "La longitud debe estar entre -180 y 180.")
            return false
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)

        return try {
            val updatedDoctor = doctor.copy(
                clinicName = cleanClinicName,
                clinicAddress = cleanClinicAddress,
                clinicLatitude = latitude,
                clinicLongitude = longitude
            )

            doctorRepository.updateDoctor(updatedDoctor)
            currentDoctor = updatedDoctor
            uiState = updatedDoctor.toUiState().copy(
                isLoading = false,
                successMessage = "Ubicación del consultorio guardada."
            )
            true
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo guardar la ubicación."
            )
            false
        }
    }

    suspend fun updateProfessionalInfo(
        specialty: String,
        professionalLicense: String,
        experienceYearsText: String,
        consultationPriceText: String
    ): Boolean {
        val doctor = currentDoctor

        if (doctorRepository == null || doctor == null) {
            uiState = uiState.copy(errorMessage = "No se pudo editar la información profesional.")
            return false
        }

        val cleanSpecialty = specialty.trim()
        val cleanLicense = professionalLicense.trim()
        val years = experienceYearsText.trim().toIntOrNull()
        val price = consultationPriceText.trim().replace(",", ".").toDoubleOrNull()

        if (cleanSpecialty.isBlank()) {
            uiState = uiState.copy(errorMessage = "Ingresa la especialidad.")
            return false
        }

        if (years == null || years < 0) {
            uiState = uiState.copy(errorMessage = "Ingresa años de experiencia válidos.")
            return false
        }

        if (price == null || price <= 0.0) {
            uiState = uiState.copy(errorMessage = "Ingresa una tarifa válida.")
            return false
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null, successMessage = null)

        return try {
            val updatedDoctor = doctor.copy(
                specialty = cleanSpecialty,
                professionalLicense = cleanLicense.ifBlank { "Pendiente" },
                experienceYears = years,
                consultationPrice = price
            )

            doctorRepository.updateDoctor(updatedDoctor)
            currentDoctor = updatedDoctor
            uiState = updatedDoctor.toUiState().copy(
                isLoading = false,
                successMessage = "Información profesional guardada."
            )
            true
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo guardar la información profesional."
            )
            false
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
            clinicLatitude = clinicLatitude,
            clinicLongitude = clinicLongitude,
            consultationPrice = consultationPrice
        )
    }
}
