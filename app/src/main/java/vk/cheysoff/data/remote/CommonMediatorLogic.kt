package vk.cheysoff.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import vk.cheysoff.data.mappers.toProductEntity
import vk.cheysoff.data.remote.dto.ProductDto

@OptIn(ExperimentalPagingApi::class)
suspend fun commonRemoteMediatorLogicLoad(
    loadType: LoadType,
    state: PagingState<Int, ProductEntity>,
    shopDatabase: ShopDatabase,
    networkCall: suspend (skip: Int, limit: Int) -> List<ProductDto>,
    databaseInteraction: (suspend (products: List<ProductDto>) -> Unit)? = null
): RemoteMediator.MediatorResult {
    return try {
        val skip = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return RemoteMediator.MediatorResult.Success(
                endOfPaginationReached = true
            )

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                lastItem?.id ?: 0
            }
        }

        val products = networkCall(
            skip,
            state.config.pageSize
        )
        shopDatabase.withTransaction {
            if (loadType == LoadType.REFRESH) {
                shopDatabase.dao.clearAll()
            }
            val productEntities = products.map { it.toProductEntity() }
            shopDatabase.dao.upsertAll(productEntities)
        }

        RemoteMediator.MediatorResult.Success(
            endOfPaginationReached = products.isEmpty()
        )

    } catch (e: Exception) {
        RemoteMediator.MediatorResult.Error(e)
    }
}