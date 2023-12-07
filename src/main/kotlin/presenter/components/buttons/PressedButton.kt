package presenter.components.buttons

import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import kotlinx.coroutines.*
import presenter.*

@Composable
fun PressedButton(
    modifier: Modifier,
    onPress: () -> Unit,
    content: @Composable RowScope.() -> Unit
)  {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed by mutableInteractionSource.collectIsPressedAsState()

    LaunchedEffect(key1 = isPressed, block = {
        while (isPressed) {
            onPress()
            delay(FRAME_TIME)
        }
    })

    Button(
        modifier = Modifier,
        interactionSource = mutableInteractionSource,
        onClick = { },
        content = content
    )
}