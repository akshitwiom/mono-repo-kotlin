# mono-repo-kotlin

A **multi-app Android monorepo** built with Kotlin, Jetpack Compose, Hilt, and Clean Architecture following the Android Development Guidelines.

---

## Apps

### 1. Connection Provider (`app:partner`)
The ISP partner's app for managing bookings and installers.

| Screen | Description |
|--------|-------------|
| **Login** | Phone+OTP or Username+Password authentication |
| **Home** | Dashboard showing pending task count (tappable) |
| **Tickets** | Two-tab view: *To Do* and *In Progress* |
| **My Team** | List of installers (Rohits) with status and task count |
| **Settings** | User profile and logout |

### 2. Connection Setup (`app:rohit`)
The installer's app for viewing and managing assigned tasks.

| Screen | Description |
|--------|-------------|
| **Login** | Phone+OTP or Username+Password authentication |
| **Home** | Dashboard showing pending task count (tappable) |
| **Tickets** | Two-tab view: *To Do* and *In Progress* |

---

## Architecture

```
mono-repo-kotlin/
├── build-logic/                    # Convention plugins (shared build config)
│   └── convention/
├── app/
│   ├── partner/                    # Connection Provider APK
│   └── rohit/                      # Connection Setup APK
├── core/
│   ├── common/                     # Dispatchers, Result type, extensions
│   ├── model/                      # Domain models (User, Ticket, Installer)
│   ├── data/                       # Repository interfaces + mock implementations
│   ├── network/                    # Retrofit services, interceptors, DI
│   ├── database/                   # Room database, entities, DAOs
│   ├── designsystem/              # Theme, shared components (StatCard, etc.)
│   └── ui/                         # Shared composables (TicketListItem, etc.)
├── feature/
│   ├── auth/                       # Login screen + ViewModel
│   ├── home/                       # Home dashboard
│   ├── tickets/                    # Ticket list with tabs
│   ├── settings/                   # Settings + logout (Partner only)
│   └── team/                       # Installer list (Partner only)
└── gradle/
    └── libs.versions.toml          # Single version catalog
```

### Layer Overview

- **UI Layer** → Jetpack Compose screens (stateless content composables)
- **ViewModel Layer** → `StateFlow<UiState>` + UDF event handling
- **Domain Layer** → Repository interfaces in `:core:data`
- **Data Layer** → Mock repositories, Retrofit services, Room DAOs

### Key Design Decisions

- **Convention Plugins** — All compile SDK, JVM target, flavor, and Compose config is defined once in `build-logic/` and applied via `monorepo.android.*` plugin IDs.
- **Feature modules never depend on each other** — they only depend on `:core:*` modules.
- **Mock flavor** — Full offline-first development with hardcoded data. No backend needed.
- **Shared navigation** — Each feature module exposes a `navigation/` package with route constants and `NavGraphBuilder` extensions. App modules wire them together.

---

## Build Flavors

| Flavor | `BASE_URL` | `USE_MOCK` | Purpose |
|--------|-----------|------------|---------|
| `dev` | `https://dev-api.example.com/v1/` | – | Development server |
| `mock` | `https://mock-api.example.com/v1/` | `true` | Fully offline mock data |
| `qa` | `https://qa-api.example.com/v1/` | – | QA / staging server |
| `prod` | `https://api.example.com/v1/` | – | Production server |

Build variant example: `mockDebug`, `devDebug`, `qaRelease`, `prodRelease`.

---

## Mock Login Credentials

### Partner App
| Method | Credential | Value |
|--------|-----------|-------|
| Username/Password | Username | `partner` |
| | Password | `partner123` |
| Phone/OTP | Phone | `9999900000` |
| | OTP | `123456` |

### Rohit App
| Method | Credential | Value |
|--------|-----------|-------|
| Username/Password | Username | `rohit` |
| | Password | `rohit123` |
| Phone/OTP | Phone | `9999911111` |
| | OTP | `123456` |

---

## Tech Stack

| Concern | Library |
|---------|---------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture + UDF |
| DI | Hilt (KSP) |
| Networking | Retrofit 3 + OkHttp + kotlinx.serialization |
| Local Storage | Room + DataStore Preferences |
| Async | Coroutines + StateFlow |
| Navigation | Navigation Compose |
| Image Loading | Coil 3 |
| Logging | Timber |

---

## Building

```bash
# Partner app — mock flavor, debug build
./gradlew :app:partner:assembleMockDebug

# Rohit app — mock flavor, debug build
./gradlew :app:rohit:assembleMockDebug

# All variants
./gradlew assembleDebug
```

---

## Module Dependency Graph

```
app:partner ──┬── feature:auth ────┬── core:data ──── core:network
              ├── feature:home ────┤                  core:model
              ├── feature:tickets ─┤                  core:common
              ├── feature:settings ┤
              ├── feature:team ────┘
              ├── core:designsystem
              └── core:ui

app:rohit ────┬── feature:auth
              ├── feature:home
              ├── feature:tickets
              ├── core:designsystem
              └── core:ui
```

Feature modules depend only on `:core:*` — never on each other.
