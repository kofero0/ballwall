package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mdrew.ballwall.DispatcherProvider
import mdrew.ballwall.Result
import mdrew.ballwall.http.LoginClient
import mdrew.ballwall.http.RegisterClient
import mdrew.ballwall.preferences.UserPreferences

interface LoginComponent :
    ActionComponent<LoginComponent.Action, LoginComponent.State, LoginComponent.LoginError> {
    sealed interface Action : ActionComponent.Action {
        data object Login : Action
        data object Register : Action
        data object Cancel : Action
        data object Guest : Action
        data object ErrorAcknowledged : Action
        data class ConfirmLogin(val user: String, val pass: String, val remember: Boolean) : Action
        data class ConfirmRegister(val user: String, val pass: String, val remember: Boolean) :
            Action
    }

    sealed interface LoginError : Component.ComponentError {
        data class Http(val error: mdrew.ballwall.http.HttpError) : LoginError
        data object SplashLoginFailed: LoginError
        data object Other : LoginError
    }

    enum class UIState {
        REGISTER, LOGIN
    }

    data class State(
        override val loading: Boolean = false,
        override val error: LoginError? = null,
        val uiState: UIState? = null
    ) : Component.UIState<LoginError>

    fun interface Builder {
        fun build(componentContext: ComponentContext, toHomeComponent: () -> Unit, initialError: LoginError?): LoginComponent
    }
}

class LoginComponentImpl(
    componentContext: ComponentContext,
    val userPreferences: UserPreferences,
    private val loginClient: LoginClient,
    private val registerClient: RegisterClient,
    private val dispatcherProvider: DispatcherProvider,
    private val toHomeComponent: () -> Unit,
    initialError: LoginComponent.LoginError? = null
) : LoginComponent, ComponentContext by componentContext {

    override fun onAction(action: LoginComponent.Action) = when (action) {
        LoginComponent.Action.Guest -> toHomeComponent()

        LoginComponent.Action.Login -> _state.value =
            state.value.copy(uiState = LoginComponent.UIState.LOGIN)

        LoginComponent.Action.Register -> _state.value =
            state.value.copy(uiState = LoginComponent.UIState.REGISTER)

        LoginComponent.Action.ErrorAcknowledged -> _state.value = state.value.copy(error = null)

        LoginComponent.Action.Cancel -> _state.value = state.value.copy(uiState = null)

        is LoginComponent.Action.ConfirmLogin -> onConfirmLogin(action)
        is LoginComponent.Action.ConfirmRegister -> onConfirmRegister(action)
    }

    private fun onConfirmLogin(action: LoginComponent.Action.ConfirmLogin) {
        _state.value = state.value.copy(loading = true)
        CoroutineScope(dispatcherProvider.http).launch {
            val result = loginClient.login(LoginClient.Request(action.user, action.pass))
            _state.value = state.value.copy(loading = false)
            when (result) {
                is Result.Success -> {
                    userPreferences.apply {
                        set(UserPreferences.Key.REMEMBER_LOGIN, action.remember)
                        set(UserPreferences.Key.API_TOKEN, result.data.apiToken)
                        if (action.remember) {
                            set(UserPreferences.Key.USERNAME, action.user)
                            set(UserPreferences.Key.PASSWORD, action.pass)
                        } else {
                            set(UserPreferences.Key.USERNAME, "")
                            set(UserPreferences.Key.PASSWORD, "")
                        }
                    }
                    toHomeComponent()
                }

                is Result.Failure -> {
                    when (result.error) {
                        LoginClient.Error.ConnectionRefused, is LoginClient.Error.HttpError -> _state.value =
                            state.value.copy(error = LoginComponent.LoginError.Http(result.error))

                        LoginClient.Error.Other -> _state.value =
                            state.value.copy(error = LoginComponent.LoginError.Other)
                    }
                }
            }
        }
    }

    private fun onConfirmRegister(action: LoginComponent.Action.ConfirmRegister) {
        _state.value = state.value.copy(loading = true)
        CoroutineScope(dispatcherProvider.http).launch {
            val result = registerClient.register(RegisterClient.Request(action.user, action.pass))
            _state.value = state.value.copy(loading = false)
            when (result) {
                is Result.Success -> {
                    userPreferences.apply {
                        set(UserPreferences.Key.REMEMBER_LOGIN, action.remember)
                        set(UserPreferences.Key.API_TOKEN, result.data.apiToken)
                        if (action.remember) {
                            set(UserPreferences.Key.USERNAME, action.user)
                            set(UserPreferences.Key.PASSWORD, action.pass)
                        } else {
                            set(UserPreferences.Key.USERNAME, "")
                            set(UserPreferences.Key.PASSWORD, "")
                        }
                    }
                    toHomeComponent()
                }

                is Result.Failure -> {
                    when (result.error) {
                        RegisterClient.Error.ConnectionRefused, is RegisterClient.Error.HttpError -> _state.value =
                            state.value.copy(error = LoginComponent.LoginError.Http(result.error))

                        RegisterClient.Error.Other -> _state.value =
                            state.value.copy(error = LoginComponent.LoginError.Other)
                    }
                }
            }
        }
    }

    override fun onStart() {
    }

    private val _state = MutableValue(LoginComponent.State(error = initialError))
    override val state: Value<LoginComponent.State> = _state
}