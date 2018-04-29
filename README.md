# ADH "Places" App Challenge

To install and run, just clone the repo, open in Android Studio, and run.  If you're using an emulator, you may have issues with the Maps widget, which requires an up-to-date version of Play Services - you can create (or update) an emulator that will work, or just use a real device which should have updated services automatically (unless it hasn't had a connection for some time - or is from China).

There's not a ton to note - I tried to stick to the spec where possible.  One small exception was that in the detail view, I added a few more fields - it seemed a bit odd to have a collapsing toolbar to just see 2 lines of information.

Other random notes:

1.  Minimum API is 5.0 (L).  This is almost 90% of worldwide users now (https://developer.android.com/about/dashboards/), but in general I'd probably advise going back as far as 4.0.  There's a lot of freebies when your minimum is 5.0, and 4.0 sometimes feels like it requires almost parallel development, so if we can get away with later APIs, that's great.
1.  Typeahead is on a 500ms debounce.  Query minimum is 4 characters, you'll see a Snack if you haven't achieved the minimum.
1.  Error handling is pretty basic - basically all errors are caught and a message is presented to the user as a Snack.
1.  There is a loader while in the search (it's a spinner in the right edge of the EditText) but on a modern connection the response is received so fast that you won't see it.
1.  Favorites are stored in an in-memory Set<String>, then backed up in SharedPrefs (which has an API for string sets).  In a production app I might do this in a database, since prefs are slow, but we're really relying on the in-memory version first and doing the saves asynchronously so it's not that impactful (except on startup, when we read it out).
1.  I kept API secrets in constants.  I know there are a lot of conversations around this, and I'd probably take more care in a production app (at least obfuscation), but in my opinion, as soon as the secret touches the client, it's accessible to an attacker, so I think a lot of the effort around that has poor ROI.  I believe the server should manage secrets and provide, at a minimum, proxies.
1.  I used the static maps API as instructed for the detail page, but inverted it so the white title text shows up more clearly.
1.  Anywhere you see a heart, that's a favorite toggle button.  If it's filled, it's a favorite; if it's empty, it's not.  I recognize these are small and can be difficult to tap; I should probably have used a hit delegate (or at least wrapped it in larger container).
1.  We're only loading 20 results.  We should probably either offer a pager ("next"), or a "load more" row at the end, or (in my opinion, the best option) an "endless" RecyclerView that detects when we're nearing the end and fills with dummy rows (often with spinners or loading labels) until we have the information from the server (I have an example of this on my public github: i have an exaple on my github: https://github.com/moagrius/EndlessRecyclerView).
1.  I wrote some unit tests but they're by no means comprehensive.  I used standard JUnit and Espresso, and was deliberate about what I tested to show a variety of strategies (e.g., mocking network responses, testing AOSP-specific things Activties and Intents, manipulating views, traditional junit input versus output, etc.  I did not write any instrumentation tests - Espresso takes forever to set up and is both unwiedly and unreliably, in my opinion - I'm not totally adverse to using it, I just think (like any tool) there are times when it's value return against time make it worth it, and times when it does not.
1.  One pattern you'll see that I'm a big advocate for but is not broadly used in the Android community is the Application object as a "server".  Any time you have a Context, you have access to an Application instance.  I take advantage of this, and put common, shared functionality (image loaders, database connections, services, etc) on the Application instance not as singletons but as regular members.  This gives us the advantage of near-universal access (as would a singleton) but without the cost of loss of control over lifecycle (which is the main drawback of singletons).
1.  I suspect this is just a coincidence, but I have an source Android tiling widget that's quite similar to a lot of what we're doing here: https://github.com/moagrius/TileView.  Please don't judge that code - the library is extremely old and I've been working on a V3 as time allows (ha) which is much more modern. 
1.  I included Stetho for debugging.  That should be taken out for production, or at least limited to debug builds.

I think that covers everything.  Let me know if you have any questions.