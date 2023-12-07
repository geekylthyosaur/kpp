package domain.model.client

sealed class ClientType(val stringKey: String) {
    data object Common : ClientType("common")
    data object Disabled : ClientType("disabled")
    data object WithChild : ClientType("with_child")
    data object Military : ClientType("military")
}