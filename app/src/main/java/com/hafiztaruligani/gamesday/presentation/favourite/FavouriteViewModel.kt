package com.hafiztaruligani.gamesday.presentation.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafiztaruligani.cryptoday.util.Resource
import com.hafiztaruligani.gamesday.domain.model.GameSimple
import com.hafiztaruligani.gamesday.domain.usecase.GetFavoriteGames
import com.hafiztaruligani.gamesday.presentation.CommonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavoriteGames: GetFavoriteGames
) : ViewModel() {

    private val _uiState = MutableStateFlow( CommonUiState< List<GameSimple> >(loading = true) )
    val uiState: StateFlow< CommonUiState< List<GameSimple> > > = _uiState

    init {
        viewModelScope.launch (Dispatchers.IO) {

            // initiate data and make ui state (depend on resource)
            getFavoriteGames.invoke().collect{ resource ->

                _uiState.value = when(resource){
                    is Resource.Error ->  CommonUiState(error = resource.message)
                    is Resource.Loading -> CommonUiState(loading = true)
                    is Resource.Success -> CommonUiState(data = resource.data)
                }

            }
        }
    }

}