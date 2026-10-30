package com.example.roomdemov2

import com.example.roomdemov2.network.KtorClient
import com.example.roomdemov2.room.FunFact
import com.example.roomdemov2.room.FunFactDao
import kotlinx.coroutines.flow.Flow


class FunFactRepository(
    private val dao: FunFactDao,
    private val api: KtorClient
) {

    fun getFacts(query: String): Flow<List<FunFact>> =
        if (query.isBlank()) dao.getAllFacts() else dao.searchFacts(query.trim())

    suspend fun fetchAndSaveFact() {
        val fact = api.fetchRandomFact()
        dao.insertFact(fact)
    }
}
