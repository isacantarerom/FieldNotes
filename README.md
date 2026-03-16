# 📋 FieldNotes

> A modern Android field task & notes app built with Kotlin + Jetpack Compose

![Android](https://img.shields.io/badge/Android-16%20Baklava-green?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-latest-blue?logo=jetpack-compose)
![API](https://img.shields.io/badge/Min%20SDK-API%2036-brightgreen)

---

## 🧭 Overview

**FieldNotes** is a clean, offline-first Android application designed for field workers who need to quickly capture tasks, notes, and observations on the go — no internet required.

Built as a learning project to explore the modern Android development stack: **Kotlin**, **Jetpack Compose**, **MVVM architecture**, and **Room database**.

---

## ✨ Features

### MVP (v1.0)
- [ ] 📝 Create notes with a title and body
- [ ] 📋 View all notes in a scrollable list
- [ ] ✅ Mark notes/tasks as complete
- [ ] 🗑️ Delete notes (swipe to dismiss)
- [ ] 💾 Offline-first — all data stored locally with Room DB

### Stretch Goals (v2.0)
- [ ] 🏷️ Tag / categorize notes
- [ ] 🔍 Search and filter notes
- [ ] 📅 Add due dates and reminders
- [ ] 🌙 Dark mode support (Material 3)
- [ ] 📤 Export notes as plain text

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 Expressive |
| Architecture | MVVM (Model-View-ViewModel) |
| Local Storage | Room Database |
| State Management | StateFlow + ViewModel |
| Build System | Gradle with Kotlin DSL (`build.gradle.kts`) |
| Min SDK | API 36 (Android 16 "Baklava") |

---

## 🏗️ Architecture

```
app/
├── data/
│   ├── local/
│   │   ├── NoteDao.kt          # Database access object
│   │   ├── NoteDatabase.kt     # Room database definition
│   │   └── NoteEntity.kt       # DB table model
│   └── repository/
│       └── NoteRepository.kt   # Single source of truth
│
├── domain/
│   └── model/
│       └── Note.kt             # Core business model
│
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   └── detail/
│   │       ├── DetailScreen.kt
│   │       └── DetailViewModel.kt
│   ├── components/
│   │   └── NoteCard.kt         # Reusable composable
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt
```

This follows **Clean Architecture** principles — data flows one way, each layer only knows about the layer below it.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17+
- Android device or emulator running API 36

Open the project in Android Studio, let Gradle sync, then hit **Run ▶️**.

---

## 📚 What I Learned

This project was built to get hands-on with:
- Kotlin syntax, null safety, and coroutines
- Declarative UI with Jetpack Compose
- MVVM pattern and separation of concerns
- Room for local persistence
- StateFlow for reactive UI updates
- Navigation in Compose
- Modern Android project structure

---

## 🙋 Author

Built by **me** as part of a self-directed Android learning journey.

---

## 📄 License

Feel free to use, fork, and learn from this project.
