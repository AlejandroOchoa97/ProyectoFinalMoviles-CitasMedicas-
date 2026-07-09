package proyecto.moviles.citasmedicas.data.repository

import proyecto.moviles.citasmedicas.data.local.dao.DoctorAvailabilityDAO
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity

class DoctorAvailabilityRepository(
    private val doctorAvailabilityDAO: DoctorAvailabilityDAO
) {

    suspend fun insertAvailability(availability: DoctorAvailabilityEntity) {
        doctorAvailabilityDAO.insertAvailability(availability)
    }

    suspend fun insertAvailabilityBlocks(availabilityBlocks: List<DoctorAvailabilityEntity>) {
        doctorAvailabilityDAO.insertAvailabilityBlocks(availabilityBlocks)
    }

    suspend fun getAvailabilityByDoctorId(doctorId: Int): List<DoctorAvailabilityEntity> {
        return doctorAvailabilityDAO.getAvailabilityByDoctorId(doctorId)
    }

    suspend fun getAvailabilityByDoctorAndDay(
        doctorId: Int,
        day: String
    ): List<DoctorAvailabilityEntity> {
        return doctorAvailabilityDAO.getAvailabilityByDoctorAndDay(
            doctorId = doctorId,
            day = day
        )
    }

    suspend fun replaceAvailabilityForDoctor(
        doctorId: Int,
        availabilityBlocks: List<DoctorAvailabilityEntity>
    ) {
        doctorAvailabilityDAO.deleteAvailabilityByDoctorId(doctorId)
        doctorAvailabilityDAO.insertAvailabilityBlocks(availabilityBlocks)
    }
}
