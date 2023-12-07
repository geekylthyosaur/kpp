package domain.model.drawable

import androidx.compose.ui.geometry.*
import presenter.utils.*

data class DrawableClient(
    var xy: Offset,
    var isServed: Boolean,
    var movingTargetIndex: Int
) {

    companion object {

        fun moveClients(clients: List<DrawableClient>, cashRegisters: List<DrawableCashRegister>) {
            val moveSpeed = 3f

            for (client in clients) {
                if (client.isServed) continue

                if (client.movingTargetIndex < 0) {
                    val cr = cashRegisters.sortedBy { it.clientQueueXY.measureDistance(client.xy) }.sortedBy { it.clientsMovingToIt }[0]
                    cr.clientsMovingToIt++
                    client.movingTargetIndex = cashRegisters.indexOf(cr)
                } else if (cashRegisters[client.movingTargetIndex].clientQueueXY.measureDistance(client.xy) < 32f) {
                    cashRegisters[client.movingTargetIndex].clients.add(client)
                    cashRegisters[client.movingTargetIndex].clientsMovingToIt--
                    client.isServed = true
                    client.movingTargetIndex = -1
                    continue
                }

                client.xy = client.xy.moveToBy(cashRegisters[client.movingTargetIndex].clientQueueXY, moveSpeed)
            }
        }
    }
}