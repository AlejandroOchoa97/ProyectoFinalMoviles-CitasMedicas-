package proyecto.moviles.citasmedicas.data

import android.content.Context
import proyecto.moviles.citasmedicas.data.local.database.DatabaseProvider
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository

/**
 * AppContainer
 *
 * Esta clase centraliza las dependencias principales de la aplicación.
 *
 * Aquí se crean:
 * - La base de datos local
 * - Los repositorios
 *
 * Con esto los ViewModels reciben repositorio en lugar
 * de crear directamente DAOs o bases de datos.
 */
class AppContainer(
    private val context: Context
) {

    /**
     * Instancia de la base de datos local.
     *
     * Se obtiene desde DatabaseProvider para asegurarnos de usar
     * una sola base de datos durante la ejecución de la app.
     */
    private val database = DatabaseProvider.getDatabase(context)

    /**
     * Repositorio de pacientes.
     *
     * Usa el PatientDAO que viene desde la base de datos.
     */
    val patientRepository: PatientRepository by lazy {
        PatientRepository(
            patientDAO = database.patientDAO()
        )
    }

    /**
     * Repositorio de médicos.
     *
     * Usa el DoctorDAO que viene desde la base de datos.
     */
    val doctorRepository: DoctorRepository by lazy {
        DoctorRepository(
            doctorDAO = database.doctorDAO()
        )
    }

    /**
     * Repositorio de citas médicas.
     *
     * Usa el AppointmentDAO que viene desde la base de datos.
     */
    val appointmentRepository: AppointmentRepository by lazy {
        AppointmentRepository(
            appointmentDAO = database.appointmentDAO()
        )
    }

    /**
     * Repositorio de disponibilidad médica.
     *
     * Guarda y consulta los bloques de horario configurados por el médico.
     */
    val doctorAvailabilityRepository: DoctorAvailabilityRepository by lazy {
        DoctorAvailabilityRepository(
            doctorAvailabilityDAO = database.doctorAvailabilityDAO()
        )
    }
}
