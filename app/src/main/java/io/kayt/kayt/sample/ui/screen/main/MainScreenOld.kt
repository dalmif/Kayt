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
import io.kayt.kayt.sample.domain.entity.Device
import io.kayt.kayt.sample.domain.entity.DeviceType
import io.kayt.kayt.sample.domain.entity.User
import io.kayt.kayt.sample.ui.theme.KatalysisTheme

object MainScreenOld {

    @Composable
    fun UsersScreen(viewModel: MainViewModel = viewModel()) {
        val state by viewModel.uiState.collectAsState()
        UsersScreen(
            state,
            onRefreshClicked = viewModel::refreshUsers,
            onItemClicked = viewModel::checkAvailability,
            onDeleteButtonClicked = viewModel::deleteUser,
            onAddFabClicked = {
                // navigate to add screen
            })
    }

    @Composable
    fun UsersScreen(
        state: MainViewModel.UiState,
        onRefreshClicked: () -> Unit,
        onItemClicked: (User) -> Unit,
        onDeleteButtonClicked: (User) -> Unit,
        onAddFabClicked: () -> Unit
    ) {
        Scaffold(
            topBar = {
                ActionbarMenu(
                    modifier = Modifier.statusBarsPadding(),
                    onRefreshClicked = onRefreshClicked
                )
            },
            floatingActionButton = { AddUserFab(onAddFabClicked) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (state) {
                    is MainViewModel.UiState.Success -> UsersList(
                        state.users,
                        onDeleteButtonClicked,
                        onItemClicked
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
        onRefreshClicked: () -> Unit
    ) {
        Row(
            modifier = modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Users", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = modifier.weight(1f))
            IconButton(onClick = onRefreshClicked) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

    }

    @Composable
    fun UsersList(
        users: List<User>,
        onDeleteButtonClicked: (User) -> Unit,
        onItemClicked: (User) -> Unit
    ) {
        LazyColumn {
            items(users, key = { it.id }) {
                UserItem(it, onDeleteButtonClicked, onItemClicked)
            }
        }
    }

    @Composable
    fun UserItem(
        user: User,
        onDeleteButtonClicked: (User) -> Unit,
        onItemClicked: (User) -> Unit
    ) {
        Column(modifier = Modifier.clickable { onItemClicked(user) }) {
            Row(Modifier.padding(vertical = 7.dp)) {
                Text(
                    text = user.id.toString(), fontSize = 30.sp, modifier = Modifier
                        .width(58.dp)
                        .padding(start = 8.dp)
                )
                Column {
                    Text(text = user.firstName)
                    Text(text = user.lastName)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onDeleteButtonClicked(user) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
            Divider()
        }
    }

    @Composable
    fun AddUserFab(onAddFabClicked: () -> Unit) {
        FloatingActionButton(onClick = onAddFabClicked) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        KatalysisTheme {
            val users = listOf(
                User(
                    1, "mohammad", "fallah", "image",
                    Device(DeviceType.iOS, "iPhone 6s", 100)
                )
            )
            UsersScreen(MainViewModel.UiState.Success(users), {}, {}, {}, {})
        }
    }
}