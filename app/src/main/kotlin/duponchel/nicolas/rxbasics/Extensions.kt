package duponchel.nicolas.rxbasics

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast

/** inline function to avoid making VMFactory **/
inline fun <reified T : ViewModel> AppCompatActivity.viewModel(crossinline factory: () -> T): T {
    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
    return ViewModelProviders.of(this, vmFactory)
            .get(T::class.java)
}


fun Collection<String>.toJokeString(): String =
        StringBuilder().apply { this@toJokeString.forEach { append("$it\n\n") } }.toString()


fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun View.hide() {
    visibility = GONE
}

fun View.show() {
    visibility = VISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}