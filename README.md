# Lumina AI - Kotlin Native

Android native version of Lumina AI music generation app built with Kotlin and Jetpack Compose.

## Tech Stack

- **Language**: Kotlin 1.9
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM with Clean Architecture
- **DI**: Hilt
- **Network**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Media Player**: ExoPlayer (Media3)
- **Local Storage**: DataStore Preferences
- **Async**: Kotlin Coroutines + Flow

## Features

- User authentication (Login/Register)
- Music generation with AI
- Music player with notification controls
- Explore public music from other creators
- Favorite/unfavorite songs
- Public/private toggle
- Dark theme UI

## Project Structure

```
app/src/main/java/com/luminaai/app/
├── data/
│   ├── api/           # API service, interceptors
│   ├── model/         # Data models
│   └── repository/    # Data repositories
├── di/                # Hilt modules
├── service/           # Media playback service
├── ui/
│   ├── screens/       # Compose screens
│   ├── components/    # Reusable components
│   └── theme/         # Theme, colors, typography
└── util/              # Utilities
```

## Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on device/emulator

## Build

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## API

Uses the Lumina AI backend API at `https://luminaai.zesbe.my.id/api/v1/`

## Requirements

- Android Studio Hedgehog or newer
- Android SDK 34
- JDK 17
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

## License

MIT
