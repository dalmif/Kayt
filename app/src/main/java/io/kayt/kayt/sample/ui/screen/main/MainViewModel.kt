package io.kayt.kayt.sample.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import io.kayt.EventHandler
import io.kayt.UiEvent
import io.kayt.kayt.sample.data.repository.UserRepositoryImpl
import io.kayt.kayt.sample.domain.entity.User
import io.kayt.kayt.sample.domain.repository.UserRepository

class MainViewModel : ViewModel(), EventHandler<MainUiEvent> {

    private val userRepository : UserRepository = UserRepositoryImpl()

    val uiState: StateFlow<UiState> = userRepository.getUsers()
        .map { UiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UiState.Loading
        )

    fun refreshUsers () {
        // TODO: Refresh user list
    }

    fun checkAvailability (user : User) {
        // TODO: Check availability of user
    }

    fun deleteUser (user : User) {
        viewModelScope.launch {
            userRepository.removeUser(user.id)
        }
    }

    sealed interface UiState {
        data class Success(val users : List<User>) : UiState
        data object Loading : UiState
        data object Failed : UiState
    }

    override fun onEvent(event: MainUiEvent) {
        when (event) {
            MainUiEvent.ActionBar.RefreshTapped -> refreshUsers()
            MainUiEvent.AddUserTapped -> { /* navigate */ }
            is MainUiEvent.UserItem.DeleteUserTapped -> deleteUser(event.user)
            is MainUiEvent.UserItem.UserTapped -> checkAvailability(event.user)
        }
    }
}

sealed interface MainUiEvent : UiEvent {
    sealed interface MainUiNavigationEvent : MainUiEvent

    data object AddUserTapped : MainUiEvent

    sealed interface ActionBar : MainUiEvent {
        data object RefreshTapped : ActionBar
    }

    sealed interface UserItem : MainUiEvent {
        data class UserTapped(val user: User) : UserItem, MainUiNavigationEvent
        data class DeleteUserTapped(val user: User) : UserItem
    }
}