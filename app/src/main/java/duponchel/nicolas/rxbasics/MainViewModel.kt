package duponchel.nicolas.rxbasics

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val jokeApiService by lazy {
        JokeApiServiceFactory.createService()
    }

    private val singleRandomJoke by lazy {
        jokeApiService.randomJoke()
                .map { it.joke }
    }

    enum class LoadingStatus { LOADING, NOT_LOADING }

    private val _joke = MutableLiveData<String>()
    val joke: LiveData<String> = _joke

    private val _jokeLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokeLoadingStatus: LiveData<LoadingStatus> = _jokeLoadingStatus

    private val _jokes = MutableLiveData<Collection<String>>()
    val jokes: LiveData<Collection<String>> = _jokes

    private val _jokesLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokesLoadingStatus: LiveData<LoadingStatus> = _jokesLoadingStatus

    init {
        _jokes.postValue(emptyList())
    }

    fun onJokeRequest() = singleRandomJoke
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { _jokeLoadingStatus.postValue(LOADING) }
            .doFinally { _jokeLoadingStatus.postValue(NOT_LOADING) }
            .subscribeBy(
                    onSuccess = { _joke.postValue(it.jokeText) },
                    onError = { _joke.postValue(it.message) }
            )

    fun onJokesRequest() = singleRandomJoke.toObservable()
            .repeat(5)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe {
                _jokes.postValue(emptyList())
                _jokesLoadingStatus.postValue(LOADING)
            }
            .doFinally { _jokesLoadingStatus.postValue(NOT_LOADING) }
            .subscribeBy(
                    onNext = { _jokes.postValue(_jokes.value?.plus(it.jokeText)) },
                    onError = { _jokes.postValue(emptyList()) },
                    onComplete = { }
            )
}