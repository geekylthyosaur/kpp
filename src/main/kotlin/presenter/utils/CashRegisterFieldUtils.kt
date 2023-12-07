package presenter.utils

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import domain.model.drawable.*
import presenter.drawableComponents.*
import kotlin.math.*
import kotlin.random.*

const val entityToWholeCoefficient = 0.6f

fun DrawScope.drawCashRegistersAndExits(
    canvasSize: IntSize,
    cashRegisters: MutableList<DrawableCashRegister>,
    exits: MutableList<DrawableExit>,
    clients: MutableList<DrawableClient>,
    drawingOffset: Int
) {
    val random = Random(System.currentTimeMillis())
    val drawRollLength = (canvasSize.width + canvasSize.height) * 2
    val defaultOffset = (drawRollLength * 0.8f).roundToInt()

    val wholeWidth = drawRollLength / (cashRegisters.size + exits.size)
    val entityWidth = (wholeWidth * entityToWholeCoefficient).roundToInt()
    val rectWidth = 48f

    var rollPointer = (drawingOffset % wholeWidth)
    var c = 0

    while (rollPointer < drawRollLength && c < cashRegisters.size + exits.size) {
        val xy = drawRectangleOnRoll(
            rollPointer = rollPointer.withOffset(drawRollLength, defaultOffset + drawingOffset),
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = if (c >= cashRegisters.size) Color.Gray else Color.Green
        )

        if (xy.isValid) {
            drawCircle(
                color = Color.Red,
                radius = 10f,
                center = xy
            )
        }

        if (c < cashRegisters.size + exits.size) {
            if (c >= cashRegisters.size) {
                exits[c - cashRegisters.size].xy = xy
                exits[c - cashRegisters.size].roll = rollPointer
                exits[c - cashRegisters.size].clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, rectWidth, entityWidth)
            } else {
                cashRegisters[c].xy = xy
                cashRegisters[c].roll = rollPointer
                cashRegisters[c].clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, rectWidth, entityWidth)
            }
        }

        rollPointer += wholeWidth
        c++
    }

    clients.forEach {
        it.xy = exits.random(random).clientQueueXY
    }
}

fun DrawScope.updateCashRegistersAndExits(
    canvasSize: IntSize,
    cashRegisters: MutableList<DrawableCashRegister>,
    exits: MutableList<DrawableExit>,
    clients: MutableList<DrawableClient>,
    drawingOffset: Int
) {
    val drawRollLength = (canvasSize.width + canvasSize.height) * 2

    val wholeWidth = drawRollLength / (cashRegisters.size + exits.size)
    val entityWidth = (wholeWidth * entityToWholeCoefficient).roundToInt()

    //TODO do with offset / wholeWidth

    cashRegisters.forEach {
//        println("xy: ${it.xy.toRollPointer(canvasSize.width, canvasSize.height, 64f)}; roll: ${it.roll} ")
        val xy = drawRectangleOnRoll(
            rollPointer = it.xy.toRollPointer(canvasSize.width, canvasSize.height, 64f).withOffset(drawRollLength, drawingOffset),
//            rollPointer = it.roll,
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = Color.Green
        )

        drawCircle(
            color = Color.Red,
            radius = 10f,
            center = xy
        )

        it.clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, 64f, entityWidth)

        drawCircle(
            color = Color.Blue,
            radius = 10f,
            center = it.clientQueueXY
        )
        drawClientsQueue(
            xy = it.clientQueueXY,
            clients = it.clients
        )
    }

    exits.forEach {
//        println("xy: ${it.xy.toRollPointer(canvasSize.width, canvasSize.height, 64f)}; roll: ${it.roll} ")
        val xy = drawRectangleOnRoll(
            rollPointer = it.xy.toRollPointer(canvasSize.width, canvasSize.height, 64f).withOffset(drawRollLength, drawingOffset),
//            rollPointer = it.roll,
            entityWidth = entityWidth,
            canvasWidth = canvasSize.width,
            canvasHeight = canvasSize.height,
            color = Color.Gray
        )

        drawCircle(
            color = Color.Red,
            radius = 10f,
            center = xy
        )

        it.clientQueueXY = xy.toClientQueueXY(canvasSize.width, canvasSize.height, 64f, entityWidth)

        drawCircle(
            color = Color.Blue,
            radius = 10f,
            center = it.clientQueueXY
        )
    }

    //TODO return list of rectangles (exits and cash registers)
    //TODO gen clients and move them
}

