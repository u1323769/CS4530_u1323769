package com.example.roomdemov2.network

import com.example.roomdemov2.room.FunFact
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object KtorClient {

    private const val RANDOM_FACT_URL = "https://uselessfacts.jsph.pl/random.json"

    private val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true   // the API's string "id" etc. are ignored
                    isLenient = true
                })
            }
        }
    }

    suspend fun fetchRandomFact(language: String = "en"): FunFact =
        client.get(RANDOM_FACT_URL) {
            parameter("language", language)
        }.body()
}
