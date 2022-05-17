package com.example.game2048.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.game2048.R

// Set of Material typography styles to start with
val Bebas = FontFamily(
    Font(R.font.bebas_neue)
)
val Undertale = FontFamily(
    Font(R.font.undertale)
)
val Cyberpunk = FontFamily(
    Font(R.font.cyberpunk)
)
val Typography = Typography(

    body1 = TextStyle(
        fontFamily = Bebas,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)