import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import kotlinx.serialization.Serializable
import com.russhwolf.settings.Settings
import presentation.HomeComponent
import presentation.OnboardingComponent


class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    private val settings = Settings()
    private val showOnboarding = settings.getBoolean("onboarding", true)

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = if (showOnboarding) Configuration.OnboardingScreen else Configuration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Configuration,
        context: ComponentContext,
    ): Child {
        return when (config) {
            Configuration.OnboardingScreen -> {
                Child.OnboardingScreen(
                    OnboardingComponent(context, onNavigate = {
                        settings.putBoolean("onboarding", false)
                        navigation.replaceAll(Configuration.HomeScreen)
                    })
                )
            }

            is Configuration.HomeScreen -> {
                Child.HomeScreen(
                    HomeComponent(
                        context
                    )
                )
            }
        }
    }

    sealed class Child {
        data class OnboardingScreen(val component: OnboardingComponent) : Child()
        data class HomeScreen(val component: HomeComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object OnboardingScreen : Configuration()

        @Serializable
        data object HomeScreen : Configuration()
    }
}