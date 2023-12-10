package domain.model.drawable

import androidx.compose.ui.geometry.*

data class DrawableCashRegister(
    var id: Int,
    var xy: Offset,
    var clientQueueXY: Offset,
    var textXY: Offset,
    var roll: Float,
    val clients: MutableList<DrawableClient>,
    var clientsMovingToIt: Int,
    var highlightFrames: Int
) {

    companion object {
        const val HIGHLIGHT_FRAMES_MAX = 60
    }
}