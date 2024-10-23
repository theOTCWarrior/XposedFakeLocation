# **XposedFakeLocation**

![GitHub License](https://img.shields.io/github/license/noobexon1/XposedFakeLocation)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)]()

**XposedFakeLocation** is an Android application and Xposed module that allows you to spoof your device's location globally or for specific apps. Customize your location with precision, including sensor data, and add randomization within a specified radius for enhanced privacy.

---

## **Table of Contents**

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Development](#development)
- [License](#license)
- [Disclaimer](#disclaimer)
- [Acknowledgements](#acknowledgements)

---

## **Features**

- **Global Location Spoofing**: Override your device's location data system-wide (Requires Android 11+).
- **Per-App Location Control**: Apply location spoofing to specific applications.
- **Custom Coordinates**: Set precise latitude and longitude.
- **Altitude and Accuracy Settings**: Customize altitude and accuracy values.
- **Randomization**: Add random offsets within a specified radius for enhanced privacy.
- **User-Friendly Interface**: Intuitive design for easy configuration.

---

## **Prerequisites**

- **Rooted Android Device**: The app requires root access to function properly. That being said, you can try working with Xposed virtual environement on non rooted device.
- **LSPosed**: Install the Xposed Framework compatible with your Android version.
  - [LSPosed](https://github.com/LSPosed/LSPosed)

---

## **Installation**

You can always install the latest stable version from the releases page. If you want to build by yourself:

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
   - Enable the **XposedFakeLocation** module.
   - If you decide to apply the module system wide, Reboot your device to apply changes.

---

## **Usage**

1. **Launch the App**

   - Open **XposedFakeLocation** from your app drawer.

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
   - The app will now override your device's location data based on the target(s) specified in the Xpsoed manager app.

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
  - **Value**: specify the radius in meters.

### **Favorites**

- Save frequently used locations for quick access.
- If a marker is already present on the map, the coordinates for the new favorite location will automatically be copied to the fields from it.
- Manage your favorites by adding or removing locations.

---

## **Development**

### **Built With**

- **Kotlin**: Programming language for Android development.
- **Jetpack Compose**: Modern toolkit for building native Android UI.
- **Xposed API**: Framework for runtime modification of system and app behavior.
- **OSMDroid**: Open-source map rendering engine for Android.

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

## **License**

Distributed under the **MIT License**. See [LICENSE](LICENSE) for more information.

---

## **Disclaimer**

This application is intended for **development and testing purposes only**. Misuse of location spoofing can violate terms of service of other applications and services. Use at your own risk.

---

## **Acknowledgements**

- [GpsSetter](https://github.com/Android1500/GpsSetter) - Highly inspired by this amazing project!
- [Xposed Framework](https://repo.xposed.info/) 
- [LSPosed](https://github.com/LSPosed/LSPosed)
- [OSMDroid](https://github.com/osmdroid/osmdroid) 
- [Contributors](https://github.com/yourusername/xposed-fake-location/graphs/contributors)


