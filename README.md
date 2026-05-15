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
        │  User · Earning      │
        └──────────────────────┘
```

**`model` — Domain layer**
Pure Kotlin data classes representing the core entities: `Ad`, `Charity`, `User`, `Earning`, `AdRating`, and `AdReport`. This layer is the stable foundation, containing no platform dependencies.

**`repository` — Data access layer**
Encapsulates data retrieval and persistence logic. Repositories (`AdRepository`, `WalletRepository`, etc.) act as a buffer between the domain logic and data sources, using `StateFlow` to provide reactive data updates to the ViewModels.

**`viewmodel` — Business logic layer**
Feature-specific ViewModels that manage UI state and handle user interactions. They interact with Repositories to perform business operations and expose state to the UI layer via read-only `StateFlow` objects.

**`ui.screens` — Presentation layer**
Stateless `@Composable` functions that define the user interface. They react to state changes from ViewModels and delegate user events back to them, ensuring a clear separation of concerns.

**`navigation` — Composition root**
`MainActivity` initializes the app and hosts the `AdWalletNavGraph`, which defines all routes, arguments, and screen-ViewModel pairings. This layer orchestrates the application's overall structure and flow.

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
│  │  │  AdWalletNavGraph (NavHost + Routes)   │  │   │
│  │  │  ViewModels (Feature-specific)         │  │   │
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
| `MainActivity` | Android entry point; determines theme and hosts the NavGraph |
| `AdWalletNavGraph` | Composition root; defines routes and orchestrates navigation |
| ViewModels | Lifecycle-scoped state holders for each feature; handle business logic |
| Screen Composables | Stateless UI functions for each screen; react to ViewModel state |
| `AdWalletTheme` | Applies consistent Material 3 styling and color palette |

#### Technologies & Justification

**Kotlin** — Null safety, data classes, extension functions, and first-class coroutine support reduce boilerplate compared to Java and align naturally with the reactive patterns used throughout the app.

**Jetpack Compose** — Google's declarative UI toolkit. Composable functions describe the UI as a function of state; when state changes, only the affected parts of the tree recompose. Eliminates manual `findViewById` wiring, reduces UI/state inconsistency, and is the current recommended approach for new Android projects.

**Jetpack Navigation Compose** — Route-based navigation integrated directly into the Compose tree. Routes are type-safe strings with declared argument types (`NavType.StringType` for `adId`), and the `NavController` manages the back stack automatically — avoiding the complexity of mixing the older Fragment API with Compose.

**Kotlin Coroutines / StateFlow** — `StateFlow` is a coroutine-based observable stream that always holds a current value. It is the idiomatic replacement for `LiveData` in Compose projects: it integrates with `collectAsState()` to trigger recomposition, requires no lifecycle observer boilerplate, and is trivially testable with `runTest`. Exposing read-only `StateFlow` from a `MutableStateFlow` inside the ViewModel enforces unidirectional data flow.

**Android ViewModel (Lifecycle library)** — Survives Activity recreation events (screen rotation, language change) without re-initialising state, which is critical for maintaining balance and user data across configuration changes without a persistent storage layer.

**Material Design 3 (Material You)** — Provides the standard component set (`Card`, `NavigationBar`, `Scaffold`, `Dialog`, `TextField`) and a colour system with adaptive light/dark theming. Using the official library ensures accessibility compliance (contrast ratios, touch target sizes) without custom implementation effort.

**Gradle (Kotlin DSL)** — The standard Android build system. Using `build.gradle.kts` instead of Groovy gives type-safe build script authoring and IDE autocompletion. The `libs.versions.toml` version catalogue centralises all dependency versions in one file, avoiding version drift across modules.

### Functional Prototype

<!--
To help on validating all the architectural, design and technological decisions made, we usually implement a functional prototype, a thin vertical slice of the system integrating as much technologies as we can.

In this subsection please describe which feature, or part of it, you have implemented, and how, together with a snapshot of the user interface, if applicable.

At this phase, instead of a complete user story, you can simply implement a small part of a feature that demonstrates that you can use the technology, for example, show a screen with the app credits (name and authors).
-->

---

## Project Management

<!--
Software project management is the art and science of planning and leading software projects, in which they are planned, implemented, monitored and controlled.

In the context of ESOF, we recommend each team to adopt a set of project management practices and tools capable of registering tasks, assigning tasks to team members, adding estimations to tasks, monitor tasks progress, and therefore being able to track their projects.

Common practices of managing agile software development with Scrum are: backlog management, release management, estimation, Sprint planning, Sprint development, acceptance tests, and Sprint retrospectives.

You can find below information and references related with the project management:

* Backlog management: Product backlog and Sprint backlog in a [Github Projects board](https://github.com/orgs/FEUP-LEIC-ES-2023-24/projects/64);
* Release management: [v0](#), v1, v2, v3, ...;
* Sprint planning and retrospectives:
  * plans: screenshots of Github Projects board at begin and end of each Sprint;
  * retrospectives: meeting notes in a document in the repository, addressing the following questions:
    * Did well: things we did well and should continue;
    * Do differently: things we should do differently and how;
    * Puzzles: things we don't know yet if they are right or wrong;
    * list of a few improvements to implement next Sprint;
-->

### Sprint 0
### Functional Prototype

The v0 vertical prototype successfully demonstrated the core value proposition of AdWallet: allowing users to watch an advertisement, answer a brief questionnaire, and immediately receive a monetary reward in their virtual wallet. This specific flow was prioritized over standard initialization features like user registration or login because it represents the fundamental interaction loop that differentiates AdWallet from other applications. Proving that this core interaction is seamless and rewarding was critical to validating the project's viability.

The prototype was developed using Jetpack Compose for the UI and an MVVM architecture with in-memory state management. This allowed us to quickly implement the logic for calculating rewards and updating the wallet balance without the overhead of setting up a persistent database early on. The focus was entirely on user experience and the reliability of the reward transaction.

This vertical slice was officially tagged and released as v0 on GitHub. The release includes the compiled `.apk` file, allowing stakeholders to install and test the core ad-watching and earning flow directly on their Android devices. You can view the release here: [v0 — Sprint 0 Prototype](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/releases/tag/v0).

### Change Management

During Sprints 0 and 1, the team utilized a simplified Git strategy, pushing commits directly to the `main` branch. This approach was highly appropriate for the rapid prototyping phase, where the primary objective was to quickly build the foundational architecture and the v0 vertical prototype. The low overhead allowed us to iterate quickly without the ceremony of pull requests.

However, as the team transitioned into implementing distinct, parallel features in Sprint 2, this direct-commit approach became a bottleneck. It limited our ability to work concurrently without merge conflicts and bypassed formal code reviews. To address this, starting from Sprint 2, the team adopted a strict "branch-per-feature" workflow.

Under this new system, every feature is developed on an independent branch (e.g., `feature/25-watch-ads`) and merged into `main` exclusively via Pull Requests. This ensures that all code is reviewed, tested via our CI/CD pipeline, and validated before integration, significantly improving code quality and collaboration stability.

The project began with a high-level "Vision" aimed at transforming digital engagement into social impact. The goal was to create an intuitive "Ad Wallet" where users could interact with advertisements to earn rewards, which could then be directed toward charitable causes. We prioritized clarity and motivation, ensuring the mission—connecting brands, users, and charities—was at the core of every design decision.

We transitioned from a vision to a formal structure by defining our Domain Concepts. This involved creating a robust UML model that mapped out the relationships between Users, Ads, and Charities. By focusing on the domain first, we ensured that the logic (e.g., how a reward from an Ad translates into a balance) was well-described and technically sound before full-scale implementation.

Using patterns like "Small Items" and "Definition of Ready," we built a comprehensive Product Backlog. Every User Story was crafted to meet the INVEST criteria (Independent, Negotiable, Valuable, Estimable, Small, and Testable). This phase established our "Enabling Specification" process, where epics were broken down into actionable 1–3 day tasks, complete with mockups and acceptance tests to ensure the team was never blocked.

The project reached a milestone with the release of a Vertical Prototype. Instead of focusing on standard features like Login, we implemented the unique core loop: viewing ads and managing rewards. This was supported by a growing suite of Unit and Integration tests to ensure the correctness of our functional requirements from day one.

We encountered some difficulties like defining the "Unique" Core: In the early stages, it was difficult to decide which functionality to prioritize for the Vertical Prototype. There was a strong temptation to build "standard" features like authentication first, but we had to pivot to ensure the most characteristic part of the "Ad Wallet" worked perfectly first.

Another challenge was balancing Modeling with Development: Maintaining high-quality UML diagrams (Logical and Physical) while the code was evolving rapidly required disciplined synchronization. Ensuring the "textual description supported the UML" was a constant exercise in technical writing.

Sprint 0 Retrospective

Did Well:

◦ Domain Modeling: We successfully modeled the domain concepts early on. The UML diagrams and textual descriptions provided a clear roadmap that kept the code clean.

◦Vision Clarity: The "Vision" pattern worked well to keep the team motivated and focused on the "why" of the project rather than just the "how."

◦User Story Quality: Applying the INVEST criteria early meant that our tasks were actionable and testable from day one.

Do Differently:

◦Architecture vs. Prototyping Balance: We spent a significant amount of time on the Logical and Physical Architecture (UML diagrams). In the next sprint, we should try to balance documentation time more closely with active coding to avoid bottlenecks.

◦Mockup Detail: While our UI mockups were explicit, some transitions were missing, leading to slight confusion during the prototype implementation.

Puzzles:

◦UML Synchronization: We are still figuring out the best way to keep our UML Package and Deployment diagrams perfectly in sync with the code as the project evolves rapidly.

◦Testing Scope: We aren't yet sure if our Acceptance Tests are too broad or if they should be more granular for specific UI components.

Improvements for Sprint 1:

◦Refine UI Navigation: We will explicitly map every button click in the mockups to a specific navigation event in the code.

◦Daily Sync on "Definition of Ready": Ensure that even bugs/improvements include screenshots of intended behavior before they are moved to "In Progress."

## Sprint 1
### Development Environment Setup

AdWallet is built using modern Android development tools. To set up the development environment, ensure you have the following versions: Android Studio (latest stable release), Kotlin 2.0.21, Android Gradle Plugin 9.0.1, and Jetpack Compose BOM 2024.09.00.

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Clone the repository: `git clone https://github.com/LEIC-ES-2025-26-2LEIC03/T5.git`
3. Open the cloned `T5` directory in Android Studio.
4. Allow Gradle to sync and download dependencies automatically (no additional configuration is required).
5. Build and run the project using the "Run" button (Shift+F10) on an emulator or physical device.

