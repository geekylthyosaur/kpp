package presenter.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import presenter.components.buttons.*

@Composable
fun UiPanel(
    modifier: Modifier,
    onExtraOffset: (Int) -> Unit
) {
    val offsetChangeSpeed = 10

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Змінити розташування кас: "
        )
        PressedButton(
            modifier = Modifier,
            onPress = {
                onExtraOffset(-offsetChangeSpeed)
            }
        ) {
            Text(
                text = "-"
            )
        }
        PressedButton(
            modifier = Modifier,
            onPress = {
                onExtraOffset(offsetChangeSpeed)
            }
        ) {
            Text(
                text = "+"
            )
        }
    }
}