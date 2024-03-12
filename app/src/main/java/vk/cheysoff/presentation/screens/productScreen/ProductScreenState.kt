package vk.cheysoff.presentation.screens.productScreen

import vk.cheysoff.domain.model.ProductModel

data class ProductScreenState (
    val productModel: ProductModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)