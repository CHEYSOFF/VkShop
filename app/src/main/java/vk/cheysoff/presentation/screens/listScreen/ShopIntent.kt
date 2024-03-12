package vk.cheysoff.presentation.screens.listScreen

import vk.cheysoff.domain.model.ProductModel

sealed class ShopIntent {
    class SearchTextChangeIntent(val query: String) : ShopIntent()
    data object SearchIntent : ShopIntent()
    data object OnToggleSearchIntent : ShopIntent()
    data object GetAllProductsIntent : ShopIntent()
    class ChangeSearchTypeIntent(val searchType: SearchType) : ShopIntent()
}