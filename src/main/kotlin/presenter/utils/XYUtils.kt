package presenter.utils

import androidx.compose.ui.geometry.*
import kotlin.math.*

fun Offset.measureDistance(xy: Offset): Float {
    return sqrt((x - xy.x).pow(2) + (y - xy.y).pow(2))
}

fun Offset.moveToBy(xy: Offset, by: Float): Offset {
    val a = xy.x - x
    val b = xy.y - y
    val cBy = sqrt(a.pow(2) + b.pow(2)) / by

    return Offset(
        x = x + a / cBy,
        y = y + b / cBy
    )
}