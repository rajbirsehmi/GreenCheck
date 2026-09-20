# GreenCheck 🌱

**GreenCheck** is a minimalist, privacy-first Android application designed to help users instantly identify vegan products. By scanning barcodes or searching an extensive database, GreenCheck leverages the [Open Food Facts](https://world.openfoodfacts.org/) API to provide real-time ingredient analysis and ethical dietary guidance.

Built with a "Botanical Minimalist" aesthetic, GreenCheck offers a sophisticated, ad-free experience that respects user privacy through a strictly local-only architecture.

## 🚀 Core Features

- **Advanced Barcode Scanning**: Rapid product identification using CameraX and Google ML Kit.
- **Intelligent Ingredient Analysis**: Deep-dive analysis of ingredients to determine vegan suitability, presented in a clean, categorized list.
- **Deep Database Search**: Discover products by name or individual ingredients across millions of entries.
- **Locale-Aware Intelligence**: Automatically routes API requests based on your device's locale (e.g., US, IN, FR) for maximum regional relevance.
- **Daily Usage Quotas**: Locally enforced, rolling 24-hour limits (e.g., 10 scans/day) to ensure fair API access and project stability.
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
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Local Storage**: [Room](https://developer.android.com/training/data-storage/room) & [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with custom Interceptors for locale-awareness and mandatory User-Agent compliance.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Camera & Scanning**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit](https://developers.google.com/ml-kit/vision/barcode-scanning)
- **Testing**: [JUnit 4](https://junit.org/junit4/), [MockK](https://mockk.io/), and a custom Robot-based UI testing engine.

## 🏗 Architecture

GreenCheck follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern:
- **Data Layer**: Handles API communication, locale-based routing, and local persistence.
- **Domain Layer**: Contains pure Kotlin business models and repository abstractions.
- **UI Layer**: Declarative Compose screens that react to `StateFlow` updates.

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
