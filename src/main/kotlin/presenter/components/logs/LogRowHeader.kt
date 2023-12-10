package presenter.components.logs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
fun LogRowHeader(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .height(40.dp)
            .background(Color(0x33000000))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(39.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(1 / 10f),
                text = "CL",
                style = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
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
                text = "CR",
                style = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
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
                text = "Start time",
                style = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
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
                text = "End time",
                style = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
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