package vk.cheysoff.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import vk.cheysoff.data.mappers.toProductEntity
import vk.cheysoff.data.mappers.toProductModel
import vk.cheysoff.data.remote.SearchRemoteMediator
import vk.cheysoff.data.remote.ShopApi
import vk.cheysoff.domain.model.ProductModel
import vk.cheysoff.domain.repository.ShopRepository
import vk.cheysoff.domain.util.Resource
import javax.inject.Inject
import javax.inject.Named

class ShopRepositoryImpl @Inject constructor(
    @Named("shopPager") private val shopPager: Pager<Int, ProductEntity>,
    private val shopDatabase: ShopDatabase,
    private val shopApi: ShopApi,
) : ShopRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProducts(): Flow<PagingData<ProductModel>> {
        return shopPager
            .flow
            .mapLatest { pagingData ->
                pagingData.map { entity ->
                    entity.toProductModel()
                }
            }
    }

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override fun getProductsBySearch(query: String): Flow<PagingData<ProductModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = SearchRemoteMediator(
                shopDatabase = shopDatabase,
                api = shopApi,
                queryString = query
            ),
            pagingSourceFactory = {
                shopDatabase.dao.pagingSource()
            }
        )
            .flow
            .mapLatest { pagingData ->
                pagingData.map { entity ->
                    entity.toProductModel()
                }
            }
    }

    override suspend fun getProductById(id: Int): Resource<ProductModel> {
        return try {
            Resource.Success(
                data = shopApi.getProductById(id)
                    .toProductEntity()
                    .toProductModel()
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Unknown error")
        }
    }
}