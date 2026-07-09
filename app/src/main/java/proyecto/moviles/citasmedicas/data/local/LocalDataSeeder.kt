package proyecto.moviles.citasmedicas.data.local

import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository

/**
 * Inserta datos iniciales para poder probar la app con Room.
 *
 * Esta clase solo agrega datos si las tablas están vacías.
 * Así evitamos duplicar registros cada vez que se abre la aplicación.
 */
class LocalDataSeeder(
    private val patientRepository: PatientRepository,
    private val doctorRepository: DoctorRepository,
    private val appointmentRepository: AppointmentRepository
) {

    /**
     * Prepara paciente, médicos y citas demo para pruebas locales.
     */
    suspend fun seedIfNeeded() {
        seedPatientsIfNeeded()
        seedDoctorsIfNeeded()
        seedAppointmentsIfNeeded()
    }

    /**
     * Inserta un paciente demo si todavía no existe ninguno.
     */
    private suspend fun seedPatientsIfNeeded() {
        if (patientRepository.getAllPatients().isNotEmpty()) return

        patientRepository.insertPatient(
            PatientEntity(
                name = "Juan Pérez",
                email = "paciente@medicitas.com",
                password = "123456",
                phone = "+52 55 0000 0000",
                birthDate = "1998-05-12"
            )
        )
    }

    /**
     * Inserta médicos demo si todavía no existe ninguno.
     */
    private suspend fun seedDoctorsIfNeeded() {
        if (doctorRepository.getAllDoctors().isNotEmpty()) return

        doctorRepository.insertDoctors(
            listOf(
                DoctorEntity(
                    name = "Dra. Elena Ruiz",
                    email = "elena@medicitas.com",
                    password = "123456",
                    specialty = "Cardiología",
                    professionalLicense = "CARD-001",
                    experienceYears = 10,
                    clinicName = "Torre Médica Metropolitan",
                    clinicAddress = "Av. Insurgentes Sur 1582, CDMX",
                    consultationPrice = 800.0
                ),
                DoctorEntity(
                    name = "Dr. Ricardo Salinas",
                    email = "ricardo@medicitas.com",
                    password = "123456",
                    specialty = "Pediatría",
                    professionalLicense = "PED-002",
                    experienceYears = 15,
                    clinicName = "Clínica Infantil Reforma",
                    clinicAddress = "Paseo de la Reforma 250, CDMX",
                    consultationPrice = 950.0
                ),
                DoctorEntity(
                    name = "Dra. Claudia Méndez",
                    email = "claudia@medicitas.com",
                    password = "123456",
                    specialty = "Dermatología",
                    professionalLicense = "DER-003",
                    experienceYears = 8,
                    clinicName = "Dermacenter Roma",
                    clinicAddress = "Calle Durango 120, CDMX",
                    consultationPrice = 1100.0
                ),
                DoctorEntity(
                    name = "Dr. Manuel Torres",
                    email = "manuel@medicitas.com",
                    password = "123456",
                    specialty = "Medicina General",
                    professionalLicense = "MED-004",
                    experienceYears = 22,
                    clinicName = "Consultorio Médico Central",
                    clinicAddress = "Av. Universidad 300, CDMX",
                    consultationPrice = 600.0
                )
            )
        )
    }

    /**
     * Inserta citas demo ligadas al primer paciente y a médicos existentes.
     */
    private suspend fun seedAppointmentsIfNeeded() {
        if (appointmentRepository.getAllAppointments().isNotEmpty()) return

        val patient = patientRepository.getAllPatients().firstOrNull() ?: return
        val doctors = doctorRepository.getAllDoctors()

        val cardiologist = doctors.firstOrNull { it.specialty == "Cardiología" } ?: return
        val pediatrician = doctors.firstOrNull { it.specialty == "Pediatría" } ?: cardiologist
        val dermatologist = doctors.firstOrNull { it.specialty == "Dermatología" } ?: cardiologist

        listOf(
            AppointmentEntity(
                patientId = patient.id,
                doctorId = cardiologist.id,
                date = "2024-10-15",
                time = "10:30",
                reason = "Chequeo general y seguimiento de presión arterial.",
                status = "CONFIRMED",
                createdAt = "2024-10-01"
            ),
            AppointmentEntity(
                patientId = patient.id,
                doctorId = pediatrician.id,
                date = "2024-10-18",
                time = "16:45",
                reason = "Consulta de revisión pediátrica.",
                status = "CONFIRMED",
                createdAt = "2024-10-02"
            ),
            AppointmentEntity(
                patientId = patient.id,
                doctorId = dermatologist.id,
                date = "2024-10-22",
                time = "09:00",
                reason = "Revisión de irritación en la piel.",
                status = "PENDING",
                createdAt = "2024-10-03"
            )
        ).forEach { appointment ->
            appointmentRepository.insertAppointment(appointment)
        }
    }
}
