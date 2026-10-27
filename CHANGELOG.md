# Changelog

All notable changes to this project will be documented in this file.

## [v2] - Sprint 2

### Added
- Architecture refactor — monolithic `AdWalletApp.kt` split into per-screen ViewModels and composables (MVVM, repository pattern with singleton instances)
- Feature #25 — Watch Ads: video playback via `VideoView`, progress tracking, 80% skip gate, questionnaire with colour feedback, earn reward on completion
- Feature #54 — Advertiser Upload: full ad submission form with validation (title, company, category, description, reward 0.01–1.00), confirmation dialog, ad added to feed
- Feature #42 — Rate Advertisements: 1–5 star rating screen shown after watching an ad; rating saved to repository
- Feature #44 — Earnings History: sortable earnings list with total earned summary, empty state view
- Feature #52 — Donation Confirmation: success screen shown after a donation, displaying amount donated, new balance, and navigation to charity or donation history
- Feature #53 — Donation History: full donation history list sorted by most recent, with total donated summary card and empty state
- Feature #41 — Report Inappropriate Advertisements: flag button in ad watch screen opens a report dialog with five selectable reasons and an optional comment
- Feature #49 — Notification Preferences: settings screen with four toggleable notification types (new ads, earnings, donation reminders, weekly digest)
- Wallet screen consolidated into Balance / Earnings / Donations tabs
- All features include unit tests and UI acceptance tests

## [v1] - Sprint 1

### Added
- Donations system — users can donate wallet balance to verified charities
- Charities screen with SDG-aligned organisations
- Settings screen with light/dark/system theme toggle
- Bottom navigation bar for tabbed navigation between main screens
- User registration screen with demographic information (name, age, gender)

## [v0] - Sprint 0

### Added
- Project initialized with Kotlin and Jetpack Compose
- CI/CD pipeline with GitHub Actions
- Basic directory structure for MVVM/Clean Architecture
- Core ad feed screen with available advertisements
- Quick Survey flow — watch an ad, answer a questionnaire, earn a reward
- Virtual wallet balance management using ViewModel
- Unit and acceptance tests for wallet and survey logic
