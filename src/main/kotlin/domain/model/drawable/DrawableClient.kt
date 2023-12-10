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
    @Transient var lastResult: CurveMovingResult = CurveMovingResult(Offset.Zero, 0f, 0f)
) {

    companion object {

        fun moveClients(
            centerXY: Offset,
            clients: List<DrawableClient>,
            cashRegisters: List<DrawableCashRegister>,
            closedCashRegisterIds: List<Int>,
            onServe: (DrawableClient, DrawableCashRegister) -> Unit
        ) {
            //TODO make them push each other

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
//                    if (closedCashRegisterIds.contains(cr.id)) {
//                        cashRegisters[0].clientsMovingToIt++
//                        client.controlXY = client.startXY.calculateControlXY(cashRegisters[0].clientQueueXY, centerXY)
//                        client.movingTargetIndex = 0
//                    } else {
//
//                    }
                } else if (closedCashRegisterIds.contains(client.movingTargetIndex)) {
                    client.startXY = client.xy
                    client.movingTargetIndex = -1
                    client.lastResult = CurveMovingResult(Offset.Zero, 0f, 0f)
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
                        lastResult = CurveMovingResult(Offset.Zero, 0f, 0f)
                    }

                    continue
                }

                if (client.isServed || client.movingTargetIndex < 0) continue

//                client.xy = client.xy.moveToByLinear(cashRegisters[client.movingTargetIndex].clientQueueXY, moveSpeed)
                client.apply {
                    val moveResult = startXY.moveToByCurve(cashRegisters[movingTargetIndex].clientQueueXY, controlXY, lastResult)

                    xy = moveResult.newXY
                    lastResult = moveResult
                }
            }
        }
    }
}