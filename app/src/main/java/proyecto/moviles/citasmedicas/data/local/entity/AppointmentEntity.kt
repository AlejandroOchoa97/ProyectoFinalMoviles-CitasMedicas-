package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * AppointmentEntity
 *
 * Representa la tabla de citas médicas dentro de la base de datos local.
 *
 * Cada cita pertenece a un paciente y a un médico.
 */
@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoctorEntity::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["patientId"]),
        Index(value = ["doctorId"])
    ]
)
data class AppointmentEntity(

    /**
     * Identificador único de la cita.
     *
     * Room lo genera automáticamente cuando se registra una nueva cita.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * ID del paciente que agenda la cita.
     */
    val patientId: Int,

    /**
     * ID del médico con quien se agenda la cita.
     */
    val doctorId: Int,

    /**
     * Fecha de la cita.
     *
     * De momento es con String para el entregable, debemos manejarlo con
     * la clase que se maneja el calendario creado en los componentes.
     */
    val date: String,

    /**
     * Hora de la cita.
     *
     * De momento es con String para el entregable, debemos manejarlo con
     * la clase que se maneja el horario creado en los componentes.
     */
    val time: String,

    /**
     * Motivo de la consulta escrito por el paciente.
     */
    val reason: String,

    /**
     * Estado de la cita.
     *
     * Ejemplos:
     * - "PENDING"
     * - "CONFIRMED"
     * - "COMPLETED"
     * - "CANCELLED"
     */
    val status: String = "PENDING",

    /**
     * Fecha en la que se creó la cita.
     *
     * Por ahora también se guarda como String.
     * Ejemplo: "2026-07-06"
     */
    val createdAt: String = ""
)