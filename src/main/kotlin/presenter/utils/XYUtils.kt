package presenter.utils

import androidx.compose.ui.geometry.*
import java.util.Random
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

fun Offset.moveToRandomByLinear(by: Float): Offset {
    val random = Random(System.currentTimeMillis())
    val a = random.nextFloat()
    val b = random.nextFloat()

    return Offset(
        x = x + a * by,
        y = y + b * by
    )
}

fun Offset.measureDistance(xy: Offset): Float {
    return sqrt((x - xy.x).pow(2) + (y - xy.y).pow(2))
}

fun Offset.toClientQueueXY(width: Float, height: Float, distance: Float): Offset {
    val firstIteration = when {
        (x != 0f) && (y == 0f) -> Offset(x, distance)
        (x >= width) && (y != 0f) -> Offset(width - distance, y)
        (x != 0f) && (y >= height) -> Offset(x, height - distance)
        (x == 0f) && (y != 0f) -> Offset(distance, y)
        else -> Offset.Zero
    }

    return Offset(
        x = max(distance, min(width - distance, firstIteration.x)),
        y = max(distance, min(height - distance, firstIteration.y))
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