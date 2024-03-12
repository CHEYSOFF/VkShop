package vk.cheysoff.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import javax.inject.Inject

class SearchPagerFactory @Inject constructor(
    private val shopDatabase: ShopDatabase,
    private val shopApi: ShopApi
) {

    @OptIn(ExperimentalPagingApi::class)
    fun createSearchPager(queryString: String): Pager<Int, ProductEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = SearchRemoteMediator(
                shopDatabase = shopDatabase,
                api = shopApi,
                queryString = queryString
            ),
            pagingSourceFactory = {
                shopDatabase.dao.pagingSource()
            }
        )
    }
}