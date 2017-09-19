package com.github.colecionista.ui.login

import android.content.Intent
import com.github.colecionista.ui.base.BasePresenter
import com.github.colecionista.ui.base.BaseView
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by renan on 07/09/17.
 */
object LoginContract {

    interface View : BaseView {
        fun startGoogleLoginActivity(intent: Intent)
        fun startMainActivity()
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter : BasePresenter<View> {
        fun doLoginWithGoogle()
        fun checksFirebaseAuthentication()
        fun handleSignInResult(result: GoogleSignInResult)
    }
}