package duponchel.nicolas.rxbasics

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        viewModel { MainViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
    }

    fun randomJoke(view: View) = viewModel.onJokeRequest()

    fun randomJokes(view: View) = viewModel.onJokesRequest()

    private fun observeViewModel() {
        viewModel.jokeLoadingStatus.observe(this, Observer {
            when (it) {
                LOADING -> {
                    loader_single_joke.show()
                    btn_single_joke.disable()
                }
                NOT_LOADING -> {
                    loader_single_joke.hide()
                    btn_single_joke.enable()
                }
            }
        })

        viewModel.joke.observe(this, Observer {
            tv_single_joke.text = it ?: EMPTY_STRING
        })



        viewModel.jokesLoadingStatus.observe(this, Observer {
            when (it) {
                LOADING -> {
                    loader_jokes.show()
                    btn_jokes.disable()
                }
                NOT_LOADING -> {
                    loader_jokes.hide()
                    btn_jokes.enable()
                }
            }
        })

        viewModel.jokes.observe(this, Observer {
            tv_jokes.text = it?.toJokeString() ?: EMPTY_STRING
        })
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}
