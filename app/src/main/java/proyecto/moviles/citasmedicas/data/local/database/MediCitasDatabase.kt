package proyecto.moviles.citasmedicas.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import proyecto.moviles.citasmedicas.data.local.dao.AppointmentDAO
import proyecto.moviles.citasmedicas.data.local.dao.DoctorAvailabilityDAO
import proyecto.moviles.citasmedicas.data.local.dao.DoctorDAO
import proyecto.moviles.citasmedicas.data.local.dao.PatientDAO
import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity


@Database(
    entities = [
        PatientEntity::class,
        DoctorEntity::class,
        AppointmentEntity::class,
        DoctorAvailabilityEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MediCitasDatabase : RoomDatabase() {

    /**
     * DAO para acceder a la tabla de pacientes.
     */
    abstract fun patientDAO(): PatientDAO

    /**
     * DAO para acceder a la tabla de médicos.
     */
    abstract fun doctorDAO(): DoctorDAO

    /**
     * DAO para acceder a la tabla de citas médicas.
     */
    abstract fun appointmentDAO(): AppointmentDAO

    abstract fun doctorAvailabilityDAO(): DoctorAvailabilityDAO
}
