import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import presenter.window.*

const val SCREEN_WIDTH = 1440
const val SCREEN_HEIGHT = 810

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(SCREEN_WIDTH.dp, SCREEN_HEIGHT.dp),
            placement = WindowPlacement.Maximized
        )
    ) {
        MaterialTheme {
            MainWindow(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
