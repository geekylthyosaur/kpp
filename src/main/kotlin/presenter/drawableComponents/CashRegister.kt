package presenter.drawableComponents

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import domain.model.drawable.*
import presenter.utils.*
import kotlin.math.*
import kotlin.random.*

const val entityToWholeCoefficient = 0.5f
const val rectWidth = 32f

val defaultCashRegisterColor = Color(0xFF33DD33)
val reserveCashRegisterColor = Color(0xFF3333CC)
val exitColor = Color(0xFF666666)

fun DrawScope.drawCashRegistersAndExits(
    canvasSize: Size,
    cashRegisters: MutableList<DrawableCashRegister>,
    exits: MutableList<DrawableExit>,
    clients: MutableList<DrawableClient>,
    drawingOffset: Float
) {
    val random = Random(System.currentTimeMillis())
    val drawRollLength = (canvasSize.width + canvasSize.height) * 2

    val wholeWidth = drawRollLength / (cashRegisters.size + exits.size)
    val entityWidth = wholeWidth * entityToWholeCoefficient

    var rollPointer = (drawingOffset % wholeWidth)
    var c = 0

    while (rollPointer < drawRollLength && c < cashRegisters.size + exits.size) {
        val xy = drawRectangleOnRoll(
            rollPointer = rollPointer.withOffset(drawRollLength, drawingOffset + canvasSize.width),
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = if (c >= cashRegisters.size) exitColor else if (c == 0) Color.Blue else defaultCashRegisterColor
        )

        if (c < cashRegisters.size + exits.size) {
            if (c >= cashRegisters.size) {
                exits[c - cashRegisters.size].apply {
                    this.xy = xy
                    this.roll = rollPointer
                    this.clientExitXY = xy.toClientExitXY(canvasSize.width, canvasSize.height, rectWidth)
                }
            } else {
                cashRegisters[c].apply {
                    this.xy = xy
                    this.roll = rollPointer
                    this.clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, rectWidth * 3f)
                    this.textXY = xy.toClientExitXY(canvasSize.width, canvasSize.height, rectWidth)
                }
            }
        }

        rollPointer += wholeWidth
        c++
    }

    for (index in 0 until clients.size) {
        clients[index].xy = exits.random(random).clientExitXY
    }
}

fun DrawScope.updateCashRegistersAndExits(
    textMeasurer: TextMeasurer,
    canvasSize: Size,
    cashRegisters: MutableList<DrawableCashRegister>,
    closedCashRegisterIds: List<Int>,
    exits: MutableList<DrawableExit>,
    drawingOffset: Float,
    internalCounter: Int
) {
    val drawRollLength = (canvasSize.width + canvasSize.height) * 2

    val wholeWidth = drawRollLength / (cashRegisters.size + exits.size)
    val entityWidth = wholeWidth * entityToWholeCoefficient

    cashRegisters.forEach { cashRegister ->
        val xy = drawRectangleOnRoll(
            rollPointer = cashRegister.roll.withOffset(drawRollLength, drawingOffset),
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = (if (cashRegister.id == 0) reserveCashRegisterColor else defaultCashRegisterColor)
                .run {
                    if (closedCashRegisterIds.contains(cashRegister.id)) {
                        copy(
                            red = red * 0.33f,
                            green = green * 0.33f,
                            blue = blue * 0.33f,
                        )
                    } else this
                }
                .run {
                    if (cashRegister.highlightFrames > 0) {
                        copy(
                            red = min(red * (1f + 4f * cashRegister.highlightFrames / DrawableCashRegister.HIGHLIGHT_FRAMES_MAX), 1f),
                            green = min(green * (1f + 4f * cashRegister.highlightFrames / DrawableCashRegister.HIGHLIGHT_FRAMES_MAX), 1f),
                            blue = min(blue * (1f + 4f * cashRegister.highlightFrames / DrawableCashRegister.HIGHLIGHT_FRAMES_MAX), 1f),
                        )
                    } else this
                }
        )

        if (cashRegister.highlightFrames > 0) cashRegister.highlightFrames--
        cashRegister.clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, rectWidth * 3f)
        cashRegister.textXY = xy.toClientExitXY(canvasSize.width, canvasSize.height, rectWidth)

        drawCashRegisterText(
            textMeasurer = textMeasurer,
            text = "${if (cashRegister.id == 0) "R" else ""}CR${cashRegister.id}",
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            xy = cashRegister.textXY,
            color = if (cashRegister.id == 0) Color.White else Color.Black
        )

        drawClientsQueue(
            xy = cashRegister.clientQueueXY,
            clients = cashRegister.clients
        )
    }

    exits.forEachIndexed { index, exit ->
        val xy = drawRectangleOnRoll(
            rollPointer = exit.roll.withOffset(drawRollLength, drawingOffset),
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = exitColor
        )

        exit.clientExitXY = xy.toClientExitXY(canvasSize.width, canvasSize.height, rectWidth)

        drawCashRegisterText(
            textMeasurer = textMeasurer,
            text = "E${index + 1}",
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            xy = exit.clientExitXY,
            color = Color.White
        )
    }
}

