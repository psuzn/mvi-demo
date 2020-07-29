package me.sujanpoudel.mvidemo.ui.login

import me.sujanpoudel.mvidemo.base.mvi.Action
import me.sujanpoudel.mvidemo.base.mvi.InternalAction
import me.sujanpoudel.mvidemo.base.mvi.SingleEvent
import me.sujanpoudel.mvidemo.base.mvi.State


data class LoginState(
    val loading: Boolean = false,
    val userNameError: String? = null,
    val passwordError: String? = null,
    val success: SingleEvent<Boolean> = SingleEvent(),
    val error: SingleEvent<String> = SingleEvent()
) : State() {

}

sealed class LoginUIAction : Action() {
    class Login(val emailOrUserName: String, val password: String) : LoginUIAction()
    object EmailChanged : LoginUIAction()
    object PasswordChanged : LoginUIAction()
}

sealed class LoginInternalAction : InternalAction() {
    object LoggingIn : LoginInternalAction()
    object LoginSuccess : LoginInternalAction()
    class ValidationError(val userNameError: String? = null, val passwordError: String? = null) : LoginInternalAction()
    class Error(val message: String) : LoginInternalAction()

    object EmailErrorClear : LoginInternalAction()
    object PasswordErrorClear : LoginInternalAction()
}
