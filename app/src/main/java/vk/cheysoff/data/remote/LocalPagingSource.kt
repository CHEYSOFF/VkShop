package vk.cheysoff.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase

class LocalPagingSource (
    private val database: ShopDatabase,
    private val query: String
): PagingSource<Int, ProductEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ProductEntity>): Int? {
        return state.anchorPosition?.let { anchorPage ->
            val page = state.closestPageToPosition(anchorPage)
            page?.nextKey?.minus(1) ?: page?.prevKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductEntity> {

        return try {
            val response = database.dao.searchProducts(query)

            LoadResult.Page(
                data = response,
                nextKey = null,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}