package proyecto.moviles.citasmedicas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity

@Dao
interface DoctorDAO {

    /**
     * Inserta un nuevo médico.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDoctor(doctor: DoctorEntity)

    /**
     * Inserta una lista de médicos.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDoctors(doctors: List<DoctorEntity>)

    /**
     * Actualiza los datos de un médico existente.
     */
    @Update
    suspend fun updateDoctor(doctor: DoctorEntity)

    /**
     * Obtiene todos los médicos registrados.
     */
    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<DoctorEntity>

    /**
     * Busca un médico por su ID.
     */
    @Query("SELECT * FROM doctors WHERE idDoctor = :doctorId LIMIT 1")
    suspend fun getDoctorById(doctorId: Int): DoctorEntity?

    /**
     * Busca un médico por correo electrónico.
     */
    @Query("SELECT * FROM doctors WHERE emailDoctor = :email LIMIT 1")
    suspend fun getDoctorByEmail(email: String): DoctorEntity?

    /**
     * Valida el inicio de sesión del médico.
     */
    @Query("SELECT * FROM doctors WHERE emailDoctor = :email AND passwordDoctor = :password LIMIT 1")
    suspend fun loginDoctor(
        email: String,
        password: String
    ): DoctorEntity?

    /**
     * Busca médicos por nombre o especialidad.
     */
    @Query("""SELECT * FROM doctors WHERE nameDoctor LIKE '%' || :query || '%' OR specialty LIKE '%' || :query || '%'""")
    suspend fun searchDoctors(query: String): List<DoctorEntity>
}