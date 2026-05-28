# Angelatro Project Guide

## 📋 Project Overview

**Angelatro** is a libGDX game project that runs on multiple platforms:
- **Desktop** (Windows, macOS, Linux) using LWJGL3
- **Android** mobile devices
- **iOS** (with additional setup)

The project is structured using Gradle multi-project configuration with:
- `core/` - Shared game logic
- `lwjgl3/` - Desktop launcher
- `android/` - Android launcher

---

## 🏗️ Project Structure

```
Angelatro/
├── core/                    # Main game logic (shared by all platforms)
│   ├── src/main/java/
│   │   └── io/angellsan94/angelatro/
│   │       ├── Main.java    # Game entry point
│   │       └── FirstScreen.java  # First game screen
│   └── build.gradle         # Core dependencies
├── lwjgl3/                  # Desktop launcher
│   ├── src/main/java/
│   │   └── Lwjgl3Launcher.java
│   └── build.gradle         # Desktop-specific configuration
├── android/                 # Android launcher
│   ├── src/main/java/
│   │   └── AndroidLauncher.java
│   ├── res/                 # Android resources
│   └── build.gradle         # Android-specific configuration
├── assets/                  # Game assets (images, sounds, etc.)
├── gradle/                  # Gradle configuration
├── build.gradle             # Root build configuration
├── settings.gradle          # Project settings
└── gradle.properties        # Project properties
```

---

## 🚀 Quick Start Guide

### Prerequisites

1. **Java Development Kit (JDK) 17+**
   - The project uses Java 17 with release mode
   - Gradle will automatically download compatible JDK versions

2. **Android Studio** (for Android development)
   - Required for Android SDK and emulator setup
   - Install Android Studio with Android SDK

3. **LibGDX SDK**
   - The project uses libGDX 1.14.0
   - All necessary dependencies are managed via Gradle

### First Build

```bash
# Sync Gradle and build the project
./gradlew clean build

# This will create:
# - core/build/libs/*.jar (shared library)
# - lwjgl3/build/libs/*.jar (desktop executable)
# - android/build/outputs/apk/ (Android APK)
```

### Running the Game

#### Desktop (Windows/macOS/Linux)

```bash
# Build the desktop version
./gradlew lwjgl3:run

# Or run directly from the built executable
./Angelatro-1.0.0.jar
```

#### Android

```bash
# Run on connected device or emulator
./gradlew android:installDebug

# Or build release APK
./gradlew android:assembleRelease
```

---

## 📱 Platform-Specific Instructions

### Android Development

1. **Set up Android SDK**
   ```bash
   # Install Android SDK command-line tools
   # Follow Android Studio installation guide
   ```

2. **Configure Android SDK location**
   Edit `android/local.properties`:
   ```properties
   sdk.dir=/path/to/android/sdk
   ```

3. **Build APK**
   ```bash
   ./gradlew android:assembleDebug
   ```

4. **Install on device**
   ```bash
   ./gradlew android:installDebug
   ```

### iOS Development

1. **Install Xcode**
   - Download from Mac App Store
   - Install via Xcode installer

2. **Configure iOS**
   - Edit `ios.xcodeproj/project.pbxproj`
   - Set `CODE_SIGNING_ALLOWED = NO`
   - Set `CODE_SIGNING_REQUIRED = NO`

3. **Build for iOS**
   ```bash
   # Use Xcode to build and run
   # Or use gradlew ios:installDebug
   ```

---

## 🔧 Build Configuration

### Gradle Tasks

#### Core Tasks
- `clean` - Clean build directory
- `build` - Build all projects
- `assemble` - Assemble all artifacts
- `installDebug` - Install debug APK

#### Platform-Specific Tasks
- `lwjgl3:run` - Run desktop version
- `android:installDebug` - Install debug APK
- `android:assembleRelease` - Build release APK

### Custom Tasks

#### Native Image (GraalVM)
```bash
# Build native executables for each platform
./gradlew nativeImage

# Platform-specific
./gradlew nativeImageLinux
./gradlew nativeImageMac
./gradlew nativeImageWin
```

#### Optimized JARs
```bash
# Build Linux-only JAR (5MB smaller)
./gradlew jarLinux

# Build Windows-only JAR (6MB smaller)
./gradlew jarWin
```

---

## 📦 Dependencies

### Core Dependencies (build.gradle)
- **libGDX**: 1.14.0
- **KryoNet**: 2.22.9
- **GraalVM Helper**: 2.0.1
- **LWJGL3**: 3.4.1

### Android Dependencies (android/build.gradle)
- **libGDX Android Backend**: 1.14.0
- **Desugar JDK Libs**: 2.1.5
- **ProGuard**: For release builds

### Native Dependencies
- **OpenJDK 21** for native image building
- Platform-specific native libraries (managed by libGDX)

---

## 🎨 Resources

