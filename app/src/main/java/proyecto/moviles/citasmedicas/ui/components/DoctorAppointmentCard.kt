package proyecto.moviles.citasmedicas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import proyecto.moviles.citasmedicas.data.SampleData
import proyecto.moviles.citasmedicas.model.DoctorAppointment
import proyecto.moviles.citasmedicas.ui.theme.*

@Composable
fun DoctorAppointmentCard(
    appointment: DoctorAppointment,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val borderColor = if (appointment.isUrgent) ErrorRed.copy(alpha = 0.5f) else BorderSoft
    val leftBarColor = if (appointment.isUrgent) ErrorRed else Color.Transparent

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxHeight()) {
            // Left urgency indicator bar
            if (appointment.isUrgent) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(leftBarColor)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar with Status Badge
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(SecondaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = PrimaryBlue
                                )
                            }
                            
                            // Status icon at bottom right of avatar
                            val statusIcon = if (appointment.isUrgent) Icons.Default.Warning else Icons.Default.CheckCircle
                            val statusIconTint = if (appointment.isUrgent) ErrorRed else TertiaryGreen
                            
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(20.dp),
                                shape = CircleShape,
                                color = AppWhite
                            ) {
                                Icon(
                                    imageVector = statusIcon,
                                    contentDescription = null,
                                    tint = statusIconTint,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = appointment.patientName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${appointment.patientAge} años • ${appointment.time}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    StatusBadge(status = appointment.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Reason section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppBackground)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val reasonIcon = if (appointment.isUrgent) Icons.Default.Warning else Icons.Default.Info
                    val reasonIconColor = if (appointment.isUrgent) ErrorRed else TextSecondary

                    Icon(
                        imageVector = reasonIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = reasonIconColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = appointment.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = AppBackgroundPreview)
@Composable
fun DoctorAppointmentCardPreview() {
    MediCitasTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            DoctorAppointmentCard(SampleData.sampleDoctorAppointments[0])
            DoctorAppointmentCard(SampleData.sampleDoctorAppointments[2])
        }
    }
}
