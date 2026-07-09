package proyecto.moviles.citasmedicas.data.repository

import proyecto.moviles.citasmedicas.data.local.dao.DoctorDAO
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity


class DoctorRepository(
    private val doctorDAO: DoctorDAO
) {

    suspend fun insertDoctor(doctor: DoctorEntity) {
        doctorDAO.insertDoctor(doctor)
    }

    suspend fun insertDoctors(doctors: List<DoctorEntity>) {
        doctorDAO.insertDoctors(doctors)
    }

    suspend fun updateDoctor(doctor: DoctorEntity) {
        doctorDAO.updateDoctor(doctor)
    }

    suspend fun getAllDoctors(): List<DoctorEntity> {
        return doctorDAO.getAllDoctors()
    }

    suspend fun getDoctorById(doctorId: Int): DoctorEntity? {
        return doctorDAO.getDoctorById(doctorId)
    }

    suspend fun getDoctorByName(doctorName: String): DoctorEntity? {
        return doctorDAO.getDoctorByName(doctorName)
    }

    suspend fun getDoctorByEmail(email: String): DoctorEntity? {
        return doctorDAO.getDoctorByEmail(email)
    }

    suspend fun loginDoctor(
        email: String,
        password: String
    ): DoctorEntity? {
        return doctorDAO.loginDoctor(
            email = email,
            password = password
        )
    }

    suspend fun searchDoctors(query: String): List<DoctorEntity> {
        return doctorDAO.searchDoctors(query)
    }
}