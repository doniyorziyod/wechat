package doniyor.wechat.sceens

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import doniyor.wechat.api.Firebase
import doniyor.wechat.api.SharedHelper
import doniyor.wechat.model.Message
import doniyor.wechat.model.User
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Primary
import doniyor.wechat.ui.theme.Text2

@Composable
fun ChatScreen(navController: NavController, key:String){
    val user = remember { mutableStateOf(User("", "", "", "", "")) }
    val messages = remember { mutableStateListOf<Message>() }
    val context = LocalContext.current

    Firebase.getUser(key){
        user.value = it
    }
    Firebase.getMessages(context, key){
        messages.clear()
        messages.addAll(it)
    }

    Column {
        ChatTopBar(user, navController)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages.size) { index ->
                MessageItem(messages[index])
            }
        }

        if (user.value.fullName!!.isNotEmpty()) EnterMessage(userKey = user.value.key!!)
    }
}

@Composable
fun MessageItem(
    message: Message
) {
    val currentUserKey = SharedHelper.getInstance(LocalContext.current).getKey()
    val fromMe = message.from == currentUserKey
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        if (fromMe) Spacer(modifier = Modifier.width(100.dp))
        Card(
            modifier = Modifier
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(if (fromMe) Primary else Color.Gray)
        ) {
            Text(
                text = message.text!!,
                color = Color.Black,
                modifier = Modifier.padding(12.dp),
                textAlign = if (fromMe) TextAlign.End else TextAlign.Start
            )
        }
        if (!fromMe) Spacer(modifier = Modifier.width(100.dp))
    }
}

@Composable
fun ChatTopBar(users: MutableState<User>, navController: NavController) {
    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Row (modifier = Modifier.padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween){
        IconButton(onClick = { navController.navigate(Screens.Home.route) }) {
            Icon(Icons.Rounded.ArrowBack, "", Modifier.size(40.dp))
        }
        Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ){
            Text(text = users.value.fullName!!, textAlign = TextAlign.Center, color = Color.Black)
            Text(text = "@"+users.value.username!!, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
        }
        Text(text = if (users.value.fullName!!.isNotEmpty()) users.value.fullName!!.toCharArray()[0].toString() else "", modifier = Modifier
            .background(Text2, CircleShape)
            .size(50.dp)
            .padding(4.dp)
            .clickable { navController.navigate("details_screen/${users.value.key}") }, fontSize = 28.sp, textAlign = TextAlign.Center, color = Color.White
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterMessage(
    userKey: String
) {
    val context = LocalContext.current
    val message = remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = message.value,
        onValueChange = { message.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            Color.Black,
            containerColor = Color.White,
            focusedBorderColor = Primary,
            unfocusedBorderColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    Firebase.writeMessage(message.value.trim(), context, userKey)
                    message.value = ""
                    focusManager.clearFocus()
                },
                enabled = message.value.isNotEmpty(),
            ) {
                Icon(
                    Icons.Rounded.Send,
                    contentDescription = "",
                    tint = if (message.value.isNotEmpty()) Primary else Color.Gray
                )
            }
        },
        maxLines = 3,
        placeholder = {
            Text(
                text = "Write a message", color = Color.Gray
            )
        }
    )
}