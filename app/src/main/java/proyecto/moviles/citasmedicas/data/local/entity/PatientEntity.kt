package proyecto.moviles.citasmedicas.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * PatientEntity
 *
 * Representa la tabla de pacientes dentro de la base de datos local.
 *
 */
@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val birthDate: String = ""
)