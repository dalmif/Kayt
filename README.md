# Kayt
A secure and easy way to handle UI events in Composables. Pass one argument instead of dozens of function type parameter but in a secure and easy way.

## Ui Event
You can define all of the events that can be happened with a sealed interface in a central place so you can maintain them easily. remember that your root interface should implement `UiEvent`.
```kotlin
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
```

## Event Handler
Now you can handle this events inside your ViewModel, So your ViewModel should implement `EventHandler<MainUiEvent>` then you can implment `onEvent` function.
```kotlin
override fun onEvent(event: MainUiEvent) {
    when (event) {
        MainUiEvent.ActionBar.RefreshTapped -> refreshUsers()
        MainUiEvent.AddUserTapped -> { /* navigate */ }
        is MainUiEvent.UserItem.DeleteUserTapped -> deleteUser(event.user)
        is MainUiEvent.UserItem.UserTapped -> checkAvailability(event.user)
    }
}
```
## Create root event inside composable
You should create an object from `Event` to pass it through composables in your screen and we recommand that create this object inside your root composable (the stateful root composable).

```kotlin
val state by vm.uiState.collectAsState()
UsersScreen(
    state,
    events = rememberEvent(vm)
)
```
using `rememberEvent` helps you to keep the same object in recompositions.

## Scoped Events
Now you can pass the events through composables but you shouldn't pass the whole events to the children you need to pass some events that the child needs.
You can scope the events that you want to send to the child composable with `scoped<MainUiEvent.ActionBar>()` function.
```kotlin
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
        }) { /* body */}
}
```

## Fire the event
finally you need to call the ViewModel (Event Handler) for that specific event so you should use `on()` function.

```kotlin
events.on(MainUiEvent.ActionBar.RefreshTapped)
```

## Send the reference of the event
if you have some composable function that needs function type parameter (e.g. `() -> Unit`) you can send a reference from the event with `ref()` function.
```kotlin
IconButton(onClick = events.ref(MainUiEvent.ActionBar.RefreshTapped)) {
    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
}
```

## Reference with parameter
Sometimes you want to send a reference of an event but it is a data class with parameters in its constructor.

consider this event
```kotlin
data class DeleteUserTapped(val user: User) : UserItem
```
and assume you have this composable
```kotlin
@Composable
fun DeleteButton (onDeleteButtonClicked : () -> Unit) {
    IconButton(onClick = onDeleteButtonClicked) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
    }
}
```
now you can't pass the reference of `DeleteUserTapped` for this composable because it needs an instance of `User` (it means `(User)->Unit`) but the composable needs `() -> Unit` you can do this using `ref()` function.
just pass the parameter it needs in the call site then you have a `() -> Unit`.
```kotlin
DeleteButton(onDeleteButtonClicked = events.ref(MainUiEvent.UserItem::DeleteUserTapped::invoke, user))
```


# License

```
Copyright 2024 Mohammad Fallah.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
