package presenter.drawableComponents

import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import domain.model.client.*
import domain.model.drawable.*
import kotlinx.coroutines.*
import presenter.*
import presenter.utils.*

@Composable
fun CashRegisterField(
    modifier: Modifier,
    drawingOffset: Int,
    cashRegistersCount: Int,
    exitsCount: Int,
    clients: List<Client>
) {
    var internalCounter by remember {
        mutableIntStateOf(0)
    }

    var isEntitiesInitialized by remember {
        mutableStateOf(false)
    }

    var canvasSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val cashRegisters = remember {
        mutableStateListOf<DrawableCashRegister>().apply {
            addAll(List(10) {
                DrawableCashRegister(
                    xy = Offset.Zero,
                    clientQueueXY = Offset.Zero,
                    roll = 0,
                    clients = mutableListOf(),
                    clientsMovingToIt = 0
                )
            })
        }
    }

    val exits = remember {
        mutableStateListOf<DrawableExit>().apply {
            addAll(List(3) {
                DrawableExit(
                    xy = Offset.Zero,
                    clientQueueXY = Offset.Zero,
                    roll = 0
                )
            })
        }
    }

    val clients = remember {
        mutableStateListOf<DrawableClient>().apply {
            addAll(List(50) {
                DrawableClient(
                    xy = Offset.Zero,
                    isServed = false,
                    movingTargetIndex = -1
                )
            })
        }
    }

    LaunchedEffect(key1 = true, block = {
        delay(100)
        while (drawingOffset < 5000) {
            internalCounter++
            DrawableClient.moveClients(clients, cashRegisters)
            delay(FRAME_TIME)
        }
    })

    Canvas(
        modifier = modifier
            .onSizeChanged { newSize ->
                canvasSize = newSize
            }
    ) {
        if (!isEntitiesInitialized) {
            drawCashRegistersAndExits(
                canvasSize = canvasSize,
                cashRegisters = cashRegisters,
                exits = exits,
                clients = clients,
                drawingOffset = drawingOffset
            )
            updateCashRegistersAndExits(
                canvasSize = canvasSize,
                cashRegisters = cashRegisters,
                exits = exits,
                clients = clients,
                drawingOffset = drawingOffset
            )

            isEntitiesInitialized = true
        } else {
            with(internalCounter) {
                updateCashRegistersAndExits(
                    canvasSize = canvasSize,
                    cashRegisters = cashRegisters,
                    exits = exits,
                    clients = clients,
                    drawingOffset = drawingOffset
                )

                drawClients(clients)
            }
        }
    }
}