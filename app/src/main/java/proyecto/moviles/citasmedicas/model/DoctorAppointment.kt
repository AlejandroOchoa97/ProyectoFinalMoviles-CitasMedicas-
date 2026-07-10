package proyecto.moviles.citasmedicas.model

import java.time.LocalDate
import java.time.LocalTime

data class DoctorAppointment(
    val id: Int,
    val patientName: String,
    val patientAge: Int,
    val patientGender: String = "Masculino",
    val patientPhone: String = "+52 55 1234 5678",
    val date: LocalDate,
    val time: LocalTime,
    val reason: String,
    val detailedReason: String = "",
    val specialty: String = "Medicina General",
    val status: String,
    val prescription: String = "",
    val isUrgent: Boolean = false,
    val hasNotification: Boolean = false
)
