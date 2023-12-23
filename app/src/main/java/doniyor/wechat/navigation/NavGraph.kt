package doniyor.wechat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import doniyor.wechat.sceens.ChatScreen
import doniyor.wechat.sceens.DetailsScreen
import doniyor.wechat.sceens.HomeScreen
import doniyor.wechat.sceens.LoginScreen
import doniyor.wechat.sceens.ProfileScreen
import doniyor.wechat.sceens.RegisterScreen
import doniyor.wechat.sceens.SplashScreen

@Composable
fun NavGraph (){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route)
    {
        composable(route = Screens.Splash.route){
            SplashScreen(navController)
        }
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screens.Regist.route) {
            RegisterScreen(navController)
        }
        composable(route = Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screens.Chat.route, arguments = listOf(
            navArgument("key"){type= NavType.StringType}
        )) {
            ChatScreen(navController, it.arguments?.getString("key")!!)
        }
        composable(route = Screens.Details.route, arguments = listOf(
            navArgument("key"){type= NavType.StringType}
        )) {
            DetailsScreen(navController = navController, it.arguments?.getString("key")!!)
        }
        composable(route = Screens.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}