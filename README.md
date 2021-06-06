# Miketoide

The goal of this exercise was to create an entry screen for an app marketplace.

## Project structure
The project is separated into 4 different layers:
- Core
>- Application entry point
- Data
>- Event models
>- Data models
>- Type extensions
- Domain
>- Utilities
>- Middle layer components invoked by the viewModels or controllers
- UI (with the MVVM pattern)
>- ViewModels for the business logic
>- Controllers ('base' activity and fragments for each result) and adapters to handle user inputs and present data
>- Custom components

**Note:** The project also has some unit tests.

## Communication between Fragments and ViewModels
- Fragment->ViewModel 
>- Direct invocations
- ViewModel -> Fragment
>- MutableLiveData observables for state updates (updating UI)

## Communication between Fragments and Activity
>- EventBus for using pub/sub between the fragments (publishers) and the activity (subscriber)

## Dependency Injection (DI)
I used Hilt for DI, so the components would access a behavior abstraction and not a concretion, for better flexibility of implementation (respecting one of the SOLID principles in the process).

**Note:** All pieces in the 'Domain' (except for 'Scope') are injected into the DI container.

## API access
I used [Retrofit](https://square.github.io/retrofit/) for accessing the Apps API.

## Image loading
I used [Picasso](https://square.github.io/picasso/) for loading images from the Apps API and into the ImageViews.

## Animations
I used [Lottie](https://github.com/airbnb/lottie-android) for rendering animations.

## Data loading
Once the app's data is retrieved from the Apps API, the results are shown inside a vertical RecyclerView (in the 'All our apps' category).
As for the 'Hottest Apps', these are the five best rated apps of those (API) results and are shown inside a horizontal RecyclerView.

## Miscellaneous tech details
The app is written entirely using [Kotlin](https://kotlinlang.org/) and I used Kotlin coroutines for thread management.
