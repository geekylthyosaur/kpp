package domain.model.drawable

import androidx.compose.ui.geometry.*

data class DrawableExit(
    var xy: Offset,
    var clientQueueXY: Offset,
    var roll: Int
)