### Responsible Use of Agentic AI

Throughout the development process, the team utilized AI tools (specifically ChatGPT) to support code generation, troubleshoot compilation errors, and assist with boilerplate UI structuring. This allowed the team to maintain a high velocity while focusing on the core business logic and architectural design of the application.

It is important to note that while AI assisted in generating implementation code, all unit and acceptance tests were written manually by the team members. This ensures that human oversight remains the ultimate validator of the system's correctness and alignment with our user stories.

To maintain transparency, as required by the course guidelines, all AI-assisted code contributions are clearly identified. In early sprints, this was marked using `// MisterAI` comments directly within the source files. From Sprint 2 onwards, we standardized this practice by prefixing the relevant Git commit messages with the `[MisterAI]` tag, providing a clear audit trail of AI involvement in the project history.

In Sprint 1, the team successfully transitioned from a proof-of-concept to a robust application by implementing essential user features, including comprehensive user registration, a functioning donations system, a dedicated charities screen, and a settings interface with a system-wide theme toggle, all accessible via a new bottom navigation bar. These features, along with automated APK builds via our GitHub Actions CI/CD pipeline, were bundled into the [v1 release](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/releases/tag/v1).

## Sprint 2
In Sprint 2, the team successfully closed the core value loop of AdWallet by fully implementing the ad interaction and reward system, alongside tools for advertisers. We transitioned to a professional branch-per-feature Git workflow, integrating 8 distinct features into `main` via formal Pull Requests, backed by comprehensive testing and automated CI/CD checks.

