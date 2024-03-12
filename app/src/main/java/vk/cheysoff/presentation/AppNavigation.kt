package vk.cheysoff.presentation

enum class Screen {
    ListScreen,
    ProductScreen,
}

sealed class NavigationItem(val route: String) {
    data object ListScreen : NavigationItem(Screen.ListScreen.name)
    data object ProductScreen : NavigationItem(Screen.ProductScreen.name)
}