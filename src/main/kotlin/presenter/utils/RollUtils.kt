package presenter.utils

import androidx.compose.ui.geometry.*
import kotlin.math.*

val Offset.isValid get() = x >= 0 && y >= 0

//rollPointer to xy coordinates
fun Int.toXY(width: Int, height: Int, entityWidth: Int = 0): Offset {

//    if (this in 0..width) {
//        return Offset(this.toFloat() + entityWidth / 2, 0f)
//    }
//
//    if (this in width..width + height) {
//        return Offset(width.toFloat(), this.toFloat() - width + entityWidth / 2)
//    }
//
//    if (this in width + height..2 * width + height) {
//        return Offset(2 * width + height - this.toFloat() - entityWidth / 2, height.toFloat())
//    }
//
//    if (this > 2 * width + 2 * height) {
//        return Offset(this.toFloat() - (2 * width + 2 * height) - entityWidth, 0f)
//    }
//
//    return Offset(0f, 2 * width + 2 * height - this.toFloat() - entityWidth)

    if (this in 0..width) {
        return Offset(this.toFloat(), 0f)
    }

    if (this in width..width + height) {
        return Offset(width.toFloat(), this.toFloat() - width)
    }

    if (this in width + height..2 * width + height) {
        return Offset(2 * width + height - this.toFloat(), height.toFloat())
    }

    if (this > 2 * width + 2 * height) {
        return Offset(this.toFloat() - (2 * width + 2 * height), 0f)
    }

    return Offset(0f, 2 * width + 2 * height - this.toFloat())
}

fun Offset.toRollPointer(width: Int, height: Int, rectWidth: Float): Int {
//    return when {
//        (x != 0f) && (y == 0f) -> x.toInt()
//        (x == width - rectWidth) && (y != 0f) -> width + rectWidth.toInt() + y.toInt()
//        (x != 0f) && (y == height - rectWidth) -> 2 * width - x.toInt() + y.toInt()
//        (x == 0f) && (y != 0f) -> 2 * width + 2 * height - y.toInt()
//        else -> 0
//    }
    return when {
        (x != 0f) && (y == 0f) -> x.toInt()
        (x >= width * 0.75f) && (y != 0f) -> width + y.toInt()
        (x != 0f) && (y >= height * 0.75f) -> ((2 * width - x.toInt() + y.toInt()) - (width + y.toInt() - height) / 8)
        (x == 0f) && (y != 0f) -> 2 * width + 2 * height - y.toInt() - width / 6
        else -> 0
    }
}

fun Offset.toClientQueueXY(width: Int, height: Int, rectWidth: Float, entityWidth: Int): Offset {
//    return when {
//        (x != 0f) && (y == 0f) -> x.toInt()
//        (x == width - rectWidth) && (y != 0f) -> width + rectWidth.toInt() + y.toInt()
//        (x != 0f) && (y == height - rectWidth) -> 2 * width - x.toInt() + y.toInt()
//        (x == 0f) && (y != 0f) -> 2 * width + 2 * height - y.toInt()
//        else -> 0
//    }
    val firstIteration = when {
        (x != 0f) && (y == 0f) -> Offset(x + entityWidth / 2, rectWidth * 2)
        (x >= width - rectWidth) && (y != 0f) -> Offset(width - rectWidth * 2, y + entityWidth / 2)
        (x != 0f) && (y >= height - rectWidth) -> Offset(x + entityWidth / 2, height - rectWidth * 2)
        (x == 0f) && (y != 0f) -> Offset(rectWidth * 2, y + entityWidth / 2)
        else -> Offset.Zero
    }

    return Offset(
        x = max(rectWidth * 2, min(width - rectWidth * 2, firstIteration.x)),
        y = max(rectWidth * 2, min(height - rectWidth * 2, firstIteration.y))
    )
}

fun Int.withOffset(drawRollLength: Int, offset: Int): Int {
    return if (this + offset >= 0) {
        (this + offset) % drawRollLength
    }  else {
        drawRollLength - (drawRollLength - (this + offset) % drawRollLength) % drawRollLength
    }
}
