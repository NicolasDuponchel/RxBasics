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

    enum class LoadingStatus { LOADING, NOT_LOADING }

    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _joke = MutableLiveData<String>()
    val joke: LiveData<String> = _joke

    fun onJokeRequest() = jokeApiService.randomJoke()
            .map { it.joke }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { _loadingStatus.postValue(LOADING) }
            .doFinally { _loadingStatus.postValue(NOT_LOADING) }
            .subscribeBy(
                    onSuccess = { _joke.postValue(it.jokeText) },
                    onError = { _joke.postValue(it.message) }
            )
}