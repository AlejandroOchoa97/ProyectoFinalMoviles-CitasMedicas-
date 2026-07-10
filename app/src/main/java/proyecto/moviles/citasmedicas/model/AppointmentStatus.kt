package proyecto.moviles.citasmedicas.model

/**
 * Estados permitidos para una cita médica.
 *
 * Room guarda el estado como String para mantener la base de datos simple,
 * pero estas constantes evitan escribir textos repetidos en varias clases.
 */
object AppointmentStatus {
    const val PENDING = "PENDING"
    const val CONFIRMED = "CONFIRMED"
    const val COMPLETED = "COMPLETED"
    const val CANCELLED = "CANCELLED"
    const val URGENT = "URGENT"

    /**
     * Convierte el valor guardado en Room a un texto amigable para la interfaz.
     */
    fun toDisplayName(status: String): String {
        return when (status.uppercase()) {
            CONFIRMED -> "Confirmada"
            PENDING -> "Pendiente"
            COMPLETED -> "Completada"
            CANCELLED -> "Cancelada"
            URGENT -> "Urgente"
            else -> status
        }
    }

    /**
     * Indica si una cita debe tratarse como urgente en la pantalla médica.
     */
    fun isUrgent(status: String): Boolean {
        return status.equals(URGENT, ignoreCase = true)
    }
}
