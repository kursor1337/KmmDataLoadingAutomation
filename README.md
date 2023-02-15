# KmmDataLoadingAutomation
Simple KMM library for loading data

This library was created as KMM duplicate to [Replica](https://github.com/aartikov/Replica/) for university project (Replica didn't have kmm support at the time)

Replica now has official KMM support, so check it out!

## Getting Started
To use this library, just add a dependency into your gradle build file

```kotlin
implementation("io.github.kursor1337:kmm-data-loading-automation:0.1")
```

## Usage
First create an instance of LoaderClient

```kotlin
val loaderClient = LoaderClient(CoroutineScope())
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
  // observerCoroutineScope = viewModelScope or component.lifecycle.coroutineScope(),
  // isObserverActive = stateflow that shows that this observer is still active
  // ignoreExceptions = if false, exceptions will be thrown
  val pokemons = pokemonLoader.observe(
    observerCoroutineScope: CoroutineScope, 
    isObserverActive: StateFlow<Boolean>, 
    ignoreExceptions: Boolean
  )
```

loader.observe() returns Observable object which has flow in it, which you can than convert into something you want (for example into Compose State)
