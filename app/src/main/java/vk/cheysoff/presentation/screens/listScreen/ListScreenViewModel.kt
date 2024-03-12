package vk.cheysoff.presentation.screens.listScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vk.cheysoff.domain.model.ProductModel
import vk.cheysoff.domain.repository.ShopRepository
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {
    var state by mutableStateOf(ListScreenState())
        private set

    fun processIntent(intent: ShopIntent) {
        when (intent) {
            is ShopIntent.SearchIntent -> {
                state = state.copy(
                    error = null,
                    isSearching = true
                )
                state = state.copy(
                    products = handleSearchResults(state.searchText),
                    error = null,
                    isSearching = false
                )

            }

            ShopIntent.GetAllProductsIntent -> {
                state = state.copy(
                    isLoading = true,
                    error = null
                )
                state = state.copy(
                    products = getAllProducts(),
                    isLoading = false,
                    error = null
                )
            }

            is ShopIntent.OnToggleSearchIntent -> onToggleSearch()
            is ShopIntent.SearchTextChangeIntent -> onSearchTextChange(intent.query)
            is ShopIntent.GoToProductCardIntent -> TODO()
        }
    }

    private fun onSearchTextChange(text: String) {
        state = state.copy(searchText = text)
    }

    private fun onToggleSearch() {
        state = state.copy(isSearching = !state.isSearching)
        if (!state.isSearching) {
            onSearchTextChange("")
        }
    }


    private fun getAllProducts(): Flow<PagingData<ProductModel>> {
        return repository
            .getProducts()
            .map { pagingData ->
                pagingData.map { it }
            }
            .cachedIn(viewModelScope)
    }

    private fun handleSearchResults(query: String): Flow<PagingData<ProductModel>> {
        return repository
            .getProductsByRemoteSearch(query)
            .map { pagingData ->
                pagingData.map { it }
            }
            .cachedIn(viewModelScope)
    }

}