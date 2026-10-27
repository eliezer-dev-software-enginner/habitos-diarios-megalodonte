# Hábitos Diários

Android sample application built with Megalodonte's native UI components. It lets you add, mark or unmark, and remove daily habits. Habits are stored on the device; their completion status resets when the app starts on a different local date.

The app uses Java screens and Megalodonte's Android bridge. It does not use a WebView. The [Android JSONC guide](https://github.com/eliezer-dev-software-enginner/megalodonte-android/blob/main/docs/JSONC_ANDROID.md) documents the bridge's JSONC support, although this sample builds its screens in Java.

## Requirements and build

- JDK 25, Android SDK Platform 35, and an Android SDK configured for Gradle.
- Checkouts of `megalodonte-android`, `megalodonte-core`, `megalodonte-theme`, and `megalodonte-reactivity-portable`. These framework projects are required by `settings.gradle.kts` and are not included here.

By default, Gradle looks for `megalodonte-android` at `../../framework/megalodonte-android` relative to this repository. The other three checkouts must be in `../../framework/libraries/`, each in a directory matching its project name. Alternatively, set `MEGALODONTE_ANDROID_HOME` to the `megalodonte-android` checkout; the other checkouts must then be under `../libraries/` relative to it.

From the repository root, run:

```sh
./gradlew :app:assembleDebug :app:lintDebug
```

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk`. Open the repository in Android Studio to run it on an Android 15 (API 35) device or emulator; the app's minimum, compile, and target SDK are all 35. If Android Studio shows an import or constructor error while Gradle builds successfully, use **Sync Project with Gradle Files** to refresh the local framework modules.

## Use

Tap **Novo hábito**, enter a name, and tap **Salvar hábito** to return to the list. Enter a displayed habit number and tap **Marcar / desmarcar** or **Remover hábito**. **Voltar** and Android Back return from the form to the list. Names remain saved between app launches; completion resets when the app is next started after the local date changes.

The Java host registers `/habits` and `/new` with Megalodonte's `AndroidRouter`. Each route creates a native `ScreenComponent`; `HabitStore` shares the saved habits between them using Android `SharedPreferences`.

The project was generated with `megalodonte-cli --platform android --mode native` and then completed as a functional sample. To run the project's Gradle checks, use `./gradlew :app:assembleDebug :app:lintDebug :app:check`.

## License

No license file is included in this repository. Confirm licensing terms with the project maintainer before reuse.
