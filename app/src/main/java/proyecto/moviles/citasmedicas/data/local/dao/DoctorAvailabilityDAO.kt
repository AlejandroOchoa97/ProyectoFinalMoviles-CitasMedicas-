package proyecto.moviles.citasmedicas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity

@Dao
interface DoctorAvailabilityDAO {

    /**
     * Inserta un bloque de disponibilidad médica.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailability(availability: DoctorAvailabilityEntity)

    /**
     * Inserta varios bloques de disponibilidad médica.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailabilityBlocks(availabilityBlocks: List<DoctorAvailabilityEntity>)

    /**
     * Obtiene todos los bloques configurados por un médico.
     */
    @Query("SELECT * FROM doctor_availability WHERE doctorId = :doctorId")
    suspend fun getAvailabilityByDoctorId(doctorId: Int): List<DoctorAvailabilityEntity>

    /**
     * Obtiene los bloques del médico para un día específico.
     */
    @Query("SELECT * FROM doctor_availability WHERE doctorId = :doctorId AND day = :day")
    suspend fun getAvailabilityByDoctorAndDay(
        doctorId: Int,
        day: String
    ): List<DoctorAvailabilityEntity>

    /**
     * Elimina los bloques de un médico para guardar una nueva configuración.
     */
    @Query("DELETE FROM doctor_availability WHERE doctorId = :doctorId")
    suspend fun deleteAvailabilityByDoctorId(doctorId: Int)
}
