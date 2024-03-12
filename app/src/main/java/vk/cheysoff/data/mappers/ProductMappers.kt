package vk.cheysoff.data.mappers

import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.remote.dto.ProductDto
import vk.cheysoff.data.remote.dto.ProductListDto
import vk.cheysoff.domain.model.ProductModel

//fun ProductListDto.toProductModelList(): List<ProductModel> {
//    return this.productList.map { productDto ->
//        productDto.toProductModel()
//    }
//}
//
//fun ProductDto.toProductModel(): ProductModel {
//    return ProductModel(
//        title = this.title,
//        description = this.description,
//        thumbnailUri = this.thumbnail,
//        rating = 0f,
//        price = 0f
//    )
//}

fun ProductDto.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images,
    )
}

fun ProductEntity.toProductModel(): ProductModel {
    return ProductModel(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images,
    )
}