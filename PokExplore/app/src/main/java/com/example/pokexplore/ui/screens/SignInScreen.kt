package com.example.pokexplore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.ui.PokemonRoute

@Composable
fun SignInScreen(navController: NavHostController) {
    val email = remember { mutableStateOf("")}
    var isErrorEmail by remember { mutableStateOf(false) }

    val userPassword = remember { mutableStateOf("")}
    var isHiddenPassword by remember { mutableStateOf(true) }

    fun validateEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return !regex.matches(email)
    }

    // Column to arrange UI elements vertically
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(40.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            TextButton(onClick = { navController.navigate(PokemonRoute.SignIn.route) }) {
                Text(
                    text = stringResource(R.string.signin_title),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(onClick = { navController.navigate(PokemonRoute.SignUp.route) }) {
                Text(
                    text = stringResource(R.string.signup_title),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        //Email
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
                isErrorEmail = validateEmail(it)
            },
            label = { Text(text = stringResource(R.string.email_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            trailingIcon = {
                if (email.value.isNotEmpty()) {
                    IconButton(onClick = {
                        email.value = ""
                        isErrorEmail = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(R.string.cancel_input),
                        )
                    }
                }
            },
            isError = isErrorEmail,
            supportingText = {
                if (isErrorEmail) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.email_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
        )

        // Password input field
        OutlinedTextField(
            value = userPassword.value,
            onValueChange = {
                userPassword.value = it
            },
            label = {
                Text(text = stringResource(R.string.password_input_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = if (isHiddenPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                if(userPassword.value.isNotEmpty()) {
                    IconButton(onClick = { isHiddenPassword = !isHiddenPassword }) {
                        val visibilityIcon =
                            if (isHiddenPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (isHiddenPassword) stringResource(R.string.show_password) else stringResource(R.string.hide_password)
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            }

        )

        // Login button
        OutlinedButton(onClick = {
             navController.navigate(PokemonRoute.AllPokemonList.route)
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp)) {
            Text(text = stringResource(R.string.signin_button_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}