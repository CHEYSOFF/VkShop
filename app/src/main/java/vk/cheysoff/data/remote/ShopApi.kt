package vk.cheysoff.data.remote

import androidx.paging.PagingSource
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vk.cheysoff.data.remote.dto.ProductDto
import vk.cheysoff.data.remote.dto.ProductListDto
import vk.cheysoff.domain.model.ProductModel

interface ShopApi {
    @GET("products")
    suspend fun getProductList(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ProductListDto

    @GET("products/search")
    suspend fun getProductListBySearch(
        @Query("q") searchQuery: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): ProductListDto

    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto

    companion object {
        const val BASE_URL = "https://dummyjson.com"
    }
}