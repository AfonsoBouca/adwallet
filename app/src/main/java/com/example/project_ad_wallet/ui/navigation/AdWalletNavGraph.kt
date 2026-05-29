package com.example.project_ad_wallet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_ad_wallet.data.model.ThemeMode
import com.example.project_ad_wallet.data.repository.UserRepository
import com.example.project_ad_wallet.ui.adFeed.AdFeedScreen
import com.example.project_ad_wallet.ui.adWatch.AdWatchScreen
import com.example.project_ad_wallet.ui.adRating.AdRatingScreen
import com.example.project_ad_wallet.ui.advertiserUpload.AdvertiserUploadScreen
import com.example.project_ad_wallet.ui.advertiser.AdvertiserResponsesScreen
import com.example.project_ad_wallet.ui.charities.CharitiesScreen
import com.example.project_ad_wallet.ui.donations.DonationConfirmationScreen
import com.example.project_ad_wallet.ui.donations.DonationHistoryScreen
import com.example.project_ad_wallet.ui.notifications.NotificationPrefsScreen
import com.example.project_ad_wallet.ui.components.BottomNavItem
import com.example.project_ad_wallet.ui.registration.LoginScreen
import com.example.project_ad_wallet.ui.registration.RegistrationScreen
import com.example.project_ad_wallet.ui.registration.ResetPasswordScreen
import com.example.project_ad_wallet.ui.settings.SettingsScreen
import com.example.project_ad_wallet.ui.settings.SettingsViewModel
import com.example.project_ad_wallet.ui.wallet.WalletScreen
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalRequestScreen
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalStatusScreen
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.ui.admin.AdminAdsScreen
import com.example.project_ad_wallet.ui.admin.AdminUsersScreen
import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.ui.admin.AdminCharitiesScreen

@Composable
fun AdWalletNavGraph(
    onThemeModeChanged: (ThemeMode) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    val bottomNavItems = listOf(
        BottomNavItem(Routes.AD_FEED, "Home") {
            Icon(Icons.Default.Home, contentDescription = "Home")
        },
        BottomNavItem(Routes.WALLET, "Wallet") {
            Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet")
        },
        BottomNavItem(Routes.SETTINGS, "Profile") {
            Icon(Icons.Default.Person, contentDescription = "Profile")
        },
    )

    NavHost(
        navController = navController,
        startDestination = Routes.REGISTRATION,
    ) {
        composable(Routes.REGISTRATION) {
            RegistrationScreen(
                onRegistered = {
                    navController.navigate(Routes.AD_FEED) {
                        popUpTo(Routes.REGISTRATION) { inclusive = true }
                    }
                },
                onLoginRequested = {
                    navController.navigate(Routes.LOGIN)
                },
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.AD_FEED) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onCreateAccount = {
                    navController.navigate(Routes.REGISTRATION)
                },
                onResetPassword = {
                    navController.navigate(Routes.RESET_PASSWORD)
                },
            )
        }

        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onPasswordReset = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.RESET_PASSWORD) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
        
        composable(Routes.ADMIN_USERS) {
            val walletRepo = com.example.project_ad_wallet.data.repository.UserRepository.instance
            val users by walletRepo.allUsers.collectAsState()
    
            AdminUsersScreen(
                users = users,
                onToggleBlock = { userId: String -> walletRepo.toggleBlockStatus(userId) }, // Adicionado ": String" aqui
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_CHARITIES) {
            val charityRepo = com.example.project_ad_wallet.data.repository.CharityRepository.instance
            val charities by charityRepo.charities.collectAsState()

            val pendingCharities = charities.filter { it.status == com.example.project_ad_wallet.data.model.CharityStatus.PENDING }

            AdminCharitiesScreen(
                pendingCharities = pendingCharities,
                onApprove = { charityId -> charityRepo.approveCharity(charityId) },
                onReject = { charityId -> charityRepo.rejectCharity(charityId) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_ADS) {
            val adRepo = com.example.project_ad_wallet.data.repository.AdRepository.instance
            val ads by adRepo.ads.collectAsState()
    
            val pendingAds = ads.filter { it.status == AdStatus.PENDING }

            AdminAdsScreen(
                pendingAds = pendingAds,
                onApprove = { adId -> adRepo.approveAd(adId) },
                onReject = { adId -> adRepo.rejectAd(adId) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.AD_FEED) {
            AdFeedScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.WALLET) {
            WalletScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.CHARITIES) {
            CharitiesScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.SETTINGS) {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
                onThemeModeChanged = onThemeModeChanged,
                onResetApp = {
                    navController.navigate(Routes.REGISTRATION) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onDeleteAccount = {
                    navController.navigate(Routes.REGISTRATION) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                viewModel = settingsViewModel,
            )
        }

        composable(
            route = Routes.AD_WATCH,
            arguments = listOf(navArgument("adId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val adId = backStackEntry.arguments?.getString("adId").orEmpty()
            AdWatchScreen(adId = adId, navController = navController)
        }

        composable(Routes.NOTIFICATION_PREFS) {
            NotificationPrefsScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.ADVERTISER_UPLOAD) {
            AdvertiserUploadScreen(navController = navController)
        }

        composable(Routes.ADVERTISER_RESPONSES) {
            AdvertiserResponsesScreen()
        }

        composable(Routes.EARNINGS_HISTORY) {
            EarningsHistoryPlaceholder(navController = navController)
        }

        composable(
            route = Routes.DONATION_CONFIRMATION,
            arguments = listOf(
                navArgument("charityId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val charityId = backStackEntry.arguments?.getString("charityId").orEmpty()
            val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
            val walletRepo = com.example.project_ad_wallet.data.repository.WalletRepository.instance
            val charityRepo = com.example.project_ad_wallet.data.repository.CharityRepository.instance
            val charityName = charityRepo.getCharityById(charityId)?.name ?: charityId
            DonationConfirmationScreen(
                charityName = charityName,
                amount = amount,
                newBalance = walletRepo.balance.value,
                navController = navController,
            )
        }

        composable(Routes.DONATION_HISTORY) {
            DonationHistoryScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.WITHDRAWAL_STATUS) {
            WithdrawalStatusScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(Routes.WITHDRAWAL_REQUEST) {
            WithdrawalRequestScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
            )
        }

        composable(
            route = Routes.AD_RATING,
            arguments = listOf(navArgument("adId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val adId = backStackEntry.arguments?.getString("adId").orEmpty()
            AdRatingScreen(adId = adId, navController = navController)
        }
    }
}
