package vk.cheysoff.presentation.screens.listScreen

import android.util.Log
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

    init {
        getAllProducts()
    }

    fun processIntent(intent: ShopIntent) {
        when (intent) {
            ShopIntent.SearchIntent -> {
                searchProducts()
            }

            ShopIntent.GetAllProductsIntent -> {
                getAllProducts()
            }

            ShopIntent.OnToggleSearchIntent -> {
                onToggleSearch()
            }

            is ShopIntent.SearchTextChangeIntent -> {
                onSearchTextChange(intent.query)
            }

            is ShopIntent.ChangeSearchTypeIntent -> {
                changeSearchType(intent.searchType)
            }
        }
    }

    private fun searchProducts() {
        state = state.copy(
            error = null,
            isSearching = true
        )
        handleSearchResults(state.searchText).also {
            state = state.copy(
                products = it,
                error = null,
                isSearching = false
            )
        }
    }

    private fun getAllProducts() {
        state = state.copy(
            isLoading = true,
            error = null
        )
        getAllProductsFlow().also {
            state = state.copy(
                products = it,
                isLoading = false,
                error = null
            )
        }

    }

    private fun changeSearchType(searchType: SearchType) {
        state.searchType = searchType
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


    private fun getAllProductsFlow(): Flow<PagingData<ProductModel>> {
        return repository
            .getProducts()
            .map { pagingData ->
                pagingData.map { it }
            }
            .cachedIn(viewModelScope)
    }

    private fun handleSearchResults(query: String): Flow<PagingData<ProductModel>> {
        return when (state.searchType) {
            SearchType.Local -> repository.getProductsByLocalSearch(query)
            SearchType.Remote -> repository.getProductsByRemoteSearch(query)
        }
            .cachedIn(viewModelScope)
    }

}