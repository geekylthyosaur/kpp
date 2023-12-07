package presenter.drawableComponents

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import domain.model.client.*
import domain.model.drawable.*

fun DrawScope.drawClients(
    clients: List<DrawableClient>
) {
    val circleRadius = 32f

    clients.forEach {
        if (!it.isServed) {
            drawCircle(
                color = Color.Red,
                radius = circleRadius,
                center = it.xy
            )
        }
    }
}