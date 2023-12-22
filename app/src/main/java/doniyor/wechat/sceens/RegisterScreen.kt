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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import doniyor.wechat.R
import doniyor.wechat.api.Firebase
import doniyor.wechat.model.User
import doniyor.wechat.navigation.Screens
import doniyor.wechat.ui.theme.Background
import doniyor.wechat.ui.theme.Black20
import doniyor.wechat.ui.theme.Primary
import doniyor.wechat.ui.theme.Secondary
import doniyor.wechat.ui.theme.Text2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current

    val passwordVisibility = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    val errorText1 = "Username can not be empty"
    val errorText2 = "Username can contain only letters and numbers"
    val errorText3 = "Username is already taken"
    val errorText4 = "Username can not start with a number"

    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val fullName = remember {
        mutableStateOf("")
    }
    val fullNameError = remember {
        mutableStateOf(false)
    }
    val usernameError = remember {
        mutableStateOf(false)
    }
    val usernameErrorText = remember {
        mutableStateOf(errorText1)
    }
    val passwordError = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(42.dp))
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Super app logo",
            Modifier.height(100.dp)
        )
        Spacer(modifier = Modifier.height(42.dp))
        TextField(value = fullName.value,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onValueChange = {
                fullName.value = it
                fullNameError.value = fullName.value.isEmpty()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text(text = "Fullname", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Black20
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
            isError = fullNameError.value,
            supportingText = {
                if (fullNameError.value) Text(
                    text = "Fullname can not be empty",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = username.value,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onValueChange = { it ->
                username.value = it.trim()

                if (username.value.isEmpty()) {
                    usernameError.value = true
                    usernameErrorText.value = errorText1
                } else if (!username.value.all { it.isLetterOrDigit() }) {
                    usernameError.value = true
                    usernameErrorText.value = errorText2
                } else {
                    if (it.toCharArray()[0].isDigit()) {
                        usernameError.value = true
                        usernameErrorText.value = errorText4
                    } else {
                        usernameError.value = false
                        Firebase.usernameAvailable(username.value) { usernameExists ->
                            if (!usernameExists) {
                                usernameError.value = true
                                usernameErrorText.value = errorText3
                            }
                        }
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(text = "Username", fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "",
                    tint = Black20
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
            isError = usernameError.value,
            supportingText = {
                if (usernameError.value) Text(
                    text = usernameErrorText.value,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = password.value,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onValueChange = {
                password.value = it.trim()
                val invalidPassword = password.value.length < 8
                passwordError.value = invalidPassword
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(text = "Password", fontSize = 14.sp)
            },
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
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility.value) R.drawable.password_toggle_hide
                else R.drawable.password_toggle

                val description = if (passwordVisibility.value) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(painter = painterResource(id = image), description, tint = Black20)
                }
            },
            isError = passwordError.value,
            supportingText = {
                if (passwordError.value) Text(
                    text = "Password must have at least 8 characters",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            })
        Spacer(modifier = Modifier.height(42.dp))
        Button(
            enabled = username.value.isNotEmpty() && fullName.value.isNotEmpty() && password.value.isNotEmpty() && !usernameError.value && !fullNameError.value && !passwordError.value,
            onClick = {
                val user = User(username.value, password.value, fullName.value, "", "")
                Firebase.register(user, context) { success ->
                    if (success) {
                        Toast.makeText(context, "Successfully signed up", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.Home.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
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
                text = "Sign up",
                modifier = Modifier.padding(6.dp),
                color = Color.White,
                fontSize = 14.sp
            )
        }

        Row {
            Text(
                text = "Do you already have an account?",
                modifier = Modifier.padding(6.dp),
                color = Color.Black,
                fontSize = 14.sp
            )
            Text(
                text = "Log in",
                modifier = Modifier
                    .padding(6.dp)
                    .clickable {
                        navController.navigate(Screens.Login.route)
                    },
                color = Primary,
                fontSize = 14.sp
            )
        }
    }
}