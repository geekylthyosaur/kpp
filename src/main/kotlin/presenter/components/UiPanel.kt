package presenter.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import domain.model.log.*
import presenter.components.buttons.*
import presenter.components.logs.*

@Composable
fun UiPanel(
    modifier: Modifier,
    onExtraOffset: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Змінити розташування кас"
        )
        PressedButton(
            modifier = Modifier,
            onPress = {
                onExtraOffset(-5)
            }
        ) {
            Text(
                text = "-"
            )
        }
        PressedButton(
            modifier = Modifier,
            onPress = {
                onExtraOffset(5)
            }
        ) {
            Text(
                text = "+"
            )
        }
    }
}