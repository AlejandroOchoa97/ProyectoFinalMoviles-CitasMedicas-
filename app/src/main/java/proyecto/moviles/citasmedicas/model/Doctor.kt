package proyecto.moviles.citasmedicas.model

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int,
    val experienceYears: Int,
    val price: String
)
