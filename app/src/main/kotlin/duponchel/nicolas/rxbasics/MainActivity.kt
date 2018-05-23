package duponchel.nicolas.rxbasics

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.ERROR
import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.SUCCESS
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING
import kotlinx.android.synthetic.main.layout_completable_install.*
import kotlinx.android.synthetic.main.layout_observable_jokes.*
import kotlinx.android.synthetic.main.layout_single_joke.*

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

    fun fakeInstall(view: View) = viewModel.onFakeInstall()

    private fun observeViewModel() {

        /** SINGLE EXAMPLE **/
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



        /** OBSERVABLE EXAMPLE **/
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



        /** COMPLETABLE EXAMPLE **/
        viewModel.installLoadingStatus.observe(this, Observer {
            when (it) {
                LOADING -> {
                    loader_install.show()
                    btn_install.disable()
                }
                NOT_LOADING -> {
                    loader_install.hide()
                    btn_install.enable()
                }
            }
        })

        viewModel.installation.observe(this, Observer {
            when(it) {
                SUCCESS -> toast("Fake installation succeed")
                ERROR -> toast("Fake installation failed! Hum ... let me guess this never happen")
            }
        })
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}
