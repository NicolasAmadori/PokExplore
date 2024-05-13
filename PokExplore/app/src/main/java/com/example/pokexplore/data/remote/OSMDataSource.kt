package com.example.pokexplore.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("address")
    val address: Address
)

@Serializable
data class Address(
    @SerialName("country_code")
    val country_code: String
)

class OSMDataSource(
    private val httpClient: HttpClient
) {
    private val baseUrl = "https://nominatim.openstreetmap.org/reverse?format=json"

    suspend fun getCountryISOCode(latitude: Double, longitude: Double): String {
        return httpClient.get("$baseUrl$&lat=$latitude&lon=$longitude").body<Country>().address.country_code
    }
}