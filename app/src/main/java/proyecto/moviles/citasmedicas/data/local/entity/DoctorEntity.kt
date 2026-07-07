package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val specialty: String,
    val professionalLicense: String,
    val experienceYears: Int,
    val clinicName: String,
    val clinicAddress: String,
    val consultationPrice: Double,
    //val rating: Double = 0.0,
    //val imageUrl: String = ""
)