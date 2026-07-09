package proyecto.moviles.citasmedicas.data.local

import proyecto.moviles.citasmedicas.data.local.entity.AppointmentEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorAvailabilityEntity
import proyecto.moviles.citasmedicas.data.local.entity.DoctorEntity
import proyecto.moviles.citasmedicas.data.local.entity.PatientEntity
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
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
    private val appointmentRepository: AppointmentRepository,
    private val doctorAvailabilityRepository: DoctorAvailabilityRepository
) {

    /**
     * Prepara pacientes, médicos, citas y disponibilidad demo.
     */
    suspend fun seedIfNeeded() {
        seedPatientsIfNeeded()
        seedDoctorsIfNeeded()
        seedAppointmentsIfNeeded()
        seedDoctorAvailabilityIfNeeded()
    }

    /**
     * Inserta pacientes demo para que el médico tenga varias citas visibles.
     */
    private suspend fun seedPatientsIfNeeded() {
        if (patientRepository.getAllPatients().isNotEmpty()) return

        listOf(
            PatientEntity(
                name = "Juan Pérez",
                email = "paciente@medicitas.com",
                password = "123456",
                phone = "+52 55 0000 0000",
                birthDate = "1998-05-12"
            ),
            PatientEntity(
                name = "Ricardo Gómez",
                email = "ricardo.gomez@medicitas.com",
                password = "123456",
                phone = "+52 55 1234 5678",
                birthDate = "1979-03-18"
            ),
            PatientEntity(
                name = "Elena Ortiz",
                email = "elena.ortiz@medicitas.com",
                password = "123456",
                phone = "+52 55 8765 4321",
                birthDate = "1952-07-24"
            ),
            PatientEntity(
                name = "Marco Ruiz",
                email = "marco.ruiz@medicitas.com",
                password = "123456",
                phone = "+52 55 2468 1357",
                birthDate = "1996-11-09"
            )
        ).forEach { patient ->
            patientRepository.insertPatient(patient)
        }
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
     * Inserta citas demo ligadas a Room.
     *
     * Se agregan varias citas para el primer médico para probar
     * los filtros de la pantalla médica.
     */
    private suspend fun seedAppointmentsIfNeeded() {
        if (appointmentRepository.getAllAppointments().isNotEmpty()) return

        val patients = patientRepository.getAllPatients()
        val doctors = doctorRepository.getAllDoctors()

        val cardiologist = doctors.firstOrNull { it.specialty == "Cardiología" } ?: return
        val pediatrician = doctors.firstOrNull { it.specialty == "Pediatría" } ?: cardiologist
        val dermatologist = doctors.firstOrNull { it.specialty == "Dermatología" } ?: cardiologist

        val juan = patients.getOrNull(0) ?: return
        val ricardo = patients.getOrNull(1) ?: juan
        val elena = patients.getOrNull(2) ?: juan
        val marco = patients.getOrNull(3) ?: juan

        listOf(
            AppointmentEntity(
                patientId = ricardo.id,
                doctorId = cardiologist.id,
                date = "2024-10-24",
                time = "09:00",
                reason = "Chequeo general y revisión de presión arterial.",
                status = "CONFIRMED",
                createdAt = "2024-10-01"
            ),
            AppointmentEntity(
                patientId = elena.id,
                doctorId = cardiologist.id,
                date = "2024-10-24",
                time = "10:30",
                reason = "Control de hipertensión y seguimiento de tratamiento.",
                status = "CONFIRMED",
                createdAt = "2024-10-02"
            ),
            AppointmentEntity(
                patientId = marco.id,
                doctorId = cardiologist.id,
                date = "2024-10-24",
                time = "12:00",
                reason = "Dolor abdominal agudo reportado durante la mañana.",
                status = "URGENT",
                createdAt = "2024-10-03"
            ),
            AppointmentEntity(
                patientId = juan.id,
                doctorId = pediatrician.id,
                date = "2024-10-18",
                time = "16:45",
                reason = "Consulta de revisión pediátrica.",
                status = "CONFIRMED",
                createdAt = "2024-10-02"
            ),
            AppointmentEntity(
                patientId = juan.id,
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

    /**
     * Inserta disponibilidad inicial para el primer médico.
     */
    private suspend fun seedDoctorAvailabilityIfNeeded() {
        val doctor = doctorRepository.getAllDoctors().firstOrNull() ?: return

        if (doctorAvailabilityRepository.getAvailabilityByDoctorId(doctor.id).isNotEmpty()) return

        doctorAvailabilityRepository.insertAvailabilityBlocks(
            listOf(
                DoctorAvailabilityEntity(
                    doctorId = doctor.id,
                    day = "4",
                    title = "Turno Mañana",
                    startTime = "08:00",
                    endTime = "14:00",
                    consultationFee = 850.0
                ),
                DoctorAvailabilityEntity(
                    doctorId = doctor.id,
                    day = "4",
                    title = "Turno Tarde",
                    startTime = "16:00",
                    endTime = "20:00",
                    consultationFee = 850.0
                )
            )
        )
    }
}
