# GreenCheck 🌱

**GreenCheck** is a native, privacy-first Android application designed to help users instantly identify vegan products. By scanning product barcodes or entering UPC codes, GreenCheck leverages the [Open Food Facts](https://world.openfoodfacts.org/) API to provide real-time ingredient analysis and ethical dietary guidance.

Built following pure **Android Material 3 (M3) UI Guidelines** and a vibrant "Botanical Minimalist" aesthetic, GreenCheck offers a sophisticated, ad-free experience that respects user privacy through a strictly local-only architecture.

---

## 📱 App Screenshots

<p align="center">
  <img src="images/1. Welcome Screen.png" width="140" alt="Welcome Screen" />
  <img src="images/2. Privacy & Transparency.png" width="140" alt="Privacy & Transparency" />
  <img src="images/3. Home Screen.png" width="140" alt="Home Screen" />
  <img src="images/4. Manual Screen.png" width="140" alt="Manual Barcode Entry" />
  <img src="images/10. camera Screen.png" width="140" alt="Camera Barcode Scanner" />
  <img src="images/5. Product Screen - 1.png" width="140" alt="Product Screen - Status Banner" />
  <img src="images/6. Product Screen -21.png" width="140" alt="Product Screen - Breakdown" />
  <img src="images/7. Product Screen - 3.png" width="140" alt="Product Screen - Ingredients" />
  <img src="images/8. History Screen.png" width="140" alt="Scan History" />
  <img src="images/9. History Screen - Empty.png" width="140" alt="Empty History" />
</p>

---

## ✨ Pure Material 3 Native Design & Features

GreenCheck features a complete **Material 3 (M3) UI Layout & Color Restructuring**, delivering a fluid, native Android look and feel:

- **Material You & Dynamic Color Support**: Built on the official M3 color system with **Dynamic Color** (Android 12+) enabled by default, harmonized with a rich botanical tonal palette (`primaryContainer`, `secondaryContainer`, `tertiaryContainer`, `surfaceContainerHigh`).
- **Standard M3 Typography & Shape Token Scale**: Implements standard Material 3 typography tokens (`Display`, `Headline`, `Title`, `Body`, `Label`) and shape scales (4.dp to 28.dp).
- **4-Tab Navigation Scaffolding (`MainScaffolding`)**: Bottom `NavigationBar` with active M3 selection indicator pills and filled/outlined icon states for **Home**, **Manual Entry**, **Camera Scanner**, and **Scan History**.
- **Per-Screen Top App Bars**: Dynamic top app bars with screen titles and native back navigation arrow buttons (`IconButton` with `Icons.AutoMirrored.Filled.ArrowBack`) on sub-screens and product details.
- **Botanical Home Dashboard (`HomeScreen`)**: M3 hero card, prominent M3 `ExtendedFloatingActionButton` for scanning, and colorful quick action cards.
- **Real-Time Barcode Scanner (`ScannerScreen`)**: CameraX & Google ML Kit scanner with M3 translucent overlay card, `FilledTonalIconButton` vibration toggle, and local database caching.
- **Manual Barcode Dialpad (`ManualEntryScreen`)**: M3 `ElevatedCard` form container with digit counter supporting text and instant search.
- **Comprehensive Product Analysis (`ProductScreen`)**: Rich detail view with semantic status banners (Vegan, Non-Vegan, Uncertain), M3 `AssistChip` category/label badges, M3 `ListItem` ingredient rows with status dots and percentage badges, and full ingredient lists.
- **Plant-Based Alternatives Engine**: Discover certified vegan alternatives in the same product category with one-tap fetching, scrollable alternative product recommendation cards, and quick-preview modal sheets.
- **Local History & Wipe (`HistoryScreen` & `EmptyHistoryScreen`)**: Persistent scan history powered by Room database with M3 product cards and single-tap history clear.
- **Granular Daily Quotas & Privacy (`TransparencyInfoSheet` & `QuotaExhaustedScreen`)**: Independent rolling 24-hour limit enforcement across features (Barcode Scans, Manual Entries, Alternative Searches) with exact reset countdowns and zero tracking promises.
- **Automated Test Coverage (`TestTags.V2`)**: Every M3 component, screen, and action is tagged with standardized `TestTags` for automated UI and robot testing.

---

## 🚀 Core Features

- **Advanced Barcode Scanning**: Rapid product identification using CameraX and Google ML Kit.
- **Intelligent Ingredient Analysis**: Deep-dive analysis of ingredients to determine vegan suitability, presented in a clean, categorized list.
- **Plant-Based Alternatives Engine**: Discover certified vegan alternatives in the same category with interactive quick-view modal sheets and direct product navigation.
- **Locale-Aware Intelligence**: Automatically routes API requests based on your device's locale (e.g., US, IN, FR) for maximum regional relevance.
- **Fair-Use Usage Quotas**: Locally enforced, rolling 24-hour limits per feature to ensure fair API access and project stability.
- **Strictly Local History**: Persistent history of your scans, stored safely on your physical device.

## 🔒 Privacy & Transparency

GreenCheck is built on the principle of absolute privacy:

- **No Accounts Required**: No Google Sign-In, no emails, and no cloud accounts. You are completely anonymous.
- **Zero Data Collection**: We do not track your behavior, sell your data, or upload your scan history to any servers.
- **On-Device Sovereignty**: All your history, settings, and usage counters are stored strictly on your device using Jetpack DataStore and Room.
- **Open Source Integrity**: Our code is fully open source. We encourage independent audits of our codebase.

## 🎨 Design Philosophy: Native Botanical Material 3

- **Dynamic Botanical Palette**: Vibrant, accessible M3 tonal container fills and semantic status colors.
- **Soft Geometry**: Native M3 shape scale tokens (Extra Small to Extra Large) for organic surface depth.
- **Typography-First**: Official M3 typography hierarchy for maximum legibility.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 & Navigation Compose
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) & [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) with custom Interceptors for locale-awareness and User-Agent compliance.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Camera & Scanning**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit](https://developers.google.com/ml-kit/vision/barcode-scanning)
- **Testing**: [JUnit 4](https://junit.org/junit4/), [MockK](https://mockk.io/), and [UI Automation Engine](https://github.com/rajbirsehmi/UI-Automation-Engine) (custom Robot-based UI testing framework).

## 📦 Getting Started

1. Clone the repository: `git clone https://github.com/rajbirsehmi/GreenCheck.git`
2. Open in **Android Studio (Ladybug or newer)**.
3. Sync Gradle and run the `:app` module.

## ⚖️ Licensing & Attribution

- **Data Source**: This application uses data from [Open Food Facts](https://world.openfoodfacts.org/).
- **License**: The data is governed by the [Open Database License (ODbL)](https://opendatacommons.org/licenses/odbl/1-0/).
- **App Source**: GreenCheck is an open-source project on [GitHub](https://github.com/rajbirsehmi/GreenCheck).

---

*Scan responsibly. Eat ethically. 🌱*
