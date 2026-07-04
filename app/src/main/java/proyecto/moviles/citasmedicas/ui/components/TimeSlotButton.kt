package proyecto.moviles.citasmedicas.ui.components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import proyecto.moviles.citasmedicas.ui.theme.AppWhite
import proyecto.moviles.citasmedicas.ui.theme.PrimaryBlue
import proyecto.moviles.citasmedicas.ui.theme.SecondaryBlue
import proyecto.moviles.citasmedicas.ui.theme.BorderSoft
import proyecto.moviles.citasmedicas.ui.theme.MediCitasTheme
import proyecto.moviles.citasmedicas.ui.theme.TextPrimary

/**
 * Crea un botón de horario
 */
@Composable
fun TimeSlotButton(time: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier){

    //OutlinedButton es un botón con borde.
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),

        /**
         * Borde del botón.
         *
         * Si está seleccionado, el borde es más grueso y es de color azul(PrimaryBlue)
         * Si no está seleccionado, el borde es más delgado y de un color gris(BorderSoft)
         */
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) PrimaryBlue else BorderSoft
        ),
        /**
         * Color del botón.
         *
         * Si está seleccionado, el fondo es azul claro (SecondaryBlue) y el texto es azul fuerte (PrimaryBlue)
         * Si no está seleccionado, el fondo es blanco (AppWhite) y el texto oscuro (TextPrimary)
         */
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) SecondaryBlue else AppWhite,
            contentColor = if (selected) PrimaryBlue else TextPrimary
        ),

        //Espacio para que el texto no quede pegado en los bordes
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        //Texto del botón (Hora)
        Text(
            text = time,
            style = MaterialTheme.typography.bodyLarge,
            //Si el botón está seleccionado, el texto es mas grueso.
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

/**
 * Preview del botón de horario
 */
@Preview
@Composable
fun TimeSlotButtonPreview(){
    MediCitasTheme {
        TimeSlotButton(
            time = "11:30",
            selected = true,
            onClick = {}
        )
    }
}