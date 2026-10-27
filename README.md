# AdWallet — Mobile Reward & Social Impact Application

[![Android CI/CD](https://github.com/AfonsoBouca/AdWallet-Android-Mobile-Application/actions/workflows/main.yml/badge.svg)](https://github.com/AfonsoBouca/AdWallet-Android-Mobile-Application/actions)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024.09.00-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Design-blue.svg)](https://m3.material.io/)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-API%2036-orange.svg)](https://developer.android.com)

Welcome to the documentation of **AdWallet**!

AdWallet is a modern Android mobile application designed to bridge digital advertising and social impact. Users interact with targeted advertisements, answer brief engagement questionnaires, and receive monetary rewards in a virtual wallet. Accumulated earnings can be withdrawn or donated directly to verified charitable organizations aligned with the United Nations Sustainable Development Goals (SDGs).

---

## Table of Contents

- [Business Modelling](#business-modelling)
  - [Product Vision](#product-vision)
  - [Features and Assumptions](#features-and-assumptions)
- [Requirements](#requirements)
  - [User Stories](#user-stories)
  - [Domain Model](#domain-model)
  - [User Interfaces](#user-interfaces)
- [Architecture and Design](#architecture-and-design)
  - [Architecture Overview](#architecture-overview)
  - [Navigation & Data Flow](#navigation--data-flow)
  - [Design Patterns](#design-patterns)
  - [Notable Design Decisions](#notable-design-decisions)
  - [Logical Architecture](#logical-architecture)
  - [Physical Architecture](#physical-architecture)
  - [Functional Prototype](#functional-prototype)
- [Project Management](#project-management)
  - [Sprint 0](#sprint-0)
  - [Sprint 1](#sprint-1)
  - [Sprint 2](#sprint-2)
  - [Future Roadmap](#future-roadmap)
- [Getting Started](#getting-started)
- [Responsible Use of AI](#responsible-use-of-ai)
- [Authors & Credits](#authors--credits)

---

## Business Modelling

Business modeling involves defining the product's vision, understanding market needs, aligning features with user expectations, and setting the groundwork for strategic planning and execution.

### Product Vision

> **Earn, engage, and give back** — a mobile platform that rewards users for interacting with ads while supporting meaningful social causes.

### Features and Assumptions

#### Features

- **User Account Management** — registration, login, email verification, password reset, profile editing, and account deletion
- **Advertisement Browsing** — list of available ads with key details such as duration and reward
- **Ad Interaction** — watch ads, answer questionnaires, rate ads, skip irrelevant ads, and report inappropriate content
- **Reward System (Wallet)** — virtual wallet to store earnings and track real-time balance updates
- **Earnings History** — detailed history of rewards earned from advertisements
- **Withdrawals** — request transfer of wallet balance to a bank account and track request status
- **Donations System** — donate earnings to verified charities aligned with the UN Sustainable Development Goals (SDGs)
- **Social Impact Tracking** — view donation history and total contributions per charity
- **Notification System** — receive updates about new ads, rewards, and special campaigns, with customizable preferences
- **Advertiser Dashboard** — upload ads, define rewards, create questionnaires, set targeting parameters, and schedule campaigns
- **Campaign Analytics** — access statistics on ad performance and user feedback
- **Administration Panel** — manage users, monitor activity, approve ads, manage charities, and generate reports

#### Assumptions

- Users are motivated to watch advertisements in exchange for monetary rewards and social contributions
- Advertisers value targeted campaigns reaching engaged mobile user communities
- Accurate demographic data improves ad relevance and viewer engagement
- The platform can interface with secure external payment systems for withdrawals
- Verified charities are available and willing to partner with the platform
- Users value both financial incentives and social impact (charitable donations)
- Configurable notification preferences improve user retention and satisfaction
- Administrative moderation is essential to ensure content quality and platform safety
- The application complies with data protection regulations (e.g., GDPR)
- The system is designed natively for mobile devices

---

## Requirements

### User Stories

The following user stories reflect the core value delivered to platform participants:

1. **As a User**, I want to watch ads and answer their respective questionnaires so that I can earn money for my virtual wallet.
2. **As a User**, I want to register on the application by providing my demographic information (mandatory and optional) so that the app can show me ads relevant to my profile.
3. **As a User**, I want to donate my accumulated balance to charities available in the app so that I can contribute to social causes and support the Sustainable Development Goals (SDGs).
4. **As a User**, I want to request the withdrawal of my virtual wallet balance to my bank account so that I can use the earned money for myself.
5. **As an Advertising Company**, I want to set specific demographic parameters (age, gender, interests) for my ads so that my advertising reaches exactly the desired target audience.

The system is designed to support three main types of users: **end users**, **advertising companies**, and **administrators**.

- **For end users**: The platform provides comprehensive account management (registration, login, profile customization, password recovery), ad interaction workflows, wallet management (earnings, withdrawals, donations), rating, reporting, and notification settings.
- **For advertising companies**: The system offers campaign creation and management tools, including ad uploads, reward definitions, demographic targeting, campaign scheduling, questionnaire authoring, and performance analytics.
- **For administrators**: The platform includes system oversight and governance features, such as user account management, fraud monitoring, advertisement review and approval, charity verification, and platform analytics reporting.

### Domain Model

The domain model represents a platform where users interact with advertisements to earn rewards and manage their virtual wallet. End users watch and rate ads, generating transactions such as earnings, donations, and withdrawals. Advertisers create campaigns with questionnaires to validate engagement. Donations support verified charities, connecting engagement with social impact.

<img width="710" height="709" alt="Domain Model" src="https://github.com/user-attachments/assets/22a711cf-3e76-4623-9404-f4221143bb57" />

### User Interfaces

<p align="center">
  <img src="https://github.com/user-attachments/assets/aace9e32-9073-4c29-81a1-da28b83427d5" height="500" style="width:auto; margin: 4px;"/>
  <img src="https://github.com/user-attachments/assets/cf80df67-614f-45de-844d-8ab5add83f91" height="500" style="width:auto; margin: 4px;"/>
  <img src="https://github.com/user-attachments/assets/721deda4-7661-4895-8446-3035b25c25c6" height="500" style="width:auto; margin: 4px;"/>
</p>

---

## Architecture and Design

### Architecture Overview

The system is organized into three logical layers:

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  RegistrationScreen  AdFeedScreen       │
│  SurveyScreen        CharitiesScreen    │
│  SettingsScreen      MainActivity       │
├─────────────────────────────────────────┤
│           ViewModel Layer               │
│           WalletViewModel               │
│    (balance, ads, charities, user)      │
├─────────────────────────────────────────┤
│             Data Layer                  │
│   In-memory state (StateFlow)           │
│   Ad list · Charity list                │
│   DemographicState                      │
└─────────────────────────────────────────┘
```

- **`WalletViewModel`** acts as a centralized source of truth for the core flow. It holds state as `StateFlow` streams and exposes business operations (`registerUser`, `addReward`, `donate`, `setThemeOverride`). By maintaining reactive state scoped to the lifecycle, screens observe updates without tightly coupled coordination.
- **Screen Composables** (`RegistrationScreen`, `AdFeedScreen`, `SurveyScreen`, `CharitiesScreen`, `SettingsScreen`) are stateless — they receive data and event callbacks as parameters and do not manage mutable business state directly, making each screen independently testable.
- **`MainActivity`** acts as the composition root: it hosts the ViewModel, collects reactive flows, and wires together the navigation graph using Jetpack Navigation Compose.

### Navigation & Data Flow

Navigation follows a clear onboarding path followed by tabbed core sections:

```
Registration ──► AdFeed ──► Survey
                  │
            (Bottom Nav)
                  │
            Charities ── Settings
```

State changes propagate reactively: the ViewModel exposes `StateFlow` values, `MainActivity` collects them with `collectAsState()`, and passes them down as immutable parameters to composables. User interactions trigger callbacks that call ViewModel methods, causing Compose to automatically recompose only the affected UI components — applying the **Observer pattern**.

#### Example end-to-end flow — watching an ad and earning a reward

1. User selects an ad card in `AdFeedScreen` → navigates to `SurveyScreen` with the ad's data.
2. User completes the ad and questionnaire → `onSurveySubmit(reward)` callback fires.
3. `WalletViewModel.addReward()` updates the `_balance` StateFlow.
4. All screens observing the balance (Feed TopBar, Charities dialog, Wallet tab) recompose immediately.

### Design Patterns

| Pattern | Where Used | Why |
|---|---|---|
| **MVVM** | ViewModels + Screen composables | Decouples UI presentation from business logic; enables isolated unit testing of state transitions |
| **Observer (Reactive Streams)** | `StateFlow` + `collectAsState()` | Eliminates manual UI refresh calls; UI always reflects the current reactive state |
| **Single Activity** | `MainActivity` | Android modern architecture standard; simplifies back-stack management with Compose Navigation |
| **Callback / Event Delegation** | `onRegisterSuccess`, `onDonate`, `onSurveySubmit` | Keeps composables focused on rendering; they report intent upward, ViewModel manages mutations |
| **Scaffold + NavHost** | `MainActivity` / `AdWalletNavGraph` | Standard Compose pattern hosting bottom navigation with dynamic content swapping |

### Notable Design Decisions

#### Reactive State Management
All state lives in reactive `StateFlow` streams and repositories, ensuring unidirectional data flow and clean testing with Kotlin Coroutines `runTest`.

#### Feature-Scoped ViewModels & Repositories
The application leverages dedicated ViewModels and repositories (e.g., `UserRepository`, `AdRepository`, `DonationRepository`) to ensure high cohesion and modular separation of concerns.

#### Input Validation at Entry Point
Form screens filter input dynamically (e.g., name validation, numeric inputs) rather than solely validating on submission. This prevents invalid input states from propagating into business layers.

#### Dynamic Theming via Material 3
Theme preference (system default, light mode, dark mode) is observed reactively in `MainActivity` and applied to `Project_Ad_WalletTheme`, repainting the entire UI tree smoothly on change.

### Logical Architecture

The application is decomposed into structured packages in horizontal layers:

```
┌──────────────────────────────────────────────┐
│               navigation                     │
│          MainActivity · AdWalletNavGraph     │
└───────────┬──────────────┬───────────────────┘
            │              │
            ▼              ▼
┌───────────────┐  ┌───────────────────────────┐
│   viewmodel   │  │         ui.screens        │
│WalletViewModel│  │ RegistrationScreen        │
│               │  │ AdFeedScreen              │
│               │  │ SurveyScreen              │
│               │  │ CharitiesScreen           │
│               │  │ SettingsScreen            │
└───────┬───────┘  └──────────┬────────────────┘
        │                     │         │
        │                     │         ▼
        │                     │  ┌─────────────┐
        │                     │  │  ui.theme   │
        │                     │  │ Color·Theme │
        │                     │  │ Type        │
        │                     │  └─────────────┘
        │                     │
        └──────────┬──────────┘
                   ▼
        ┌──────────────────────┐
        │        model         │
        │  Ad · Charity        │
        │  DemographicState    │
        └──────────────────────┘
```

#### Package Roles

- **`model` (Domain Layer)**: Pure data classes with no dependencies on Android or UI frameworks (`Ad`, `Charity`, `User`, `Donation`).
- **`viewmodel` / `domain` (Business Logic Layer)**: Manages application state, repository interactions, and business rules. Exposes read-only `StateFlow` properties to presentation.
- **`ui.screens` (Presentation Layer)**: Stateless `@Composable` functions receiving data and callbacks as parameters.
- **`ui.theme` (Styling Layer)**: Design tokens, Material 3 color schemes, typography scales, and theme wrappers.
- **`navigation` (Composition Root)**: Configures routes, navigation graphs, argument parsing, and back-stack transitions.

### Physical Architecture

AdWallet is a self-contained Android mobile application running client-side on the user's Android device:

```
┌─────────────────────────────────────────────────────┐
│                   Android Device                    │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │           Android OS (API 26–36)             │   │
│  │                                              │   │
│  │  ┌────────────────────────────────────────┐  │   │
│  │  │            adwallet.apk                │  │   │
│  │  │                                        │  │   │
│  │  │  MainActivity  (Entry Point)           │  │   │
│  │  │  AdWalletApp   (NavHost + Navigation)  │  │   │
│  │  │  ViewModels    (State + Logic)         │  │   │
│  │  │  Screen Composables (UI Layer)         │  │   │
│  │  │  AdWalletTheme (Material 3 Theming)    │  │   │
│  │  └────────────────────────────────────────┘  │   │
│  │                                              │   │
│  │  Jetpack Compose Runtime                     │   │
│  │  Navigation Compose                          │   │
│  │  Kotlin Coroutines / StateFlow               │   │
│  │  AndroidX Lifecycle & ViewModel              │   │
│  │  Material 3 Components                       │   │
│  └──────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

#### Technology Stack & Justifications

- **Kotlin** — Null safety, expressive syntax, coroutine primitives, and idiomatic support for modern Android development.
- **Jetpack Compose** — Modern declarative UI framework that re-renders only components affected by state updates, eliminating XML boilerplate and view synchronization errors.
- **Jetpack Navigation Compose** — Type-safe, composable-first in-app routing that manages back-stack history seamlessly.
- **Kotlin Coroutines & StateFlow** — Asynchronous execution primitives and reactive state holders providing unidirectional data flow and straightforward testing.
- **Android ViewModel & Lifecycle** — Lifecycle-aware state persistence across configuration changes (e.g., device rotation).
- **Material Design 3 (Material You)** — Accessible, modern design system components with dynamic theming support.
- **Gradle (Kotlin DSL)** — Type-safe build script configuration with version catalogs (`libs.versions.toml`).

### Functional Prototype

The v0 vertical prototype demonstrated the core value proposition of AdWallet: allowing users to watch an advertisement, answer a brief questionnaire, and immediately receive a monetary reward in their virtual wallet. Proving that this core interaction is seamless and rewarding validated the product's foundation before building out supporting systems.

The compiled APK for the initial slice was tagged and released: [v0 — Prototype Release](../../releases/tag/v0).

---

## Project Management

### Sprint 0 — Foundation & Vertical Prototype

The project began by defining the product vision: connecting brands, users, and non-profit organizations through transparent value exchange. We modeled the domain using UML diagrams, established user stories adhering to the INVEST criteria (Independent, Negotiable, Valuable, Estimable, Small, Testable), and built an initial vertical prototype demonstrating the core ad-to-reward cycle supported by unit and acceptance tests.

#### Sprint 0 Retrospective Highlights
- **What went well**: Domain modeling provided a clean roadmap; early adoption of INVEST criteria ensured actionable, testable tasks.
- **Areas for adaptation**: Streamlined documentation workflows to maintain synchronization between UML models and rapidly evolving Compose code.

### Sprint 1 — Expanding Features & Architecture

In Sprint 1, development focused on velocity and expanding user capabilities:
- Implemented user registration with demographic profile information (age, gender, preferences).
- Added the Charities exploration screen with SDG-aligned non-profit organizations.
- Built the donation mechanism allowing users to direct wallet funds to chosen charities.
- Added dark/light theme switching and bottom navigation.
- Established pair programming practices and continuous integration checks via GitHub Actions.

#### Sprint 1 Retrospective Highlights
- **What went well**: Collaborative pair programming reduced defects and shared architectural understanding; realistic estimation kept backlog delivery predictable.
- **Areas for adaptation**: Standardized automated build validation for release artifacts across sprints.

### Sprint 2 — Full Feature Loop & Branch-per-Feature Workflow

Sprint 2 closed the core value loop of AdWallet, delivering a complete experience for users and advertisers. The team adopted a strict branch-per-feature Git workflow, integrating 8 distinct features through Pull Requests verified by automated CI/CD:

- **Watch Ads & Questionnaire**: Video playback with `VideoView`, progress tracking, 80% skip gate, questionnaire feedback, and wallet reward crediting.
- **Advertiser Portal**: Ad upload form with comprehensive input validation (title, company, category, reward limits 0.01–1.00), confirmation dialog, and feed integration.
- **Rate Advertisements**: 1–5 star rating screen displayed after ad completion, recorded to the repository.
- **Earnings History**: Sortable earnings ledger with total earned summary card and empty state illustration.
- **Donation Confirmation**: Success feedback screen showing donation amount, updated wallet balance, and direct links to charity or donation history.
- **Donation History**: Complete historical donation log with SDG badge indicators and total donated tracking.
- **Report Inappropriate Ads**: Reporting modal allowing users to select standard violation reasons with an optional comment.
- **Notification Preferences**: Granular notification settings with persistent preferences across sessions.

#### Sprint 2 Key Results
- Strict **Branch-per-feature** workflow with peer code reviews.
- Comprehensive test suite: **57 unit tests** and **16 acceptance tests** passing.
- Automated CI/CD builds validating every push and pull request.

#### Sprint Documentation
- [Sprint Planning](docs/sprint2/planning.md)
- [Sprint Review](docs/sprint2/review.md)
- [Sprint Retrospective](docs/sprint2/retrospective.md)
- [Happiness Meter](docs/sprint2/happiness.md)
- [Defects Log](docs/sprint2/defects.md)

### Future Roadmap

- **Sprint 3 (Planned)**: Remote backend API integration, dynamic charity filtering by SDG category, and real push notification service integration.
- **Sprint 4 (Planned)**: Advanced advertiser analytics dashboard, demographic campaign matching optimization, and fraud prevention pipelines.
- **Production Release**: Final security audit, automated end-to-end tests, and Google Play Store preparation.

---

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Koala Feature Drop or newer recommended)
- JDK 17 (Eclipse Temurin 17 or equivalent)
- Android SDK Platform 36 / Build-Tools 34+
- Physical device or Android Virtual Device (AVD) running Android 8.0 (API 26) or higher

### Building & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AfonsoBouca/AdWallet-Android-Mobile-Application.git
   ```

2. **Open in Android Studio:**
   Launch Android Studio and open the cloned project folder.

3. **Sync Gradle:**
   Allow Android Studio to download dependencies and sync the project via Gradle.

4. **Run Unit Tests:**
   ```bash
   ./gradlew test
   ```

5. **Run the Application:**
   Select your connected device or emulator and press **Run (Shift + F10)**.

---

## Responsible Use of AI

Throughout the development process, AI-assisted development tools were utilized to support boilerplate UI scaffolding and troubleshooting, while architectural design, business logic, domain modeling, and test suites were written and verified manually by the team.

---

## Authors & Credits

Developed by:
- **Afonso Bouça**
- **André Santos**
- **Rodrigo Carvalho**
- **Tomás Costa**
