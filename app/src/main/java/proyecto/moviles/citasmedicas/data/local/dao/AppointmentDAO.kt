package proyecto.moviles.citasmedicas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity

@Dao
interface AppointmentDAO {

    /**
     * Inserta una nueva cita médica.
     *
     * IGNORE evita que se reemplace una cita existente si ocurre algún conflicto.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    /**
     * Actualiza una cita existente.
     */
    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    /**
     * Obtiene todas las citas registradas.
     */
    @Query("SELECT * FROM appointments")
    suspend fun getAllAppointments(): List<AppointmentEntity>

    /**
     * Busca una cita por su ID.
     */
    @Query("SELECT * FROM appointments WHERE idAppointment = :appointmentId LIMIT 1")
    suspend fun getAppointmentById(appointmentId: Int): AppointmentEntity?

    /**
     * Obtiene todas las citas de un paciente específico.
     */
    @Query("SELECT * FROM appointments WHERE patientId = :patientId")
    suspend fun getAppointmentsByPatientId(patientId: Int): List<AppointmentEntity>

    /**
     * Obtiene todas las citas de un médico específico.
     */
    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId")
    suspend fun getAppointmentsByDoctorId(doctorId: Int): List<AppointmentEntity>

    /**
     * Obtiene las citas de un paciente según su estado.
     *
     * Ejemplo:
     * - PENDING
     * - CONFIRMED
     * - COMPLETED
     * - CANCELLED
     */
    @Query("SELECT * FROM appointments WHERE patientId = :patientId AND status = :status")
    suspend fun getAppointmentsByPatientAndStatus(
        patientId: Int,
        status: String
    ): List<AppointmentEntity>

    /**
     * Obtiene las citas de un médico según su estado.
     */
    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId AND status = :status")
    suspend fun getAppointmentsByDoctorAndStatus(
        doctorId: Int,
        status: String
    ): List<AppointmentEntity>

    /**
     * Revisa si un horario ya está ocupado para un médico en una fecha específica.
     */
    @Query(
        """
        SELECT * FROM appointments
        WHERE doctorId = :doctorId
        AND dateAppointment = :date
        AND timeAppointment = :time
        LIMIT 1
        """
    )
    suspend fun getAppointmentByDoctorDateAndTime(
        doctorId: Int,
        date: String,
        time: String
    ): AppointmentEntity?
}