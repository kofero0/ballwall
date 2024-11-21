import shared
import SwiftUI

struct RootView: View {
    private let component: RootComponent
    
    @StateValue
    private var state: RootComponentState
    
    
    init(_ root: RootComponent){
        self.component = root
        self._state = StateValue(root.state as! Value<RootComponentState>)
    }
    
    var body: some View {
           StackView(
               stackValue: StateValue(component.stack),
               getTitle: { _ in "Heh" },
               onBack: component.onBackClicked
           ) { child in
               switch child {
               case let child as RootComponentChild.Home: HomeView(child.component)
               case let child as RootComponentChild.Login: LoginView(child.component)
               case let child as RootComponentChild.Splash: SplashView(child.component)
               default: fatalError("unreachable")
               }
           }
       }
}
