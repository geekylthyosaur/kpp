package presenter.components.logs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import domain.model.log.*

@Composable
fun LogPanel(
    modifier: Modifier,
    logs: List<Log>
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyColumnState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(logs) { log ->
            LogRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                log = log
            )
        }
    }
}