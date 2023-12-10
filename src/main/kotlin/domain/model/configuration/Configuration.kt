package domain.model.configuration

import kotlinx.serialization.*

@Serializable
data class Configuration(
    @SerialName("cashRegisterCount") val cashRegisterCount: Int,
    @SerialName("entriesCount") val exitsCount: Int,
    @SerialName("minServingTime") val minServingTime: Int,
    @SerialName("maxServingTime") val maxServingTime: Int,
    @SerialName("generationStrategy") val generationStrategy: String,
    @SerialName("maxClientsInsideBuilding") val maxClientsInsideBuilding: Int
)
