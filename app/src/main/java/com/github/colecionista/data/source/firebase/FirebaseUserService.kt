package com.github.colecionista.data.source.firebase

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.github.colecionista.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


/**
 * Created by renan on 16/09/17.
 */
class FirebaseUserService {

    var mAuth = FirebaseAuth.getInstance()

    fun getAuthWithGoogle(acct: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        return mAuth.signInWithCredential(credential)
    }

    fun getGoogleLoginIntent(context: Context): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        var mGoogleApiClient = GoogleApiClient.Builder(context)
                .enableAutoManage(context as FragmentActivity, {})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
    }

    fun logout() {
        mAuth.signOut()
    }

}