package com.example.pokexplore.ui.screens.signup

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.data.database.User
import com.example.pokexplore.ui.PokemonRoute

@Composable
fun SignUpScreen(
    navController: NavHostController,
    onUserSignUp: (User) -> Unit
) {
    val context = LocalContext.current

    val email = remember { mutableStateOf("") }
    var isErrorEmail by remember { mutableStateOf(false) }

    val firstName = remember { mutableStateOf("") }
    var isErrorFirstName by remember { mutableStateOf(false) }

    val lastName = remember { mutableStateOf("") }
    var isErrorLastName by remember { mutableStateOf(false) }

    val phoneNumber = remember { mutableStateOf("") }
    var isErrorPhone by remember { mutableStateOf(false) }

    val password = remember { mutableStateOf("") }
    var isHiddenPassword by remember { mutableStateOf(true) }
    var isErrorPassword by remember { mutableStateOf(false) }

    val password2 = remember { mutableStateOf("") }
    var isHiddenPassword2 by remember { mutableStateOf(true) }
    var isErrorPassword2 by remember { mutableStateOf(false) }

    fun validatePassword(password: String): Boolean {
        return password.length < 5
    }

    fun validatePasswordEqualness(password1: String, password2: String): Boolean {
        return password2.isNotEmpty() && password2 != password1
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
                if(isErrorEmail) {
                    isErrorEmail = validateEmail(it)
                }
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
                        isErrorEmail = false
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
            value = firstName.value,
            onValueChange = {
                firstName.value = it
                if(isErrorFirstName) {
                    isErrorFirstName = validateString(it)
                }
            },
            label = { Text(text = stringResource(R.string.first_name_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            trailingIcon = {
                if (firstName.value.isNotEmpty()) {
                    IconButton(onClick = {
                        firstName.value = ""
                        isErrorFirstName = false
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
            value = lastName.value,
            onValueChange = {
                lastName.value = it
                if(isErrorLastName) {
                    isErrorLastName = validateString(it)
                }
            },
            label = { Text(text = stringResource(R.string.last_name_input_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = true,
            trailingIcon = {
                if (lastName.value.isNotEmpty()) {
                    IconButton(onClick = {
                        lastName.value = ""
                        isErrorLastName = false
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
                if(isErrorPhone) {
                    isErrorPhone = validatePhoneNumber(it)
                }
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
        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
            if(isErrorPassword) {
                isErrorPassword = validatePassword(it)
            }
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
                if(password.value.isNotEmpty()) {
                    IconButton(onClick = { isHiddenPassword = !isHiddenPassword }) {
                        val visibilityIcon =
                            if (isHiddenPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (isHiddenPassword) stringResource(R.string.show_password) else stringResource(
                            R.string.hide_password)
                        Icon(imageVector = visibilityIcon, contentDescription = description)
                    }
                }
            },
            isError = isErrorPassword,
            supportingText = {
                if(isErrorPassword) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.length_password_error),
                        textAlign = TextAlign.Left,
                    )
                }
            },
        )

        // Password 2 input field
        OutlinedTextField(value = password2.value, onValueChange = {
            password2.value = it
            if(isErrorPassword2) {
                isErrorPassword2 = validatePassword(it) || validatePasswordEqualness(password.value, it)
            }
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
            trailingIcon = {
                if(password2.value.isNotEmpty()) {
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
            isError = isErrorPassword2,
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
            isErrorEmail = validateEmail(email.value)
            isErrorFirstName = validateString(firstName.value)
            isErrorLastName = validateString(lastName.value)
            isErrorPhone = validatePhoneNumber(phoneNumber.value)
            isErrorPassword = validatePassword(password.value)
            isErrorPassword2 = validatePassword(password2.value) || validatePasswordEqualness(password.value, password2.value)

            if(isErrorEmail || isErrorFirstName || isErrorLastName || isErrorPhone || isErrorPassword || isErrorPassword2) {
                Toast.makeText(context, R.string.field_error, Toast.LENGTH_LONG).show()
            }
            else {
                val newUser = User(
                    email = email.value,
                    password = password.value,
                    firstName = firstName.value,
                    lastName = lastName.value,
                    phone = phoneNumber.value.toInt(),
                    profilePicUrl = null
                )
                onUserSignUp(newUser)
                navController.navigate(PokemonRoute.Loading.route)
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