package me.sujanpoudel.mvidemo.base.mvi

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import me.sujanpoudel.mvidemo.helpers.into


abstract class MviViewModel<A : Action, S : State, IA : InternalAction>(private val initialState: S) : ViewModel(),
    MviStore<A, S, IA> {

    val disposeBag = CompositeDisposable()
    private val stateRelay: BehaviorSubject<S> by lazy { BehaviorSubject.createDefault(this.initialState) }
    private val actionRelay: PublishSubject<A> by lazy { PublishSubject.create<A>() }

    init {
        actionRelay
            .publish {
                Observable.mergeArray(it.filter { it is InitAction }.firstOrError().toObservable(),
                    it.filter { it !is InitAction }) as Observable<A>
            }
            .flatMap { transform(it) }
            .withLatestFrom(stateRelay) { a: IA, s: S -> reduce(s, a) }
            .doOnNext(stateRelay::onNext)
            .subscribe().into(disposeBag)
    }

    override fun state(): Observable<S> = stateRelay
        .observeOn(AndroidSchedulers.mainThread())

    override fun handleAction(actions: A) {
        this.actionRelay.onNext(actions)
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }

    override fun reduce(state: S, action: IA) = state

    override fun transform(action: A): Observable<IA> = Observable.empty()
}