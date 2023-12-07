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
import presenter.components.*
import presenter.components.logs.*
import presenter.drawableComponents.*

@Composable
fun MainWindow(
    modifier: Modifier
) {
    var drawingOffset by remember {
        mutableIntStateOf(0)
    }

    Row(
        modifier = modifier
            .background(Color(0xFFCCCCDD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .fillMaxHeight()
        ) {
            UiPanel(
                modifier = Modifier.fillMaxWidth(),
                onExtraOffset = { extraOffset ->
                    drawingOffset += extraOffset
                }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Black,
                thickness = 1.dp
            )
            ManagementPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                onStartSimulation = { cashRegistersCount, exitsCount, minServingTime, maxServingTime, selectedStrategy ->

                }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Black,
                thickness = 1.dp
            )
            LogPanel(
                modifier = Modifier.fillMaxSize(),
                logs = emptyList()
            )
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
                .clip(RoundedCornerShape(1))
        ) {
            CashRegisterField(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEEEEFF)),
                drawingOffset = drawingOffset,
                cashRegistersCount = 12,
                exitsCount = 4,
                clients = emptyList()
            )
        }
    }
}