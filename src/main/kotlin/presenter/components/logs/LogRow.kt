package presenter.components.logs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import domain.model.log.*

@Composable
fun LogRow(
    modifier: Modifier,
    log: Log
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = log.clientId.toString()
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = Color.Black,
                thickness = 1.dp
            )
            Text(
                text = log.cashRegisterId.toString()
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = Color.Black,
                thickness = 1.dp
            )
            Text(
                text = log.startTime
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = Color.Black,
                thickness = 1.dp
            )
            Text(
                text = log.endTime
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = Color.Black,
                thickness = 1.dp
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