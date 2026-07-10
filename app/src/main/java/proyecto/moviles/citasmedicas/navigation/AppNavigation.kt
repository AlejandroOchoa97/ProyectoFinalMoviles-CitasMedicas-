package proyecto.moviles.citasmedicas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.data.repository.AppointmentRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorAvailabilityRepository
import proyecto.moviles.citasmedicas.data.repository.DoctorRepository
import proyecto.moviles.citasmedicas.data.repository.PatientRepository
import proyecto.moviles.citasmedicas.ui.screens.auth.LoginScreen
import proyecto.moviles.citasmedicas.ui.screens.auth.RegisterScreen
import proyecto.moviles.citasmedicas.ui.screens.auth.RecoverPasswordScreen
import proyecto.moviles.citasmedicas.ui.screens.onboarding.OnboardingScreen
import proyecto.moviles.citasmedicas.ui.screens.onboarding.onboardingPages
import proyecto.moviles.citasmedicas.ui.screens.splash.SplashScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.PatientHomeScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.SearchDoctorScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.PatientAppointmentDetailScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.ScheduleDetailsScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.AppointmentHistoryScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.UserProfileScreen
import proyecto.moviles.citasmedicas.ui.screens.doctor.DoctorHomeScreen
import proyecto.moviles.citasmedicas.ui.screens.doctor.DoctorAppointmentDetailScreen
import proyecto.moviles.citasmedicas.ui.screens.doctor.DoctorAvailabilityScreen
import proyecto.moviles.citasmedicas.ui.screens.doctor.DoctorProfileScreen
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview

