package vk.cheysoff.presentation.screens.productScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vk.cheysoff.domain.repository.ShopRepository
import vk.cheysoff.domain.util.Resource
import vk.cheysoff.presentation.screens.listScreen.ShopIntent
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {
    var state by mutableStateOf(ProductScreenState())
        private set

    fun processIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.GetProductByIdIntent -> loadProductInfo(intent.productId)
            is ProductIntent.GoToShopIntent -> TODO()
        }
    }

    fun loadProductInfo(productId: Int?) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            val errorLambda = { error: String? ->
                state = state.copy(
                    productModel = null,
                    isLoading = false,
                    error = error
                )
            }

            if (productId == null) {
                errorLambda("product id is null")
                return@launch
            }

            when (val result = repository.getProductById(productId)) {
                is Resource.Success -> {
                    state = state.copy(
                        productModel = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    errorLambda(result.message)
                }
            }


        }
    }
}