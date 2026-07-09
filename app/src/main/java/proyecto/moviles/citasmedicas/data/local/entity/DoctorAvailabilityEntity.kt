package proyecto.moviles.citasmedicas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * DoctorAvailabilityEntity
 *
 * Representa un bloque de disponibilidad configurado por un médico.
 * Cada registro guarda el día, horario de inicio, horario de fin y tarifa.
 */
@Entity(
    tableName = "doctor_availability",
    foreignKeys = [
        ForeignKey(
            entity = DoctorEntity::class,
            parentColumns = ["idDoctor"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["doctorId"])
    ]
)
data class DoctorAvailabilityEntity(

    /**
     * Identificador único del bloque de disponibilidad.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idAvailability")
    val id: Int = 0,

    /**
     * Médico dueño de este bloque de disponibilidad.
     */
    @ColumnInfo(name = "doctorId")
    val doctorId: Int,

    /**
     * Día visible en el calendario de disponibilidad.
     *
     * Se guarda como texto por ahora para mantenerlo simple en este avance.
     */
    @ColumnInfo(name = "day")
    val day: String,

    /**
     * Nombre visual del bloque.
     *
     * Ejemplos: "Turno Mañana", "Turno Tarde".
     */
    @ColumnInfo(name = "title")
    val title: String,

    /**
     * Hora de inicio del bloque.
     */
    @ColumnInfo(name = "startTime")
    val startTime: String,

    /**
     * Hora de fin del bloque.
     */
    @ColumnInfo(name = "endTime")
    val endTime: String,

    /**
     * Tarifa de consulta configurada para mostrarse al paciente.
     */
    @ColumnInfo(name = "consultationFee")
    val consultationFee: Double
)
