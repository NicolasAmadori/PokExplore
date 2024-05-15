package com.example.pokexplore.ui.screens.allpokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokexplore.data.database.Pokemon
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AllPokemonListState(val pokemonList: List<Pokemon>)

class AllPokemonListViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val databaseRepository: PokExploreRepository
) : ViewModel()  {
    val state = databaseRepository.pokemons.map { AllPokemonListState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = AllPokemonListState(emptyList())
    )

//    //first state whether the search is happening or not
//    val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()
//
//    //second state the text typed by the user
//    val _searchText = MutableStateFlow("")
//    val searchText = _searchText.asStateFlow()
//    //third state the list to be filtered
//    val _countriesList = MutableStateFlow(state.value.pokemonList.map{it.name})
//    val countriesList = searchText
//        .combine(_countriesList) { text, countries ->//combine searchText with _contriesList
//            if (text.isBlank()) { //return the entery list of countries if not is typed
//                countries
//            }
//            countries.filter { country ->// filter and return a list of countries based on the text the user typed
//                country.lowercase().contains(text.trim().lowercase())
//            }
//        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
//            initialValue = _countriesList.value
//        )
//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//    }
//
//    fun onToogleSearch() {
//        _isSearching.value = !_isSearching.value
//        if (!_isSearching.value) {
//            onSearchTextChange("")
//        }
//    }
}