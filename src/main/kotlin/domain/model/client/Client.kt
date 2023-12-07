package domain.model.client

import kotlinx.serialization.*

@Serializable
data class Client(
    @SerialName("clientId") val clientId: Int,
    @SerialName("clientName") val clientName: String,
    @SerialName("desiredTicketsCount") val desiredTicketsCount: Int,
    @SerialName("clientType") val clientType: String,
    @SerialName("cashRegisterId") val cashRegisterId: Int? = null
)
