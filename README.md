# Anime Tracker

Anime Tracker is an Android application for searching anime, saving titles to personal lists, and tracking episode progress. It combines the Jikan API with Firebase Authentication and Firebase Realtime Database.

## Features

- Create an account and sign in
- Search anime through the Jikan API
- View title, score, year, type, synopsis, image, and trailer details
- Save titles to Watchlist, Watching, or Completed
- Update watched episode counts
- Move titles between lists
- Store each user's data separately in Firebase

## Technology

- Java
- Android SDK
- Firebase Authentication
- Firebase Realtime Database
- Retrofit and Gson
- Glide
- RecyclerView
- ViewPager2
- Jikan REST API

## Run locally

1. Open the repository in Android Studio.
2. Connect it to a Firebase project.
3. Add `google-services.json` to the app module.
4. Enable Email/Password authentication.
5. Create a Realtime Database and configure its rules.
6. Run the application on an emulator or Android device.

## Current limitations

- Firebase error handling can be improved in several callbacks.
- The application does not include automated tests.
- Network, empty-state, and offline experiences need further refinement.
- Firebase security rules are not documented here.
- More logic should be moved from Activities and Fragments into repositories and ViewModels.

## Possible improvements

- Migrate to Kotlin and MVVM
- Add Room caching
- Add pagination and debounced search
- Add unit and UI tests
- Add loading, empty, and retry states
- Publish screenshots and a demo video
