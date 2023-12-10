package domain.curvature

import androidx.compose.ui.geometry.*

data class CurveMovingResult(
    val newXY: Offset,
    val mod: Float,
    val t: Float
) {

    companion object {
        fun new() = CurveMovingResult(Offset.Zero, 0f, 0f)
    }
}
