package doniyor.wechat.sceens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import doniyor.wechat.api.Firebase
import doniyor.wechat.api.SharedHelper
import doniyor.wechat.model.User
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Black20
import doniyor.wechat.ui.theme.Primary
import doniyor.wechat.ui.theme.Secondary
import doniyor.wechat.ui.theme.Text2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val password = remember {
        mutableStateOf("")
    }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.padding(16.dp).fillMaxWidth()) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "")
            }
        }
        TextField(
            value = password.value,
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp),
            onValueChange = {
                password.value = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text(text = "New password", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock, contentDescription = "", tint = Black20
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Text2,
                containerColor = Secondary
            ),
            textStyle = TextStyle(fontSize = 16.sp),
        )
        Button(
            enabled = password.value.length > 7, onClick = {
                Firebase.updatePassword(context, password.value) {
                    if (it) {
                        navController.popBackStack()
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = Secondary
            )

        ) {
            Text(
                text = "Change Password",
                modifier = Modifier.padding(6.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }
        Button(
            onClick = {
                SharedHelper.getInstance(context).logOut()
                navController.navigate(Screens.Login.route){
                    popUpTo(navController.graph.id){
                        inclusive = true
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                disabledContainerColor = Secondary
            )

        ) {
            Text(
                text = "Log out",
                modifier = Modifier.padding(6.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}