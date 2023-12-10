package domain.curvature

import androidx.compose.ui.geometry.*

data class CurveMovingResult(
    val newXY: Offset,
    val mod: Float,
    val t: Float
)
