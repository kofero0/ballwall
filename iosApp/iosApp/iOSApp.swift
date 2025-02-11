import SwiftUI
import shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
        var appDelegate: AppDelegate
        
        var body: some Scene {
            WindowGroup {
                RootView(appDelegate.root)
            }
        }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    let root = DefaultObjectGraph.shared.rootComponent(componentContext: DefaultComponentContext(lifecycle: ApplicationLifecycle()))
}
