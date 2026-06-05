package com.example.project_ad_wallet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.project_ad_wallet.ui.auth.LoginScreen
import com.example.project_ad_wallet.ui.admin.AdminDashboardScreen
import com.example.project_ad_wallet.ui.advertiser.AdvertiserDashboardScreen
import com.example.project_ad_wallet.ui.registration.RegistrationScreen
import com.example.project_ad_wallet.ui.registration.ResetPasswordScreen
import com.example.project_ad_wallet.ui.settings.SettingsScreen
import com.example.project_ad_wallet.ui.settings.SettingsViewModel
import com.example.project_ad_wallet.ui.wallet.WalletScreen
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalRequestScreen
import com.example.project_ad_wallet.ui.withdrawals.WithdrawalStatusScreen
import com.example.project_ad_wallet.data.model.AdStatus
import com.example.project_ad_wallet.ui.admin.AdminAdsScreen
import com.example.project_ad_wallet.ui.admin.AdminCharitiesScreen
import com.example.project_ad_wallet.ui.admin.AdminUsersScreen
import com.example.project_ad_wallet.data.model.CharityStatus
import com.example.project_ad_wallet.ui.auth.LoginScreen
import com.example.project_ad_wallet.ui.onboarding.OnboardingScreen

@Composable
fun AdWalletNavGraph(
    onThemeModeChanged: (ThemeMode) -> Unit,
    navController: NavHostController = rememberNavController(),
    authRepository: com.example.project_ad_wallet.data.auth.AuthRepository,
    startDestination: String = Routes.LOGIN
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

    val onLogout = {
        authRepository.logout()
        navController.navigate(Routes.LOGIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToUserFeed = {
                    navController.navigate(Routes.AD_FEED) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToAdminDashboard = {
                    navController.navigate(Routes.ADMIN_DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToAdvertiserDashboard = {
                    navController.navigate(Routes.ADVERTISER_DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(onLogout = onLogout)
        }

        composable(Routes.ADVERTISER_DASHBOARD) {
            AdvertiserDashboardScreen(onLogout = onLogout)
        }

        composable(Routes.AD_FEED) {
            AdFeedScreen(
                navController = navController,
                bottomNavItems = bottomNavItems,
                onLogout = onLogout
            )
        }

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
            val adminUsersViewModel: com.example.project_ad_wallet.ui.admin.AdminUsersViewModel = hiltViewModel()
            val users by adminUsersViewModel.users.collectAsState()
    
            com.example.project_ad_wallet.ui.admin.AdminUsersScreen(
                users = users,
                onToggleBlock = { userId: String -> adminUsersViewModel.toggleBlockStatus(userId) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_CHARITIES) {
            val adminCharitiesViewModel: com.example.project_ad_wallet.ui.admin.AdminCharitiesViewModel = hiltViewModel()
            val pendingCharities by adminCharitiesViewModel.pendingCharities.collectAsState()

            com.example.project_ad_wallet.ui.admin.AdminCharitiesScreen(
                pendingCharities = pendingCharities,
                onApprove = { charityId -> adminCharitiesViewModel.approveCharity(charityId) },
                onReject = { charityId -> adminCharitiesViewModel.rejectCharity(charityId) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_ADS) {
            val adminAdsViewModel: com.example.project_ad_wallet.ui.admin.AdminAdsViewModel = hiltViewModel()
            val pendingAds by adminAdsViewModel.pendingAds.collectAsState()

            com.example.project_ad_wallet.ui.admin.AdminAdsScreen(
                pendingAds = pendingAds,
                onApprove = { adId -> adminAdsViewModel.approveAd(adId) },
                onReject = { adId -> adminAdsViewModel.rejectAd(adId) },
                onBack = { navController.popBackStack() }
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
            val settingsViewModel: SettingsViewModel = hiltViewModel()
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
