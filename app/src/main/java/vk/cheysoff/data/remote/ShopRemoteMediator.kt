package vk.cheysoff.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ShopRemoteMediator @Inject constructor(
    private val shopDatabase: ShopDatabase,
    private val api: ShopApi,
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return commonRemoteMediatorLogicLoad(loadType = loadType,
            state = state,
            shopDatabase = shopDatabase,
            networkCall =  { skip, limit ->
                val tm = api.getProductList(
                    skip = skip,
                    limit = limit
                ).products
                Log.d("CHEYSOFF", tm.size.toString())
                tm
            })
    }
}