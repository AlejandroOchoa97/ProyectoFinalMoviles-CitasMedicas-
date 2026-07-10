package proyecto.moviles.citasmedicas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity

@Dao
interface PatientDAO {

    /**
     * Inserta un nuevo paciente.
     *
     * IGNORE significa que si ya existe un conflicto,
     * Room no reemplaza el registro anterior.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatient(patient: PatientEntity)

    /**
     * Actualiza los datos de un paciente existente.
     */
    @Update
    suspend fun updatePatient(patient: PatientEntity)

    /**
     * Obtiene todos los pacientes registrados.
     */
    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<PatientEntity>

    /**
     * Busca un paciente por su ID.
     */
    @Query("SELECT * FROM patients WHERE idPatient = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: Int): PatientEntity?

    /**
     * Busca un paciente por su nombre.
     */
    @Query("SELECT * FROM patients WHERE namePatient = :patientName")
    suspend fun getPatientByName(patientName: String): PatientEntity?

    /**
     * Busca un paciente por correo electrónico.
     */
    @Query("SELECT * FROM patients WHERE emailPatient = :email LIMIT 1")
    suspend fun getPatientByEmail(email: String): PatientEntity?

    /**
     * Busca un paciente por su Firebase UID.
     */
    @Query("SELECT * FROM patients WHERE firebaseUid = :uid LIMIT 1")
    suspend fun getPatientByUid(uid: String): PatientEntity?

    /**
     * Valida el inicio de sesión del paciente.
     */
    @Query("SELECT * FROM patients WHERE emailPatient = :email AND passwordPatient = :password LIMIT 1")
    suspend fun loginPatient(
        email: String,
        password: String
    ): PatientEntity?
}