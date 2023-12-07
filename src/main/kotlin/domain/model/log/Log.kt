package domain.model.log

data class Log(
    val clientId: Int,
    val cashRegisterId: Int,
    val startTime: String,
    val endTime: String
)
