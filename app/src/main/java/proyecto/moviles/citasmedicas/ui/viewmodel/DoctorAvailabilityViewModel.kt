package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository

data class DoctorAvailabilityBlockUi(
    val title: String,
    val startTime: String,
    val endTime: String
)

data class DoctorAvailabilityUiState(
    val selectedDay: String = "4",
    val selectedBlockTitle: String = "Turno Mañana",
    val consultationFee: String = "850",
    val blocks: List<DoctorAvailabilityBlockUi> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)

/**
 * ViewModel de disponibilidad médica.
 *
 * Conecta la pantalla con Room para cargar y guardar los horarios
 * configurados por el médico.
 */
class DoctorAvailabilityViewModel(
    private val availabilityRepository: DoctorAvailabilityRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(
        DoctorAvailabilityUiState(
            blocks = defaultBlocks()
        )
    )
        private set

    /**
     * Carga la disponibilidad del médico desde Room.
     *
     * Si todavía no hay datos guardados, se muestran bloques por defecto
     * para que la pantalla no aparezca vacía.
     */
    suspend fun loadAvailability(doctorId: Int) {
        if (availabilityRepository == null) {
            uiState = uiState.copy(blocks = defaultBlocks())
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null, message = null)

        try {
            val availability = availabilityRepository.getAvailabilityByDoctorId(doctorId)

            if (availability.isEmpty()) {
                uiState = uiState.copy(
                    blocks = defaultBlocks(),
                    consultationFee = "850",
                    selectedDay = "4",
                    selectedBlockTitle = "Turno Mañana",
                    isLoading = false
                )
                return
            }

            val firstBlock = availability.first()
            uiState = uiState.copy(
                selectedDay = firstBlock.day,
                selectedBlockTitle = firstBlock.title,
                consultationFee = firstBlock.consultationFee.toInt().toString(),
                blocks = availability.map { it.toUiBlock() },
                isLoading = false
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar la disponibilidad."
            )
        }
    }

    /**
     * Selecciona un día del calendario visual.
     */
    fun selectDay(day: String) {
        uiState = uiState.copy(selectedDay = day)
    }

    /**
     * Selecciona un bloque de horario.
     */
    fun selectBlock(title: String) {
        uiState = uiState.copy(selectedBlockTitle = title)
    }

    /**
     * Actualiza la tarifa de consulta escrita por el médico.
     */
    fun updateConsultationFee(fee: String) {
        uiState = uiState.copy(consultationFee = fee.filter { it.isDigit() })
    }

    /**
     * Agrega un bloque temporal que después puede guardarse en Room.
     */
    fun addBlock() {
        val newBlockNumber = uiState.blocks.size + 1
        val newBlock = DoctorAvailabilityBlockUi(
            title = "Bloque $newBlockNumber",
            startTime = "12:00",
            endTime = "13:00"
        )

        uiState = uiState.copy(
            blocks = uiState.blocks + newBlock,
            selectedBlockTitle = newBlock.title,
            message = "Bloque agregado"
        )
    }

    /**
     * Elimina un bloque temporal de la lista.
     */
    fun deleteBlock(index: Int) {
        val updatedBlocks = uiState.blocks.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }

        uiState = uiState.copy(
            blocks = updatedBlocks,
            selectedBlockTitle = updatedBlocks.firstOrNull()?.title.orEmpty(),
            message = "Bloque eliminado"
        )
    }

    /**
     * Guarda la disponibilidad del médico en Room.
     */
    suspend fun saveAvailability(doctorId: Int) {
        if (availabilityRepository == null) {
            uiState = uiState.copy(message = "Disponibilidad guardada en preview")
            return
        }

        try {
            val fee = uiState.consultationFee.toDoubleOrNull() ?: 0.0
            val availabilityBlocks = uiState.blocks.map { block ->
                DoctorAvailabilityEntity(
                    doctorId = doctorId,
                    day = uiState.selectedDay,
                    title = block.title,
                    startTime = block.startTime,
                    endTime = block.endTime,
                    consultationFee = fee
                )
            }

            availabilityRepository.replaceAvailabilityForDoctor(
                doctorId = doctorId,
                availabilityBlocks = availabilityBlocks
            )

            uiState = uiState.copy(message = "Disponibilidad guardada en Room")
        } catch (exception: Exception) {
            uiState = uiState.copy(errorMessage = "No se pudo guardar la disponibilidad.")
        }
    }

    /**
     * Limpia mensajes para evitar mostrarlos repetidamente.
     */
    fun clearMessages() {
        uiState = uiState.copy(message = null, errorMessage = null)
    }

    private fun DoctorAvailabilityEntity.toUiBlock(): DoctorAvailabilityBlockUi {
        return DoctorAvailabilityBlockUi(
            title = title,
            startTime = startTime,
            endTime = endTime
        )
    }

    private companion object {
        fun defaultBlocks(): List<DoctorAvailabilityBlockUi> {
            return listOf(
                DoctorAvailabilityBlockUi("Turno Mañana", "08:00", "14:00"),
                DoctorAvailabilityBlockUi("Turno Tarde", "16:00", "20:00")
            )
        }
    }
}
