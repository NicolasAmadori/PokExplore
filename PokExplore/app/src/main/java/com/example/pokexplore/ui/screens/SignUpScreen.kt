package com.example.pokexplore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.pokexplore.utilities.GeneralTextInput

@Composable
fun SignUpScreen(navController: NavHostController) {
    val userName = remember { mutableStateOf("") }

    val userFirstName = remember { mutableStateOf("") }

    val userLastName = remember { mutableStateOf("") }

    val phoneNumber = remember { mutableStateOf("") }

    val userPassword = remember { mutableStateOf("") }
    var isHiddenPassword by remember { mutableStateOf(true) }

    val userPassword2 = remember { mutableStateOf("") }
    var isHiddenPassword2 by remember { mutableStateOf(true) }
    var isErrorPassword2 by remember { mutableStateOf(false) }

    fun validatePasswordEqualness() {
        isErrorPassword2 = userPassword2.value != userPassword.value
    }

    // Column to arrange UI elements vertically
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(40.dp)) {

        Text(text = stringResource(R.string.signup_title), fontSize = 25.sp, color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 50.dp, 0.dp, 0.dp)
        )

        GeneralTextInput(userName, stringResource(R.string.username_input_label))
        GeneralTextInput(userFirstName, stringResource(R.string.first_name_input_label))
        GeneralTextInput(userLastName, stringResource(R.string.last_name_input_label))

        // Phone Number input field
        OutlinedTextField(value = phoneNumber.value, onValueChange = {
            phoneNumber.value = it
        },
            label = {
                Text(text = stringResource(R.string.phone_input_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        // Password 1 input field
        OutlinedTextField(value = userPassword.value, onValueChange = {
            userPassword.value = it
            validatePasswordEqualness()
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
                        val description = if (isHiddenPassword) stringResource(R.string.show_password) else stringResource(
                            R.string.hide_password)
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            }
        )

        // Password 2 input field
        OutlinedTextField(value = userPassword2.value, onValueChange = {
            userPassword2.value = it
            validatePasswordEqualness()
        },
            label = {
                Text(text = stringResource(R.string.password_confirm_input_label))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            visualTransformation = if (isHiddenPassword2) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = isErrorPassword2,
            trailingIcon = {
                if(userPassword2.value.isNotEmpty()) {
                    IconButton(onClick = { isHiddenPassword2 = !isHiddenPassword2 }) {
                        val visibilityIcon =
                            if (isHiddenPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (isHiddenPassword2) stringResource(R.string.show_password) else stringResource(
                            R.string.hide_password)
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            },
            supportingText = {
                if(isErrorPassword2) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.password_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
        )

        // Login button
        OutlinedButton(onClick = {
            navController.navigate(PokemonRoute.AllPokemonList.route)
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 25.dp, 0.dp, 0.dp)) {
            Text(text = stringResource(R.string.signup_button_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}