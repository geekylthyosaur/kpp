package presenter.components.logs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import domain.model.log.*

@Composable
fun LogPanel(
    modifier: Modifier,
    servedClientLogs: List<ServedClientLog>
) {
    val lazyColumnState = rememberLazyListState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 17.dp)
        ) {
            LogRowHeader(
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .offset(x = (-16).dp),
            thickness = 1.dp,
            color = Color.Black
        )
        VerticalScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .width(16.dp)
                .padding(4.dp)
                .align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(lazyColumnState)
        )
    }
}