- Watch ads and answer questionnaires to earn wallet rewards ([#25](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/25))
- Advertiser portal to upload advertisements for review ([#54](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/54))
- Rate advertisements after watching with 1-5 star system ([#42](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/42))
- Earnings history screen displaying all past earnings ([#44](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/44))
- Donation confirmation dialog and success screen ([#52](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/52))
- [Donation history screen with total donated tracking](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/53)
- [Report inappropriate advertisements with reason selection](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/41)
- [Notification preferences screen with per-type toggles](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/issues/49)

**Sprint 2 Audit Remediation:**
Following a comprehensive code audit, the following improvements were implemented to ensure 100% specification compliance:
- **Feature #49 (Notifications):** Added master toggle logic, missing "Promotions" toggle, and persistent storage via `SharedPreferences`.
- **Feature #53 (Donations):** Integrated SDG badges into the donation history records.
- **Architecture & Style:** Standardized all data models and aligned theme colors with specific hex codes.
- **Testing:** Upgraded to **Robolectric** to support advanced unit testing of Android components.

**Key Results:**
- Transitioned to strict **Branch-per-feature workflow**
- Authored **60+ unit tests** (all passing)
- Authored **16+ acceptance tests** (all passing)

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

iness.md)
- [Defects Log](docs/sprint2/defects.md)

All features have been integrated, verified by GitHub Actions, and packaged in the latest release. You can download and test the application here: [v2 — Sprint 2 Release](https://github.com/LEIC-ES-2025-26-2LEIC03/T5/releases/tag/v2).
### Sprint 3

### Sprint 4

### Final Release

