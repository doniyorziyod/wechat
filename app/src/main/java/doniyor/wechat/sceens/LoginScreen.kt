package doniyor.wechat.sceens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import doniyor.wechat.R
import doniyor.wechat.api.Firebase
import doniyor.wechat.api.SharedPreference
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Background
import doniyor.wechat.ui.theme.Primary
import doniyor.wechat.ui.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisibility = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Super app logo",
            Modifier.height(100.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(modifier = Modifier.padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = username.value,
            singleLine = true,
            onValueChange = { username.value = it },
            placeholder = { Text("Username") })

        TextField(modifier = Modifier.padding(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                containerColor = Secondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            value = password.value,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { password.value = it },
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility.value) R.drawable.password_toggle_hide
                else R.drawable.password_toggle

                val description =
                    if (passwordVisibility.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(painter = painterResource(id = image), description, tint = Secondary)
                }
            })

        Button(
            onClick = {
                if (username.value.isNotEmpty() && password.value.length > 7) {
                    Firebase.signIn(username.value, password.value) {
                        if (it == null) {
                            Toast.makeText(
                                context,
                                "Login yoki parol noto'g'ri",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            SharedPreference.getInstance(context).saveKey(it)
                            navController.navigate(Screens.Home.route)
                        }
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 100.dp, vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)

        ) {
            Text(
                text = "Log in",
                modifier = Modifier.padding(6.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Row {
            Text(
                text = "Do not have an account?",
                modifier = Modifier.padding(6.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = "Sign up",
                modifier = Modifier
                    .padding(6.dp)
                    .clickable {
                        navController.navigate(Screens.Regist.route)
                    },
                color = Primary,
                fontSize = 14.sp
            )
        }
    }
}





