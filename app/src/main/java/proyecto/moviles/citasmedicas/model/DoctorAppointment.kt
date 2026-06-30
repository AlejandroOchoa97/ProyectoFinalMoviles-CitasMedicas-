package proyecto.moviles.citasmedicas.model

data class DoctorAppointment(
    val id: Int,
    val patientName: String,
    val patientAge: Int,
    val patientGender: String = "Masculino",
    val patientPhone: String = "+52 55 1234 5678",
    val date: String = "24 Oct, 2023",
    val time: String,
    val reason: String,
    val detailedReason: String = "",
    val specialty: String = "Medicina General",
    val status: String,
    val isUrgent: Boolean = false,
    val hasNotification: Boolean = false
)
