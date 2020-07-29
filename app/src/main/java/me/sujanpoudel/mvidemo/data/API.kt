package me.sujanpoudel.mvidemo.data

import io.reactivex.Observable
import me.sujanpoudel.mvidemo.data.models.AuthResponse
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class API @Inject constructor() {
    fun login(email: String, password: String): Observable<AuthResponse> { // mock login
        return Observable.just(AuthResponse(UUID.randomUUID().toString()))
            .delay(2, TimeUnit.SECONDS)
            .doOnNext {
                if (Random().nextBoolean())
                    throw Exception()
            }
    }
}