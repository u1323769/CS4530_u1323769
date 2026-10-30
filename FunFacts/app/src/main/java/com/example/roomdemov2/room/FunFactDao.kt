package com.example.roomdemov2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: FunFact)

    @Query("SELECT * FROM fun_facts ORDER BY id DESC")
    fun getAllFacts(): Flow<List<FunFact>>


    @Query("SELECT * FROM fun_facts WHERE text LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchFacts(query: String): Flow<List<FunFact>>
}
