package vk.cheysoff.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ShopDao {

    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM vkshop")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("DELETE FROM vkshop")
    suspend fun clearAll()
}