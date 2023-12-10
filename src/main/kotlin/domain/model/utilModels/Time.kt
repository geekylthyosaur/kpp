package domain.model.utilModels

import kotlinx.serialization.*

@Serializable
data class Time(
    @SerialName("currentTime") val currentTime: String
)
