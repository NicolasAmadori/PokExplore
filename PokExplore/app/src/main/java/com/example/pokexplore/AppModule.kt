package com.example.pokexplore

import com.example.pokexplore.data.remote.PokeApiDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

import androidx.room.Room
import com.example.pokexplore.data.database.PokExploreDatabase
import com.example.pokexplore.data.repositories.PokExploreRepository
import com.example.pokexplore.ui.PokExploreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

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

    single { PokeApiDataSource(get()) }

    /* Room */
    single {
        Room.databaseBuilder(
            get(),
            PokExploreDatabase::class.java,
            "pokexplore"
        ).build()
    }

    single { PokExploreRepository(
        get<PokExploreDatabase>().pokemonDAO(),
        get<PokExploreDatabase>().userDAO(),
        get<PokExploreDatabase>().userPokemonDAO(),
        ) }

    viewModel { PokExploreViewModel(get()) }
}
