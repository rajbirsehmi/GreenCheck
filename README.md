# Is It Vegan? 🌱

**Is It Vegan?** is a modern Android application designed to help users instantly determine if a food product is vegan-friendly by scanning its barcode. Leveraging the Open Food Facts database, the app analyzes ingredients and provides a clear status, helping users make informed dietary choices on the go.

## 🚀 Features

- **Barcode Scanning**: High-performance scanning using CameraX and ML Kit.
- **Instant Analysis**: Real-time ingredient analysis for vegan and vegetarian status.
- **Detailed Product Info**: View brands, ingredients lists, and environmental scores (Eco-Score).
- **Recent Scans**: Keep track of your scanning history locally.
- **Modern UI**: Built entirely with Jetpack Compose following Material 3 guidelines.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Camera & Scanning**: [CameraX](https://developer.android.com/training/camerax) & [ML Kit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)
- **Testing**:
    - [JUnit 4](https://junit.org/junit4/) for Unit Testing.
    - [MockK](https://mockk.io/) for mocking.
    - [UI-Automation-Engine](https://github.com/rajbirsehmi/UI-Automation-Engine): Custom Robot-based testing framework for robust Compose UI tests.

## 🏗 Architecture

The project follows **Clean Architecture** principles and the **MVVM (Model-View-ViewModel)** pattern:

- **Data Layer**: Handles API communication (Retrofit) and local persistence (Room).
- **Domain Layer**: Contains business models and repository interfaces.
- **UI Layer**: Composable screens and ViewModels managing state with `StateFlow`.

## 🧪 Testing

The project emphasizes quality with a robust testing suite.

### Running Unit Tests
```bash
./gradlew :app:testDebugUnitTest
```

### Running UI Tests
UI tests are built using the **Robot Pattern** and the [UI-Automation-Engine](https://github.com/rajbirsehmi/UI-Automation-Engine).
```bash
./gradlew :app:connectedDebugAndroidTest
```

## 📦 Getting Started

1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync the project with Gradle files.
4. Run the `:app` module on an emulator or physical device.

---

*Note: This app uses data from [Open Food Facts](https://world.openfoodfacts.org/).*
