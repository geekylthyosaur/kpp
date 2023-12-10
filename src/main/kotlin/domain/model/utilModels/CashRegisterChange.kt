package domain.model.utilModels

import domain.model.drawable.*
import kotlinx.serialization.*

@Serializable
data class CashRegisterChange(
    @SerialName("closedCashRegisterId") val closedCashRegisterId: Int,
    @SerialName("openedCashRegisterId") val openedCashRegisterId: Int,
    @SerialName("clients") val clients: List<DrawableClient>,
    @Transient val clientIds: MutableList<Int> = mutableListOf()
)
