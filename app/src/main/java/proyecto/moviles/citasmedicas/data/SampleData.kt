package proyecto.moviles.citasmedicas.data

import proyecto.moviles.citasmedicas.model.Appointment
import proyecto.moviles.citasmedicas.model.Doctor
import proyecto.moviles.citasmedicas.model.DoctorAppointment

object SampleData {
    val sampleAppointments = listOf(
        Appointment(1, "Dr. Alejandro Martínez", "Cardiología", "15/10/2024", "10:30", "$800 MXN", "Confirmada"),
        Appointment(2, "Dra. Elena Ruiz", "Pediatría", "18/10/2024", "16:45", "$650 MXN", "Confirmada"),
        Appointment(3, "Dr. Carlos Villa", "Dermatología", "22/10/2024", "09:00", "$950 MXN", "En revisión")
    )

    val sampleDoctors = listOf(
        Doctor(1, "Dra. Elena Ruiz", "Cardiología", 4.8, 120, 10, "$800 MXN"),
        Doctor(2, "Dr. Ricardo Salinas", "Pediatría", 4.9, 245, 15, "$950 MXN"),
        Doctor(3, "Dra. Claudia Méndez", "Dermatología", 4.7, 89, 8, "$1,100 MXN"),
        Doctor(4, "Dr. Manuel Torres", "Medicina General", 5.0, 56, 22, "$600 MXN")
    )

    val sampleDoctorAppointments = listOf(
        DoctorAppointment(1, "Ricardo Gómez", 45, "Masculino", "+52 55 1111 2222", "15 Oct, 2024", "09:00", "Chequeo general", specialty = "Medicina General", status = "Confirmada"),
        DoctorAppointment(2, "Elena Ortiz", 72, "Femenino", "+52 55 3333 4444", "15 Oct, 2024", "10:30", "Control de hipertensión", specialty = "Cardiología", status = "Confirmada"),
        DoctorAppointment(3, "Marco Ruiz", 28, "Masculino", "+52 55 5555 6666", "15 Oct, 2024", "12:00", "Dolor abdominal agudo", specialty = "Urgencias", status = "Urgente", isUrgent = true, hasNotification = true),
        DoctorAppointment(
            id = 4,
            patientName = "Ricardo Mendoza",
            patientAge = 68,
            patientGender = "Masculino",
            patientPhone = "+52 55 1234 5678",
            date = "24 Oct, 2023",
            time = "14:30 h",
            reason = "Seguimiento de hipertensión arterial.",
            detailedReason = "Seguimiento de hipertensión arterial. El paciente reporta mareos ligeros por las mañanas durante la última semana. Requiere revisión de dosis de Enalapril.",
            specialty = "CARDIOLOGÍA",
            status = "Confirmada"
        )
    )
}