private fun DrawScope.drawRectangleOnRoll(
    rollPointer: Float,
    entityWidth: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    color: Color
): Offset {
    val rectWidth = 48f

//    drawDebugRectangles(rectWidth, entityWidth, canvasWidth, canvasHeight)

    return when (rollPointer) {
        in 0f..entityWidth / 2 -> {
            val secondPart = entityWidth / 2 - rollPointer
            val offset = Offset(0f, 0f)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rollPointer + entityWidth / 2, rectWidth)
            )
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, secondPart)
            )
            return offset
        }

        in entityWidth / 2..canvasWidth - entityWidth / 2 -> {
            val offset = Offset(rollPointer, 0f)
            drawRect(
                color = color,
                topLeft = offset.plus(Offset( - entityWidth / 2, 0f)),
                size = Size(entityWidth, rectWidth)
            )
            return offset
        }

        //-------------------------------------------------------------------------

        in canvasWidth - entityWidth / 2..canvasWidth + entityWidth / 2 -> {
            val secondPart = entityWidth / 2 - (canvasWidth - rollPointer)
            val offset = Offset(canvasWidth, 0f)
            drawRect(
                color = color,
                topLeft = Offset(rollPointer - entityWidth / 2, 0f),
                size = Size(entityWidth, rectWidth)
            )
            drawRect(
                color = color,
                topLeft = Offset(canvasWidth - rectWidth, 0f),
                size = Size(rectWidth, secondPart)
            )
            return offset
        }

        in canvasWidth + entityWidth / 2..canvasWidth + canvasHeight - entityWidth / 2 -> {
            val offset = Offset(canvasWidth, rollPointer - canvasWidth)
            drawRect(
                color = color,
                topLeft = offset.plus(Offset(-rectWidth, -entityWidth / 2)),
                size = Size(rectWidth, entityWidth)
            )
            return offset
        }

        //-------------------------------------------------------------------------

        in canvasWidth + canvasHeight - entityWidth / 2..canvasWidth + canvasHeight + entityWidth / 2 -> {
            val secondPart = entityWidth / 2 - (canvasWidth + canvasHeight - rollPointer)
            val offset = Offset(canvasWidth, canvasHeight)
            drawRect(
                color = color,
                topLeft = Offset(canvasWidth - rectWidth,  rollPointer - canvasWidth - entityWidth / 2),
                size = Size(rectWidth, entityWidth)
            )
            drawRect(
                color = color,
                topLeft = Offset(canvasWidth - secondPart, canvasHeight - rectWidth),
                size = Size(secondPart + rectWidth, rectWidth)
            )
            return offset
        }

        in canvasWidth + canvasHeight + entityWidth / 2..2 * canvasWidth + canvasHeight - entityWidth / 2 -> {
            val offset = Offset(2 * canvasWidth + canvasHeight - rollPointer, canvasHeight)
            drawRect(
                color = color,
                topLeft = offset.plus(Offset(-entityWidth / 2, -rectWidth)),
                size = Size(entityWidth, rectWidth)
            )
            return offset
        }

        //-------------------------------------------------------------------------

        in 2 * canvasWidth + canvasHeight - entityWidth / 2..2 * canvasWidth + canvasHeight + entityWidth / 2 -> {
            val secondPart = entityWidth / 2 - (2 * canvasWidth + canvasHeight - rollPointer)
            val offset = Offset(0f, canvasHeight)
            drawRect(
                color = color,
                topLeft = Offset(0f, canvasHeight - secondPart),
                size = Size(rectWidth, secondPart + rectWidth)
            )
            drawRect(
                color = color,
                topLeft = Offset(0f, canvasHeight - rectWidth),
                size = Size(entityWidth - secondPart, rectWidth)
            )
            return offset
        }

        in 2 * canvasWidth + canvasHeight + entityWidth / 2..2 * canvasWidth + 2 * canvasHeight - entityWidth / 2 -> {
            val offset = Offset(0f, 2 * canvasWidth + 2 * canvasHeight - rollPointer)
            drawRect(
                color = color,
                topLeft = offset.plus(Offset(0f, -entityWidth / 2)),
                size = Size(rectWidth, entityWidth)
            )
            return offset
        }

        //-------------------------------------------------------------------------

        in 2 * canvasWidth + 2 * canvasHeight - entityWidth / 2..2 * canvasWidth + 2 * canvasHeight + entityWidth / 2 -> {
            val secondPart = entityWidth / 2 - (2 * canvasHeight + 2 * canvasWidth - rollPointer)
            val offset = Offset(0f, 0f)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(secondPart, rectWidth)
            )
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, entityWidth - secondPart)
            )
            return offset
        }

        else -> Offset.Zero
    }
}

