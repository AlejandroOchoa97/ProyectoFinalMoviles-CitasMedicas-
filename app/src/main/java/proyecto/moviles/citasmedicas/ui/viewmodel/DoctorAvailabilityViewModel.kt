package proyecto.moviles.citasmedicas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
 * Carga, valida y guarda los bloques de disponibilidad del médico en Room.
 * Cada día se maneja de forma independiente para no borrar otros días.
 */
class DoctorAvailabilityViewModel(
    private val availabilityRepository: DoctorAvailabilityRepository? = null
) : ViewModel() {

    var uiState by mutableStateOf(
        DoctorAvailabilityUiState(blocks = defaultBlocks())
    )
        private set

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    suspend fun loadAvailability(doctorId: Int) {
        loadAvailabilityForDay(
            doctorId = doctorId,
            day = uiState.selectedDay
        )
    }

    suspend fun loadAvailabilityForDay(
        doctorId: Int,
        day: String
    ) {
        if (availabilityRepository == null) {
            uiState = uiState.copy(
                selectedDay = day,
                blocks = defaultBlocks(),
                selectedBlockTitle = "Turno Mañana"
            )
            return
        }

        uiState = uiState.copy(
            selectedDay = day,
            isLoading = true,
            errorMessage = null,
            message = null
        )

        try {
            val availability = availabilityRepository.getAvailabilityByDoctorAndDay(
                doctorId = doctorId,
                day = day
            )

            if (availability.isEmpty()) {
                uiState = uiState.copy(
                    blocks = emptyList(),
                    selectedBlockTitle = "",
                    isLoading = false,
                    message = "No hay bloques configurados para este día."
                )
                return
            }

            val firstBlock = availability.first()
            uiState = uiState.copy(
                consultationFee = firstBlock.consultationFee.toInt().toString(),
                blocks = availability.map { it.toUiBlock() },
                selectedBlockTitle = firstBlock.title,
                isLoading = false
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                errorMessage = "No se pudo cargar la disponibilidad."
            )
        }
    }

    fun selectBlock(title: String) {
        uiState = uiState.copy(selectedBlockTitle = title)
    }

    fun updateConsultationFee(fee: String) {
        uiState = uiState.copy(
            consultationFee = fee.filter { it.isDigit() },
            errorMessage = null,
            message = null
        )
    }

    fun addBlock() {
        val newBlockNumber = uiState.blocks.size + 1
        val newBlock = DoctorAvailabilityBlockUi(
            title = "Bloque $newBlockNumber",
            startTime = "08:00",
            endTime = "09:00"
        )

        uiState = uiState.copy(
            blocks = uiState.blocks + newBlock,
            selectedBlockTitle = newBlock.title,
            message = "Bloque agregado",
            errorMessage = null
        )
    }

    fun deleteBlock(index: Int) {
        val updatedBlocks = uiState.blocks.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }

        uiState = uiState.copy(
            blocks = updatedBlocks,
            selectedBlockTitle = updatedBlocks.firstOrNull()?.title.orEmpty(),
            message = "Bloque eliminado",
            errorMessage = null
        )
    }

    fun updateStartTime(index: Int, time: LocalTime) {
        updateBlock(index) { block ->
            block.copy(startTime = time.format(timeFormatter))
        }
    }

    fun updateEndTime(index: Int, time: LocalTime) {
        updateBlock(index) { block ->
            block.copy(endTime = time.format(timeFormatter))
        }
    }

    suspend fun saveAvailability(doctorId: Int) {
        if (availabilityRepository == null) {
            uiState = uiState.copy(message = "Disponibilidad guardada en preview")
            return
        }

        val validationMessage = validateBlocks()
        if (validationMessage != null) {
            uiState = uiState.copy(errorMessage = validationMessage, message = null)
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

            availabilityRepository.replaceAvailabilityForDoctorAndDay(
                doctorId = doctorId,
                day = uiState.selectedDay,
                availabilityBlocks = availabilityBlocks
            )

            uiState = uiState.copy(
                message = "Disponibilidad guardada para el día ${uiState.selectedDay}",
                errorMessage = null
            )
        } catch (exception: Exception) {
            uiState = uiState.copy(errorMessage = "No se pudo guardar la disponibilidad.")
        }
    }

    fun clearMessages() {
        uiState = uiState.copy(message = null, errorMessage = null)
    }

    private fun updateBlock(
        index: Int,
        transform: (DoctorAvailabilityBlockUi) -> DoctorAvailabilityBlockUi
    ) {
        val updatedBlocks = uiState.blocks.mapIndexed { currentIndex, block ->
            if (currentIndex == index) transform(block) else block
        }

        uiState = uiState.copy(
            blocks = updatedBlocks,
            selectedBlockTitle = updatedBlocks.getOrNull(index)?.title ?: uiState.selectedBlockTitle,
            errorMessage = null,
            message = null
        )
    }

    private fun validateBlocks(): String? {
        if (uiState.consultationFee.isBlank()) {
            return "Ingresa la tarifa de consulta."
        }

        if (uiState.blocks.isEmpty()) {
            return "Agrega al menos un bloque de horario."
        }

        val parsedBlocks = uiState.blocks.map { block ->
            val start = block.startTime.toLocalTimeOrNull()
                ?: return "Hay una hora de inicio inválida."
            val end = block.endTime.toLocalTimeOrNull()
                ?: return "Hay una hora de fin inválida."

            if (!start.isBefore(end)) {
                return "La hora de inicio debe ser menor que la hora de fin."
            }

            block to (start to end)
        }

        val repeatedBlocks = parsedBlocks
            .groupBy { (_, range) -> range }
            .any { (_, repeated) -> repeated.size > 1 }

        if (repeatedBlocks) {
            return "No puedes guardar bloques repetidos."
        }

        val sortedRanges = parsedBlocks
            .map { (_, range) -> range }
            .sortedBy { it.first }

        sortedRanges.zipWithNext().forEach { (current, next) ->
            if (current.second.isAfter(next.first)) {
                return "Los bloques de horario no deben traslaparse."
            }
        }

        return null
    }

    private fun DoctorAvailabilityEntity.toUiBlock(): DoctorAvailabilityBlockUi {
        return DoctorAvailabilityBlockUi(
            title = title,
            startTime = startTime,
            endTime = endTime
        )
    }

    private fun String.toLocalTimeOrNull(): LocalTime? {
        return runCatching { LocalTime.parse(this, timeFormatter) }.getOrNull()
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
