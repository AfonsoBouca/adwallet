# Changelog

All notable changes to this project will be documented in this file.

## [v2] - Sprint 2
### Added

- Watch ads and answer questionnaires to earn wallet rewards (#25)
- Advertiser portal to upload advertisements for review (#54)
- Rate advertisements after watching with 1-5 star system (#42)
- Earnings history screen displaying all past earnings (#44)
- Donation confirmation dialog and success screen (#52)
- Donation history screen with total donated tracking (#53)
- Report inappropriate advertisements with reason selection (#41)
- Notification preferences screen with per-type toggles (#49)

### Tests

- 57 unit tests covering all features
- 16 acceptance tests for user workflows
- CI/CD pipeline green on all merges

## [v1] - Sprint 1
### Added

- User registration with demographic information (name, age, gender)
- Donations system allowing wallet balance transfers to charities
- Charities screen displaying SDG-aligned organizations
- Settings screen with light/dark/system theme toggle
- Bottom navigation bar for tabbed screen navigation

## [v0] - Sprint 0
### Added

- Project initialized with Kotlin and Jetpack Compose
- CI/CD pipeline configured with GitHub Actions
- MVVM architecture with Clean Architecture patterns
- Core ad feed screen listing available advertisements
- Quick Survey flow: watch ad → answer questionnaire → earn reward
- Virtual wallet balance management via ViewModel
- Unit tests for wallet and survey logic
- Acceptance tests for user interactions
