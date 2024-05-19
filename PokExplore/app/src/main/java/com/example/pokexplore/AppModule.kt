package com.example.pokexplore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.pokexplore.data.database.PokExploreDatabase
import com.example.pokexplore.data.remote.OSMDataSource
import com.example.pokexplore.data.repositories.DataStoreRepository
import com.example.pokexplore.data.repositories.PokExploreRepository
import com.example.pokexplore.ui.PokExploreViewModel
import com.example.pokexplore.ui.screens.allpokemonlist.AllPokemonListViewModel
import com.example.pokexplore.ui.screens.gpsMandatory.GpsMandatoryViewModelViewModel
import com.example.pokexplore.ui.screens.loading.LoadingViewModel
import com.example.pokexplore.ui.screens.pokemonDetails.PokemonDetailsViewModel
import com.example.pokexplore.ui.screens.profile.ProfileViewModel
import com.example.pokexplore.ui.screens.signin.SignInViewModel
import com.example.pokexplore.ui.screens.signup.SignUpViewModel
import com.example.pokexplore.ui.screens.theme.ThemeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("user_preferences")

val appModule = module {
    /* HTTP */
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { OSMDataSource(get()) }

    /* Room */
    single {
        Room.databaseBuilder(
            get(),
            PokExploreDatabase::class.java,
            "pokexplore"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<Context>().dataStore }

    single { PokExploreRepository(
        get<PokExploreDatabase>().pokemonDAO(),
        get<PokExploreDatabase>().userDAO(),
        get<PokExploreDatabase>().userPokemonDAO(),
        get<PokExploreDatabase>().userWithPokemonsDao()
    ) }

    single { DataStoreRepository(get()) }

    viewModel { GpsMandatoryViewModelViewModel(get()) }
    viewModel { PokExploreViewModel(get()) }
    viewModel { LoadingViewModel(get()) }
    viewModel { AllPokemonListViewModel(get(), get()) }
    viewModel { PokemonDetailsViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
}
