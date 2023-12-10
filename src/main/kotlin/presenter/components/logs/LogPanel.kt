package presenter.components.logs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import domain.model.log.*
import presenter.window.*

@Composable
fun LogPanel(
    modifier: Modifier,
    servedClientLogs: List<ServedClientLog>
) {
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(key1 = servedClientLogs.size, block = {
        if (servedClientLogs.isNotEmpty()) lazyColumnState.scrollToItem(servedClientLogs.size - 1)
    })

    if (lazyColumnState.canScrollForward || lazyColumnState.canScrollBackward) {
        Box(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .size(17.dp, 40.dp)
                    .background(Color(0x33000000))
                    .align(Alignment.TopEnd)
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomCenter),
                    color = Color.Black,
                    thickness = 1.dp
                )
            }
            LogRowHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 17.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, end = 17.dp)
                    .background(lighterBackgroundColor),
                state = lazyColumnState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(servedClientLogs) { log ->
                    LogRow(
                        modifier = Modifier.fillMaxWidth(),
                        servedClientLog = log
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .padding(top = 40.dp)
                    .offset(x = (-16).dp),
                thickness = 1.dp,
                color = Color.Black
            )
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(16.dp)
                    .padding(top = 40.dp)
                    .background(lightBackgroundColor)
                    .padding(all = 4.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(lazyColumnState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = Color.DarkGray,
                    hoverColor = Color.Gray
                )
            )
        }
    } else {
        Box(
            modifier = modifier
        ) {
            LogRowHeader(
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
                    .background(lighterBackgroundColor),
                state = lazyColumnState,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(servedClientLogs) { log ->
                    LogRow(
                        modifier = Modifier.fillMaxWidth(),
                        servedClientLog = log
                    )
                }
            }
        }
    }
}