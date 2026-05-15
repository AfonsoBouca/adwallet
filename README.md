<!-- Template file for README.md for LEIC-ES-2025-26 -->

> [!NOTE] In this file, you will find the structure you should follow to document your mobile app in the README.md file for LEIC-ES-2025-26. It is a single file with guidelines, as comments, only seen in Edit mode, not in Preview mode. You can add more sections, but for assessment reasons and automation, please make sure to include all sections of this template. Your professors will clarify about specificities of your app.

# AdWallet Development Report

Welcome to the documentation of AdWallet!

This Software Development Report, tailored for LEIC-ES-2025-26, provides comprehensive details about AdWallet, starting from a high-level vision and going into low-level implementation decisions.

It is organised by the following activities:

* [Business Modelling](#business-modelling)
  * [Product Vision](#product-vision)
  * [Features and Assumptions](#features-and-assumptions)
* [Requirements](#requirements)
  * [User Stories](#user-stories)
  * [Domain Model](#domain-model)
  * [User Interfaces](#user-interfaces)
* [Architecture and Design](#architecture-and-design)
  * [Architecture Overview](#architecture-overview)
  * [Navigation & Data Flow](#navigation--data-flow)
  * [Design Patterns](#design-patterns)
  * [Notable Design Decisions](#notable-design-decisions)
  * [Logical Architecture](#logical-architecture)
  * [Physical Architecture](#physical-architecture)
  * [Functional Prototype](#functional-prototype)
* [Project Management](#project-management)
  * [Sprint 0](#sprint-0)
  * [Sprint 1](#sprint-1)
  * [Sprint 2](#sprint-2)
  * [Sprint 3](#sprint-3)
  * [Final Release](#final-release)

Contributions are expected to be made exclusively by the initial team, but we may open them to the community, after the course, in all areas and topics: requirements, technologies, development, experimentation, testing, etc.

Please contact us!

Thank you!

- André Miguel Rodrigues dos Santos — andremsantos06@gmail.com
- Rodrigo Silveira de Carvalho — rodrigosdecarvalho2006@gmail.com
- Afonso Cerqueira de Sousa Gomes Bouça — afonsogomesbouca@gmail.com
- Tomás Ferreira da Costa — tfcosta05@gmail.com

---

## Business Modelling

Business modeling in software development involves defining the product's vision, understanding market needs, aligning features with user expectations, and setting the groundwork for strategic planning and execution.

### Product Vision

Earn, engage and give-back — a mobile app that rewards users for interacting with ads while supporting meaningful causes.

### Features and Assumptions

<!-- 
Indicate an initial/tentative list of high-level features - high-level capabilities or desired services of the system that are necessary to deliver benefits to the users.
 - Feature XPTO - a few words to briefly describe the feature
 - Feature ABCD - ...
...

Optionally, indicate an initial/tentative list of assumptions that you are doing about the app and dependencies of the app to other systems.
-->

#### Features

- **User Account Management** — registration, login, email verification, password reset, profile editing, and account deletion
- **Advertisement Browsing** — list of available ads with information such as duration and reward
- **Ad Interaction** — watch ads, answer questionnaires, rate ads, skip irrelevant ads, and report inappropriate content
- **Reward System (Wallet)** — virtual wallet to store earnings and track balance updates
- **Earnings History** — detailed history of rewards earned from ads
- **Withdrawals** — request transfer of wallet balance to a bank account and track request status
- **Donations System** — donate earnings to verified charities aligned with SDGs
- **Social Impact Tracking** — view donation history and total contributions per charity
- **Notification System** — receive updates about new ads, rewards, and special campaigns, with customisable preferences
- **Advertiser Dashboard** — upload ads, define rewards, create questionnaires, set targeting parameters, and schedule campaigns
- **Campaign Analytics** — access statistics on ad performance and user feedback
- **Administration Panel** — manage users, monitor activity, approve ads, manage charities, and generate reports

#### Assumptions

- Users are willing to watch advertisements in exchange for monetary rewards
- Advertisers are interested in targeted campaigns focused on the academic community
- Accurate demographic data improves ad relevance and effectiveness
- The platform integrates with secure external payment systems for withdrawals
- Verified charities are available and willing to partner with the platform
- Users value both financial incentives and social impact (donations)
- Notification systems improve user engagement and retention
- Administrative moderation is required to ensure content quality and platform safety
- The application complies with data protection regulations (e.g., GDPR)
- The system will be primarily used on mobile devices

---

## Requirements

### User Stories

<!-- 
In this section you should describe all kinds of requirements for your module: functional and non-functional requirements.

For LEIC-ES-2025-26, the requirements will be gathered and documented as user stories.

Please add in this section a concise summary of all the user stories (not each user story!).

**User stories as GitHub Project Items**
The user stories themselves should be created and described as items in your GitHub Project with the label "user story".

A user story is a description of a desired functionality told from the perspective of the user or customer. A starting template for the description of a user story is *As a < user role >, I want < goal > so that < reason >.*

Name the item with either the full user story or a shorter name (recommended). In the "comments" field, add relevant notes, mockup images, and acceptance test scenarios, linking to the acceptance tests when available, and finally estimate value and effort.

**INVEST in good user stories**.
You may add more details after, but the shorter and complete, the better. In order to decide if the user story is good, please follow the [INVEST guidelines](https://xp123.com/articles/invest-in-good-stories-and-smart-tasks/).

**User interface mockups**.
After the user story text, you should add a draft of the corresponding user interfaces, a simple mockup or draft, if applicable.

**Acceptance tests**.
For each user story you should write also the acceptance tests (textually in [Gherkin](https://cucumber.io/docs/gherkin/reference/)), i.e., a description of scenarios (situations) that will help to confirm that the system satisfies the requirements addressed by the user story.

**Value and effort**.
At the end, it is good to add a rough indication of the value of the user story to the customers (e.g. [MoSCoW](https://en.wikipedia.org/wiki/MoSCoW_method) method) and the team should add an estimation of the effort to implement it using points in a kind-of-a Fibonnacci scale (1,2,3,5,8,13,20,40, no idea).
-->

The following user stories are ordered by perceived value for the users:

1. **As a** User, **I want** to watch ads and answer their respective questionnaires **so that** I can earn money for my virtual wallet.
2. **As a** User, **I want** to register on the application by providing my demographic information (mandatory and optional) **so that** the app can show me ads relevant to my profile.
3. **As a** User, **I want** to donate my accumulated balance to charities available in the app **so that** I can contribute to social causes and support the Sustainable Development Goals (SDGs).
4. **As a** User, **I want** to request the withdrawal of my virtual wallet balance to my bank account **so that** I can use the earned money for myself.
5. **As an** Advertising Company, **I want** to set specific demographic parameters (age, gender, ethnicity, etc.) for my ads **so that** my advertising reaches exactly the desired target audience.

The system is designed to support three main types of users: **end users**, **advertising companies**, and **administrators**.

For end users, the platform provides functionality for **account management**, including registration, login, profile customisation, and security features such as email verification and password recovery. Users can browse available advertisements, view key details such as duration and reward, and choose which ads to watch. By interacting with ads and completing associated questionnaires, users earn rewards stored in a virtual wallet. They can track their earnings history, withdraw funds, or donate to verified charities, contributing to social impact initiatives. Additionally, users can rate ads, report inappropriate content, and manage notification preferences.

For advertising companies, the system offers tools to **create and manage advertising campaigns**, including uploading ads, defining rewards, setting demographic targeting parameters, scheduling campaigns, and creating questionnaires. Advertisers can also access analytics and user feedback to evaluate campaign performance and improve engagement.

For administrators, the platform includes features for **system oversight and governance**, such as managing user accounts, monitoring activity to detect fraud, approving or rejecting advertisements, maintaining a list of verified charities, and generating reports to evaluate platform performance.

### Domain Model

<!-- 
To better understand the context of the software system, it is useful to have a simple UML class diagram with all and only the key concepts (names, attributes) and relationships involved of the problem domain addressed by your app.
Also provide a short textual description of each concept (domain class).

Example:
 <p align="center" justify="center">
  <img src="https://github.com/FEUP-LEIC-ES-2022-23/templates/blob/main/images/DomainModel.png"/>
</p>
-->

The domain model represents a platform where users interact with advertisements to earn rewards and manage their virtual wallet. End users can watch and rate ads, generating transactions such as earnings, donations, and withdrawals. Advertisers create ads, which may include questionnaires to validate user engagement. Donations made by users can support different charities available in the system. Overall, the model captures the main entities and relationships involved in user interaction, financial transactions, and social contribution within the application.

<img width="710" height="709" alt="Domain Model" src="https://github.com/user-attachments/assets/22a711cf-3e76-4623-9404-f4221143bb57" />

### User Interfaces

<p align="center">
  <img src="https://github.com/user-attachments/assets/aace9e32-9073-4c29-81a1-da28b83427d5" height="500" style="width:auto;"/>
  <img src="https://github.com/user-attachments/assets/cf80df67-614f-45de-844d-8ab5add83f91" height="500" style="width:auto;"/>
  <img src="https://github.com/user-attachments/assets/721deda4-7661-4895-8446-3035b25c25c6" height="500" style="width:auto;"/>
</p>

---

## Architecture and Design

<!--
The architecture of a software system encompasses the set of key decisions about its organization.

A well written architecture document is brief and reduces the amount of time it takes new programmers to a project to understand the code to feel able to make modifications and enhancements.

To document the architecture requires describing the decomposition of the system in their parts (high-level components) and the key behaviors and collaborations between them.

In this section you should start by briefly describing the components of the project and their interrelations. You should describe how you solved typical problems you may have encountered, pointing to well-known architectural and design patterns, if applicable.
-->

### Architecture Overview

The system is organised into three logical layers:

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

**`WalletViewModel`** is the single source of truth for the entire application. It holds all mutable state as `StateFlow` objects and exposes business operations (`registerUser`, `addReward`, `donate`, `setThemeOverride`). By centralising state in one ViewModel scoped to the Activity lifecycle, any screen can observe changes without explicit coordination.

**Screen Composables** (`RegistrationScreen`, `AdFeedScreen`, `SurveyScreen`, `CharitiesScreen`, `SettingsScreen`) are stateless — they receive data and callbacks as parameters and never own mutable state themselves, making each screen independently testable.

**`MainActivity`** acts as the composition root: it creates the ViewModel, collects state from its flows, and wires together the navigation graph using Jetpack Navigation Compose.

### Navigation & Data Flow

Navigation follows a linear onboarding flow followed by a tabbed main area:

```
Registration ──► AdFeed ──► Survey
                  │
           (Bottom Nav)
                  │
           Charities ── Settings
```

State changes propagate reactively: the ViewModel exposes `StateFlow` values, `MainActivity` collects them with `collectAsState()`, and passes them down as immutable parameters to composables. User interactions trigger callbacks that call ViewModel methods, which update the flows, causing Compose to automatically recompose only the affected parts of the UI — an application of the **Observer pattern**.

#### Example end-to-end flow — watching an ad and earning a reward

1. User clicks an Ad card in `AdFeedScreen` → navigates to `SurveyScreen` with the ad's data.
2. User completes the survey → `onSurveySubmit(reward)` callback fires.
3. `WalletViewModel.addReward()` updates `_balance` StateFlow.
4. All screens observing the balance (Feed AppBar, Charities dialog) recompose instantly.

### Design Patterns

| Pattern | Where Used | Why |
|---|---|---|
| **MVVM** | `WalletViewModel` + Screen composables | Decouples UI from business logic; enables unit testing of state transitions without a running UI |
| **Observer (Reactive Streams)** | `StateFlow` + `collectAsState()` | Eliminates manual UI refresh calls; UI always reflects current state |
| **Single Activity** | `MainActivity` only | Modern Android best practice; simplifies back-stack management with Compose Navigation |
| **Callback / Event Delegation** | `onRegisterSuccess`, `onDonate`, `onSurveySubmit` | Keeps composables dumb; they report intent upward, ViewModel decides effect |
| **Scaffold + NavHost** | `MainActivity` | Standard Compose pattern for hosting bottom navigation with a changing content area |

### Notable Design Decisions

#### In-memory state only
There is no database or persistence layer (no Room, no SharedPreferences). All state lives in `WalletViewModel` and is reset when the app process dies. This was appropriate for a prototype/demo context and keeps the architecture simple — adding a Repository backed by Room would be the natural next step for a production version.

#### Global ViewModel
A single `WalletViewModel` serves all screens, rather than per-screen ViewModels. This avoids the need to pass data between screens via navigation arguments for most state (e.g., balance is visible everywhere without re-fetching). The trade-off is that the ViewModel grows larger as the app scales, but for this scope it keeps the design straightforward.

#### Input validation at entry point
`RegistrationScreen` filters keystrokes in real-time (name allows only letters/spaces; age only digits) rather than validating on submit. This prevents invalid data from ever entering the ViewModel, keeping the model clean without defensive checks throughout the business layer.

#### Theme control via ViewModel
The dark/light mode preference is stored in `WalletViewModel` as a nullable `Boolean?` (`null` = follow system). `MainActivity` observes this and passes it into the custom `TPC3Theme`, so changing the setting from `SettingsScreen` immediately repaints the entire app — a clean demonstration of how a single reactive state change can cascade through the whole UI tree.

### Logical Architecture

<!--
The purpose of this subsection is to document the high-level logical structure of the code (Logical View), using a UML diagram with logical packages, without the worry of allocating to components, processes or machines.

It can be beneficial to present the system in a horizontal decomposition, defining layers and implementation concepts, such as the user interface, business logic and concepts.

Example of _UML package diagram_ showing a _logical view_ of the Eletronic Ticketing System (to be accompanied by a short description of each package):

![LogicalView](https://user-images.githubusercontent.com/9655877/160585416-b1278ad7-18d7-463c-b8c6-afa4f7ac7639.png)
-->

The application is decomposed into four logical packages arranged in horizontal layers, from the top-most user-facing layer down to the domain model.

```
┌──────────────────────────────────────────────┐
│               navigation                     │
│          MainActivity · AdWalletApp          │
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

#### Package Descriptions

**`model` — Domain layer**
Pure data classes with no dependencies on Android or UI frameworks. Contains `Ad` (an advertisement with a reward value), `Charity` (a cause users can donate to), and `DemographicState` (the registered user's profile). The stable base on which all other layers build.

**`viewmodel` — Business logic layer**
`WalletViewModel` is the single application-wide state holder. It owns all mutable state as `StateFlow` objects (`balance`, `availableAds`, `charities`, `demographics`, `themePreference`) and exposes the only operations that may change that state (`registerUser`, `addReward`, `donate`, `setThemeOverride`). Depends only on `model` and the Android ViewModel lifecycle library.

**`ui.screens` — Presentation layer**
Each screen is a stateless `@Composable` function that receives data and event callbacks as parameters — screens never hold mutable state or talk to the ViewModel directly. This makes each screen independently testable and reusable. Depends on `model` (for parameter types) and `ui.theme` (for colour and typography tokens).

**`ui.theme` — Styling layer**
`Color.kt` defines the colour palette for both light and dark schemes; `Type.kt` defines the typography scale; `Theme.kt` composes these into the `TPC3Theme` Composable that wraps the entire app. All visual tokens are centralised here so that a single theme change propagates everywhere.

**`navigation` — Composition root**
`MainActivity` creates the `WalletViewModel`, observes the theme preference, and applies `TPC3Theme`. `AdWalletApp` owns the `NavController`, defines the navigation graph (routes, arguments, back-stack rules), collects state from the ViewModel, and wires it into the correct screen Composable. The only layer that knows about both the ViewModel and the individual screens.

#### Dependency Direction

Dependencies flow strictly downward:

```
navigation → ui.screens → model
navigation → viewmodel  → model
navigation → ui.theme
ui.screens → ui.theme
```

No lower layer ever references a higher one. Business logic in `viewmodel` is entirely unaware of Compose or navigation, and `model` classes are plain Kotlin data with no framework coupling — keeping each layer independently replaceable.

### Physical Architecture

<!--
The goal of this subsection is to document the high-level physical structure of the software system (machines, connections, software components installed, and their dependencies) using UML deployment diagrams (Deployment View) or component diagrams (Implementation View), separate or integrated, showing the physical structure of the system.

It should describe also the technologies considered and justify the selections made. Examples of technologies relevant for ESOF are, for example, frameworks for mobile applications (such as Flutter).

Example of _UML deployment diagram_ showing a _deployment view_ of the Eletronic Ticketing System (please notice that, instead of software components, one should represent their physical/executable manifestations for deployment, called artifacts in UML; the diagram should be accompanied by a short description of each node and artifact):

![DeploymentView](https://user-images.githubusercontent.com/9655877/160592491-20e85af9-0758-4e1e-a704-0db1be3ee65d.png)
-->

AdWallet is a fully self-contained Android application with no backend or network dependency. The entire system runs on a single physical node: the user's Android device.

```
┌─────────────────────────────────────────────────────┐
│                   Android Device                    │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │           Android OS (API 36)                │   │
│  │                                              │   │
│  │  ┌────────────────────────────────────────┐  │   │
│  │  │            adwallet.apk                │  │   │
│  │  │                                        │  │   │
│  │  │  MainActivity  (Entry Point)           │  │   │
│  │  │  AdWalletApp   (NavHost + Bottom Nav)  │  │   │
│  │  │  WalletViewModel (State + Logic)       │  │   │
│  │  │  Screen Composables (UI Layer)         │  │   │
│  │  │  AdWalletTheme (Material 3 Theming)    │  │   │
│  │  └────────────────────────────────────────┘  │   │
│  │                                              │   │
│  │  Jetpack Compose Runtime                     │   │
│  │  Navigation Compose                          │   │
│  │  Kotlin Coroutines / StateFlow               │   │
│  │  ViewModel Lifecycle Library                 │   │
│  │  Material3 Library                           │   │
│  └──────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

#### Node & Artifact Description

**Android Device / Android OS (API 36)** — The sole execution environment. Minimum and target SDK is API 36 (Android 15); there is no server node, no cloud backend, and no peer-to-peer connection. All computation and state reside entirely on the device.

**`adwallet.apk`** — The single deployable artifact produced by the Gradle build. It packages all compiled Kotlin bytecode, Compose UI descriptors, resources (strings, colours, icons), and the `AndroidManifest` into one installable file.

| Component | Role |
|---|---|
| `MainActivity` | Android entry point; applies the theme and hosts the Compose content tree |
| `AdWalletApp` | Composition root; owns the `NavController` and navigation graph |
| `WalletViewModel` | Lifecycle-scoped state holder; survives configuration changes (e.g. rotation) |
| Screen Composables | Stateless UI functions for each screen; recompose on state changes |
| `AdWalletTheme` | Applies the Material 3 colour scheme and typography to the entire tree |

#### Technologies & Justification

**Kotlin** — Null safety, data classes, extension functions, and first-class coroutine support reduce boilerplate compared to Java and align naturally with the reactive patterns used throughout the app.

**Jetpack Compose** — Google's declarative UI toolkit. Composable functions describe the UI as a function of state; when state changes, only the affected parts of the tree recompose. Eliminates manual `findViewById` wiring, reduces UI/state inconsistency, and is the current recommended approach for new Android projects.

**Jetpack Navigation Compose** — Route-based navigation integrated directly into the Compose tree. Routes are type-safe strings with declared argument types (`NavType.StringType` for `adId`), and the `NavController` manages the back stack automatically — avoiding the complexity of mixing the older Fragment API with Compose.

**Kotlin Coroutines / StateFlow** — `StateFlow` is a coroutine-based observable stream that always holds a current value. It is the idiomatic replacement for `LiveData` in Compose projects: it integrates with `collectAsState()` to trigger recomposition, requires no lifecycle observer boilerplate, and is trivially testable with `runTest`. Exposing read-only `StateFlow` from a `MutableStateFlow` inside the ViewModel enforces unidirectional data flow.

**Android ViewModel (Lifecycle library)** — Survives Activity recreation events (screen rotation, language change) without re-initialising state, which is critical for maintaining balance and user data across configuration changes without a persistent storage layer.

**Material Design 3 (Material You)** — Provides the standard component set (`Card`, `NavigationBar`, `Scaffold`, `Dialog`, `TextField`) and a colour system with adaptive light/dark theming. Using the official library ensures accessibility compliance (contrast ratios, touch target sizes) without custom implementation effort.

**Gradle (Kotlin DSL)** — The standard Android build system. Using `build.gradle.kts` instead of Groovy gives type-safe build script authoring and IDE autocompletion. The `libs.versions.toml` version catalogue centralises all dependency versions in one file, avoiding version drift across modules.

### Functional Prototype

The v0 vertical prototype successfully demonstrated the core value proposition of AdWallet: allowing users to watch an advertisement, answer a brief questionnaire, and immediately receive a monetary reward in their virtual wallet. This specific flow was prioritized over standard initialization features like user registration or login because it represents the fundamental interaction loop that differentiates AdWallet from other applications. Proving that this core interaction is seamless and rewarding was critical to validating the project's viability.

The prototype was developed using Jetpack Compose for the UI and an MVVM architecture with in-memory state management. This allowed us to quickly implement the logic for calculating rewards and updating the wallet balance without the overhead of setting up a persistent database early on. The focus was entirely on user experience and the reliability of the reward transaction.

This vertical slice was officially tagged and released as v0 on GitHub. The release includes the compiled `.apk` file, allowing stakeholders to install and test the core ad-watching and earning flow directly on their Android devices. You can view the release here: [v0 — Sprint 0 Prototype](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/releases/tag/v0).

---

## Project Management

### Sprint 0

The project began with a high-level "Vision" aimed at transforming digital engagement into social impact. The goal was to create an intuitive "Ad Wallet" where users could interact with advertisements to earn rewards, which could then be directed toward charitable causes. We prioritized clarity and motivation, ensuring the mission—connecting brands, users, and charities—was at the core of every design decision.

We transitioned from a vision to a formal structure by defining our Domain Concepts. This involved creating a robust UML model that mapped out the relationships between Users, Ads, and Charities. By focusing on the domain first, we ensured that the logic (e.g., how a reward from an Ad translates into a balance) was well-described and technically sound before full-scale implementation.

Using patterns like "Small Items" and "Definition of Ready," we built a comprehensive Product Backlog. Every User Story was crafted to meet the INVEST criteria (Independent, Negotiable, Valuable, Estimable, Small, and Testable). This phase established our "Enabling Specification" process, where epics were broken down into actionable 1–3 day tasks, complete with mockups and acceptance tests to ensure the team was never blocked.

The project reached a milestone with the release of a Vertical Prototype. Instead of focusing on standard features like Login, we implemented the unique core loop: viewing ads and managing rewards. This was supported by a growing suite of Unit and Integration tests to ensure the correctness of our functional requirements from day one.

We encountered some difficulties like defining the "Unique" Core: In the early stages, it was difficult to decide which functionality to prioritize for the Vertical Prototype. There was a strong temptation to build "standard" features like authentication first, but we had to pivot to ensure the most characteristic part of the "Ad Wallet" worked perfectly first.

Another challenge was balancing Modeling with Development: Maintaining high-quality UML diagrams (Logical and Physical) while the code was evolving rapidly required disciplined synchronization. Ensuring the "textual description supported the UML" was a constant exercise in technical writing.

#### Sprint 0 Retrospective

**Did Well:**
◦ Domain Modeling: We successfully modeled the domain concepts early on. The UML diagrams and textual descriptions provided a clear roadmap that kept the code clean.
◦ Vision Clarity: The "Vision" pattern worked well to keep the team motivated and focused on the "why" of the project rather than just the "how."
◦ User Story Quality: Applying the INVEST criteria early meant that our tasks were actionable and testable from day one.

**Do Differently:**
◦ Architecture vs. Prototyping Balance: We spent a significant amount of time on the Logical and Physical Architecture (UML diagrams). In the next sprint, we should try to balance documentation time more closely with active coding to avoid bottlenecks.
◦ Mockup Detail: While our UI mockups were explicit, some transitions were missing, leading to slight confusion during the prototype implementation.

**Puzzles:**
◦ UML Synchronization: We are still figuring out the best way to keep our UML Package and Deployment diagrams perfectly in sync with the code as the project evolves rapidly.
◦ Testing Scope: We aren't yet sure if our Acceptance Tests are too broad or if they should be more granular for specific UI components.

**Improvements for Sprint 1:**
◦ Refine UI Navigation: We will explicitly map every button click in the mockups to a specific navigation event in the code.
◦ Daily Sync on "Definition of Ready": Ensure that even bugs/improvements include screenshots of intended behavior before they are moved to "In Progress."

### Sprint 1

### Development Environment Setup

AdWallet is built using modern Android development tools. To set up the development environment, ensure you have the following versions: Android Studio (latest stable release), Kotlin 2.0.21, Android Gradle Plugin 9.0.1, and Jetpack Compose BOM 2024.09.00.

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Clone the repository: \`git clone https://github.com/LEIC-ES-2025-26-2LEIC03/T5.git\`
3. Open the cloned \`T5\` directory in Android Studio.
4. Allow Gradle to sync and download dependencies automatically (no additional configuration is required).
5. Build and run the project using the "Run" button (Shift+F10) on an emulator or physical device.

### Responsible Use of Agentic AI

Throughout the development process, the team utilized AI tools (specifically ChatGPT) to support code generation, troubleshoot compilation errors, and assist with boilerplate UI structuring. This allowed the team to maintain a high velocity while focusing on the core business logic and architectural design of the application.

It is important to note that while AI assisted in generating implementation code, all unit and acceptance tests were written manually by the team members. This ensures that human oversight remains the ultimate validator of the system's correctness and alignment with our user stories.

To maintain transparency, as required by the course guidelines, all AI-assisted code contributions are clearly identified. In early sprints, this was marked using \`// MisterAI\` comments directly within the source files. From Sprint 2 onwards, we standardized this practice by prefixing the relevant Git commit messages with the \`[MisterAI]\` tag, providing a clear audit trail of AI involvement in the project history.

In Sprint 1, we transitioned from establishing a vision to a focus on execution velocity. Using patterns like "Yesterday’s Weather," we analyzed our performance from Sprint 0 to create a realistic Sprint Backlog that avoided over-commitment. We ensured the backlog contained no epics, only "Small Items" that were ready for immediate action. Through "Pigs Estimate," the team members actually responsible for the code provided the estimates, leading to a much higher level of accuracy and ownership over our sprint goals.

Our workflow matured through a strict "Low WiP" (Work in Progress) policy and collaborative patterns. We managed our tasks on the GitHub Projects board, ensuring that items were only assigned to a pair, trio, or larger group at the moment work actually started. This prevented tasks from being "paused" and ensured that knowledge was shared across the team, preventing technical silos and improving the overall quality of the v1 release.

A major highlight of this phase was the "Sprint Review," where we demonstrated the new product increment to our stakeholders, including the teacher. The feedback provided was immediately recorded in our repository and used to adapt the Product Backlog. This ensured that our development remained aligned with user expectations, specifically regarding how we display charity impact and rewards.
We reached a significant milestone with the release of v1. While the Vertical Prototype (v0) proved the core loop, v1 focused on expanding the app's robustness.

We ran into some trouble figuring out the best group sizes for our tasks. It was a bit of a learning curve to decide when we needed a pair versus a trio. Some of the easier UI tasks actually took longer because there were too many of us trying to do the same thing at once. On the other hand, the harder logic stuff was way easier with three people checking for mistakes. After solving that it wasmuch easier to keep our work on track and making sure we didn't slow ourselves down.

Another challenge was PBI Granularity for Complex Features: As we moved beyond the simple prototype, breaking down integrated features like "SDG-based Charity Filtering" into 1–3 day tasks proved difficult. It required multiple refinement sessions to ensure these items met our "Definition of Ready" without becoming too large to manage within the sprint.

#### Sprint 1 Retrospective

### Development Environment Setup

AdWallet is built using modern Android development tools. To set up the development environment, ensure you have the following versions: Android Studio (latest stable release), Kotlin 2.0.21, Android Gradle Plugin 9.0.1, and Jetpack Compose BOM 2024.09.00.

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Clone the repository: \`git clone https://github.com/LEIC-ES-2025-26-2LEIC03/T5.git\`
3. Open the cloned \`T5\` directory in Android Studio.
4. Allow Gradle to sync and download dependencies automatically (no additional configuration is required).
5. Build and run the project using the "Run" button (Shift+F10) on an emulator or physical device.

### Responsible Use of Agentic AI

Throughout the development process, the team utilized AI tools (specifically ChatGPT) to support code generation, troubleshoot compilation errors, and assist with boilerplate UI structuring. This allowed the team to maintain a high velocity while focusing on the core business logic and architectural design of the application.

It is important to note that while AI assisted in generating implementation code, all unit and acceptance tests were written manually by the team members. This ensures that human oversight remains the ultimate validator of the system's correctness and alignment with our user stories.

To maintain transparency, as required by the course guidelines, all AI-assisted code contributions are clearly identified. In early sprints, this was marked using \`// MisterAI\` comments directly within the source files. From Sprint 2 onwards, we standardized this practice by prefixing the relevant Git commit messages with the \`[MisterAI]\` tag, providing a clear audit trail of AI involvement in the project history.

**Did Well:**
• Pair Programming: Collaborative coding significantly reduced our defect rate and ensured that everyone understood the core architecture.
• Realistic Planning: Using "Yesterday's Weather" and "Pigs Estimate" meant we completed our Sprint Backlog without any "overflow" items, boosting team morale.
• Feedback Integration: The Sprint Review process was highly effective at identifying small UI friction points that we hadn't noticed during development.

**Do Differently:**
• Release Timing: We waited until the very end of the sprint to finalize the v1 .apk. In the next sprint, we should aim for "Nightly Builds" or mid-sprint internal releases to avoid a last-minute rush.
• Meeting Efficiency: Our refinement sessions were occasionally too long. We need to implement stricter time-boxing for PBI discussions to keep the team focused.

**Puzzles:**
• State Management Scalability: We are still debating if our current AppUiState model will become too "bloated." We need to investigate if splitting it into smaller, feature-specific states is the right move for Sprint 2.
• Testing Coverage Priorities: We are unsure if we should prioritize deep UI testing or focus more on the edge cases of the reward calculation logic.

**Improvements for Sprint 2:**
• Continuous Deployment Strategy: We will implement a verifiable strategy to build the APK automatically after every major merge to ensure the release is always ready.
• Refined "Definition of Done": We will add "Cross-pair Code Review" to our DoD to ensure that even work done in pairs is verified by someone outside that specific group.

### Sprint 2

In Sprint 2, the team successfully closed the core value loop of AdWallet by fully implementing the ad interaction and reward system, alongside tools for advertisers. We transitioned to a professional branch-per-feature Git workflow, integrating 8 distinct features into `main` via formal Pull Requests, backed by comprehensive testing and automated CI/CD checks.

- Watch ads and answer questionnaires to earn wallet rewards ([#25](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/25))
- Advertiser portal to upload advertisements for review ([#54](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/54))
- Rate advertisements after watching with 1-5 star system ([#42](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/42))
- Earnings history screen displaying all past earnings ([#44](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/44))
- Donation confirmation dialog and success screen ([#52](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/52))
- Donation history screen with total donated tracking ([#53](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/53))
- Report inappropriate advertisements with reason selection ([#41](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/41))
- Notification preferences screen with per-type toggles ([#49](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/49))

**Key Results:**
- Transitioned to strict **Branch-per-feature workflow**
- Authored **57 unit tests** (all passing)
- Authored **16 acceptance tests** (all passing)

**Sprint Documentation:**
- [Sprint Planning](docs/sprint2/planning.md)
- [Sprint Review](docs/sprint2/review.md)
- [Sprint Retrospective](docs/sprint2/retrospective.md)
- [Happiness Meter](docs/sprint2/happiness.md)
- [Defects Log](docs/sprint2/defects.md)

All features have been integrated, verified by GitHub Actions, and packaged in the latest release. You can download and test the application here: [v2 — Sprint 2 Release](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/releases/tag/v2).

### Sprint 3

### Sprint 4

### Final Release
