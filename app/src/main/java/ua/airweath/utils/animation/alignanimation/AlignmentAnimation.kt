package ua.airweath.utils.animation.alignanimation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment

@Composable
fun animateAlignmentAsState(
    targetBiasValue: Float,
    verticalAlignment: Float = 0f
): State<BiasAlignment> {
    val bias by animateFloatAsState(
        targetBiasValue,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    return remember { derivedStateOf { BiasAlignment(bias, verticalAlignment) } }
}