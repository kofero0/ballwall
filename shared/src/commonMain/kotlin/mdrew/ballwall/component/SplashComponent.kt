package mdrew.ballwall.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mdrew.ballwall.ApplicationExiter
import mdrew.ballwall.DeviceIdProvider
import mdrew.ballwall.DispatcherProvider
import mdrew.ballwall.Result
import mdrew.ballwall.http.LoginClient
import mdrew.ballwall.http.PingClient
import mdrew.ballwall.preferences.UserPreferences

interface SplashComponent : ActionComponent<SplashComponent.Action, SplashComponent.State, SplashComponent.SplashError> {
    sealed interface Action: ActionComponent.Action {
        data object Exit: Action
        data object Offline: Action
    }

    sealed interface SplashError : Component.ComponentError {
        data object ServerUnreachable: SplashError
    }

    data class State(
        override val loading: Boolean = false,
        override val error: SplashError? = null
    ) : Component.UIState<SplashError>

    fun interface Builder {
        fun build(componentContext: ComponentContext, toHome: () -> Unit, toLogin: (LoginComponent.LoginError?) -> Unit): SplashComponent
    }
}

class SplashComponentImpl(
    componentContext: ComponentContext,
    private val routeToHome: () -> Unit,
    private val routeToLogin: (error:LoginComponent.LoginError?) -> Unit,
    private val pingClient: PingClient,
    private val userPreferences: UserPreferences,
    private val loginClient: LoginClient,
    private val deviceIdProvider:DeviceIdProvider,
    private val applicationExiter: ApplicationExiter,
    private val dispatcherProvider: DispatcherProvider
) : SplashComponent, ComponentContext by componentContext {


    override fun onAction(action: SplashComponent.Action) = when(action){
        SplashComponent.Action.Exit -> applicationExiter.exit()
        SplashComponent.Action.Offline -> routeToHome()
    }

    override fun onStart() {
        CoroutineScope(dispatcherProvider.http).launch {
            val splashResponse = pingClient.ping(PingClient.Request(deviceIdProvider.deviceId))
            when(splashResponse){
                is Result.Success -> {
                    login()
                }
                is Result.Failure -> {
                    _state.update { state.value.copy(error = SplashComponent.SplashError.ServerUnreachable) }
                }
            }
        }
    }

    private suspend fun login(){
        val user = userPreferences.get(UserPreferences.Key.USERNAME,"")
        val pass = userPreferences.get(UserPreferences.Key.PASSWORD,"")
        if(user == "" || pass == ""){
            CoroutineScope(dispatcherProvider.main).launch {
                routeToLogin(null)
            }
        }
        else{
            when(val loginResponse = loginClient.login(LoginClient.Request(user,pass))){
                is Result.Success -> {
                    userPreferences.set(UserPreferences.Key.API_TOKEN,loginResponse.data.apiToken)
                    userPreferences.set(UserPreferences.Key.API_TOKEN_TIME,loginResponse.data.time)
                    routeToHome()
                }
                is Result.Failure -> {
                    routeToLogin(LoginComponent.LoginError.SplashLoginFailed)
                }
            }
        }
    }

    private val _state = MutableValue(SplashComponent.State())
    override val state: Value<SplashComponent.State> = _state


}