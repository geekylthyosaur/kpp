package domain.curvature

import androidx.compose.ui.geometry.*
import presenter.utils.*

fun Offset.calculateBezierCoordinate(p1: Offset, p2: Offset, t: Float): Offset {
    val u = 1 - t
    val tt = t * t
    val uu = u * u

    val x = uu * this.x + 2 * u * t * p1.x + tt * p2.x
    val y = uu * this.y + 2 * u * t * p1.y + tt * p2.y

    return Offset(x, y)
}

fun Offset.moveToByCurve(to: Offset, controlXY: Offset, lastResult: CurveMovingResult): CurveMovingResult {
    val moveSpeed = 4.5f

    val oldT = if (lastResult.t == 0f) 0.01f else lastResult.t
    val t = if (lastResult.mod == 0f) 0f else oldT + moveSpeed / lastResult.mod

    val thisTwice = this.times(2f)

    return CurveMovingResult(
        newXY = calculateBezierCoordinate(controlXY, to, t),
        mod = (thisTwice - controlXY.times(4f) + to.times(2f)).times(t).plus(controlXY.times(2f) - thisTwice).module,
        t = t
    )
}

fun Offset.calculateControlXY(end: Offset, centerXY: Offset): Offset {
    val half = end.minus(this).div(2f)
    val divCoef = 2.5f
    val first = this.plus(half).plus(Offset(-half.y, half.x).div(divCoef))
    val second = this.plus(half).plus(Offset(half.y, -half.x).div(divCoef))

    return if (first.measureDistance(centerXY) < second.measureDistance(centerXY)) first else second
}