//TODO return drawnRectangle
private fun DrawScope.drawRectangleOnRoll(
    rollPointer: Int,
    entityWidth: Int,
    canvasWidth: Int,
    canvasHeight: Int,
    color: Color
): Offset {
//    drawRect(
//        color = Color(0x090000FF),
//        topLeft = Offset(0f, 0f),
//        size = Size(canvasWidth - entityWidth.toFloat(), 64f)
//    )
//    drawRect(
//        color = Color(0x09FF0088),
//        topLeft = Offset(canvasWidth - entityWidth.toFloat(), 0f),
//        size = Size(entityWidth.toFloat(), 64f)
//    )
//
//    drawRect(
//        color = Color(0x090000FF),
//        topLeft = Offset(canvasWidth - 64f, 64f),
//        size = Size(64f, canvasHeight - entityWidth.toFloat())
//    )
//    drawRect(
//        color = Color(0x09FF0088),
//        topLeft = Offset(canvasWidth - 64f, canvasHeight - entityWidth.toFloat()),
//        size = Size(64f, entityWidth.toFloat())
//    )
//
//    drawRect(
//        color = Color(0x090000FF),
//        topLeft = Offset(entityWidth.toFloat(), canvasHeight - 64f),
//        size = Size(canvasWidth - entityWidth.toFloat() - 64f, 64f)
//    )
//    drawRect(
//        color = Color(0x09FF0088),
//        topLeft = Offset(0f, canvasHeight - 64f),
//        size = Size(entityWidth.toFloat(), 64f)
//    )
//
//    drawRect(
//        color = Color(0x090000FF),
//        topLeft = Offset(0f, entityWidth.toFloat()),
//        size = Size(64f, canvasHeight - entityWidth.toFloat() - 64f)
//    )
//    drawRect(
//        color = Color(0x09FF0088),
//        topLeft = Offset(0f, 0f),
//        size = Size(64f, entityWidth.toFloat())
//    )

    val rectWidth = 48f

    return when (rollPointer) {
        in 0..canvasWidth - entityWidth -> {
            val offset = Offset(rollPointer.toFloat(), 0f)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(entityWidth.toFloat(), rectWidth)
            )
            return offset
        }
        in canvasWidth - entityWidth..canvasWidth -> {
            val offset = Offset(rollPointer.toFloat(), 0f)
            val firstPart = canvasWidth - rollPointer
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(firstPart.toFloat(), rectWidth)
            )
            drawRect(
                color = color,
                topLeft = Offset(canvasWidth - rectWidth, 0f),
                size = Size(rectWidth, entityWidth.toFloat() - firstPart)
            )
            return offset
        }

        in canvasWidth..canvasWidth + canvasHeight - entityWidth -> {
            val offset = Offset(canvasWidth - rectWidth, rollPointer.toFloat() - canvasWidth)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, entityWidth.toFloat())
            )
            return offset
        }
        in canvasWidth + canvasHeight - entityWidth..canvasWidth + canvasHeight -> {
            val offset = Offset(canvasWidth - rectWidth, rollPointer.toFloat() - canvasWidth)
            val firstPart = canvasWidth + canvasHeight - rollPointer
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, firstPart.toFloat())
            )
            drawRect(
                color = color,
                topLeft = Offset(canvasWidth.toFloat() - entityWidth.toFloat() + firstPart, canvasHeight - rectWidth),
                size = Size(entityWidth.toFloat() - firstPart, rectWidth)
            )
            return offset
        }

        in canvasWidth + canvasHeight..2 * canvasWidth + canvasHeight - entityWidth -> {
            val offset = Offset(2 * canvasWidth.toFloat() + canvasHeight - rollPointer - entityWidth, canvasHeight - rectWidth)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(entityWidth.toFloat(), rectWidth)
            )
            return offset
        }
        in 2 * canvasWidth + canvasHeight - entityWidth..2 * canvasWidth + canvasHeight -> {
            val offset = Offset(2 * canvasWidth.toFloat() + canvasHeight - rollPointer - entityWidth, canvasHeight - rectWidth)
            val firstPart = 2 * canvasWidth + canvasHeight - rollPointer
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(firstPart.toFloat(), rectWidth)
            )
            drawRect(
                color = color,
                topLeft = Offset(0f, canvasHeight.toFloat() - entityWidth + firstPart),
                size = Size(rectWidth, entityWidth.toFloat() - firstPart)
            )
            return offset
        }

        in 2 * canvasWidth + canvasHeight..2 * canvasWidth + 2 * canvasHeight - entityWidth -> {
            val offset = Offset(0f, 2 * canvasWidth.toFloat() + 2 * canvasHeight - rollPointer - entityWidth)
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, entityWidth.toFloat())
            )
            return offset
        }
        in 2 * canvasWidth + 2 * canvasHeight - entityWidth..2 * canvasWidth + 2 * canvasHeight -> {
            val offset = Offset(0f, 2 * canvasWidth.toFloat() + 2 * canvasHeight - rollPointer - entityWidth)
            val firstPart = 2 * canvasWidth + 2 * canvasHeight - rollPointer
            drawRect(
                color = color,
                topLeft = offset,
                size = Size(rectWidth, firstPart.toFloat())
            )
            drawRect(
                color = color,
                topLeft = Offset(0f, 0f),
                size = Size(entityWidth.toFloat() - firstPart, rectWidth)
            )
            return offset
        }

        else -> Offset(-1f, -1f)
    }
}