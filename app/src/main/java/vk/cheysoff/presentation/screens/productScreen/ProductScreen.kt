package vk.cheysoff.presentation.screens.productScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import vk.cheysoff.domain.model.ProductModel
import vk.cheysoff.presentation.NavigationItem
import vk.cheysoff.presentation.screens.listScreen.ListScreenState
import vk.cheysoff.presentation.screens.listScreen.ShopIntent
import vk.cheysoff.presentation.screens.mutualComponents.ShowError
import vk.cheysoff.presentation.screens.mutualComponents.ShowLoader
import vk.cheysoff.presentation.screens.mutualComponents.ShowRatingPriceRow
import vk.cheysoff.presentation.screens.productScreen.components.ShowImageSlider

@Composable
fun ShowProductScreen(
    state: ProductScreenState,
    navController: NavController,
) {
    val product = state.productModel

    if (state.isLoading) {
        ShowLoader()
    } else if (product == null || state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            ShowError(
                modifier = Modifier.align(Alignment.Center),
                errorMessage = state.error ?: "Product is null"
            ) {
                navController.navigate(NavigationItem.ListScreen.route)
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {


            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(0.9f)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = { navController.navigate(NavigationItem.ListScreen.route) },
                                modifier = Modifier
                                    .size(50.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                ),
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Go back",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = product.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.End
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "By ${product.brand}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                        ShowImageSlider(product = product)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Category: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = product.category,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Currently in stock: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Text(
                                text = product.stock.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }



                        ShowRatingPriceRow(
                            rating = product.rating,
                            price = product.price,
                            discountPercentage = product.discountPercentage,
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

        }
    }


}


