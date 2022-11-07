package com.hafiztaruligani.gamesday.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.domain.model.GameDetail
import com.hafiztaruligani.gamesday.domain.usecase.GetGameDetail
import com.hafiztaruligani.gamesday.domain.usecase.SetFavorite
import com.hafiztaruligani.gamesday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGameDetail: GetGameDetail,
    private val setFavorite: SetFavorite
) : ViewModel() {


    private val _uiState = MutableStateFlow( CommonUiState<GameDetail>(loading = true) )
    val uiState : StateFlow< CommonUiState <GameDetail> > = _uiState

    // collecting data and make ui state (depend on resource)
    fun submitId(gameId: Int)= viewModelScope.launch (Dispatchers.IO) {
        getGameDetail.invoke(gameId).collect { resource ->
            when (resource){
                is Resource.Error -> _uiState.value = CommonUiState(error = resource.message, data = resource.data)
                is Resource.Loading -> _uiState.value = CommonUiState(loading = true)
                is Resource.Success -> _uiState.value = CommonUiState(data = resource.data)
            }
        }
    }

    // to change/update favorite data.
    // not using viewModel scope because viewModel scope is canceled if the job is not finished
    // and the fragment is destroyed
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun onClickFavorite() = coroutineScope.launch {
        val data = _uiState.value.data
        data?.let {
            setFavorite.invoke(
                data.id,
                !data.favorite
            )
        }
    }

}