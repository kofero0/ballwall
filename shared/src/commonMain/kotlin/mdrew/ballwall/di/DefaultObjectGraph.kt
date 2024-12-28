package mdrew.ballwall.di

import com.arkivanov.decompose.ComponentContext
import io.ktor.client.HttpClient
import mdrew.ballwall.DispatcherProvider
import mdrew.ballwall.Result
import mdrew.ballwall.component.HomeComponent
import mdrew.ballwall.component.HomeComponentImpl
import mdrew.ballwall.component.LoginComponent
import mdrew.ballwall.component.LoginComponentImpl
import mdrew.ballwall.component.RootComponent
import mdrew.ballwall.component.RootComponentImpl
import mdrew.ballwall.component.SplashComponent
import mdrew.ballwall.component.SplashComponentImpl
import mdrew.ballwall.http.LoginClient
import mdrew.ballwall.http.RegisterClient
import mdrew.ballwall.http.PingClient

class DefaultObjectGraph(private val platformGraph: PlatformObjectGraph) {
    fun rootComponent(
        componentContext: ComponentContext
    ): RootComponent = RootComponentImpl(
        componentContext = componentContext,
        splashBuilder = splashBuilder(),
        homeBuilder = homeBuilder(),
        loginBuilder = loginBuilder()
    )

    private fun loginBuilder() = LoginComponent.Builder { componentContext, toHomeComponent, initialError ->
        LoginComponentImpl(
            componentContext = componentContext,
            userPreferences = platformGraph.userPreferences(),
            toHomeComponent = toHomeComponent,
            dispatcherProvider = dispatcherProvider,
            loginClient = mockedLoginClient,
            registerClient = mockedRegisterClient,
            initialError = initialError
        )
    }

    private var authClient: HttpClient? = null
    private fun authClient(/* requestInterceptor */): HttpClient {
        if (authClient == null) {
            authClient = HttpClient {
                //TODO: request interceptor putting api token in
            }
        }
        return authClient!!
    }

    private var noAuthClient: HttpClient? = null
    private fun noAuthClient(): HttpClient {
        if (noAuthClient == null) {
            noAuthClient =
                HttpClient() // Failed to find HTTP client engine implementation in the classpath: consider adding client engine dependency. See https://ktor.io/docs/http-client-engines.html
        }
        return noAuthClient!!
    }

    private val loginUrl = "http://"

    private val registerUrl = "http://"

//    private val registerClient: RegisterClient =
//        defaultRegisterClient(httpClient = noAuthClient(), url = registerUrl)

    private val mockedRegisterClient =
        RegisterClient { Result.Success(RegisterClient.Response("1234", 1234L)) }

//    private val loginClient: LoginClient =
//        defaultLoginClient(httpClient = noAuthClient(), url = loginUrl)

    private val mockedLoginClient =
        LoginClient { Result.Success(LoginClient.Response("1234", 1234L)) }

    private val mockedPingClient =
        PingClient { Result.Success(PingClient.Response("version", 1234L)) }

    private val dispatcherProvider: DispatcherProvider = DispatcherProvider

    private fun homeBuilder() = HomeComponent.Builder { HomeComponentImpl(componentContext = it) }

    private fun splashBuilder(
    ) = SplashComponent.Builder { componentContext, toHome, toLogin ->
        SplashComponentImpl(
            componentContext = componentContext,
            routeToHome = toHome,
            routeToLogin = toLogin,
            pingClient = mockedPingClient,
            userPreferences = platformGraph.userPreferences(),
            loginClient = mockedLoginClient,
            deviceIdProvider = platformGraph.deviceIdProvider(),
            applicationExiter = platformGraph.applicationExiter(),
            dispatcherProvider = dispatcherProvider
        )
    }
}