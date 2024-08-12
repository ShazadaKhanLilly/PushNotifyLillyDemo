# PushNotifyLillyDemo

PushNotifyLillyDemo is an Android application that demonstrates the use of Firebase Cloud Messaging (FCM) to send and receive push notifications. The app includes functionality to filter notifications based on user preferences and navigate to specific screens when a notification is clicked.

## Features

- **Firebase Cloud Messaging (FCM) Integration**: The app is integrated with Firebase to send and receive push notifications.
- **Custom Notification Handling**: Notifications are handled based on user-specific criteria, such as a "drug" condition.
- **Foreground and Background Notification Handling**: The app handles notifications in both foreground and background states.
- **Dynamic Navigation**: Tapping on a notification can navigate the user to a specific screen within the app.
- **WebView Integration**: The app includes a WebView to display web content when a notification is received with a URL.
- **Room Database Integration**: User preferences, such as selected drug, are stored locally using Room database.

## Screenshots

![Screenshot 1](url-to-screenshot1)
![Screenshot 2](url-to-screenshot2)

## Requirements

- Android Studio Bumblebee or later
- Android SDK 21 or higher
- Firebase account with a configured project
- Internet connection for Firebase services

## Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/ShazadaKhanLilly/PushNotifyLillyDemo.git
    ```
2. **Open the project in Android Studio**:
    - Go to **File > Open** and navigate to the cloned repository directory.

3. **Add `google-services.json`**:
    - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
    - Add your Android app to the Firebase project.
    - Download the `google-services.json` file.
    - Place the `google-services.json` file in the `app/` directory.

4. **Build and Run**:
    - Click the **Run** button in Android Studio to build and deploy the app to your connected device or emulator.

## Usage

1. **Register Your Device**:
    - When the app is launched for the first time, it will register your device with Firebase.
    - The app will subscribe your device to specific FCM topics based on user preferences.

2. **Send a Test Notification**:
    - Use the Firebase Console or Firebase Admin SDK to send a notification.
    - Example of a data-only message:
        ```json
        {
          "to": "/topics/all",
          "data": {
            "drug": "someDrug",
            "title": "New Update Available",
            "body": "Check out the latest update for your drug.",
            "url": "https://example.com"
          }
        }
        ```

3. **View Notifications**:
    - Notifications will appear in the system tray when the app is in the background or closed.
    - When the app is in the foreground, notifications will be handled manually and displayed using a custom implementation.

4. **Navigate and View Web Content**:
    - Clicking on a notification will navigate the user to the appropriate screen within the app.
    - If a URL is included in the notification, a WebView will be displayed with the specified content.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an issue to discuss improvements or report bugs.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any questions or suggestions, feel free to contact:

- **Name**: Shazada Khan
- **Email**: shazada.khan@lilly.com
