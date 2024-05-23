package com.example.pokexplore.ui.screens.changePassword

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokexplore.R
import com.example.pokexplore.ui.screens.settings.ChangePasswordActions
import com.example.pokexplore.ui.screens.settings.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    userState: UserState,
    actions: ChangePasswordActions
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.change_password),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {innerPadding ->
        if(userState.user != null) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
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
                        .padding(40.dp, 20.dp, 40.dp, 0.dp),
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
                        .padding(40.dp, 20.dp, 40.dp, 0.dp),
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
                    isErrorPassword = validatePassword(password.value)
                    isErrorPassword2 = validatePassword(password2.value) || validatePasswordEqualness(password.value, password2.value)

                    if(isErrorPassword || isErrorPassword2) {
                        Toast.makeText(context, R.string.field_error, Toast.LENGTH_LONG).show()
                    }
                    else {
                        actions.setPassword(userState.user.email, password.value)
                        password.value = ""
                        password2.value = ""
                        Toast.makeText(context, R.string.password_changed_message, Toast.LENGTH_LONG).show()
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp, 25.dp, 40.dp, 0.dp)) {
                    Text(text = stringResource(R.string.change_password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
