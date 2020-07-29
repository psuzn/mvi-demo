package me.sujanpoudel.mvidemo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import me.sujanpoudel.mvidemo.base.mvi.MviFragment
import me.sujanpoudel.mvidemo.databinding.FragmentLoginBinding
import me.sujanpoudel.mvidemo.ui.login.LoginFragmentDirections.Companion.actionSignInFragmentToNotes
import me.sujanpoudel.mvidemo.ui.login.LoginUIAction.EmailChanged
import me.sujanpoudel.mvidemo.ui.login.LoginUIAction.PasswordChanged


@AndroidEntryPoint
class LoginFragment : MviFragment<LoginUIAction, LoginState>() {
    override val viewModel by viewModels<LoginViewModel>()
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            incAppbar.title.text = "Login"
        }
    }

    override fun actions(): Observable<LoginUIAction> {
        return Observable.mergeArray(
            binding!!.btnLogin.clicks().map { LoginUIAction.Login(binding!!.etEmail.text.toString(), binding!!.etPassword.text.toString()) },
            binding!!.etEmail.textChanges().map { EmailChanged },
            binding!!.etPassword.textChanges().map { PasswordChanged }
        )
    }

    override fun render(state: LoginState) {
        binding?.apply {
            tilEmail.error = state.userNameError
            tilPassword.error = state.passwordError
        }
        if (state.loading)
            showLoadingDialog()
        else
            hideLoadingDialog()
        state.success.value?.let {
            findNavController().navigate(actionSignInFragmentToNotes())
        }
        state.error.value?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}