package domain.model.client

import kotlinx.serialization.*

@Serializable
data class ClientService(
    @SerialName("clientId") val clientId: Int,
    @SerialName("cashRegisterId") val cashRegisterId: Int,
    @SerialName("startTime") val startTime: Long,
    @SerialName("endTime") val endTime: Long
)
