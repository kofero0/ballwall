import shared
import SwiftUI

struct HomeView : View {
    private let component: HomeComponent
    
    @StateValue
    private var state: HomeComponentState
    
    init(_ home: HomeComponent){
        self.component = home
        self._state = StateValue(component.state as! Value<HomeComponentState>)
    }
    
    var body: some View {
        VStack{
            
        }
    }
}
