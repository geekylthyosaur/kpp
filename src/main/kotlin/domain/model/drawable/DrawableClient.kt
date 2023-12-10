@file:Suppress("ReplaceManualRangeWithIndicesCalls")

package domain.model.drawable

import androidx.compose.ui.geometry.*
import domain.curvature.*
import kotlinx.serialization.*
import kotlinx.serialization.Transient
import presenter.utils.*

@Serializable
data class DrawableClient(
    @SerialName("clientId") val id: Int,
    @SerialName("clientName") val clientName: String,
    @SerialName("clientType") val clientType: String,
    @SerialName("cashRegisterId") var cashRegisterId: Int? = null,
    @SerialName("desiredTicketsCount") val desiredTicketsCount: Int,
    @Transient var isServed: Boolean = false,
    @Transient var movingTargetIndex: Int = -1,
    @Transient var servedAt: Int = -1,
    @Transient var startXY: Offset = Offset.Zero,
    @Transient var xy: Offset = Offset.Zero,
    @Transient var controlXY: Offset = Offset.Zero,
    @Transient var lastResult: CurveMovingResult = CurveMovingResult.new()
) {

    companion object {

        fun moveClients(
            centerXY: Offset,
            clients: List<DrawableClient>,
            cashRegisters: List<DrawableCashRegister>,
            closedCashRegisterIds: List<Int>,
            onServe: (DrawableClient, DrawableCashRegister) -> Unit
        ) {
            for (index in 0 until clients.size) {
                val client = clients[index]

                if (client.movingTargetIndex < 0 || (!client.isServed && client.controlXY == Offset.Zero)) {
                    if (closedCashRegisterIds.size == cashRegisters.size - 1) {
                        cashRegisters[0].clientsMovingToIt++
                        client.controlXY = client.startXY.calculateControlXY(cashRegisters[0].clientQueueXY, centerXY)
                        client.movingTargetIndex = 0

                        continue
                    }

                    val cr = cashRegisters.asSequence()
                        .drop(1)
                        .filterNot { closedCashRegisterIds.contains(it.id) }
                        .sortedBy {
                            it.clientQueueXY.measureDistance(client.xy)
                        }.sortedBy {
                            it.clientsMovingToIt
                        }.sortedBy {
                            it.clients.size
                        }.toList()[0]

                    cr.clientsMovingToIt++
                    client.controlXY = client.startXY.calculateControlXY(cr.clientQueueXY, centerXY)
                    client.movingTargetIndex = cashRegisters.indexOf(cr)
                } else if (closedCashRegisterIds.contains(client.movingTargetIndex)) {
                    client.startXY = client.xy
                    client.movingTargetIndex = -1
                    client.lastResult = CurveMovingResult.new()
                    client.controlXY = Offset.Zero
                } else if (!client.isServed && cashRegisters[client.movingTargetIndex].clientQueueXY.measureDistance(client.xy) < 24f) {
                    cashRegisters[client.movingTargetIndex].clients.add(client)
                    cashRegisters[client.movingTargetIndex].clientsMovingToIt--

                    client.apply {
                        onServe(this, cashRegisters[movingTargetIndex])
                        isServed = true
                        cashRegisterId = movingTargetIndex
                        movingTargetIndex = -1
                        controlXY = Offset.Zero
                        lastResult = CurveMovingResult.new()
                    }

                    continue
                }

                if (client.isServed || client.movingTargetIndex < 0) continue

                client.apply {
                    val moveResult = startXY.moveToByCurve(cashRegisters[movingTargetIndex].clientQueueXY, controlXY, lastResult)

                    xy = moveResult.newXY
                    lastResult = moveResult
                }

                for (j in 0 until clients.size) {
                    if (j != index && !clients[j].isServed && clients[j].xy.measureDistance(client.xy) < 32f) pushAwayClients(clients[j], client, centerXY, cashRegisters)
                }
            }
        }

        private fun pushAwayClients(first: DrawableClient, second: DrawableClient, centerXY: Offset, cashRegisters: List<DrawableCashRegister>) {
            val pushNormal = first.xy != second.xy
            val pushDistance = 4f

            first.apply {
                xy = if (pushNormal) xy.moveToByLinear(second.xy, -pushDistance) else xy.moveToRandomByLinear(pushDistance)
                startXY = xy
                controlXY = startXY.calculateControlXY(cashRegisters[movingTargetIndex].clientQueueXY, centerXY)
                lastResult = CurveMovingResult.new()
            }
            second.apply {
                xy = if (pushNormal) xy.moveToByLinear(first.xy, -pushDistance) else xy.moveToRandomByLinear(pushDistance)
                startXY = xy
                controlXY = startXY.calculateControlXY(cashRegisters[movingTargetIndex].clientQueueXY, centerXY)
                lastResult = CurveMovingResult.new()
            }
        }
    }
}