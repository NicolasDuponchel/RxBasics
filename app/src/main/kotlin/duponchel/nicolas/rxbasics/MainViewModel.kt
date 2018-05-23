package duponchel.nicolas.rxbasics

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.ERROR
import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.SUCCESS
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    private val jokeApiService by lazy {
        JokeApiServiceFactory.createService()
    }

    private val singleRandomJoke by lazy {
        jokeApiService.randomJoke()
            .map { it.joke }
    }

    enum class LoadingStatus { LOADING, NOT_LOADING }


    /** SINGLE EXAMPLE **/
    private val _joke = MutableLiveData<String>()
    val joke: LiveData<String> = _joke

    private val _jokeLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokeLoadingStatus: LiveData<LoadingStatus> = _jokeLoadingStatus


    /** OBSERVABLE EXAMPLE **/
    private val _jokes = MutableLiveData<Collection<String>>()
    val jokes: LiveData<Collection<String>> = _jokes

    private val _jokesLoadingStatus = MutableLiveData<LoadingStatus>()
    val jokesLoadingStatus: LiveData<LoadingStatus> = _jokesLoadingStatus


    /** COMPLETABLE EXAMPLE **/
    enum class InstallationStatus { SUCCESS, ERROR }

    private val _installation = MutableLiveData<InstallationStatus>()
    val installation: LiveData<InstallationStatus> = _installation

    private val _installLoadingStatus = MutableLiveData<LoadingStatus>()
    val installLoadingStatus: LiveData<LoadingStatus> = _installLoadingStatus


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
            onError = {
                _jokes.postValue(
                    it.message?.let {
                        _jokes.value?.plus(it)
                    } ?: emptyList())
            },
            onComplete = { }
        )

    fun onFakeInstall() = Completable.timer(3, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe { _installLoadingStatus.postValue(LOADING) }
        .doFinally { _installLoadingStatus.postValue(NOT_LOADING) }
        .subscribeBy(
            onComplete = { _installation.postValue(SUCCESS) },
            onError = { _installation.postValue(ERROR) }
        )
}