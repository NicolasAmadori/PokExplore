package com.example.pokexplore.ui.screens.gpsMandatory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.repositories.DataStoreRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class GpsMandatoryState(val countryCode: String?)

class GpsMandatoryViewModelViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel()  {
    var state by mutableStateOf(GpsMandatoryState(null))
        private set

    init {
        viewModelScope.launch {
            state = GpsMandatoryState(dataStoreRepository.countryCode.firstOrNull())
        }
    }

    fun setCountryCode(countryCode: String) {
        state = GpsMandatoryState(countryCode)
        viewModelScope.launch { dataStoreRepository.setCountryCode(countryCode) }
    }
}