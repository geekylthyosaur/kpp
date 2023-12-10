package domain.model.drawable

import androidx.compose.ui.geometry.*
import domain.model.client.*

data class DrawableDisappearance(
    val type: ClientType,
    var progress: Float,
    var xy: Offset
)
