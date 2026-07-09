package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "doctors",
    indices = [
        Index(value = ["emailDoctor"], unique = true)
    ]
)
data class DoctorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idDoctor")
    val id: Int = 0,

    @ColumnInfo(name = "nameDoctor")
    val name: String,

    @ColumnInfo(name = "emailDoctor")
    val email: String,

    @ColumnInfo(name = "passwordDoctor")
    val password: String,

    @ColumnInfo(name = "specialty")
    val specialty: String,

    @ColumnInfo(name = "professionalLicense")
    val professionalLicense: String,

    @ColumnInfo(name = "experienceYears")
    val experienceYears: Int,

    @ColumnInfo(name = "clinicName")
    val clinicName: String,

    @ColumnInfo(name = "clinicAddress")
    val clinicAddress: String,

    @ColumnInfo(name = "consultationPrice")
    val consultationPrice: Double,
    //val rating: Double = 0.0,
    //val imageUrl: String = ""
)