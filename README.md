# 📋 FieldNotes

> A modern Android field task & notes app built with Kotlin + Jetpack Compose

![Android](https://img.shields.io/badge/Android-16%20Baklava-green?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-latest-blue?logo=jetpack-compose)
![API](https://img.shields.io/badge/Min%20SDK-API%2036-brightgreen)
![Supabase](https://img.shields.io/badge/Backend-Supabase-3ECF8E?logo=supabase)

---

## 🧭 Overview

**FieldNotes** is a clean, offline-first Android application designed for field workers who need to quickly capture tasks, notes, and observations on the go.

Built as a learning project to explore the modern Android development stack: **Kotlin**, **Jetpack Compose**, **MVVM architecture**, **Room database**, and **Supabase** for cloud sync.

---

## ✨ Features

### MVP (v1.0) ✅ COMPLETE
- [x] 📝 Create notes with a title and body
- [x] 📋 View all notes in a scrollable list
- [x] ✅ Mark notes/tasks as complete
- [x] 🗑️ Delete notes (swipe to dismiss)
- [x] 💾 Offline-first — all data stored locally with Room DB

### v2.0 ✅ COMPLETE
- [x] 📭 Empty state when no notes exist
- [x] ✏️ Edit existing notes (tap to open)
- [x] 🎨 Color coded notes
- [x] ✨ Animations when adding/deleting notes
- [x] 🔍 Search notes by title
- [x] 🏷️ Filter notes by category

### v3.0 ✅ COMPLETE
- [x] 📅 Due dates with date picker
- [x] 🌙 Dark mode support (Material 3)
- [x] 📤 Export/share notes as plain text

### v4.0 — Cloud Sync (IN PROGRESS)
- [x] 🔐 User authentication (sign up / log in)
- [x] ☁️ Sync notes to Supabase cloud database
- [x] 📡 Real-time updates across devices
- [x] 🔄 Offline-first with background sync

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (Model-View-ViewModel) |
| Local Storage | Room Database + SQLite |
| State Management | StateFlow + ViewModel |
| Backend | Supabase (PostgreSQL + REST API) |
| Networking | Retrofit + OkHttp |
| Auth | Supabase Auth |
| Build System | Gradle with Kotlin DSL (`build.gradle.kts`) |
| Min SDK | API 36 (Android 16 "Baklava") |

---

## 🏗️ Architecture

```
app/
├── data/
│   ├── local/
│   │   ├── NoteDao.kt              # Room database access
│   │   ├── NoteDatabase.kt         # Room database definition
│   │   └── NoteEntity.kt           # DB table model
│   ├── remote/
│   │   ├── SupabaseClient.kt       # Supabase connection
│   │   ├── NoteRemoteDataSource.kt # API calls
│   │   └── NoteDto.kt              # Network data model
│   └── repository/
│       └── NoteRepository.kt       # Combines local + remote
│
├── domain/
│   └── model/
│       ├── Note.kt                 # Core business model
│       └── NoteCategory.kt         # Category enum
│
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── AddNoteScreen.kt
│   │   │   ├── EditNoteScreen.kt
│   │   │   └── NoteViewModel.kt
│   │   └── auth/
│   │       ├── LoginScreen.kt
│   │       └── SignUpScreen.kt
│   ├── utils/
│   │   └── utilities.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt
```

This follows **Clean Architecture** — data flows one way, each layer only knows about the layer below it.

---

## ☁️ Cloud Sync — How It Works

FieldNotes uses **Supabase** as its backend — an open source Firebase alternative built on PostgreSQL.

### Why Supabase?
- Real **PostgreSQL** database (industry standard used at Instagram, Spotify, Reddit)
- Auto-generated **REST API** from database tables
- Built-in **authentication** system
- **Real-time** subscriptions for live sync
- Generous free tier

### Sync Architecture

```
┌─────────────────────────────────────────────┐
│                  Android App                │
│                                             │
│  UI → ViewModel → Repository               │
│                      ↓          ↓          │
│               Room DB      Supabase API     │
│               (local)      (cloud)          │
└─────────────────────────────────────────────┘
                              ↕
                    ┌─────────────────┐
                    │    Supabase     │
                    │  PostgreSQL DB  │
                    │  + REST API     │
                    │  + Auth         │
                    └─────────────────┘
```

The app is **offline-first** — Room is always the source of truth locally. Supabase syncs in the background. If there's no internet, the app still works perfectly.

### Step by Step — What We Built

**Step 1 — Supabase Setup**
- Created a Supabase project at supabase.com
- Created a `notes` table in PostgreSQL matching our local schema
- Enabled Row Level Security (RLS) so users only see their own notes
- Got the project URL and API key

**Step 2 — Android Dependencies**
- Added Retrofit for HTTP networking
- Added OkHttp for request intercepting and logging
- Added Kotlin Serialization for JSON parsing

**Step 3 — Remote Data Layer**
- Created `SupabaseClient.kt` — configures Retrofit with the Supabase URL and API key
- Created `NoteDto.kt` — the network version of a Note (like NoteEntity but for the API)
- Created `NoteRemoteDataSource.kt` — the DAO equivalent for the API (GET, POST, PATCH, DELETE)

**Step 4 — Authentication**
- Created `LoginScreen.kt` and `SignUpScreen.kt`
- Integrated Supabase Auth for email/password login
- Added auth state to navigation — unauthenticated users see login screen

**Step 5 — Repository Sync**
- Updated `NoteRepository` to combine local Room data with remote Supabase data
- On app start: fetch remote notes and merge with local
- On create/update/delete: update Room first (instant UI), then sync to Supabase in background

**Step 6 — Row Level Security**
- Each note has a `user_id` column
- Supabase RLS policies ensure users can only read/write their own notes

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17+
- Android device or emulator running API 36
- Supabase account (free at supabase.com)

### Setup

1. Clone the repo:
```bash
git clone https://github.com/YOUR_USERNAME/FieldNotes.git
```

2. Create a Supabase project and run this SQL in the Supabase SQL editor:
```sql
create table notes (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references auth.users not null,
  title text not null,
  body text default '',
  is_completed boolean default false,
  created_at bigint default extract(epoch from now()) * 1000,
  color bigint default 4294967295,
  category text default 'NOTES',
  due_date bigint
);

alter table notes enable row level security;

create policy "Users can only access their own notes"
on notes for all
using (auth.uid() = user_id);
```

3. Add your Supabase credentials to `local.properties`:
```
SUPABASE_URL=your_project_url
SUPABASE_KEY=your_anon_key
```

4. Open in Android Studio, sync Gradle, hit **Run ▶️**

---

## 📚 What I Learned

- Kotlin syntax, null safety, coroutines, extension functions
- Declarative UI with Jetpack Compose + Material 3
- MVVM pattern and clean architecture
- Room for local persistence + database migrations
- StateFlow + combine for reactive filtering
- Retrofit + OkHttp for REST API networking
- Supabase PostgreSQL as a backend
- JWT authentication flow
- Offline-first sync strategies
- Modern Android project structure

---

## 🙋 Author

Built by **ME** as part of a self-directed Android learning journey.

---

## 📄 License

Feel free to use, fork, and learn from this project.