private fun DrawScope.drawCashRegisterText(
    textMeasurer: TextMeasurer,
    xy: Offset,
    canvasWidth: Float,
    canvasHeight: Float,
    text: String,
    color: Color
) {
    val layoutResult = textMeasurer.measure(text)
    drawText(
        textLayoutResult = layoutResult,
        topLeft = xy
            .plus(Offset(-layoutResult.size.width / 2f, -layoutResult.size.height / 2f))
            .plus(xy.defineCashRegisterTextOffset(canvasWidth, canvasHeight)),
        color = color
    )
}

private fun Offset.defineCashRegisterTextOffset(width: Float, height: Float): Offset {
    val part = 8
    return when {
        (x in 0f..width / part) && (y in 0f..height / part) -> Offset(8f, 0f)
        (x in width * (part - 1) / part..width) && (y in 0f..height / part) -> Offset(-8f, 0f)
        (x in width * (part - 1) / part..width) && (y in height * (part - 1) / part..height) -> Offset(-8f, 0f)
        (x in 0f..width / part) && (y in height * (part - 1) / part..height) -> Offset(8f, 0f)
        else -> Offset.Zero
    }
}

private fun DrawScope.drawDebugRectangles(
    rectWidth: Float,
    entityWidth: Float,
    canvasWidth: Float,
    canvasHeight: Float
) {
    try {
        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(0f, 0f),
            size = Size(entityWidth / 2, rectWidth)
        )
        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(0f, 0f),
            size = Size(rectWidth, entityWidth / 2)
        )

        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(canvasWidth - entityWidth / 2, 0f),
            size = Size(entityWidth / 2, rectWidth)
        )
        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(canvasWidth - rectWidth, 0f),
            size = Size(rectWidth, entityWidth / 2)
        )

        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(canvasWidth - rectWidth, canvasHeight - entityWidth / 2),
            size = Size(rectWidth, entityWidth / 2)
        )
        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(canvasWidth - entityWidth / 2, canvasHeight - rectWidth),
            size = Size(entityWidth / 2, rectWidth)
        )

        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(0f, canvasHeight - rectWidth),
            size = Size(entityWidth / 2, rectWidth)
        )
        drawRect(
            color = Color(0x09FF0088),
            topLeft = Offset(0f, canvasHeight - entityWidth / 2),
            size = Size(rectWidth, entityWidth / 2)
        )
    } catch (_: Exception) { }
}