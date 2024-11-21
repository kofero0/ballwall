package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface SplashComponent : Component<SplashComponent.State, SplashComponent.SplashError> {
    sealed interface SplashError : Component.ComponentError

    data class State(
        override val loading: Boolean = false, override val error: SplashError? = null
    ) : Component.UIState<SplashError>

    fun interface Builder {
        fun build(componentContext: ComponentContext): SplashComponent
    }
}

class SplashComponentImpl(componentContext: ComponentContext) : SplashComponent,
    ComponentContext by componentContext {
    override fun onStart() {
    }

    private val _state = MutableValue(SplashComponent.State())
    override val state: Value<SplashComponent.State> = _state

}