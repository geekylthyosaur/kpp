package presenter.drawableComponents

import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.*
import domain.curvature.*
import domain.model.client.*
import domain.model.drawable.*
import domain.model.log.*
import domain.serverApi.*
import kotlinx.coroutines.*
import presenter.*
import presenter.utils.*
import kotlin.random.*

@Composable
fun StationField(
    modifier: Modifier,
    drawingOffset: Float,
    cashRegistersCount: Int,
    exitsCount: Int,
    serverApi: ServerApi,
    onLogAdded: (ServedClientLog) -> Unit,
    isSimulationStarted: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val textMeasurer = rememberTextMeasurer()

    val clientsLock = Any()
    val disLock = Any()

    val clients = remember {
        mutableStateListOf<DrawableClient>()
    }

    var internalCounter by remember {
        mutableIntStateOf(0)
    }

    var isEntitiesInitialized by remember(cashRegistersCount, exitsCount) {
        mutableStateOf(false)
    }

    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }

    var clientsMovingJob by remember {
        mutableStateOf<Job?>(null)
    }

    val cashRegisters = remember(cashRegistersCount) {
        mutableStateListOf<DrawableCashRegister>().apply {
            addAll(List(cashRegistersCount + 1) {
                DrawableCashRegister(
                    id = it,
                    xy = Offset.Zero,
                    clientQueueXY = Offset.Zero,
                    textXY = Offset.Zero,
                    roll = 0f,
                    clients = mutableListOf(),
                    clientsMovingToIt = 0,
                    highlightFrames = 0
                )
            })
        }
    }

    val exits = remember(exitsCount) {
        mutableStateListOf<DrawableExit>().apply {
            addAll(List(exitsCount) {
                DrawableExit(
                    xy = Offset.Zero,
                    clientExitXY = Offset.Zero,
                    roll = 0f
                )
            })
        }
    }

    val closedCashRegisterIds = remember {
        mutableStateListOf<Int>()
    }

    val disappearances = remember {
        mutableStateListOf<DrawableDisappearance>()
    }

    LaunchedEffect(key1 = isSimulationStarted, block = {
        if (isSimulationStarted) {
            serverApi.connectServerLogicSockets(
                onClientCreated = { client ->
                    synchronized(clientsLock) {
                        clients.add(client.apply {
                            startXY = exits.random(Random(System.currentTimeMillis())).clientExitXY
                            xy = startXY
                        })
                    }
                },
                onClientServed = { servedClientLog ->
                    val client = clients.find { it.id == servedClientLog.clientId } ?: return@connectServerLogicSockets
                    val clientType = ClientType.fromString(client.clientType)

                    servedClientLog.apply {
                        clientName = client.clientName
                        this.clientType = clientType
                    }
                    onLogAdded(servedClientLog)

                    cashRegisters[servedClientLog.cashRegisterId].highlightFrames = DrawableCashRegister.HIGHLIGHT_FRAMES_MAX
                    val xy = cashRegisters[servedClientLog.cashRegisterId].clientQueueXY.moveToByLinear(
                        to = Offset(
                            canvasSize.width / 2f,
                            canvasSize.height / 2f
                        ),
                        by = 32f
                    )

                    coroutineScope.launch(Dispatchers.Default) {
                        val disappearance = DrawableDisappearance(
                            type = clientType,
                            progress = 1f,
                            xy = xy
                        )
                        synchronized(disLock) {
                            disappearances.add(disappearance)
                        }

                        val maxProgress = 50
                        for (progress in 1..maxProgress) {
                            disappearance.progress = (maxProgress - progress) / maxProgress.toFloat()
                            delay(10)
                        }

                        synchronized(disLock) {
                            disappearances.remove(disappearance)
                        }
                    }

                    synchronized(clientsLock) {
                        clients.remove(client)
                        cashRegisters[servedClientLog.cashRegisterId].clients.remove(client)
                    }
                },
                onCashRegisterClosed = { cashRegisterChange ->
                    synchronized(clientsLock) {
                        closedCashRegisterIds.apply {
                            remove(cashRegisterChange.openedCashRegisterId)
                            if (cashRegisterChange.closedCashRegisterId != 0) add(cashRegisterChange.closedCashRegisterId)
                        }

                        cashRegisters[cashRegisterChange.closedCashRegisterId].clients.forEach {
                            it.lastResult = CurveMovingResult(Offset.Zero, 0f, 0f)
                            it.startXY = it.xy
                            it.isServed = false
                            it.movingTargetIndex = cashRegisterChange.openedCashRegisterId
                            it.cashRegisterId = null
                        }

                        cashRegisters[cashRegisterChange.closedCashRegisterId].clients.clear()
                    }
                }
            )

            clientsMovingJob = coroutineScope.launch(Dispatchers.Default) {
                val canvasCenter = Offset(canvasSize.width / 2, canvasSize.height / 2)
                while (true) {
                    internalCounter++

                    synchronized(clientsLock) {
                        try {
                            DrawableClient.moveClients(canvasCenter, clients, cashRegisters, closedCashRegisterIds) { client, cashRegister ->
                                serverApi.notifyAboutClientService(
                                    ClientService(
                                        clientId = client.id,
                                        clientName = client.clientName,
                                        clientType = client.clientType,
                                        cashRegisterId = client.cashRegisterId ?: cashRegister.id,
                                        desiredTicketsCount = client.desiredTicketsCount
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            println("Error moving clients: ${e.message}}")
                        }
                    }

                    delay(FRAME_TIME)
                }
            }
        } else {
            clientsMovingJob?.cancel()
            clientsMovingJob = null

            synchronized(clientsLock) {
                clients.clear()
                cashRegisters.forEach {
                    it.clients.clear()
                    it.clientsMovingToIt = 0
                }
            }

            synchronized(disLock) {
                disappearances.clear()
            }
        }
    })

    LaunchedEffect(key1 = cashRegistersCount, key2 = exitsCount, block = {
        isEntitiesInitialized = false
    })

    LaunchedEffect(key1 = isSimulationStarted, block = {
        if (!isSimulationStarted) isEntitiesInitialized = false
    })

    Canvas(
        modifier = modifier
            .onSizeChanged { newSize ->
                canvasSize = Size(newSize.width.toFloat(), newSize.height.toFloat())
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
                textMeasurer = textMeasurer,
                canvasSize = canvasSize,
                cashRegisters = cashRegisters,
                closedCashRegisterIds = closedCashRegisterIds,
                exits = exits,
                drawingOffset = drawingOffset,
                internalCounter = internalCounter
            )

            isEntitiesInitialized = true
        } else {
            updateCashRegistersAndExits(
                textMeasurer = textMeasurer,
                canvasSize = canvasSize,
                cashRegisters = cashRegisters,
                closedCashRegisterIds = closedCashRegisterIds,
                exits = exits,
                drawingOffset = drawingOffset,
                internalCounter = internalCounter
            )

            synchronized(clientsLock) {
                drawClients(clients)
            }

            drawAllDisappearances(
                disappearances = disappearances,
                internalCounter = internalCounter
            )
        }
    }
}