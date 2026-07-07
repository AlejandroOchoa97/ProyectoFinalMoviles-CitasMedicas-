package proyecto.moviles.citasmedicas.model

import java.time.LocalDate
import java.time.LocalTime

data class Appointment(
    val id: Int,
    val doctorName: String,
    val specialty: String,
    val date: LocalDate,
    val time: LocalTime,
    val price: String,
    val status: String,
    val clinicName: String = "",
    val clinicAddress: String = "",
    // Agrega coordenadas reales en SampleData cuando estén confirmadas.
    val latitude: Double? = null,
    val longitude: Double? = null
)
