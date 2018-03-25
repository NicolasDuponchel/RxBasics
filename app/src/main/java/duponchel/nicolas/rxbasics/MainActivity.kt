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

    private fun observeViewModel() {
        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                LOADING -> {
                    loader_joke.show()
                    btn_joke.disable()
                }
                NOT_LOADING -> {
                    loader_joke.hide()
                    btn_joke.enable()
                }
            }
        })

        viewModel.joke.observe(this, Observer {
            tv_joke.text = it ?: EMPTY_STRING
        })
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}
