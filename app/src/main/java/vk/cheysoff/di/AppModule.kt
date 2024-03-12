package vk.cheysoff.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import vk.cheysoff.data.local.ProductEntity
import vk.cheysoff.data.local.ShopDatabase
import vk.cheysoff.data.remote.ShopApi
import vk.cheysoff.data.remote.ShopRemoteMediator
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShopDatabase(@ApplicationContext context: Context): ShopDatabase {
        return Room.databaseBuilder(
            context,
            ShopDatabase::class.java,
            "vkshop.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideShopApi(): ShopApi {
        return Retrofit.Builder()
            .baseUrl(ShopApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("shopPager")
    fun provideShopPager(shopDatabase: ShopDatabase, shopApi: ShopApi): Pager<Int, ProductEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = ShopRemoteMediator(
                shopDatabase = shopDatabase,
                api = shopApi
            ),
            pagingSourceFactory = {
                shopDatabase.dao.pagingSource()
            }
        )
    }


}