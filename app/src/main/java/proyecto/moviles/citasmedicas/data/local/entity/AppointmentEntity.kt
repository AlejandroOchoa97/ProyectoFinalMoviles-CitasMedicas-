package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import proyecto.moviles.citasmedicas.model.AppointmentStatus

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
            parentColumns = ["idPatient"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoctorEntity::class,
            parentColumns = ["idDoctor"],
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
    @ColumnInfo(name = "idAppointment")
    val id: Int = 0,

    /**
     * ID del paciente que agenda la cita.
     */
    @ColumnInfo(name = "patientId")
    val patientId: Int,

    /**
     * ID del médico con quien se agenda la cita.
     */
    @ColumnInfo(name = "doctorId")
    val doctorId: Int,

    /**
     * Fecha de la cita.
     *
     * De momento es con String para el entregable, debemos manejarlo con
     * la clase que se maneja el calendario creado en los componentes.
     */
    @ColumnInfo(name = "dateAppointment")
    val date: String,

    /**
     * Hora de la cita.
     *
     * De momento es con String para el entregable, debemos manejarlo con
     * la clase que se maneja el horario creado en los componentes.
     */
    @ColumnInfo(name = "timeAppointment")
    val time: String,

    /**
     * Motivo de la consulta.
     */
    @ColumnInfo(name = "reason")
    val reason: String,

    /**
     * Estado de la cita.
     *
     * Ejemplos:
     * - "PENDING"
     * - "CONFIRMED"
     * - "COMPLETED"
     * - "CANCELLED"
     * - "URGENT"
     */
    @ColumnInfo(name = "status")
    val status: String = AppointmentStatus.PENDING,

    /**
     * Fecha en la que se creó la cita.
     *
     * Por ahora también se guarda como String.
     */
    @ColumnInfo(name = "createdAt")
    val createdAt: String = ""
)
