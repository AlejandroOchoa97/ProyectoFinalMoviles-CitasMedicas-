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
        DoctorAppointment(1, "Ricardo Gómez", 45, "09:00", "Chequeo general", "Confirmada"),
        DoctorAppointment(2, "Elena Ortiz", 72, "10:30", "Control de hipertensión", "Confirmada"),
        DoctorAppointment(3, "Marco Ruiz", 28, "12:00", "Dolor abdominal agudo", "Urgente", isUrgent = true, hasNotification = true)
    )
}
