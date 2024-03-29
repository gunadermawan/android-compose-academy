package com.gunder.compose.drawer

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gunder.compose.R
import com.gunder.compose.ui.theme.ComposeTheme
import kotlinx.coroutines.launch

@Composable
fun DrawerApp() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MyTopBar(onMenuClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            })
        },
        drawerContent = {
            DrawerContent(onItemSelected = {
                scope.launch {
                    scaffoldState.drawerState.close()
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.resources.getString(
                            R.string.coming_soon,
                            it
                        ),
                        actionLabel = context.resources.getString(R.string.subscribe_question)
                    )
                    if (snackbarResult == SnackbarResult.ActionPerformed) {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.subscribed_info),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, onBackPressed = {
                if (scaffoldState.drawerState.isOpen) {
                    scope.launch { scaffoldState.drawerState.close() }
                }
            })
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen
    ) { paddingValues ->
        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(stringResource(R.string.hello_world))
        }
    }
}

@Composable
fun MyTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onMenuClick }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        title = { Text(stringResource(R.string.app_name)) }
    )
}

data class MenuItem(val title: String, val icon: ImageVector)

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: String) -> Unit,
    onBackPressed: () -> Unit
) {
    val items = listOf(
        MenuItem(title = stringResource(R.string.home), icon = Icons.Default.Home),
        MenuItem(title = stringResource(R.string.favourite), icon = Icons.Default.Favorite),
        MenuItem(title = stringResource(R.string.profile), icon = Icons.Default.AccountCircle),
    )
    Column(modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(190.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        for (item in items) {
            Row(modifier = Modifier
                .clickable { onItemSelected(item.title) }
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = item.title, style = MaterialTheme.typography.subtitle2)
            }
        }
        Divider()
    }
    BackPressHandler { onBackPressed }
}

@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallBack = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }

        }
    }
    SideEffect {
        backCallBack.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifeCycleCallback = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleCallback, backCallBack) {
        onDispose {
            backCallBack.remove()
        }
    }
}

@Preview
@Composable
fun DrawerAppPreview() {
    ComposeTheme {
        DrawerApp()
    }
}
