package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "doctors",
    indices = [
        Index(value = ["emailDoctor"], unique = true),
        Index(value = ["firebaseUid"], unique = true)
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

    /**
     * Coordenadas opcionales de la clínica.
     *
     * Se dejan listas para mostrar la ubicación exacta en el mapa cuando
     * el equipo confirme las coordenadas reales de cada consultorio.
     */
    @ColumnInfo(name = "clinicLatitude")
    val clinicLatitude: Double? = null,

    @ColumnInfo(name = "clinicLongitude")
    val clinicLongitude: Double? = null,

    @ColumnInfo(name = "consultationPrice")
    val consultationPrice: Double,

    @ColumnInfo(name = "firebaseUid")
    val firebaseUid: String? = null
)
