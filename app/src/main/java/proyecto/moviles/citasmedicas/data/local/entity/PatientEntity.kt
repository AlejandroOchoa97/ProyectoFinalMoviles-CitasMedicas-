package proyecto.moviles.citasmedicas.data.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * PatientEntity
 *
 * Representa la tabla de pacientes dentro de la base de datos local.
 *
 */
@Entity(
    tableName = "patients",
    indices = [
        Index(value = ["emailPatient"], unique = true)
    ]
)
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPatient")
    val id: Int = 0,

    @ColumnInfo(name = "namePatient")
    val name: String,

    @ColumnInfo(name = "emailPatient")
    val email: String,

    @ColumnInfo(name = "passwordPatient")
    val password: String,

    @ColumnInfo(name = "phonePatient")
    val phone: String,

    @ColumnInfo(name = "birthDatePatient")
    val birthDate: String = ""
)