package presenter.utils

import androidx.compose.ui.geometry.*
import kotlin.math.*

val Offset.module get() = sqrt(x.pow(2) + y.pow(2))

fun Offset.moveToByLinear(to: Offset, by: Float): Offset {
    val a = to.x - x
    val b = to.y - y
    val cBy = sqrt(a.pow(2) + b.pow(2)) / by

    return Offset(
        x = x + a / cBy,
        y = y + b / cBy
    )
}

fun Offset.measureDistance(xy: Offset): Float {
    return sqrt((x - xy.x).pow(2) + (y - xy.y).pow(2))
}

fun Offset.toClientQueueXY(width: Float, height: Float, entityWidth: Float): Offset {
    val firstIteration = when {
        (x != 0f) && (y == 0f) -> Offset(x, entityWidth / 2)
        (x >= width) && (y != 0f) -> Offset(width - entityWidth / 2, y)
        (x != 0f) && (y >= height) -> Offset(x, height - entityWidth / 2)
        (x == 0f) && (y != 0f) -> Offset(entityWidth / 2, y)
        else -> Offset.Zero
    }

    return Offset(
        x = max(entityWidth / 2, min(width - entityWidth / 2, firstIteration.x)),
        y = max(entityWidth / 2, min(height - entityWidth / 2, firstIteration.y))
    )
}

fun Offset.toClientExitXY(width: Float, height: Float, rectWidth: Float): Offset {
    val firstIteration = when {
        (x != 0f) && (y == 0f) -> Offset(x, rectWidth / 2)
        (x >= width) && (y != 0f) -> Offset(width - rectWidth / 4 * 3, y)
        (x != 0f) && (y >= height) -> Offset(x, height - rectWidth / 2)
        (x == 0f) && (y != 0f) -> Offset(rectWidth / 4 * 3, y)
        else -> Offset.Zero
    }

    return Offset(
        x = max(rectWidth / 2, min(width - rectWidth / 2, firstIteration.x)),
        y = max(rectWidth / 2, min(height - rectWidth / 2, firstIteration.y))
    )
}

fun Float.edge(min: Float, max: Float): Float {
    return max(min(this, max), min)
}