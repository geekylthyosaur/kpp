package domain.model.client

import kotlinx.serialization.*

@Serializable
data class ClientService(
    @SerialName("clientId") val clientId: Int,
    @SerialName("clientName") val clientName: String,
    @SerialName("clientType") val clientType: String,
    @SerialName("cashRegisterId") val cashRegisterId: Int,
    @SerialName("desiredTicketsCount") val desiredTicketsCount: Int
)
