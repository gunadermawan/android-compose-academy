package com.gunder.compose.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gunder.compose.R
import com.gunder.compose.ui.theme.ComposeTheme

@Composable
fun DrawerApp() {
    Scaffold(topBar = { MyTopBar(onMenuClick = {}) }) { paddingValues ->
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

@Preview
@Composable
fun DrawerAppPreview() {
    ComposeTheme {
        DrawerApp()
    }
}
