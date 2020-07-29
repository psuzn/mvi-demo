package me.sujanpoudel.mvidemo.base.mvi

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import me.sujanpoudel.mvidemo.base.BaseBottomSheet
import me.sujanpoudel.mvidemo.base.BaseFragment
import me.sujanpoudel.mvidemo.helpers.into


abstract class MviSheet<A : Action, S : State> : BaseBottomSheet(), MviView<A, S> {

    abstract val viewModel: MviViewModel<A, S, *>
    private val disposeBag = CompositeDisposable()

    override fun onStart() {
        actions().subscribe(viewModel::handleAction).into(disposeBag);
        viewModel.state().subscribe(this::render).into(disposeBag)
        super.onStart()
    }

    override fun onStop() {
        disposeBag.clear()
        super.onStop()
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

    override fun actions(): Observable<A> = Observable.empty()


    override fun render(state: S) {}
}