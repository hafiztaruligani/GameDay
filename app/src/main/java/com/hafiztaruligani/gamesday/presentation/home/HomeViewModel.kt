package com.hafiztaruligani.gamesday.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.domain.usecase.GetGamesPaged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    FlowPreview::class
)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGamesPaged: GetGamesPaged
) : ViewModel() {

    // this query variable is two way data binding (fragment home layout)
    val query = MutableStateFlow("")
    val isQueryNotEmpty = query.map { it.isNotEmpty() }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // collecting games, depending on query change (delay 1s between changes)
    val games : Flow<PagingData<GameSimple>> =
        query.debounce(1000).flatMapLatest {
            getGamesPaged.invoke(it)
        }.cachedIn(viewModelScope)

    fun reset() {
        query.value = ""
    }

}