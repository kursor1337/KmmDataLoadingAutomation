# KmmDataLoadingAutomation
Simple KMM library for loading data

This library was created as KMM duplicate to [Replica](https://github.com/aartikov/Replica/) for university project
Replica now has official KMM support, so check it out!

## Getting Started
To use this library, just add a dependency into your gradle build file

```kotlin
implementation("io.github.kursor1337:kmm-data-loading-automation:0.1")
```

## Usage
First create an instance of LoaderClient

```kotlin
val loaderClient = LoaderClient(*coroutineScope*))
```

In your repository create a loader
```kotlin
class PokemonRepositoryImpl(
  loaderClient: LoaderClient
) : PokemonRepository {
  override val pokemonLoader = loaderClient.createLoader(
        refreshTime = 30.seconds,
        clearTime = 60.seconds,
    ) {
        // fetcher, used to load data, you can use any networking library
        ktorHttpClient.get("pokemons")
    }
}
```

In your ViewModel or Decompose Component use
```kotlin
  // observerCoroutineScope = viewModelScope or component.lifecycle.coroutineScope(), isObserverActive = stateflow that shows that this observer is still active
  // ignore exceptions = flag that you set, if false, exceptions will not be handled(may be usefull in debugging)
  val pokemons = pokemonLoader.observe(observerCoroutineScope: CoroutineScope, isObserverActive: StateFlow<Boolean>, ignoreExceptions: Boolean)
```

loader.observe() returns Observable object which has flow in it, which you can than convert into something you want (for example into Compose State)
