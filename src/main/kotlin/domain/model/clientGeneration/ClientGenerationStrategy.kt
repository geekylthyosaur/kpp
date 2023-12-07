package domain.model.clientGeneration

sealed class ClientGenerationStrategy(val stringKey: String) {
    data object Random : ClientGenerationStrategy("random_time_generation")
    data object Fixed : ClientGenerationStrategy("fixed_time_generation")
    data object Sin : ClientGenerationStrategy("sin_time_generation")

    companion object {
        fun getDefaultList() = listOf(Random, Fixed, Sin)
    }
}