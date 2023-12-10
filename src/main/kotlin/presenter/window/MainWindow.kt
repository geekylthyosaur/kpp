package presenter.window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import domain.model.configuration.*
import domain.model.log.*
import domain.serverApi.*
import presenter.components.*
import presenter.components.logs.*
import presenter.drawableComponents.*

val backgroundColor = Color(0xFFAAAABB)
val lighterBackgroundColor = Color(0xFFCCCCDD)
val lightBackgroundColor = Color(0xFFEEEEFF)

@Composable
fun MainWindow(
    modifier: Modifier,
    serverApi: ServerApi
) {
    var isSimulationStarted by remember {
        mutableStateOf(false)
    }

    var cashRegistersCount by remember {
        mutableIntStateOf(1)
    }

    var exitsCount by remember {
        mutableIntStateOf(1)
    }

    var drawingOffset by remember {
        mutableIntStateOf(0)
    }

    val servedClientLogs = remember {
        mutableStateListOf<ServedClientLog>()
    }

    Row(
        modifier = modifier.background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.275f)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4f))
                    .background(lighterBackgroundColor)
                    .border(1.dp, Color.Black, RoundedCornerShape(4f)),
                contentAlignment = Alignment.Center
            ) {
                UiPanel(
                    modifier = Modifier.fillMaxWidth(),
                    onExtraOffset = { extraOffset ->
                        drawingOffset += extraOffset
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Black,
                thickness = 1.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(384.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4f))
                    .background(lighterBackgroundColor)
                    .border(1.dp, Color.Black, RoundedCornerShape(4f)),
                contentAlignment = Alignment.Center
            ) {
                ManagementPanel(
                    modifier = Modifier.fillMaxSize(),
                    isSimulationStarted = isSimulationStarted,
                    onIsSimulationStartedChange = { newValue ->
                        isSimulationStarted = newValue
                    },
                    onCashRegistersCountChange = { iCashRegistersCount ->
                        if (!isSimulationStarted) {
                            cashRegistersCount = iCashRegistersCount
                        }
                    },
                    onExitsCountChange = { iExitsCount ->
                        if (!isSimulationStarted) {
                            exitsCount = iExitsCount
                        }
                    },
                    onStartSimulation = { minServingTime, maxServingTime, selectedStrategy ->
                        serverApi.sendConfiguration(
                            Configuration(
                                cashRegisterCount = cashRegistersCount + 1,
                                exitsCount = exitsCount,
                                minServingTime = minServingTime,
                                maxServingTime = maxServingTime,
                                generationStrategy = selectedStrategy.stringKey,
                                maxClientsInsideBuilding = 200
                            )
                        )
                    },
                    onStopSimulation = {
                        serverApi.notifyAboutSimulationStop()
                        servedClientLogs.clear()
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Black,
                thickness = 1.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4f))
                    .background(lighterBackgroundColor)
                    .border(1.dp, Color.Black, RoundedCornerShape(4f)),
                contentAlignment = Alignment.Center
            ) {
                LogPanel(
                    modifier = Modifier.fillMaxSize(),
                    servedClientLogs = servedClientLogs
                )
            }
        }
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = Color.Black,
            thickness = 1.dp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(4f))
                .background(lighterBackgroundColor)
                .border(1.dp, Color.Black, RoundedCornerShape(4f)),
            contentAlignment = Alignment.Center
        ) {
            StationField(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightBackgroundColor),
                drawingOffset = drawingOffset.toFloat(),
                cashRegistersCount = cashRegistersCount,
                exitsCount = exitsCount,
                serverApi = serverApi,
                onLogAdded = { log ->
                    servedClientLogs.add(log)
                },
                isSimulationStarted = isSimulationStarted
            )
        }
    }
}