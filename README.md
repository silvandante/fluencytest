#Fluency Test (Offline first with Realm and audio player/visualizer for Android using Kotlin)

Demonstration: https://www.youtube.com/watch?v=q2tnKfnsy2A

Tested in: Galaxy Nexus API 30, Samsung A10 and Moto G 7 Plus


**APP MADE FOR ANDROID PLATFORM WITH:**

**Language: Kotlin
Local Database: Realm
Network: Retrofit
Architecture: MVVM**

[![Image 1. Offline first MVVM Architecture Diagram](https://raw.githubusercontent.com/silvandante/fluencytest/master/readmeimage/mvvm_offiline_architecture.png "Image 1. Offline first MVVM Architecture Diagram")](https://raw.githubusercontent.com/silvandante/fluencytest/master/readmeimage/mvvm_offiline_architecture.png "Image 1. Offline first MVVM Architecture Diagram")


**Offline first strategy:**
The app asks for user login and permissions, after both authentication and permissions are given, the app checks if there is something in Realm Database, 3 possibilities where predicted:

**OBS: Activities are always consuming the local database.**

1. There is nothing in the database and app has internet connection
Solution: the application consumes using Retrofit the given REST API (https://dev.hackit.app/webapi), first it asks for the user's deck route, then asks for deck's cards,
while the application observes the LiveData and when results return both deck and deck's cards are storage to the Realm local database.

2. There is things in the database
Solution: the application consumes the local database

3. There is nothing in the database and app has NO internet connection
Solution: the application warns the user that the app must go online at least once

The local database Realm is consumed by observing LiveData entity results, if there is any changes in the database, the activity RecycleView and adapters are updated.

But how does the app updates the database?
The app has network state change service, and if there is NO internet connection, it uses the local database, but if there IS internet connection, the app syncs the database
with modified data consumed from REST API.

(OBS: I USED TO DO THIS STRATEGY WITH ROOM, THIS WAS MY FIRST TRY WITH REALM BECAUSE THE TEST ASKED FOR IT)

**Offline audio strategy:**
Audio player library:  android.media.MediaPlayer
Audio visualizer library: rm.com.audiowave.AudioWaveView

The archives .mp3 are first downloaded to the environment DOWNLOAD folder as .mp3 files, and then played as Byte[].
Even if the app is offline, it plays the storage audios from each card.
