package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.model.Doctor

data class SearchDoctorUiState(
    val query: String = "",
    val selectedSpecialty: String = "Todos",
    val doctors: List<Doctor> = emptyList(),
    val specialties: List<String> = listOf("Todos", "Medicina General", "Cardiología", "Pediatría", "Dermatología"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val visibleDoctors: List<Doctor>
        get() = doctors.filter { doctor ->
            (selectedSpecialty == "Todos" || doctor.specialty == selectedSpecialty) &&
                (query.isBlank() ||
                    doctor.name.contains(query, ignoreCase = true) ||
                    doctor.specialty.contains(query, ignoreCase = true))
        }
}

/**
 * ViewModel de búsqueda de médicos.
 *
 * Lee médicos desde Room cuando existe repositorio, y usa datos de muestra en Preview.
 */
class SearchDoctorViewModel(
    private val doctorRepository: DoctorRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(SearchDoctorUiState())
        private set

    /**
     * Carga médicos desde Room o desde SampleData si no hay repositorio.
     */
    suspend fun loadDoctors() {
        if (doctorRepository == null) {
            uiState = uiState.copy(doctors = SampleData.sampleDoctors)
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        try {
            val doctors = doctorRepository.getAllDoctors().map { it.toDoctor() }
            uiState = uiState.copy(
                doctors = doctors,
                isLoading = false,
                errorMessage = null
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudieron cargar los médicos."
            )
        }
    }

    fun updateQuery(query: String) {
        uiState = uiState.copy(query = query)
    }

    fun selectSpecialty(specialty: String) {
        uiState = uiState.copy(selectedSpecialty = specialty)
    }

    private fun DoctorEntity.toDoctor(): Doctor {
        return Doctor(
            id = id,
            name = name,
            specialty = specialty,
            rating = 4.8,
            reviews = 120,
            experienceYears = experienceYears,
            price = "$${consultationPrice.toInt()} MXN"
        )
    }
}
