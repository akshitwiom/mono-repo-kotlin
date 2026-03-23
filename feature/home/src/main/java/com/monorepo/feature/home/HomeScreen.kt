package com.monorepo.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monorepo.core.designsystem.component.ErrorScreen
import com.monorepo.core.designsystem.component.LoadingIndicator
import com.monorepo.core.designsystem.component.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPendingTasksClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            uiState.errorMessage != null -> ErrorScreen(
                message = uiState.errorMessage.orEmpty(),
                onRetry = viewModel::refresh,
                modifier = Modifier.padding(padding),
            )
            else -> HomeContent(
                uiState = uiState,
                onPendingTasksClick = onPendingTasksClick,
                modifier = modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onPendingTasksClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Welcome, ${uiState.userName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatCard(
            title = "Pending Tasks",
            value = uiState.pendingTaskCount.toString(),
            onClick = onPendingTasksClick,
        )
    }
}
