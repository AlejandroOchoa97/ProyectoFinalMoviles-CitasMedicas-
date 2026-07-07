package proyecto.moviles.citasmedicas.data.repository

import proyecto.moviles.citasmedicas.data.local.dao.AppointmentDAO
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity


class AppointmentRepository(
    private val appointmentDAO: AppointmentDAO
) {

    suspend fun insertAppointment(appointment: AppointmentEntity) {
        appointmentDAO.insertAppointment(appointment)
    }

    suspend fun updateAppointment(appointment: AppointmentEntity) {
        appointmentDAO.updateAppointment(appointment)
    }

    suspend fun getAllAppointments(): List<AppointmentEntity> {
        return appointmentDAO.getAllAppointments()
    }

    suspend fun getAppointmentById(appointmentId: Int): AppointmentEntity? {
        return appointmentDAO.getAppointmentById(appointmentId)
    }

    suspend fun getAppointmentsByPatientId(patientId: Int): List<AppointmentEntity> {
        return appointmentDAO.getAppointmentsByPatientId(patientId)
    }

    suspend fun getAppointmentsByDoctorId(doctorId: Int): List<AppointmentEntity> {
        return appointmentDAO.getAppointmentsByDoctorId(doctorId)
    }

    suspend fun getAppointmentsByPatientAndStatus(
        patientId: Int,
        status: String
    ): List<AppointmentEntity> {
        return appointmentDAO.getAppointmentsByPatientAndStatus(
            patientId = patientId,
            status = status
        )
    }

    suspend fun getAppointmentsByDoctorAndStatus(
        doctorId: Int,
        status: String
    ): List<AppointmentEntity> {
        return appointmentDAO.getAppointmentsByDoctorAndStatus(
            doctorId = doctorId,
            status = status
        )
    }

    suspend fun getAppointmentByDoctorDateAndTime(
        doctorId: Int,
        date: String,
        time: String
    ): AppointmentEntity? {
        return appointmentDAO.getAppointmentByDoctorDateAndTime(
            doctorId = doctorId,
            date = date,
            time = time
        )
    }
}