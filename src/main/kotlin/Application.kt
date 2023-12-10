import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import domain.serverApi.*
import presenter.window.*

const val SCREEN_WIDTH = 1440
const val SCREEN_HEIGHT = 810

fun main() = application {
    val serverApi = ServerApi()

    Window(
        onCloseRequest = {
            serverApi.disconnect()
            exitApplication()
        },
        state = WindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(SCREEN_WIDTH.dp, SCREEN_HEIGHT.dp),
            placement = WindowPlacement.Maximized
        )
    ) {
        serverApi.connect()

        MaterialTheme {
            MainWindow(
                modifier = Modifier.fillMaxSize(),
                serverApi = serverApi
            )
        }
    }
}
