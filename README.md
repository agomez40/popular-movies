# Popular Movies

This is a exercise repository for Udacity's **Associate Android Developer Fast Track** course, you can
learn more about the course [here](https://www.udacity.com/course/associate-android-developer-fast-track--nd818)

# Getting Started

1. Clone the repo:

```sh
git clone git@github.com:agomez40/popular-movies.git
```

1. Create an [The Movie DB](https://www.themoviedb.org/) account.
1. Get the API keys from The Movie DB, you will need to create a new app to request an API key,
since this is an example get your API keys by creating a your app as a **Developer.**
1. Copy the values for:
    * API Key (v3 auth)
    * API Read Access Token (v4 auth)

   **NOTE: PLEASE REMOVE YOUR API KEY WHEN SHARING CODE PUBLICALLY, STORE YOUR API KEYS ON A SAFE LOCATION.**
1. Open the project using [Android Studio](https://developer.android.com/studio/index.html).
1. Locate the string resources at `app/main/res/values/strings.xml`, and replace the following entries:

    ```none
    <string name="movie_db_api_v3_key" translatable="false">YOUR V3 API KEY</string>
    <string name="movie_db_api_v4_key" translatable="false">YOUR V4 API KEY</string>
    ```

That's it, just run the project on an Android Emulator or a device.