package mdrew.ballwall.di

import com.arkivanov.decompose.ComponentContext
import io.ktor.client.HttpClient
import mdrew.ballwall.DispatcherProvider
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
import mdrew.ballwall.http.defaultLoginClient
import mdrew.ballwall.http.defaultRegisterClient
import mdrew.ballwall.preferences.UserPreferences

class DefaultObjectGraph(private val platformGraph: PlatformObjectGraph) {
    fun rootComponent(
        componentContext: ComponentContext
    ): RootComponent = RootComponentImpl(
        componentContext = componentContext,
        splashBuilder = splashBuilder(),
        homeBuilder = homeBuilder(),
        loginBuilder = loginBuilder()
    )

    private fun loginBuilder() = LoginComponent.Builder { componentContext, toHomeComponent ->
        LoginComponentImpl(
            componentContext = componentContext,
            userPreferences = platformGraph.userPreferences(),
            toHomeComponent = toHomeComponent,
            dispatcherProvider = dispatcherProvider,
            loginClient = loginClient,
            registerClient = registerClient
        )
    }

    private var authClient:HttpClient? = null
    private fun authClient(/* requestInterceptor */) = if(authClient == null){
        authClient = HttpClient{
            //TODO: request interceptor putting api token in
        }
        authClient
    } else {
        authClient
    }

    private val noAuthClient = HttpClient() // Failed to find HTTP client engine implementation in the classpath: consider adding client engine dependency. See https://ktor.io/docs/http-client-engines.html

    private val loginUrl = "http://"

    private val registerUrl = "http://"

    private val registerClient: RegisterClient = defaultRegisterClient(httpClient = noAuthClient, url = registerUrl)

    private val loginClient: LoginClient = defaultLoginClient(httpClient = noAuthClient, url = loginUrl)

    private val dispatcherProvider: DispatcherProvider = DispatcherProvider

    private fun homeBuilder() = HomeComponent.Builder { HomeComponentImpl(componentContext = it) }

    private fun splashBuilder() =
        SplashComponent.Builder { SplashComponentImpl(componentContext = it) }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlatformObjectGraph {
    fun userPreferences(): UserPreferences
}