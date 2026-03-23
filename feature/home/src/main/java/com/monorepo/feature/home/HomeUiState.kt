package com.monorepo.feature.home

/**
 * UI state for the home screen — shared by both Partner and Rohit apps.
 */
data class HomeUiState(
    val userName: String = "",
    val pendingTaskCount: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
