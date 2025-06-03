# 🚀 PaymentApp

[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Enabled-blue.svg)](https://developer.android.com/jetpack/compose)  
[![Firebase](https://img.shields.io/badge/Firebase-Integrated-orange)](https://firebase.google.com/)  
[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple)](https://kotlinlang.org/)

PaymentApp is a modern **Android payment system** built using **Jetpack Compose, Firebase Firestore, and CameraX for barcode scanning**. It enables users to **scan products, add them to the cart, and process payments seamlessly**.

---

## 📌 **Features**
✅ **QR Code & Barcode Scanner** using `CameraX` & `ML Kit`  
✅ **Cart Management** - Add, Update, and Remove Items  
✅ **Firestore Database Integration** - Realtime Sync for Orders & Cart  
✅ **Jetpack Compose UI** - Fully Modern UI Components  
✅ **CI/CD Ready** - Includes unit tests & structured architecture  

---

## 🏗 **Project Structure**
```
📂 paymentapp
│── 📂 app
│   │── 📂 data               # Data Layer (Repositories, Firestore)
│   │── 📂 domain             # Business Logic (Use Cases, Models)
│   │── 📂 ui                 # UI Layer (Jetpack Compose Screens)
│   │── 📂 di                 # Hilt DI for AppModule & RepositoryModule
│   │── 📂 utils              # Utility Classes (ResponseState, Extensions)
│   │── 📜 MainActivity.kt    # Entry Point
│── 📂 scanner                # QR/Barcode 
│── │── qrscanner             # Open Compose UI with CameraX
│── │── camerautils           # Capture QR Code from CameraX & ML Kit to get Json data
│── 📜 build.gradle.kts       # Project Configuration
│── 📜 README.md              # Documentation
```

---

## 🔥 **Tech Stack & Libraries**
| **Technology** | **Usage** |
|---------------|----------|
| **Kotlin** | Modern Android programming language |
| **Jetpack Compose** | Declarative UI framework |
| **Firebase Firestore** | NoSQL Realtime Database |
| **CameraX + ML Kit** | Barcode/QR Code Scanner |
| **Dagger-Hilt** | Dependency Injection |
| **Retrofit** | Networking (if needed) |
| **Coroutines & Flow** | Asynchronous operations |
| **JUnit & Mockito** | Unit Testing |

---

## 🛠 **Setup & Installation**
### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/MalligaSaravanan93/KioskApp.git
cd paymentapp
```

### **2️⃣ Open in Android Studio**
- Ensure you have the latest **Android Studio + Kotlin Plugin** installed.
- Open the project and **sync Gradle files**.

### **3️⃣ Setup Firebase**
1. Create a Firebase Project at [Firebase Console](https://console.firebase.google.com/)
2. Add an **Android App** and download the `google-services.json` file.
3. Place the file inside:
   ```
   app/
   ```
4. Enable **Firestore & Authentication** in Firebase.

### **4️⃣ Run the App**
```sh
./gradlew assembleDebug
```
OR click **Run ▶** in Android Studio.

---

## 📸 **Screenshots**
| **Scan Screen** | **Cart** | **Orders** | **Invoice Details** |
|----------------|------------|------------------|------------------|
|![PaymentApp-Scan](https://github.com/user-attachments/assets/368dd699-66e2-4792-bfab-e680c11e1247)| ![PaymentApp - Cart](https://github.com/user-attachments/assets/35ab6ffd-779b-47a1-a913-7046be4ec714) | ![PaymentApp - Orders](https://github.com/user-attachments/assets/ce0f189d-dad9-4987-9149-832d303736f1)| ![PaymentApp - InvoiceDetails](https://github.com/user-attachments/assets/46147d4b-d15b-47dc-adfe-fef8ffdf2ba4)|

## 📡 **Scanner Library**
The **scanner module** in this project is created as a **separate library** to make it **accessible by both Jetpack Compose and traditional View-based Android code**. This ensures **maximum compatibility** and allows seamless integration into different types of Android projects.

✅ **Supports Jetpack Compose Screens**  
✅ **Works with XML-based UI**  
✅ **Uses CameraX & ML Kit for QR/Barcode Scanning**  

To use the scanner library in your own project, you can simply include the module and call the provided scanning interface. 

---

## 🚀 **Contributing**
1. **Fork** the repo.
2. Create a **feature branch** (`git checkout -b feature-new-feature`).
3. Commit your changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature-new-feature`).
5. Open a **Pull Request**.

---

## 📜 **License**
This project is **open-source** and licensed under the **MIT License**.  
See the full license [here](LICENSE).

---
🚀 **Star this repository** if you found it useful! ⭐

