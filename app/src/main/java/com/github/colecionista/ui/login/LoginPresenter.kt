package com.github.colecionista.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.github.colecionista.R
import com.github.colecionista.data.source.firebase.FirebaseUserService
import com.github.colecionista.data.source.local.SharedPreferenceService
import com.github.colecionista.ui.base.BasePresenterImpl
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInResult

/**
 * Created by renan on 07/09/17.
 */
class LoginPresenter : BasePresenterImpl<LoginContract.View>(),
        LoginContract.Presenter {

    var firebaseUserService = FirebaseUserService()
    var sharePreferenceService : SharedPreferenceService? = null
    var loginIntent : Intent? = null

    override fun attachView(view: LoginContract.View) {
        super.attachView(view)
        sharePreferenceService = SharedPreferenceService(mView?.getContext()!!)
    }

    override fun doLoginWithGoogle() {
        mView?.showProgressBar()
        if(loginIntent == null) loginIntent =  firebaseUserService.getGoogleLoginIntent(mView?.getContext()!!)
        mView?.startGoogleLoginActivity(loginIntent!!)
    }

    override fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val acct = result.signInAccount
            firebaseAuthWithGoogle(acct as GoogleSignInAccount)
        } else {
            mView?.hideProgressBar()
            mView?.showError(mView?.getContext()?.getString(R.string.google_login_sucess_msg) )
        }
    }

    override fun checksFirebaseAuthentication() {
        if (isSignedInFirebase())
            mView!!.startMainActivity()
    }

    fun isSignedInFirebase(): Boolean = firebaseUserService.mAuth?.currentUser != null

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        firebaseUserService.getAuthWithGoogle(acct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sharePreferenceService?.setPhotoUri(acct.photoUrl.toString())
                sharePreferenceService?.setUserName(acct.displayName!!)
                sharePreferenceService?.setUserEmail(acct.email!!)
                mView?.showMessage(mView?.getContext()?.getString(R.string.login_sucess_msg))
                mView?.startMainActivity()
            } else {
                mView?.hideProgressBar()
                mView?.showError(mView?.getContext()?.getString(R.string.login_sucess_msg))
            }
        }

    }

}