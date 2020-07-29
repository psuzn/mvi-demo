package me.sujanpoudel.mvidemo.base.mvi

import io.reactivex.Observable

open class Action
open class InternalAction
open class State

interface InitAction

interface MviView<A, S> {
    fun actions(): Observable<A>
    fun render(state: S)
}

interface Reducer<IA, S> {
    fun reduce(state: S, action: IA): S
}

interface MviStore<A, S, IA> : Reducer<IA, S> {
    fun state(): Observable<S>
    fun transform(action: A): Observable<IA> // middleware
    fun handleAction(actions: A)
}

class SingleEvent<T>(value: T? = null) {

    private var _value: T? = null

    val value: T?
        get() {
            val tmp = _value;
            _value = null
            return tmp
        }

    init {
        _value = value
    }
}
