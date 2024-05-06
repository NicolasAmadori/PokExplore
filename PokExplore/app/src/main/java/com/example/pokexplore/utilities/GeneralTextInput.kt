package com.example.pokexplore.utilities

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pokexplore.R

@Composable
fun GeneralTextInput(variable : MutableState<String>, label : String) {
    OutlinedTextField(value = variable.value, onValueChange = {
        variable.value = it
    },
        label = {
            Text(text = label)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 20.dp, 0.dp, 0.dp),
        singleLine = true,
        trailingIcon = {
            if(variable.value.isNotEmpty()) {
                IconButton(onClick = { variable.value = "" }) {
                    Icon(imageVector = Icons.Filled.Cancel, contentDescription = stringResource(R.string.cancel_input))
                }
            }
        }
    )
}