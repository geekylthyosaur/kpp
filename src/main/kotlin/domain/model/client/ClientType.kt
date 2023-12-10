package domain.model.client

import androidx.compose.ui.graphics.*
import presenter.drawableComponents.*

sealed class ClientType(val stringKey: String) {
    data object Common : ClientType("common")
    data object Disabled : ClientType("disabled")
    data object WithChild : ClientType("with_child")
    data object Military : ClientType("military")

    companion object {
        fun fromString(string: String): ClientType {
            return when (string) {
                Disabled.stringKey -> Disabled
                WithChild.stringKey -> WithChild
                Military.stringKey -> Military
                else -> Common
            }
        }
    }

    fun getColor(): Color {
        return when (this) {
            Common -> defaultClientColor
            Disabled -> disabledClientColor
            Military -> militaryClientColor
            WithChild -> childColor
        }
    }
}