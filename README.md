# **XposedFakeLocation**

[![License](https://img.shields.io/github/license/yourusername/xposed-fake-location)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)]()
[![API](https://img.shields.io/badge/API-21%2B-blue.svg)]()

**XposedFakeLocation** is an Android application and Xposed module that allows you to spoof your device's location globally or for specific apps. Customize your location with precision, including sensor data, and add randomization within a specified radius for enhanced privacy.

---

## **Table of Contents**

- [Features](#features)
- [Screenshots](#screenshots)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)
- [Disclaimer](#disclaimer)
- [Acknowledgements](#acknowledgements)
- [Contact](#contact)

---

## **Features**

- **Global Location Spoofing**: Override your device's location data system-wide.
- **Per-App Location Control**: Apply location spoofing to specific applications.
- **Custom Coordinates**: Set precise latitude and longitude.
- **Altitude and Accuracy Settings**: Customize altitude and accuracy values.
- **Randomization**: Add random offsets within a specified radius for enhanced privacy.
- **User-Friendly Interface**: Intuitive design for easy configuration.
- **Open Source**: Contributions are welcome!

---

## **Screenshots**

<p align="center">
  <img src="screenshots/main_screen.png" alt="Main Screen" width="250">
  <img src="screenshots/settings_screen.png" alt="Settings Screen" width="250">
  <img src="screenshots/map_view.png" alt="Map View" width="250">
</p>

---

## **Prerequisites**

- **Rooted Android Device**: The app requires root access to function properly.
- **Xposed Framework or LSPosed**: Install the Xposed Framework compatible with your Android version.
  - [Xposed Framework](https://repo.xposed.info/module/de.robv.android.xposed.installer)
  - [LSPosed](https://github.com/LSPosed/LSPosed)

---

## **Installation**

1. **Clone or Download the Repository**

   ```bash
   git clone https://github.com/yourusername/xposed-fake-location.git
   ```

2. **Build the Application**

   - Open the project in **Android Studio**.
   - Build the APK using **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
   - Alternatively, use Gradle:

     ```bash
     ./gradlew assembleDebug
     ```

3. **Install the APK on Your Device**

   - Transfer the APK to your device.
   - Install the APK using a file manager or via ADB:

     ```bash
     adb install app/build/outputs/apk/debug/app-debug.apk
     ```

4. **Activate the Xposed Module**

   - Open **Xposed Installer** or **LSPosed Manager**.
   - Enable the **Xposed Fake Location** module.
   - Reboot your device to apply changes.

---

## **Usage**

1. **Launch the App**

   - Open **Xposed Fake Location** from your app drawer.

2. **Select a Location**

   - Use the integrated map to select your desired location.
   - Tap on the map to set the fake location.

3. **Configure Settings**

   - Access the **Settings** screen to customize:

     - **Accuracy**: Enable and set a custom accuracy value.
     - **Altitude**: Enable and set a custom altitude.
     - **Randomization Radius**: Set the radius in meters for location randomization.

4. **Start Spoofing**

   - Toggle the **Start** button to begin location spoofing.
   - The app will now override your device's location data.

5. **Stop Spoofing**

   - Toggle the **Stop** button to cease location spoofing.

---

## **Configuration**

### **Settings**

- **Accuracy**

  - Enable to set a custom accuracy value for the fake location.
  - **Accuracy Value**: Specify in meters.

- **Altitude**

  - Enable to set a custom altitude for the fake location.
  - **Altitude Value**: Specify in meters.

- **Randomization Radius**

  - Set the radius in meters to randomize your location within a circle around the selected point.
  - **Value**: Enter `0` to disable randomization.

### **Favorites**

- Save frequently used locations for quick access.
- Manage your favorites by adding or removing locations.

---

## **Development**

### **Built With**

- **Kotlin**: Programming language for Android development.
- **Jetpack Compose**: Modern toolkit for building native Android UI.
- **Xposed API**: Framework for runtime modification of system and app behavior.
- **OSMDroid**: Open-source map rendering engine for Android.

### **Project Structure**

- **App Module**: Contains the main application code and UI.
- **Xposed Module**: Contains the hooks and modifications applied via Xposed.

### **Prerequisites**

- **Android Studio Arctic Fox** or newer.
- **Android SDK** with API level 31 or above.

### **Building from Source**

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/xposed-fake-location.git
   ```

2. **Open in Android Studio**

   - Navigate to the project directory.
   - Open the project with **Android Studio**.

3. **Sync Gradle**

   - Allow Gradle to download all dependencies.

4. **Build and Run**

   - Connect your rooted device or start an emulator with root capabilities.
   - Run the app from **Android Studio**.

---

## **Contributing**

Contributions are what make the open-source community an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. **Fork the Project**

   ```bash
   git fork https://github.com/yourusername/xposed-fake-location.git
   ```

2. **Create Your Feature Branch**

   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. **Commit Your Changes**

   ```bash
   git commit -m 'Add some AmazingFeature'
   ```

4. **Push to the Branch**

   ```bash
   git push origin feature/AmazingFeature
   ```

5. **Open a Pull Request**

---

## **License**

Distributed under the **MIT License**. See [LICENSE](LICENSE) for more information.

---

## **Disclaimer**

This application is intended for **development and testing purposes only**. Misuse of location spoofing can violate terms of service of other applications and services. Use at your own risk.

---

## **Acknowledgements**

- [Xposed Framework](https://repo.xposed.info/)
- [LSPosed](https://github.com/LSPosed/LSPosed)
- [OSMDroid](https://github.com/osmdroid/osmdroid)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Contributors](https://github.com/yourusername/xposed-fake-location/graphs/contributors)

---

## **Contact**

**Your Name** - [@your_twitter](https://twitter.com/your_twitter) - your.email@example.com

Project Link: [https://github.com/yourusername/xposed-fake-location](https://github.com/yourusername/xposed-fake-location)
