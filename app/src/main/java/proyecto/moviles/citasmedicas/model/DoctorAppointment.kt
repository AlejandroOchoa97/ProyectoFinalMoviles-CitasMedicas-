package proyecto.moviles.citasmedicas.model

data class DoctorAppointment(
    val id: Int,
    val patientName: String,
    val patientAge: Int,
    val time: String,
    val reason: String,
    val status: String,
    val isUrgent: Boolean = false,
    val hasNotification: Boolean = false
)
