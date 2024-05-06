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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.data.database.User
import com.example.pokexplore.ui.PokExploreViewModel
import com.example.pokexplore.ui.PokemonRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(navController: NavHostController) {
    val vm = koinViewModel<PokExploreViewModel>()

    val email = remember { mutableStateOf("") }
    var isErrorEmail by remember { mutableStateOf(false) }

    val userFirstName = remember { mutableStateOf("") }
    var isErrorFirstName by remember { mutableStateOf(false) }

    val userLastName = remember { mutableStateOf("") }
    var isErrorLastName by remember { mutableStateOf(false) }

    val phoneNumber = remember { mutableStateOf("") }
    var isErrorPhone by remember { mutableStateOf(false) }

    val userPassword = remember { mutableStateOf("") }
    var isHiddenPassword by remember { mutableStateOf(true) }

    val userPassword2 = remember { mutableStateOf("") }
    var isHiddenPassword2 by remember { mutableStateOf(true) }
    var isErrorPassword2 by remember { mutableStateOf(false) }

    fun validatePasswordEqualness() {
        isErrorPassword2 = userPassword2.value.isNotEmpty() && userPassword2.value != userPassword.value
    }

    fun validatePhoneNumber(text: String): Boolean {
        return text.isNotEmpty() && text.any { c-> !c.isDigit()}
    }

    fun validateString(text: String): Boolean {
        return text.isEmpty() || text.any { c-> !c.isLetter()}
    }

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
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(onClick = { navController.navigate(PokemonRoute.SignUp.route) }) {
                Text(
                    text = stringResource(R.string.signup_title),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.primary
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

        //First Name
        OutlinedTextField(
            value = userFirstName.value,
            onValueChange = {
                userFirstName.value = it
                isErrorFirstName = validateString(it)
            },
            label = { Text(text = stringResource(R.string.first_name_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            trailingIcon = {
                if (userFirstName.value.isNotEmpty()) {
                    IconButton(onClick = {
                        userFirstName.value = ""
                        isErrorFirstName = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(R.string.cancel_input),
                        )
                    }
                }
            },
            isError = isErrorFirstName,
            supportingText = {
                if (isErrorFirstName) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.first_name_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
        )

        //Last Name
        OutlinedTextField(
            value = userLastName.value,
            onValueChange = {
                userLastName.value = it
                isErrorLastName = validateString(it)
            },
            label = { Text(text = stringResource(R.string.last_name_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            trailingIcon = {
                if (userLastName.value.isNotEmpty()) {
                    IconButton(onClick = {
                        userLastName.value = ""
                        isErrorLastName = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(R.string.cancel_input),
                        )
                    }
                }
            },
            isError = isErrorLastName,
            supportingText = {
                if (isErrorLastName) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.last_name_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
        )

        //Phone number
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
                isErrorPhone = validatePhoneNumber(it)
            },
            label = { Text(text = stringResource(R.string.phone_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            trailingIcon = {
                if (phoneNumber.value.isNotEmpty()) {
                    IconButton(onClick = {
                        phoneNumber.value = ""
                        isErrorPhone = false
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(R.string.cancel_input),
                        )
                    }
                }
            },
            isError = isErrorPhone,
            supportingText = {
                if (isErrorPhone) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.phone_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
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

        // Signup button
        OutlinedButton(onClick = {
            if(!isErrorEmail && !isErrorFirstName && !isErrorLastName && !isErrorPhone && !isErrorPassword2) {
                vm.actions.addUser(User(
                    email = email.value,
                    password = userPassword.value,
                    firstName = userFirstName.value,
                    lastName = userLastName.value,
                    phone = 0,
                    profilePicUrl = ""
                ))
                navController.navigate(PokemonRoute.AllPokemonList.route)
            }
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