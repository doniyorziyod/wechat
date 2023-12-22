package doniyor.wechat.sceens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import doniyor.wechat.api.Firebase
import doniyor.wechat.model.User
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Text2

@Preview
@Composable
fun DetailsView() {
    DetailsScreen(navController = rememberNavController(), "")
}

@Composable
fun DetailsScreen(navController: NavHostController, key: String) {
    val user = remember { mutableStateOf(User("", "", "", "", "")) }

    Firebase.getUser(key){
        user.value = it
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (user.value.fullName!!.isNotEmpty()) user.value.fullName!!.toCharArray()[0].toString() else "",
            modifier = Modifier
                .background(Text2, CircleShape)
                .size(65.dp)
                .padding(8.dp),
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Text(text = user.value.fullName!!, fontSize = 30.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Text(text = "@"+user.value.username!!, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(25.dp))
        Text(modifier = Modifier.clickable {
            navController.navigate("chat_screen/${user.value.key!!}")
        }, text = "SEND MESSAGE", fontSize = 24.sp, color = Color.Green, fontWeight = FontWeight.Bold)
    }
}

