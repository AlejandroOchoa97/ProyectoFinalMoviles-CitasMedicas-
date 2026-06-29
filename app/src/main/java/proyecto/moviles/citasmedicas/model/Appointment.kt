package proyecto.moviles.citasmedicas.model

data class Appointment(
    val id: Int,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val price: String,
    val status: String
)
