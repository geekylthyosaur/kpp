package domain.model.drawable

import androidx.compose.ui.geometry.*

data class DrawableCashRegister(
    var xy: Offset,
    var clientQueueXY: Offset,
    var roll: Int,
    val clients: MutableList<DrawableClient>,
    var clientsMovingToIt: Int
)