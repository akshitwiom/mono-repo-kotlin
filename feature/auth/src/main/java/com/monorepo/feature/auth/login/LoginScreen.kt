package com.monorepo.feature.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monorepo.core.model.user.UserRole

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(LoginUiEvent.ClearError)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LoginContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding(),
        )
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val appName = when (uiState.role) {
            UserRole.PARTNER -> "Connection Provider"
            UserRole.INSTALLER -> "Connection Setup"
        }

        Text(
            text = appName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        val roleLabel = when (uiState.role) {
            UserRole.PARTNER -> "Partner Login"
            UserRole.INSTALLER -> "Installer Login"
        }
        Text(
            text = roleLabel,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(40.dp))

        when (uiState.loginMode) {
            LoginMode.PHONE_OTP -> PhoneOtpForm(uiState = uiState, onEvent = onEvent)
            LoginMode.USERNAME_PASSWORD -> UsernamePasswordForm(uiState = uiState, onEvent = onEvent)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onEvent(LoginUiEvent.ToggleLoginMode) }) {
            val label = when (uiState.loginMode) {
                LoginMode.PHONE_OTP -> "Use Username & Password instead"
                LoginMode.USERNAME_PASSWORD -> "Use Phone & OTP instead"
            }
            Text(text = label)
        }
    }
}

@Composable
private fun PhoneOtpForm(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = { onEvent(LoginUiEvent.PhoneChanged(it)) },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible = !uiState.isOtpSent) {
            OutlinedButton(
                onClick = { onEvent(LoginUiEvent.RequestOtp) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && uiState.phone.isNotBlank(),
            ) {
                Text(text = "Send OTP")
            }
        }

        AnimatedVisibility(visible = uiState.isOtpSent) {
            Column {
                OutlinedTextField(
                    value = uiState.otp,
                    onValueChange = { if (it.length <= 6) onEvent(LoginUiEvent.OtpChanged(it)) },
                    label = { Text("Enter OTP") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(LoginUiEvent.SubmitLogin) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading && uiState.otp.length == 6,
                ) {
                    Text(text = if (uiState.isLoading) "Verifying…" else "Verify & Login")
                }
            }
        }
    }
}

@Composable
private fun UsernamePasswordForm(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onEvent(LoginUiEvent.SubmitLogin) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.username.isNotBlank() && uiState.password.isNotBlank(),
        ) {
            Text(text = if (uiState.isLoading) "Logging in…" else "Login")
        }
    }
}
