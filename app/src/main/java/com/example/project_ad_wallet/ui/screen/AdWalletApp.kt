package com.example.project_ad_wallet.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_ad_wallet.domain.model.Ad
import com.example.project_ad_wallet.domain.model.AppUiState
import com.example.project_ad_wallet.domain.model.Charity
import com.example.project_ad_wallet.domain.model.DonationBannerType
import com.example.project_ad_wallet.domain.model.ThemeMode
import com.example.project_ad_wallet.ui.theme.WalletGray
import com.example.project_ad_wallet.ui.viewmodel.WalletViewModel

private sealed class Screen(val route: String) {
    data object Registration : Screen("registration")
    data object Feed : Screen("feed")
    data object Charities : Screen("charities")
    data object Settings : Screen("settings")
    data object Survey : Screen("survey/{adId}") {
        fun createRoute(adId: String): String = "survey/$adId"
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)

@Composable
fun AdWalletApp(
    viewModel: WalletViewModel,
    uiState: AppUiState,
) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Feed.route, "Home") {
            Icon(Icons.Default.Home, contentDescription = "Home")
        },
        BottomNavItem(Screen.Charities.route, "Wallet") {
            Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet")
        },
        BottomNavItem(Screen.Settings.route, "Profile") {
            Icon(Icons.Default.Person, contentDescription = "Profile")
        },
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Registration.route,
    ) {
        composable(Screen.Registration.route) {
            RegistrationScreen(
                onRegister = { fullName, age, gender ->
                    viewModel.registerUser(fullName, age, gender)
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Registration.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen(
                uiState = uiState,
                navController = navController,
                bottomNavItems = bottomNavItems,
                onAdClick = { ad ->
                    navController.navigate(Screen.Survey.createRoute(ad.id))
                }
            )
        }

        composable(
            route = Screen.Survey.route,
            arguments = listOf(navArgument("adId") { type = NavType.StringType })
        ) { entry ->
            val adId = entry.arguments?.getString("adId").orEmpty()
            val ad = viewModel.getAd(adId)
            SurveyScreen(
                uiState = uiState,
                ad = ad,
                onBack = { navController.popBackStack() },
                onSubmit = {
                    if (ad != null) {
                        viewModel.claimSurveyReward(ad.id)
                    }
                }
            )
        }

        composable(Screen.Charities.route) {
            CharitiesScreen(
                uiState = uiState,
                navController = navController,
                bottomNavItems = bottomNavItems,
                onDonate = { charityId, amount ->
                    viewModel.donateToCharity(charityId, amount)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                uiState = uiState,
                navController = navController,
                bottomNavItems = bottomNavItems,
                onProfileSaved = viewModel::updateUserProfile,
                onThemeModeChanged = viewModel::setThemeMode,
                onAddFunds = { viewModel.addFunds() },
                onResetApp = {
                    viewModel.resetAppState()
                    navController.navigate(Screen.Registration.route) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreen(
    onRegister: (String, String, String) -> Unit,
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val genders = listOf("Male", "Female", "Other", "Prefer not to say")
    val buttonEnabled = fullName.isNotBlank() && age.isNotBlank() && gender.isNotBlank()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Join AdWallet",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Create your account to start earning",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Full Name")
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { input ->
                            fullName = input.filter { it.isLetter() || it.isWhitespace() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("registration_full_name"),
                        placeholder = { Text("Enter your full name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = adWalletTextFieldColors(),
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Age")
                    OutlinedTextField(
                        value = age,
                        onValueChange = { input ->
                            age = input.filter { it.isDigit() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("registration_age"),
                        placeholder = { Text("Enter your age") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = adWalletTextFieldColors(),
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Gender")
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("registration_gender"),
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("Select your gender") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = adWalletTextFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genders.forEach { option ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onRegister(fullName, age, gender) },
                enabled = buttonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("registration_button"),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledContentColor = Color.White.copy(alpha = 0.60f),
                )
            ) {
                Text(
                    text = "REGISTER",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                )
            }
        }
    }
}

@Composable
private fun FeedScreen(
    uiState: AppUiState,
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    onAdClick: (Ad) -> Unit,
) {
    Scaffold(
        topBar = {
            AdWalletTopAppBar(balance = uiState.balance)
        },
        bottomBar = {
            AdWalletBottomBar(
                navController = navController,
                items = bottomNavItems
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.ads, key = { it.id }) { ad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAdClick(ad) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = ad.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Earn ${ad.reward.asCurrency()}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SurveyScreen(
    uiState: AppUiState,
    ad: Ad?,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
) {
    val context = LocalContext.current
    var answerOne by rememberSaveable { mutableStateOf<String?>(null) }
    var answerTwo by rememberSaveable { mutableStateOf<String?>(null) }
    val submitEnabled = answerOne != null && answerTwo != null && ad != null

    Scaffold(
        topBar = {
            AdWalletTopAppBar(
                balance = uiState.balance,
                showBack = true,
                onBack = onBack,
            )
        }
    ) { innerPadding ->
        if (ad == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Ad not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = ad.imagePlaceholder,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Complete this survey to earn ${ad.reward.asCurrency()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    modifier = Modifier.testTag("survey_title"),
                    text = "How relevant was this ad?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(12.dp))
                listOf("Not Relevant", "Somewhat Relevant", "Very Relevant").forEach { option ->
                    SurveyOptionRow(
                        text = option,
                        selected = answerOne == option,
                        onClick = { answerOne = option }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Would you consider this brand?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(12.dp))
                listOf("No, unlikely", "Maybe", "Yes, likely").forEach { option ->
                    SurveyOptionRow(
                        text = option,
                        selected = answerTwo == option,
                        onClick = { answerTwo = option }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onSubmit()
                        Toast.makeText(context, "Success! Reward claimed.", Toast.LENGTH_SHORT).show()
                        onBack()
                    },
                    enabled = submitEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("survey_submit_button"),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                        disabledContentColor = Color.White.copy(alpha = 0.60f),
                    )
                ) {
                    Text(
                        text = "SUBMIT & CLAIM ${ad.reward.asCurrency()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun CharitiesScreen(
    uiState: AppUiState,
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    onDonate: (String, Double) -> Unit,
) {
    var selectedCharity by remember { mutableStateOf<Charity?>(null) }
    var donationAmount by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            AdWalletTopAppBar(balance = uiState.balance)
        },
        bottomBar = {
            AdWalletBottomBar(navController = navController, items = bottomNavItems)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.donationBanner?.let { banner ->
                item {
                    val containerColor = if (banner.type == DonationBannerType.Success) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                    val contentColor = if (banner.type == DonationBannerType.Success) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = containerColor,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = banner.message,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            color = contentColor,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            items(uiState.charities, key = { it.id }) { charity ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCharity = charity
                            donationAmount = ""
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = charity.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = charity.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                        ) {
                            Text(
                                text = charity.sdg,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }

    selectedCharity?.let { charity ->
        val parsedAmount = donationAmount.toDoubleOrNull()
        val confirmEnabled = parsedAmount != null && parsedAmount > 0.0 && parsedAmount <= uiState.balance

        AlertDialog(
            onDismissRequest = { selectedCharity = null },
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "Donate to ${charity.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "How much would you like to donate?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = donationAmount,
                        onValueChange = { input ->
                            donationAmount = input.filterIndexed { index, char ->
                                char.isDigit() || (char == '.' && '.' !in input.take(index))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("0.00") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = adWalletTextFieldColors(),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "$1" to "1.00",
                            "$5" to "5.00",
                            "Max" to uiState.balance.twoDecimals(),
                        ).forEach { (label, value) ->
                            OutlinedButton(
                                onClick = { donationAmount = value },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(label)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = parsedAmount ?: 0.0
                        onDonate(charity.id, amount)
                        if (amount > 0.0 && amount <= uiState.balance) {
                            selectedCharity = null
                        }
                    },
                    enabled = confirmEnabled,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                        disabledContentColor = Color.White.copy(alpha = 0.60f),
                    )
                ) {
                    Text("Confirm Donation", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedCharity = null }) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        )
    }
}

@Composable
private fun SettingsScreen(
    uiState: AppUiState,
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    onProfileSaved: (String, String, String) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onAddFunds: () -> Unit,
    onResetApp: () -> Unit,
) {
    val userProfile = uiState.userProfile
    var showEditProfileDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AdWalletTopAppBar(showBalance = false)
        },
        bottomBar = {
            AdWalletBottomBar(navController = navController, items = bottomNavItems)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionHeader(
                    icon = Icons.Default.Person,
                    title = "Profile",
                    actionLabel = "Edit",
                    onActionClick = { showEditProfileDialog = true }
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        ProfileItem("Name", userProfile.fullName)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        ProfileItem("Age", userProfile.age)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        ProfileItem("Gender", userProfile.gender)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionHeader(icon = Icons.Default.Settings, title = "Developer Options")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        ThemeModeRow(
                            title = "System Settings",
                            selected = uiState.settings.themeMode == ThemeMode.System,
                            onClick = { onThemeModeChanged(ThemeMode.System) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        ThemeModeRow(
                            title = "Light Mode",
                            selected = uiState.settings.themeMode == ThemeMode.Light,
                            onClick = { onThemeModeChanged(ThemeMode.Light) }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                        ThemeModeRow(
                            title = "Dark Mode",
                            selected = uiState.settings.themeMode == ThemeMode.Dark,
                            onClick = { onThemeModeChanged(ThemeMode.Dark) }
                        )
                    }
                }

                Button(
                    onClick = onAddFunds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = "ADD $50 TO WALLET",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }

                OutlinedButton(
                    onClick = onResetApp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "RESET APP STATE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                }
            }
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            initialProfile = userProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { fullName, age, gender ->
                onProfileSaved(fullName, age, gender)
                showEditProfileDialog = false
            }
        )
    }
}

@Composable
private fun AdWalletBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                icon = item.icon,
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdWalletTopAppBar(
    balance: Double = 0.0,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    showBalance: Boolean = true,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        navigationIcon = {
            if (showBack && onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AdWallet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            if (showBalance) {
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = WalletGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = balance.asCurrency(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    )
}

@Composable
private fun SurveyOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun FieldLabel(label: String) {
    Row {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = " *",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (actionLabel != null && onActionClick != null) {
            TextButton(onClick = onActionClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = actionLabel,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileDialog(
    initialProfile: com.example.project_ad_wallet.domain.model.UserProfile,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
) {
    var fullName by rememberSaveable(initialProfile.fullName) { mutableStateOf(initialProfile.fullName) }
    var age by rememberSaveable(initialProfile.age) { mutableStateOf(initialProfile.age) }
    var gender by rememberSaveable(initialProfile.gender) { mutableStateOf(initialProfile.gender) }
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other", "Prefer not to say")
    val saveEnabled = fullName.isNotBlank() && age.isNotBlank() && gender.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Edit Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Full Name")
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { input ->
                            fullName = input.filter { it.isLetter() || it.isWhitespace() }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter your full name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = adWalletTextFieldColors(),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Age")
                    OutlinedTextField(
                        value = age,
                        onValueChange = { input ->
                            age = input.filter { it.isDigit() }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter your age") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = adWalletTextFieldColors(),
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    FieldLabel("Gender")
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("Select your gender") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = adWalletTextFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genders.forEach { option ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(fullName, age, gender) },
                enabled = saveEnabled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledContentColor = Color.White.copy(alpha = 0.60f),
                )
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    )
}

@Composable
private fun ProfileItem(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value.ifBlank { "Not set" },
            color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ThemeModeRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun adWalletTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

private fun Double.asCurrency(): String = "$${twoDecimals()}"

private fun Double.twoDecimals(): String = String.format("%.2f", this)
