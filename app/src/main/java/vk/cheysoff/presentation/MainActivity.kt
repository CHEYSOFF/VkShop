package vk.cheysoff.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import vk.cheysoff.presentation.screens.listScreen.ListScreenViewModel
import vk.cheysoff.presentation.screens.listScreen.ShopIntent
import vk.cheysoff.presentation.screens.listScreen.ShowListScreen
import vk.cheysoff.presentation.screens.productScreen.ProductIntent
import vk.cheysoff.presentation.screens.productScreen.ProductScreenViewModel
import vk.cheysoff.presentation.screens.productScreen.ShowProductScreen
import vk.cheysoff.ui.theme.MyApplicationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val listScreenViewModel: ListScreenViewModel by viewModels()
    private val productScreenViewModel: ProductScreenViewModel by viewModels()

    private val MODEL_ID = "id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listScreenViewModel.processIntent(ShopIntent.GetAllProductsIntent)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = NavigationItem.ListScreen.route
                ) {

                    composable(NavigationItem.ListScreen.route) {
                        ShowListScreen(
                            state = listScreenViewModel.state,
                            navController = navController
                        ) { intent ->
                            listScreenViewModel.processIntent(intent)
                        }
                    }

                    composable(
                        route = NavigationItem.ProductScreen.route + "/{$MODEL_ID}",
                        arguments = listOf(
                            navArgument(name = MODEL_ID) {
                                type = NavType.IntType
                                defaultValue = 12
                            }
                        )
                    ) { backstackEntry ->

                        LaunchedEffect(Unit) {
                            val productId = backstackEntry.arguments?.getInt(MODEL_ID)
                            productScreenViewModel.processIntent(
                                ProductIntent.GetProductByIdIntent(
                                    productId
                                )
                            )
                        }

                        ShowProductScreen(
                            state = productScreenViewModel.state,
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}
