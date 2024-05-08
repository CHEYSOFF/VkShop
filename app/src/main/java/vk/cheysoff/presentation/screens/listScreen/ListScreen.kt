package vk.cheysoff.presentation.screens.listScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import vk.cheysoff.R
import vk.cheysoff.domain.model.ProductModel
import vk.cheysoff.presentation.NavigationItem
import vk.cheysoff.presentation.screens.listScreen.components.CustomSearchBar
import vk.cheysoff.presentation.screens.listScreen.components.ShowProductCard
import vk.cheysoff.presentation.screens.mutualComponents.ShowError
import vk.cheysoff.presentation.screens.mutualComponents.ShowLoader

@Composable
fun ShowListScreen(
    state: ListScreenState,
    navController: NavController,
    onIntentReceived: (ShopIntent) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ShowVkShopText()

        CustomSearchBar(
            onClear = {
                onIntentReceived(ShopIntent.SearchTextChangeIntent(""))
                onIntentReceived(ShopIntent.GetAllProductsIntent)
            },
            query = state.searchText,
            onQueryChange = { newText ->
                onIntentReceived(ShopIntent.SearchTextChangeIntent(query = newText))
            },
            onSearch = { onIntentReceived(ShopIntent.SearchIntent) },
            active = false,
            onActiveChange = { onIntentReceived(ShopIntent.OnToggleSearchIntent) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Search for products",
            onSearchTypeChange = { searchType ->
                onIntentReceived(ShopIntent.ChangeSearchTypeIntent(searchType))
            },
            searchType = state.searchType,
            focusRequester = focusRequester,
            focusManager = focusManager
        )


        if (state.isLoading) {
            ShowLoader()
        } else if (state.error != null) {
            ShowError(
                errorMessage = state.error,
                onClick = { onIntentReceived(ShopIntent.SearchIntent) })
        } else {
            val products = state.products.collectAsLazyPagingItems()
            when (products.loadState.refresh) {
                is LoadState.Error -> ShowError(
                    errorMessage = (products.loadState.refresh as LoadState.Error).error.message,
                    onClick = { onIntentReceived(ShopIntent.SearchIntent) }
                )

                is LoadState.Loading -> ShowLoader()
                is LoadState.NotLoading -> ShowProducts(
                    products = products,
                    onCardClick = { id ->
                        navController.navigate(NavigationItem.ProductScreen.route + "/${id}")
                    })
            }
        }


    }


}

@Composable
private fun ShowVkShopText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(47.dp),
            painter = painterResource(id = R.drawable.vk_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "SHOP",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.surfaceTint
        )
    }
}


@Composable
private fun ShowProducts(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<ProductModel>,
    onCardClick: (Int) -> Unit
) {
    if (products.itemCount == 0) {
        Box(modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Haven't found anything.\n Sorry!",
                minLines = 2,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyVerticalStaggeredGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products.itemCount) { index ->
                products[index]?.let { product ->
                    ShowProductCard(
                        product = product,
                        onClick = onCardClick,
                    )
                }

            }
            item {
                if (products.loadState.append is LoadState.Loading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}