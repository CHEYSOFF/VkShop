package vk.cheysoff.presentation.screens.productScreen

import vk.cheysoff.presentation.screens.listScreen.ShopIntent

sealed class ProductIntent {
    class GetProductByIdIntent(val productId: Int?): ProductIntent()
    data object GoToShopIntent : ProductIntent()
}