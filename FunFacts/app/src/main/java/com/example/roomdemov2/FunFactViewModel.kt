package com.example.roomdemov2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.roomdemov2.room.FunFact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FunFactViewModel(private val repository: FunFactRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    @OptIn(ExperimentalCoroutinesApi::class)
    val facts: StateFlow<List<FunFact>> = _query
        .flatMapLatest { repository.getFacts(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(newQuery: String) {
        _query.update { newQuery }
    }

    fun fetchNewFact() {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.fetchAndSaveFact()
            } catch (e: Exception) {
                _error.value = "Couldn't fetch a fact. Check your connection and try again."
            } finally {
                _isLoading.value = false
            }
        }
    }
}

class FunFactViewModelFactory(
    private val repository: FunFactRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FunFactViewModel::class.java)) {
            return FunFactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
