package me.sujanpoudel.mvidemo.ui.login

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import me.sujanpoudel.mvidemo.base.mvi.MviViewModel
import me.sujanpoudel.mvidemo.base.mvi.SingleEvent
import me.sujanpoudel.mvidemo.data.API
import me.sujanpoudel.mvidemo.data.Preferences
import java.util.concurrent.TimeUnit

class LoginViewModel @ViewModelInject constructor(
    private val api: API,
    private val preferences: Preferences
) : MviViewModel<LoginUIAction, LoginState, LoginInternalAction>(LoginState()) {

    override fun transform(action: LoginUIAction): Observable<LoginInternalAction> {
        return when (action) {
            is LoginUIAction.Login -> login(action)
            LoginUIAction.EmailChanged -> Observable.just(LoginInternalAction.EmailErrorClear)
            LoginUIAction.PasswordChanged -> Observable.just(LoginInternalAction.PasswordErrorClear)
        }
    }

    private fun login(action: LoginUIAction.Login): Observable<LoginInternalAction> {
        val passwordError = if (action.password.trim().isNotEmpty()) null else "Please enter password"
        val userNameError = action.emailOrUserName.let {
            when {
                Patterns.EMAIL_ADDRESS.matcher(it).matches() -> null
                it.length >= 4 -> null
                else -> "Enter valid info"
            }
        }

        if (passwordError != null || userNameError != null)
            return Observable.just(LoginInternalAction.ValidationError(userNameError, passwordError))

        return api.login(action.emailOrUserName, action.password)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                preferences.token = it.token
            }
            .map { LoginInternalAction.LoginSuccess as LoginInternalAction }
            .startWith(LoginInternalAction.LoggingIn)
            .onErrorResumeNext(Observable.just(LoginInternalAction.Error("Error while logging in")).delay(500, TimeUnit.MILLISECONDS))
    }

    override fun reduce(state: LoginState, action: LoginInternalAction): LoginState {
        return when (action) {
            LoginInternalAction.LoggingIn -> state.copy(loading = true, passwordError = null, userNameError = null)
            LoginInternalAction.LoginSuccess -> state.copy(success = SingleEvent(true), loading = false)
            is LoginInternalAction.Error -> state.copy(error = SingleEvent(action.message), loading = false)
            is LoginInternalAction.ValidationError -> state.copy(userNameError = action.userNameError, passwordError = action.passwordError)
            LoginInternalAction.EmailErrorClear -> state.copy(userNameError = null)
            LoginInternalAction.PasswordErrorClear -> state.copy(passwordError = null)
        }
    }
}