# GreenCheck 🌱

**GreenCheck** is a minimalist, privacy-first Android application designed to help users instantly identify vegan products. By scanning barcodes or searching an extensive database, GreenCheck leverages the [Open Food Facts](https://world.openfoodfacts.org/) API to provide real-time ingredient analysis and ethical dietary guidance.

Built with a "Botanical Minimalist" aesthetic, GreenCheck offers a sophisticated, ad-free experience that respects user privacy through a strictly local-only architecture.

---

## 📱 App Screenshots

### 🌙 Dark UI

<p align="center">
  <img src="images/Dark/GreenCheck - Welcome Screen.png" width="200" alt="Welcome Screen" />
  <img src="images/Dark/GreenCheck - Home Screen.png" width="200" alt="Home Screen" />
  <img src="images/Dark/GreenCheck - Manual Screen - 1.png" width="200" alt="Manual Entry Screen 1" />
  <img src="images/Dark/GreenCheck - Manual Screen - 2.png" width="200" alt="Manual Entry Screen 2" />
  <img src="images/Dark/GreenCheck - Ingredient Search Screen - 1.png" width="200" alt="Ingredient Search Screen 1" />
  <img src="images/Dark/GreenCheck - Ingredient Search Screen - 2.png" width="200" alt="Ingredient Search Screen 2" />
  <img src="images/Dark/GreenCheck - Product Screen - 1.png" width="200" alt="Product Screen 1" />
  <img src="images/Dark/GreenCheck - Product Screen - 2.png" width="200" alt="Product Screen 2" />
  <img src="images/Dark/GreenCheck - History Screen.png" width="200" alt="History Screen" />
  <img src="images/Dark/GreenCheck - Empty History Screen.png" width="200" alt="Empty History Screen" />
  <img src="images/Dark/GreenCheck - Privacy Screen.png" width="200" alt="Privacy Screen" />
  <img src="images/Dark/GreenCheck - Quota Usage Screen.png" width="200" alt="Quota Usage Screen" />
</p>

### ☀️ Light UI

<p align="center">
  <img src="images/Light/GreenCheck - Welcoem Screen.png" width="200" alt="Welcome Screen" />
  <img src="images/Light/GreenCheck - Home Screen.png" width="200" alt="Home Screen" />
  <img src="images/Light/GreenCheck - Manual Screen - 1.png" width="200" alt="Manual Entry Screen 1" />
  <img src="images/Light/GreenCheck - Manual Screen - 2.png" width="200" alt="Manual Entry Screen 2" />
  <img src="images/Light/GreenCheck - Ingredient Search Screen - 1.png" width="200" alt="Ingredient Search Screen 1" />
  <img src="images/Light/GreenCheck - Ingredient Search Screen - 2.png" width="200" alt="Ingredient Search Screen 2" />
  <img src="images/Light/GreenCheck - Ingredient Search Screen - 3.png" width="200" alt="Ingredient Search Screen 3" />
  <img src="images/Light/GreenCheck - Product Screen - 1.png" width="200" alt="Product Screen 1" />
  <img src="images/Light/GreenCheck - Product Screen - 2.png" width="200" alt="Product Screen 2" />
  <img src="images/Light/GreenCheck - History Screen.png" width="200" alt="History Screen" />
  <img src="images/Light/GreenCheck - Empty History Screen.png" width="200" alt="Empty History Screen" />
  <img src="images/Light/GreenCheck - Privacy Screen.png" width="200" alt="Privacy Screen" />
  <img src="images/Light/GreenCheck - Quota Usage Screen.png" width="200" alt="Quota Usage Screen" />
</p>

---

## ✨ What's New in Version 2.0 (v2)

GreenCheck v2 is a major redesign of the app, introducing a complete UI/UX overhaul, enhanced product and ingredient discovery, granular daily quota tracking, and comprehensive automated test tags:

- **5-Tab Navigation Scaffolding (`MainScaffolding`)**: A seamless navigation hub featuring **Home**, **Manual Entry**, **Camera Scanner**, **Scan History**, and **Product/Ingredient Search**.
- **Onboarding & Welcome Screen (`WelcomeScreen`)**: First-time user welcome flow highlighting privacy guarantees, zero tracking, and instant vegan scanning.
- **Botanical Home Dashboard (`HomeScreen`)**: Central hub offering quick action cards (Scan, Manual Entry, Search, History), scan statistics, and recent scan previews.
- **Real-Time Barcode Scanner (`ScannerScreen`)**: Upgraded CameraX & Google ML Kit scanner with flash controls, status indicators, and instant local database caching to minimize network usage.
- **Manual Barcode Dialpad (`ManualEntryScreen`)**: Dedicated numeric dialpad with clear and backspace controls for easy barcode entry when camera scanning isn't practical.
- **Dual-Mode Search (`SearchProductOrIngredientScreen`)**: Advanced search supporting both **Product Name Search** and **Ingredient Search** with tab switching, debounced queries, and detailed result cards.
- **Comprehensive Product Analysis (`ProductScreen`)**: Rich product detail view featuring clear vegan status banners (Vegan, Non-Vegan, Maybe/Unknown), high-res product photos, itemized ingredient breakdowns with vegan status badges, nutritional level indicators, and Open Food Facts attribution.
- **Local History & Wipe (`HistoryScreen` & `EmptyHistoryScreen`)**: Persistent scan history powered by Room database, complete with thumbnail cards, quick details view, and single-tap history wipe.
- **Granular Fair-Use Daily Quotas (`QuotaExhaustedScreen`)**: Independent rolling 24-hour limit enforcement across 4 separate features:
  - Barcode Scans
  - Manual Barcode Entries
  - Product Searches
  - Ingredient Searches
- **App Usage & Privacy Transparency Sheets (`TransparencyInfoSheet`)**: Quick-access top-bar info modal showing live daily quota usage, exact reset countdowns, ODbL dataset link, and transparent local privacy commitments.
- **Full V2 Automated Test Coverage (`TestTags.V2`)**: Every v2 component and screen is tagged with standardized `TestTags` for robust automated UI and robot testing.

---

## 🚀 Core Features

- **Advanced Barcode Scanning**: Rapid product identification using CameraX and Google ML Kit.
- **Intelligent Ingredient Analysis**: Deep-dive analysis of ingredients to determine vegan suitability, presented in a clean, categorized list.
- **Dual Product & Ingredient Search**: Discover products by name or search for specific ingredients across millions of entries.
- **Locale-Aware Intelligence**: Automatically routes API requests based on your device's locale (e.g., US, IN, FR) for maximum regional relevance.
- **Fair-Use Usage Quotas**: Locally enforced, rolling 24-hour limits per feature to ensure fair API access and project stability.
- **Strictly Local History**: Persistent history of your scans and searches, stored safely on your physical device.

## 🔒 Privacy & Transparency

GreenCheck is a passion project built on the principle of absolute privacy:

- **No Accounts Required**: No Google Sign-In, no emails, and no cloud accounts. You are completely anonymous.
- **Zero Data Collection**: We do not track your behavior, sell your data, or upload your scan history to any servers.
- **On-Device Sovereignty**: All your history, settings, and usage counters are stored strictly on your device using Jetpack DataStore and Room.
- **Open Source Integrity**: Our code is fully open source. We encourage independent audits of our codebase to verify our privacy and security claims.

## 🎨 Design Philosophy: Botanical Minimalism

GreenCheck features a custom-built UI that moves away from industrial Material defaults toward an organic, "Botanical" experience:
- **Sage Palette**: A calming, premium green-based color scheme.
- **Soft Geometry**: Generous whitespace and high-radius rounded corners for a smooth, modern feel.
- **Typography-First**: Refined text hierarchies and letter-spacing for maximum readability.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 & Navigation Compose
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) & [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with custom Interceptors for locale-awareness and mandatory User-Agent compliance.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Camera & Scanning**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit](https://developers.google.com/ml-kit/vision/barcode-scanning)
- **Testing**: [JUnit 4](https://junit.org/junit4/), [MockK](https://mockk.io/), and a custom Robot-based UI testing engine with `TestTags.V2`.

## 🏗 Architecture

GreenCheck follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern:
- **Data Layer**: Handles API communication, locale-based routing, Room database storage, and DataStore usage counters.
- **Domain Layer**: Contains pure Kotlin business models and repository abstractions.
- **UI Layer**: Declarative Compose v2 screens and components that react to `StateFlow` updates.

## 🧪 Testing & Quality

Run unit tests:
```bash
./gradlew :app:testDebugUnitTest
```

Run UI tests (requires a connected device/emulator):
```bash
./gradlew :app:connectedDebugAndroidTest
```

## 📦 Getting Started

1. Clone the repository: `git clone https://github.com/rajbirsehmi/GreenCheck.git`
2. Open in **Android Studio (Ladybug or newer)**.
3. Sync Gradle and run the `:app` module.

## ⚖️ Licensing & Attribution

- **Data Source**: This application uses data from [Open Food Facts](https://world.openfoodfacts.org/).
- **License**: The data is governed by the [Open Database License (ODbL)](https://opendatacommons.org/licenses/odbl/1-0/).
- **App Source**: GreenCheck is an open-source project. Check the [GitHub Repository](https://github.com/rajbirsehmi/GreenCheck) for more details.

---

*Scan responsibly. Eat ethically. 🌱*
