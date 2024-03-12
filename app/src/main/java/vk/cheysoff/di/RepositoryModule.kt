package vk.cheysoff.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vk.cheysoff.data.repository.ShopRepositoryImpl
import vk.cheysoff.domain.repository.ShopRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideShopRepository(
        shopRepositoryImpl: ShopRepositoryImpl
    ): ShopRepository
}