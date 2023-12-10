package presenter.utils

fun Float.withOffset(drawRollLength: Float, offset: Float): Float {
    return if (this + offset >= 0) {
        (this + offset) % drawRollLength
    }  else {
        drawRollLength - (drawRollLength - (this + offset) % drawRollLength) % drawRollLength
    }
}
