package com.example.project_ad_wallet.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_ad_wallet.data.auth.UserRole
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.res.stringResource
import com.example.project_ad_wallet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToUserFeed: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit,
    onNavigateToAdvertiserDashboard: () -> Unit
) {
    val loginSuccess by viewModel.loginSuccess.collectAsState(initial = null)

    LaunchedEffect(loginSuccess) {
        when (loginSuccess) {
            UserRole.USER -> onNavigateToUserFeed()
            UserRole.ADMIN -> onNavigateToAdminDashboard()
            UserRole.ADVERTISER -> onNavigateToAdvertiserDashboard()
            null -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AdWallet",
            color = Color(0xFF3FB950),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text(stringResource(R.string.login_email), color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF238636),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF3FB950),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text(stringResource(R.string.login_password), color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF238636),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF3FB950),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        viewModel.errorMessage?.let { error ->
            val displayError = if (error == "Email ou password incorretos.") {
                stringResource(R.string.login_error)
            } else {
                error
            }
            Text(text = displayError, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator(color = Color(0xFF3FB950))
        } else {
            val btnText = stringResource(R.string.login_button)
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = btnText },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF238636),
                    contentColor = Color.White
                )
            ) {
                Text(btnText, fontWeight = FontWeight.Bold)
            }
        }
    }
}
