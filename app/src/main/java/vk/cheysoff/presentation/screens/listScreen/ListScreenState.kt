package vk.cheysoff.presentation.screens.listScreen

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import vk.cheysoff.domain.model.ProductModel

data class ListScreenState(
    val products: Flow<PagingData<ProductModel>> = MutableStateFlow(PagingData.empty()),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val error: String? = null,
    var searchType: SearchType = SearchType.Remote
)