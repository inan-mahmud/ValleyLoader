# Valley Loader

[![AppCenter](https://build.appcenter.ms/v0.1/apps/bac4f383-e178-40f0-a695-c84d335f34a8/branches/master/badge)](https://build.appcenter.ms/v0.1/apps/bac4f383-e178-40f0-a695-c84d335f34a8/branches/master/badge)

### Specs
[![API](https://img.shields.io/badge/API-15%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=16)

A lightweight memory efficient image loader/file downloader based on [LruCache](https://developer.android.com/reference/android/util/LruCache)  

# Features

  - Tiny in size
  - Pure LruCache
  - Supports all file types

### Download

Use as Gradle dependency:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
  implementation 'com.github.prokash-sarkar:ValleyLoader:-SNAPSHOT'
}
```

### Usage

Load a Bitmap from remote url into ImageView

```kotlin
ValleyLoader.getInstance(context)
        .download(url, ivPreview)
```
        
ValleyLoader isn't limited to any specific file type. Therefore you can request for any file type and get the cache data in ByteArray

```kotlin
ValleyLoader.getInstance(context)
            .download(url, object : ValleyLoaderListener {
                override fun onByteSuccess(loader: ValleyLoader, byteArray: ByteArray, url: String) {
                    val bitmap = BitmapFactory.decodeStream(byteArray.inputStream())
                    binding.ivPreview.setImageBitmap(bitmap)
                }

                override fun onByteFailure(loader: ValleyLoader, url: String) {
                    Timber.e("Failed to load $url")
                }
            })
```

Set a cache size

```kotlin
ByteCache.getInstance(context).setCacheSize(0)
```

Clear all memory cache
```kotlin
ByteCache.getInstance(context).clear()
```

Cancel the current request

```kotlin
ValleyLoader.getInstance(context).cancel(this)
```

# Demo App 
 
 The demo app uses the following libraries 
 
  - [Android Jetpack Components](https://developer.android.com/jetpack) (ViewModel, LiveData, Paging, Navigation)
  - [Dagger 2](https://github.com/google/dagger)
  - [RxJava 2](https://github.com/ReactiveX/RxJava)
  - [Retrofit](https://square.github.io/retrofit/)
  - Static code analysis tools like Lint, Checkstyle, FindBugs and PMD
  - jUnit5 + Espresso testing
  - In an addition it also uses CircleCI to make continuous integration.
 
 ![App Workflow](https://raw.githubusercontent.com/prokash-sarkar/ValleyLoader/master/screenshots/Screenshot_1.png)
 
License
----

**Free Software, Ta-Da!**
