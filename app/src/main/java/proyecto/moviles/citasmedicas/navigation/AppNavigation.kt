package proyecto.moviles.citasmedicas.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import proyecto.moviles.citasmedicas.data.repository.*
import proyecto.moviles.citasmedicas.ui.screens.auth.*
import proyecto.moviles.citasmedicas.ui.screens.onboarding.*
import proyecto.moviles.citasmedicas.ui.screens.splash.SplashScreen
import proyecto.moviles.citasmedicas.ui.screens.patient.*
import proyecto.moviles.citasmedicas.ui.screens.doctor.*
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.AppBackgroundPreview
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    startDestination: String = Routes.SPLASH,
    appointmentRepository: AppointmentRepository? = null,
    patientRepository: PatientRepository? = null,
    doctorRepository: DoctorRepository? = null,
    doctorAvailabilityRepository: DoctorAvailabilityRepository? = null,
    authRepository: AuthRepository? = null
) {
    var currentRoute by rememberSaveable { mutableStateOf(startDestination) }
    var selectedAppointmentId by rememberSaveable { mutableStateOf(-1) }
    var selectedDoctorId by rememberSaveable { mutableStateOf(1) }
    var localUserId by rememberSaveable { mutableStateOf(-1) }
    
    val scope = rememberCoroutineScope()

    // Intentar recuperar el ID local si ya hay una sesión de Firebase activa
    LaunchedEffect(Unit) {
        val firebaseUid = authRepository?.currentUser?.uid
        if (firebaseUid != null && localUserId == -1) {
            val roleResult = authRepository.getUserRole(firebaseUid)
            roleResult.onSuccess { role ->
                if (role == "Médico") {
                    val doctor = doctorRepository?.getDoctorByUid(firebaseUid)
                    localUserId = doctor?.id ?: -1
                } else {
                    val patient = patientRepository?.getPatientByUid(firebaseUid)
                    localUserId = patient?.id ?: -1
                }
            }
        }
    }

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
            onLoginSuccess = { role ->
                scope.launch {
                    val firebaseUid = authRepository?.currentUser?.uid
                    if (firebaseUid != null) {
                        if (role == "Médico") {
                            val doctor = doctorRepository?.getDoctorByUid(firebaseUid)
                            localUserId = doctor?.id ?: -1
                            currentRoute = Routes.DOCTOR_HOME
                        } else {
                            val patient = patientRepository?.getPatientByUid(firebaseUid)
                            localUserId = patient?.id ?: -1
                            currentRoute = Routes.PATIENT_HOME
                        }
                    } else {
                        currentRoute = if (role == "Médico") Routes.DOCTOR_HOME else Routes.PATIENT_HOME
                    }
                }
            },
            onRegisterClick = { currentRoute = Routes.REGISTER },
            onForgotPasswordClick = { currentRoute = Routes.RECOVER_PASSWORD },
            authRepository = authRepository
        )
        Routes.RECOVER_PASSWORD -> RecoverPasswordScreen(
            onBack = { currentRoute = Routes.LOGIN }
        )
        Routes.REGISTER -> RegisterScreen(
            onBack = { currentRoute = Routes.LOGIN },
            onRegistrationComplete = { currentRoute = Routes.LOGIN },
            authRepository = authRepository,
            patientRepository = patientRepository,
            doctorRepository = doctorRepository
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
            patientId = localUserId
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
            patientId = localUserId,
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
            patientId = localUserId
        )
        Routes.USER_PROFILE -> UserProfileScreen(
            onBack = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHome = { currentRoute = Routes.PATIENT_HOME },
            onNavigateHistory = { currentRoute = Routes.APPOINTMENT_HISTORY },
            onLogout = { 
                authRepository?.logout()
                localUserId = -1
                currentRoute = Routes.LOGIN 
            },
            patientRepository = patientRepository,
            patientId = localUserId
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
            doctorId = localUserId
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
            doctorId = localUserId
        )
        Routes.DOCTOR_PROFILE -> DoctorProfileScreen(
            onBack = { currentRoute = Routes.DOCTOR_HOME },
            onNavigateHome = { currentRoute = Routes.DOCTOR_HOME },
            onNavigateAvailability = { currentRoute = Routes.DOCTOR_AVAILABILITY },
            onLogout = { 
                authRepository?.logout()
                localUserId = -1
                currentRoute = Routes.LOGIN 
            },
            doctorRepository = doctorRepository,
            doctorId = localUserId
        )
        else -> LoginScreen(
            onLoginSuccess = { role ->
                currentRoute = if (role == "Médico") Routes.DOCTOR_HOME else Routes.PATIENT_HOME
            },
            onRegisterClick = { currentRoute = Routes.REGISTER },
            onForgotPasswordClick = { currentRoute = Routes.RECOVER_PASSWORD },
            authRepository = authRepository
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
