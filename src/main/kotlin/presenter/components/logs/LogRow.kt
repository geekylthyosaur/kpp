package presenter.components.logs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import domain.model.client.*
import domain.model.log.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogRow(
    modifier: Modifier,
    servedClientLog: ServedClientLog
) {
    val backgroundColor = servedClientLog.clientType.getColor()
    val textColor = when (servedClientLog.clientType) {
        ClientType.Common -> Color.Black
        ClientType.Disabled -> Color.White
        ClientType.Military -> Color.White
        ClientType.WithChild -> Color.White
    }

    TooltipArea(
        modifier = modifier.height(40.dp),
        tooltip = {
            Text(
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(15))
                    .border(1.dp, Color.Black, RoundedCornerShape(15))
                    .padding(6.dp),
                text = servedClientLog.clientName,
                color = textColor
            )
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(
            offset = DpOffset(8.dp, 12.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(39.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(1 / 10f),
                    text = "${servedClientLog.clientId + 1}",
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = textColor
                )
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                    color = Color.Black,
                    thickness = 1.dp
                )
                Text(
                    modifier = Modifier.fillMaxWidth(1 / 9f),
                    text = "${servedClientLog.cashRegisterId}",
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = textColor
                )
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                    color = Color.Black,
                    thickness = 1.dp
                )
                Text(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    text = servedClientLog.startTime.currentTime,
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp
                    ),
                    color = textColor
                )
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                    color = Color.Black,
                    thickness = 1.dp
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = servedClientLog.endTime.currentTime,
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp
                    ),
                    color = textColor
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Black,
                thickness = 1.dp
            )
        }
    }
}