### Android Resources
- **Icons**: `android/res/drawable/ic_launcher.png`
- **Strings**: `android/res/values/strings.xml`
- **Styles**: `android/res/values/styles.xml`
- **Colors**: `android/res/values/color.xml`

### Desktop Resources
- **Icons**: `lwjgl3/src/main/resources/icons/`
- **Assets**: `assets/`

---

## 🔐 Security & Privacy

### Android Permissions
- **OpenGL ES 2.0** required for graphics
- **Full backup** enabled (can be disabled if needed)
- **ProGuard** enabled for release builds

### Code Obfuscation
- **ProGuard** rules in `android/proguard-rules.pro`
- **R8 full mode** disabled for better compatibility

---

## 🐛 Debugging

### Desktop Debugging
```bash
# Run with verbose logging
./gradlew lwjgl3:run --info

# Run with specific JVM args
./gradlew lwjgl3:run -Dorg.gradle.jvmargs="-Xmx2G"
```

### Android Debugging
```bash
# Enable verbose logging
./gradlew android:installDebug --info

# Run on connected device
./gradlew android:installDebug -Dandroid.debug=true
```

### Common Issues

#### "SDK not found" Error
```bash
# Set Android SDK location
echo "sdk.dir=/path/to/android/sdk" > android/local.properties
```

#### "Gradle daemon memory" Error
```bash
# Increase Gradle memory
./gradlew --max-workers=4 --parallel
```

#### "Native library not found"
```bash
# Clean and rebuild
./gradlew clean build
```

---

## 📊 Project Configuration

### Gradle Settings

#### `gradle.properties`
- **Java version**: 17
- **Gradle daemon**: Disabled (to avoid memory issues)
- **Logging level**: Quiet
- **Android**: Use AndroidX, R8 disabled
- **GraalVM**: Disabled by default

#### `settings.gradle`
- **Android**: Enabled
- **iOS**: Enabled
- **LWJGL3**: Enabled
- **Core**: Enabled

---

## 🚀 Deployment

### Desktop Deployment

1. **Build executable**
   ```bash
   ./gradlew lwjgl3:installDist
   ```

2. **Distribute**
   - Windows: `build/install/`
   - macOS: `build/install/`
   - Linux: `build/install/`

### Android Deployment

1. **Build release APK**
   ```bash
   ./gradlew android:assembleRelease
   ```

2. **Sign APK**
   - Use keystore for release builds
   - Follow Google Play Store signing requirements

3. **Upload to Play Store**
   - Create Google Play Console account
   - Follow Play Store submission guidelines

---

## 📝 Development Workflow

### Recommended Workflow

1. **Code Changes**
   - Modify game logic in `core/src/main/java/`
   - Modify launcher in `lwjgl3/src/main/java/`
   - Modify Android launcher in `android/src/main/java/`

2. **Test on Desktop**
   ```bash
   ./gradlew lwjgl3:run
   ```

3. **Test on Android**
   ```bash
   ./gradlew android:installDebug
   ```

4. **Commit Changes**
   - Commit Gradle files separately if needed
   - Test builds before committing

### Version Control

```bash
# Commit code changes
git add core/src/ lwjgl3/src/ android/src/
git commit -m "Add new game feature"

# Commit Gradle changes separately
git add build.gradle settings.gradle gradle.properties
git commit -m "Update dependencies"
```

---

## 🎯 Next Steps

1. **Set up Android SDK** (if developing for Android)
2. **Configure iOS** (if developing for iOS)
3. **Add game assets** to `assets/`
4. **Implement game logic** in `core/`
5. **Test on multiple platforms**

---

## 📚 Additional Resources

### LibGDX Documentation
- https://libgdx.com/
- https://libgdx.com/documentation/

### Gradle Documentation
- https://docs.gradle.org/
- https://docs.gradle.org/current/userguide/tutorial_java_projects.html

### Android Development
- https://developer.android.com/
- https://developer.android.com/studio/build

### iOS Development
- https://developer.apple.com/
- https://developer.apple.com/documentation/xcode

---

## 🆘 Getting Help

### Common Errors

1. **"SDK not found"**
   - Set `sdk.dir` in `android/local.properties`

2. **"JDK not found"**
   - Install JDK 17+ or let Gradle download it

3. **"Android license not accepted"**
   ```bash
   sdkmanager --licenses
   ```

### Community Support
- **LibGDX Discord**: https://discord.gg/libgdx
- **Stack Overflow**: Use tag `libgdx`

---

## ✅ Checklist

- [ ] Java 17+ installed
- [ ] Android SDK installed (for Android)
- [ ] Xcode installed (for iOS)
- [ ] Project builds successfully
- [ ] Game runs on desktop
- [ ] Game runs on Android device
- [ ] Assets added and working
- [ ] Game logic implemented

---

**Happy Coding! 🎮**