// Punto de entrada de navegación. Por ahora el único destino es el inicio de sesión.
@Composable
fun AppNavigation(
    startDestination: String = Routes.SPLASH,
    appointmentRepository: AppointmentRepository? = null,
    patientRepository: PatientRepository? = null,
    doctorRepository: DoctorRepository? = null,
    doctorAvailabilityRepository: DoctorAvailabilityRepository? = null
) {
    var currentRoute by rememberSaveable { mutableStateOf(startDestination) }
    var selectedAppointmentId by rememberSaveable { mutableStateOf(-1) }
    var selectedDoctorId by rememberSaveable { mutableStateOf(1) }

    when (currentRoute) {
        Routes.SPLASH -> SplashScreen(
            onFinished = { currentRoute = Routes.ONBOARDING_1 }
        )
        Routes.ONBOARDING_1 -> OnboardingScreen(
            page = onboardingPages[0],
            onNext = { currentRoute = Routes.ONBOARDING_2 },
            onSkip = { currentRoute = Routes.LOGIN }
        )
        Routes.ONBOARDING_2 -> OnboardingScreen(
            page = onboardingPages[1],
            onNext = { currentRoute = Routes.ONBOARDING_3 },
            onSkip = { currentRoute = Routes.LOGIN }
        )
        Routes.ONBOARDING_3 -> OnboardingScreen(
            page = onboardingPages[2],
            onNext = { currentRoute = Routes.LOGIN },
            onSkip = { currentRoute = Routes.LOGIN }
        )
        Routes.LOGIN -> LoginScreen(
            onLoginSuccess = { 
                // Simulamos que el primer usuario es un doctor para mostrar la nueva pantalla
                currentRoute = Routes.DOCTOR_HOME 
            },
            onRegisterClick = { currentRoute = Routes.REGISTER },
            onForgotPasswordClick = { currentRoute = Routes.RECOVER_PASSWORD }
        )
        Routes.RECOVER_PASSWORD -> RecoverPasswordScreen(
            onBack = { currentRoute = Routes.LOGIN }
        )
        Routes.REGISTER -> RegisterScreen(
            onBack = { currentRoute = Routes.LOGIN },
            onRegistrationComplete = { currentRoute = Routes.LOGIN }
        )
        Routes.PATIENT_HOME -> PatientHomeScreen(
            onBack = { currentRoute = Routes.LOGIN },
            onScheduleAppointment = { currentRoute = Routes.SEARCH_DOCTOR },
            onNavigateHistory = { currentRoute = Routes.APPOINTMENT_HISTORY },
            onNavigateProfile = { currentRoute = Routes.USER_PROFILE },
            onAppointmentDetails = { id ->
                selectedAppointmentId = id
                currentRoute = Routes.PATIENT_APPOINTMENT_DETAIL
            },
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository,
            patientId = 1
        )
        Routes.PATIENT_APPOINTMENT_DETAIL -> PatientAppointmentDetailScreen(
            appointmentId = selectedAppointmentId,
            onBack = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHome = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHistory = { currentRoute = Routes.APPOINTMENT_HISTORY },
            onNavigateProfile = { currentRoute = Routes.USER_PROFILE },
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository
        )
        Routes.SEARCH_DOCTOR -> SearchDoctorScreen(
            onBack = { currentRoute = Routes.PATIENT_HOME },
            onDoctorSelected = { doctorId ->
                selectedDoctorId = doctorId
                currentRoute = Routes.SCHEDULE_DETAILS
            },
            onNavigateHome = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHistory = { currentRoute = Routes.APPOINTMENT_HISTORY },
            onNavigateProfile = { currentRoute = Routes.USER_PROFILE },
            doctorRepository = doctorRepository
        )
        Routes.SCHEDULE_DETAILS -> ScheduleDetailsScreen(
            onBack = { currentRoute = Routes.SEARCH_DOCTOR },
            onConfirm = { _, _, _ ->
                currentRoute = Routes.PATIENT_HOME
            },
            appointmentRepository = appointmentRepository,
            doctorAvailabilityRepository = doctorAvailabilityRepository,
            patientId = 1,
            doctorId = selectedDoctorId
        )
        Routes.APPOINTMENT_HISTORY -> AppointmentHistoryScreen(
            onNavigateHome = { currentRoute = Routes.PATIENT_HOME },
            onNavigateProfile = { currentRoute = Routes.USER_PROFILE },
            onAppointmentDetails = { id ->
                selectedAppointmentId = id
                currentRoute = Routes.PATIENT_APPOINTMENT_DETAIL
            },
            appointmentRepository = appointmentRepository,
            doctorRepository = doctorRepository,
            patientId = 1
        )
        Routes.USER_PROFILE -> UserProfileScreen(
            onBack = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHome = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHistory = { currentRoute = Routes.APPOINTMENT_HISTORY },
            onLogout = { currentRoute = Routes.LOGIN },
            patientRepository = patientRepository,
            patientId = 1
        )
        Routes.DOCTOR_HOME -> DoctorHomeScreen(
            onBack = { currentRoute = Routes.LOGIN },
            onProfileClick = { currentRoute = Routes.DOCTOR_PROFILE },
            onAppointmentClick = { id ->
                selectedAppointmentId = id
                currentRoute = Routes.DOCTOR_APPOINTMENT_DETAIL
            },
            onNavigateToAvailability = { currentRoute = Routes.DOCTOR_AVAILABILITY },
            appointmentRepository = appointmentRepository,
            patientRepository = patientRepository,
            doctorRepository = doctorRepository,
            doctorId = 1
        )
        Routes.DOCTOR_APPOINTMENT_DETAIL -> DoctorAppointmentDetailScreen(
            appointmentId = selectedAppointmentId,
            onBack = { currentRoute = Routes.DOCTOR_HOME },
            appointmentRepository = appointmentRepository,
            patientRepository = patientRepository
        )
        Routes.DOCTOR_AVAILABILITY -> DoctorAvailabilityScreen(
            onBack = { currentRoute = Routes.DOCTOR_HOME },
            onProfileClick = { currentRoute = Routes.DOCTOR_PROFILE },
            onNavigateToHome = { currentRoute = Routes.DOCTOR_HOME },
            availabilityRepository = doctorAvailabilityRepository,
            doctorId = 1
        )
        Routes.DOCTOR_PROFILE -> DoctorProfileScreen(
            onBack = { currentRoute = Routes.DOCTOR_HOME },
            onNavigateHome = { currentRoute = Routes.DOCTOR_HOME },
            onNavigateAvailability = { currentRoute = Routes.DOCTOR_AVAILABILITY },
            onLogout = { currentRoute = Routes.LOGIN },
            doctorRepository = doctorRepository,
            doctorId = 1
        )
        else -> LoginScreen(
            onLoginSuccess = { currentRoute = Routes.PATIENT_HOME },
            onRegisterClick = { currentRoute = Routes.REGISTER },
            onForgotPasswordClick = { currentRoute = Routes.RECOVER_PASSWORD }
        )
    }
}

@Preview(
    name = "Navegación principal",
    showBackground = true,
    backgroundColor = AppBackgroundPreview,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun AppNavigationPreview() {
    MediCitasTheme {
        AppNavigation()
    }
}

