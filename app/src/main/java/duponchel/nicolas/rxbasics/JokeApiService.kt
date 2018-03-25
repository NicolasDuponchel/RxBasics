package duponchel.nicolas.rxbasics

import io.reactivex.Single
import retrofit2.http.GET

interface JokeApiService {
    @GET("jokes/random")
    fun randomJoke(): Single<JokeApiResponse>
}