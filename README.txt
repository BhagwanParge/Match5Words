Match5Words Android Project (skeleton)
-------------------------------------
This ZIP contains a ready-to-import Android Studio project skeleton.
- Default dictionary loaded from assets/auto_500.json (500 auto-generated entries).
- Additional dictionaries in assets: medium_250.json (250 entries, higher-confidence) and placeholders_500.json (english-only).
- Features included: Next 5 words, Copy, Share, Favorites (stored in SharedPreferences), Save last-set.
- How to open: Import the folder 'Match5WordsProject' into Android Studio and sync Gradle.
- To switch dictionary, in MainActivity.loadDictionary() call use filename "medium_250.json" or "placeholders_500.json"

NOTE: This is a skeleton with sample code and assets. You may want to update package names, icons, and Gradle plugin versions according to your Android Studio installation.
