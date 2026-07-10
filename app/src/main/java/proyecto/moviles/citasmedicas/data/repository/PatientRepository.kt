package proyecto.moviles.citasmedicas.data.repository

import proyecto.moviles.citasmedicas.data.local.dao.PatientDAO
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity

class PatientRepository(
    private val patientDAO: PatientDAO
) {

    suspend fun insertPatient(patient: PatientEntity) {
        patientDAO.insertPatient(patient)
    }

    suspend fun updatePatient(patient: PatientEntity) {
        patientDAO.updatePatient(patient)
    }

    suspend fun getAllPatients(): List<PatientEntity> {
        return patientDAO.getAllPatients()
    }

    suspend fun getPatientById(patientId: Int): PatientEntity? {
        return patientDAO.getPatientById(patientId)
    }

    suspend fun getPatientByName(patientName: String): PatientEntity? {
        return patientDAO.getPatientByName(patientName)
    }

    suspend fun getPatientByEmail(email: String): PatientEntity? {
        return patientDAO.getPatientByEmail(email)
    }

    suspend fun getPatientByUid(uid: String): PatientEntity? {
        return patientDAO.getPatientByUid(uid)
    }

    suspend fun loginPatient(
        email: String,
        password: String
    ): PatientEntity? {
        return patientDAO.loginPatient(
            email = email,
            password = password
        )
    }
}