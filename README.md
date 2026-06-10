# Privacy-Aware App Permission Management System for Mobile Users

## Project Overview

Privacy-Aware App Permission Management System is an Android application designed to help mobile users better understand the privacy risks related to application permissions.

Many users grant permissions to mobile applications without fully understanding what type of personal information may be accessed. This project aims to provide a simple tool that scans installed applications, extracts requested permissions, and displays permission-related privacy risk information in a user-friendly way.

This project is currently developed as a prototype for an academic integration project.

## Main Objectives

* Scan installed applications on an Android device or emulator
* Retrieve application names and package names
* Extract requested permissions from installed applications
* Display permission information to users
* Provide a basic privacy risk level based on sensitive permissions
* Prepare a local SQLite database structure for storing application and permission data

## Technologies Used

* Java
* Android Studio
* Android SDK
* XML Layouts
* Gradle
* SQLite
* Git and GitHub

## Current Features

### 1. Android Application Framework

The basic Android application framework has been created using Java and XML. The application can run on an Android emulator.

### 2. App Scanner Module

The app scanner uses Android `PackageManager` APIs to retrieve installed launcher applications from the device.

The scanner can collect:

* Application name
* Package name
* Requested permissions
* Permission count
* Basic risk level

### 3. Permission Extraction

The application extracts requested permissions from installed apps using Android package information.

Examples of permissions that may be detected include:

* Camera permission
* Location permission
* Contacts permission
* Phone permission
* SMS permission
* Microphone permission

### 4. Basic Risk Analysis

The current prototype includes a simple risk analysis method. Applications requesting sensitive permissions such as camera, location, contacts, SMS, phone, or microphone permissions may be classified as medium or high risk depending on the number and type of permissions requested.

### 5. SQLite Database Structure

A SQLite database helper class has been created to support local data storage. The database structure is prepared for storing application information, permission details, permission count, risk level, and scan time.

## Project Structure

```text
app/
 └── src/
     └── main/
         ├── java/
         │   └── com.example.privacyawarepermissionsystem/
         │       ├── MainActivity.java
         │       ├── AppScanner.java
         │       └── DatabaseHelper.java
         ├── res/
         │   ├── layout/
         │   │   └── activity_main.xml
         │   └── values/
         │       └── strings.xml
         └── AndroidManifest.xml
```

## How the System Works

1. The user opens the Android application.
2. The user taps the **Scan Installed Apps** button.
3. The app uses Android `PackageManager` to scan installed applications.
4. The app retrieves each application's package name and requested permissions.
5. The app displays permission information and a basic risk level.
6. The SQLite database helper prepares local storage for future database integration.

## Screenshots

Screenshots can be added here after the prototype interface and code screenshots are finalized.

Recommended screenshots:

1. Android Studio project structure
2. Application running on an emulator
3. App scanner source code
4. Permission extraction implementation
5. SQLite database helper implementation
6. GitHub repository page
7. GitHub commit history

Example format:

```markdown
![Application Running on Emulator](screenshots/emulator_running.png)
```

## Current Development Status

The project is currently in the prototype stage.

Completed or partially completed components include:

* Android project setup
* Basic user interface
* App scanner module
* Permission extraction logic
* Basic risk level display
* SQLite database helper structure
* GitHub version control setup

## Future Development Plan

Future development will focus on improving the privacy risk evaluation system and making the application more useful for end users.

Planned features include:

* More accurate permission risk scoring
* Permission risk classification system
* Privacy risk dashboard
* Risk visualization components
* User recommendation engine
* Improved database integration
* Better user interface design
* More detailed app permission reports

## Contributors

* William Xu
* Rex Ma

## Course Information

INFO 4290: Integration Project II

## License

This project is created for academic purposes. A license may be added later if the project is prepared for public release.
