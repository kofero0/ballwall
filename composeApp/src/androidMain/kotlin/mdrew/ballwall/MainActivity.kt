package mdrew.ballwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import mdrew.ballwall.di.DefaultObjectGraph
import mdrew.ballwall.di.PlatformObjectGraph
import mdrew.ballwall.route.RootRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RootRoute(
                component = DefaultObjectGraph(PlatformObjectGraph(activity = this)).rootComponent(defaultComponentContext()),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}