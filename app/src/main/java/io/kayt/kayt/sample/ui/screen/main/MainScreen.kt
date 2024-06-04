package io.kayt.kayt.sample.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.kayt.Event
import io.kayt.rememberEvent
import io.kayt.kayt.sample.domain.entity.Device
import io.kayt.kayt.sample.domain.entity.DeviceType
import io.kayt.kayt.sample.domain.entity.User
import io.kayt.kayt.sample.ui.theme.KatalysisTheme

object MainScreen {

    @Composable
    fun UsersScreen(vm: MainViewModel = viewModel()) {
        val state by vm.uiState.collectAsState()
        UsersScreen(
            state,
            events = rememberEvent(vm) then rememberEvent<MainUiEvent.MainUiNavigationEvent> {
                when(it) {
                    is MainUiEvent.UserItem.UserTapped -> { /* navigate using NavController */}
                }
            }
        )
    }

    @Composable
    fun UsersScreen(
        state: MainViewModel.UiState,
        events: Event<MainUiEvent>,
    ) {
        Scaffold (
            topBar = {
                ActionbarMenu(
                    modifier = Modifier.statusBarsPadding(),
                    events = events.scoped<MainUiEvent.ActionBar>()
                )
            },
            floatingActionButton = { AddUserFab(onAddButtonClicked = events.ref(MainUiEvent.AddUserTapped)) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (state) {
                    is MainViewModel.UiState.Success -> UsersList(
                        state.users,
                        events = events.scoped<MainUiEvent.UserItem>()
                    )

                    MainViewModel.UiState.Loading -> Text(text = "Loading")
                    MainViewModel.UiState.Failed -> Text(text = "Failed")
                }
            }
        }

    }

    @Composable
    fun ActionbarMenu(
        modifier: Modifier,
        events: Event<MainUiEvent.ActionBar>
    ) {
        Row(
            modifier = modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Users", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = modifier.weight(1f))
            IconButton(onClick = events.ref(MainUiEvent.ActionBar.RefreshTapped)) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    }

    @Composable
    fun UsersList(
        users: List<User>,
        events : Event<MainUiEvent.UserItem>
    ) {
        LazyColumn {
            items(users, key = { it.id }) {
                UserItem(it, events)
            }
        }
    }

    @Composable
    fun UserItem(
        user: User,
        events : Event<MainUiEvent.UserItem>
    ) {
        Column(modifier = Modifier.clickable(onClick = {events.on(MainUiEvent.UserItem.UserTapped(user))})) {
            Row(Modifier.padding(vertical = 7.dp)) {
                BigNumberText(user.id)
                Column {
                    Text(text = user.firstName)
                    Text(text = user.lastName)
                }
                Spacer(Modifier.weight(1f))
                DeleteButton(onDeleteButtonClicked = events.ref(MainUiEvent.UserItem::DeleteUserTapped::invoke, user))
            }
            Divider()
        }
    }

    @Composable
    fun BigNumberText (number : Int) {
        Text(
            text = number.toString(), fontSize = 30.sp, modifier = Modifier
                .width(58.dp)
                .padding(start = 8.dp)
        )
    }

    @Composable
    fun DeleteButton (onDeleteButtonClicked : () -> Unit) {
        IconButton(onClick = onDeleteButtonClicked) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }

    @Composable
    fun AddUserFab(onAddButtonClicked: () -> Unit) {
        FloatingActionButton(onClick = onAddButtonClicked) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val users = listOf(User(1, "mohammad","fallah", "image", Device(
            DeviceType.iOS, "iPhone 6s", 100
        )))
        KatalysisTheme {
            UsersScreen(
                MainViewModel.UiState.Success(users = users),
                events = Event {}
            )
        }
    }
}