package vk.cheysoff.data.remote.dto

data class ProductListDto (
    val products: List<ProductDto>,
    val skip: Int,
    val limit: Int,
)