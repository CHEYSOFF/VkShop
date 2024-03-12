package vk.cheysoff.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import vk.cheysoff.presentation.screens.listScreen.ListScreenViewModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator @Inject constructor(
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
        return commonMediatorLogicLoad(loadType = loadType,
            state = state,
            shopDatabase = shopDatabase,
            networkCall = { skip, limit ->
                api.getProductListBySearch(
                    searchQuery = queryString,
                    skip = skip,
                    limit = limit
                ).products
            })
    }

}