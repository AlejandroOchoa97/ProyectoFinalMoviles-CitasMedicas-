package proyecto.moviles.citasmedicas.model

data class Appointment(
    val id: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val price: String,
    val status: String,
    val clinicName: String = "",
    val clinicAddress: String = "",
    // Agrega coordenadas reales en SampleData cuando estén confirmadas.
    val latitude: Double? = null,
    val longitude: Double? = null
)
