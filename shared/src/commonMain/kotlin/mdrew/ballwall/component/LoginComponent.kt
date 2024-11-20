package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface LoginComponent: ActionComponent<LoginComponent.Action,LoginComponent.State,LoginComponent.LoginError>{
    sealed interface Action: ActionComponent.Action{
        data object Login:Action
        data object SignUp:Action
        data object Guest:Action
    }

    sealed interface LoginError: Component.ComponentError

    data class State(override val loading: Boolean = false, override val error: LoginError? = null): Component.UIState<LoginError>

    fun interface Builder{
        fun build(componentContext: ComponentContext): LoginComponent
    }
}

class LoginComponentImpl(componentContext: ComponentContext): LoginComponent, ComponentContext by componentContext {
    override fun onAction(action: LoginComponent.Action) = when(action){
        LoginComponent.Action.Guest -> onGuest()
        LoginComponent.Action.Login -> onLogin()
        LoginComponent.Action.SignUp -> onSignUp()
    }

    private fun onGuest(){

    }

    private fun onLogin(){

    }

    private fun onSignUp(){

    }

    override fun onStart() {
    }

    private val _state = MutableValue(LoginComponent.State())
    override val state: Value<LoginComponent.State> = _state

}