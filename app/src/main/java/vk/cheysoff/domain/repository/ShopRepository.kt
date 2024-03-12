package vk.cheysoff.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vk.cheysoff.domain.model.ProductModel
import vk.cheysoff.domain.util.Resource

interface ShopRepository {
    fun getProducts(): Flow<PagingData<ProductModel>>
    fun getProductsBySearch(query: String): Flow<PagingData<ProductModel>>
    suspend fun getProductById(id: Int): Resource<ProductModel>
}