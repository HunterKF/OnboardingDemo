package presentation

import com.arkivanov.decompose.ComponentContext

class OnboardingComponent(
    componentContext: ComponentContext,
    private val onNavigate: () -> Unit
) : ComponentContext by componentContext {
    fun onClick() = onNavigate()
}