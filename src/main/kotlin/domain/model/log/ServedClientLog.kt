package domain.model.log

import domain.model.client.*
import domain.model.utilModels.*
import kotlinx.serialization.*

@Serializable
data class ServedClientLog(
    @SerialName("clientId") val clientId: Int,
    @SerialName("cashRegisterId") val cashRegisterId: Int,
    @SerialName("startTime") val startTime: Time,
    @SerialName("endTime") val endTime: Time,
    @Transient var clientName: String = "",
    @Transient var clientType: ClientType = ClientType.Common,
)
