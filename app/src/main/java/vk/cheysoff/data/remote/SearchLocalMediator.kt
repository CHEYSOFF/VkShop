package vk.cheysoff.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchLocalMediator @Inject constructor(
    private val shopDatabase: ShopDatabase,
    private val api: ShopApi,
    private val queryString: String,
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return commonLocalMediatorLogicLoad(loadType = loadType,
            state = state,
            shopDatabase = shopDatabase,
            networkCall = { skip, limit ->
                shopDatabase.dao.searchProducts(queryString)
            })
    }

}