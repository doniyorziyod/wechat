package doniyor.wechat.sceens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import doniyor.wechat.api.Firebase
import doniyor.wechat.api.SharedHelper
import doniyor.wechat.model.User
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Secondary
import doniyor.wechat.ui.theme.Text2

@Preview
@Composable
fun HomeView() {
    HomeScreen(navController = rememberNavController())
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val currentUserKey = SharedHelper.getInstance(context).getKey()
    val chats = remember { mutableStateListOf<User>() }
    val users = remember { mutableStateListOf<User>() }

    val search = remember { mutableStateOf("") }

    Firebase.getChats(search.value, context) { it1 ->
        chats.clear()
        chats.addAll(it1)
        Firebase.getAllUsers(context, search.value) { it2 ->
            users.clear()
            users.addAll(it2)
            users.removeAll(chats)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Secondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(search, navController)

        Column(
            modifier = Modifier.background(White, RoundedCornerShape(16.dp))
        ) {
            if (chats.isNotEmpty()) {
                Chats(navController, chats)
            }
            if (users.isNotEmpty()) {
                Chats(navController, users)
            }
        }
    }
}

@Composable
fun Chats(navController: NavController, chats: SnapshotStateList<User>) {
    LazyColumn {
        items(chats.size) {
            LazyItem(chats[it], navController)
        }
    }
}

@Composable
fun LazyItem(chat: User, navController: NavController) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                navController.navigate("chat_screen/${chat.key!!}")
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (chat.fullName!!.isNotEmpty()) chat.fullName!!.toCharArray()[0].toString() else "",
            modifier = Modifier
                .background(Text2, CircleShape)
                .size(50.dp)
                .padding(5.dp)
                .clickable { navController.navigate("details_screen/${chat.key}")  },
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            color = White
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.fullName!!,
                textAlign = TextAlign.Center,
                color = Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "@"+chat.username!!,
                textAlign = TextAlign.Center,
                color = Gray,
                fontSize = 12.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(search: MutableState<String>, navController: NavController) {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f),
            shape = RoundedCornerShape(18.dp),
            value = search.value,
            onValueChange = { search.value = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            placeholder = { Text("Search") },
        )
        Button(
            onClick = {
                      navController.navigate(Screens.Profile.route)
            }, modifier = Modifier
                .size(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Image(
                Icons.Rounded.AccountCircle,
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


