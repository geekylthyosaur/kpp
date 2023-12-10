@file:Suppress("ReplaceManualRangeWithIndicesCalls")

package presenter.drawableComponents

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import domain.model.client.*
import domain.model.drawable.*
import presenter.utils.*

const val clientRadius = 16f

val defaultClientColor = Color(red = 0.94f, green = 0.72f, blue = 0.63f)
val militaryClientColor = Color(0xFF353B17)
val disabledClientColor = Color(0xFFBB2233)
val childColor = Color(0xFF6495ED)

fun DrawScope.drawClients(
    clients: List<DrawableClient>
) {
    for (index in 0 until clients.size) {
        if (index >= clients.size) break

        if (!clients[index].isServed) {
            drawClient(
                radius = clientRadius,
                center = clients[index].xy,
                type = ClientType.fromString(clients[index].clientType),
                shouldChangeSaturation = null
            )
        }
    }
}

private fun DrawScope.drawClient(
    radius: Float,
    center: Offset,
    type: ClientType,
    alpha: Float = 1f,
    shouldChangeSaturation: Pair<Int, Int>?,
    borderColor: Color = Color.DarkGray
) {
    drawCircle(
        color = when (type) {
            ClientType.Common -> defaultClientColor
            ClientType.Disabled -> disabledClientColor
            ClientType.Military -> militaryClientColor
            ClientType.WithChild -> defaultClientColor
        }.run {
            if (shouldChangeSaturation == null) {
                this
            } else {
                defineColorSaturation(shouldChangeSaturation.first, shouldChangeSaturation.second)
            }
        }.copy(
            alpha = alpha
        ),
        radius = radius,
        center = center
    )
    drawCircle(
        color = borderColor.copy(
            alpha = alpha
        ),
        radius = radius,
        center = center,
        style = Stroke(width = 0.5f)
    )

    if (type == ClientType.WithChild) {
        drawCircle(
            color = if (shouldChangeSaturation == null) {
                childColor.copy(
                    alpha = alpha
                )
            } else {
                childColor.defineColorSaturation(shouldChangeSaturation.first, shouldChangeSaturation.second).copy(
                    alpha = alpha
                )
            },
            radius = radius / 1.5f,
            center = center.plus(Offset(radius, radius / 2))
        )
        drawCircle(
            color = borderColor.copy(
                alpha = alpha
            ),
            radius = radius / 1.5f,
            center = center.plus(Offset(radius, radius / 2)),
            style = Stroke(width = 0.5f)
        )
    }
}

fun DrawScope.drawClientsQueue(
    xy: Offset,
    clients: List<DrawableClient>
) {
    val clientsPerRow = 8
    val c = clients.size / clientsPerRow

    for (i in 0..c) {
        val subList = if (i == c) {
            clients.subList(clientsPerRow * i, clients.size)
        } else {
            clients.subList(clientsPerRow * i, clientsPerRow * (i + 1))
        }
        val y = xy.y - clientRadius * (4f / (c + 1) * i - 0.5f)

        drawClientsRow(
            xy = xy.copy(y = y),
            clients = subList
        )
    }
}

private fun DrawScope.drawClientsRow(
    xy: Offset,
    clients: List<DrawableClient>
) {
    val size = clients.size
    for (index in 0 until size) {
        if (index >= clients.size) break
        val reverseIndex = size - 1 - index

        drawClient(
            radius = clientRadius,
            center = when (size) {
                1 -> xy
                2 -> when (index) {
                    0 -> xy.run {
                        Offset(x - clientRadius / 2, y)
                    }
                    else -> xy.run {
                        Offset(x + clientRadius / 2, y)
                    }
                }
                else -> xy.run {
                    Offset(x + 2 * clientRadius * (2f / size * index - 1), y)
                }
            },
            type = ClientType.fromString(clients[reverseIndex].clientType),
            shouldChangeSaturation = size to index
        )
    }
}

fun DrawScope.drawAllDisappearances(
    disappearances: MutableList<DrawableDisappearance>,
    internalCounter: Int
) {
    for (index in 0 until disappearances.size) {
        drawClientDisappearance(
            type = disappearances[index].type,
            progress = disappearances[index].progress,
            xy = disappearances[index].xy
        )
    }
}

private fun DrawScope.drawClientDisappearance(
    type: ClientType,
    progress: Float,
    xy: Offset
) {
    drawClient(
        radius = clientRadius + 8f * (1f - progress),
        center = xy,
        type = type,
        alpha = progress,
        borderColor = Color.DarkGray.copy(
            alpha = progress
        ),
        shouldChangeSaturation = null
    )
}

private fun Color.defineColorSaturation(size: Int, index: Int): Color {
    val red = this.red
    val green = this.green
    val blue = this.blue

    return Color(0).copy(
        alpha = 1f,
        red = (red * (index + 3) / (size.toFloat() + 2)).edge(0f, 1f),
        green = (green * (index + 3) / (size.toFloat() + 2)).edge(0f, 1f),
        blue = (blue * (index + 3) / (size.toFloat() + 2)).edge(0f, 1f)
    )
}