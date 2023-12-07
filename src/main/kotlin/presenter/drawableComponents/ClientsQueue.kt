package presenter.drawableComponents

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import domain.model.drawable.*
import kotlin.math.*

fun DrawScope.drawClientsQueue(
    xy: Offset,
    clients: List<DrawableClient>
) {
    val circleRadius = 32f

    //TODO coloring
    when (clients.size) {
        1 -> {
            drawCircle(
                color = Color.Magenta,
                radius = circleRadius,
                center = xy
            )
        }
        2 -> {
            drawCircle(
                color = Color.Magenta,
                radius = circleRadius,
                center = xy.run {
                    Offset(x - circleRadius / 2, y)
                }
            )
            drawCircle(
                color = Color.Magenta,
                radius = circleRadius,
                center = xy.run {
                    Offset(x + circleRadius / 2, y)
                }
            )
        }
        else -> {
            for (i in clients.indices) {
                drawCircle(
                    color = Color(0xFF4F444F).copy(
                        blue = min((160f + 32 * i) / 255f, 1f),
                        red = min((160f + 32 * i) / 255f, 1f),
                    ),
                    radius = circleRadius,
                    center = xy.run {
                        Offset(x + circleRadius + circleRadius * 2 * (1f / clients.size * i - 1), y)
                    }
                )
            }
        }
    }
}