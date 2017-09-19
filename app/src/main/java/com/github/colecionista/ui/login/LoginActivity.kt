package com.github.colecionista.ui.login

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.github.colecionista.R
import com.github.colecionista.ui.base.BaseActivity
import com.github.colecionista.ui.main.MainActivity
import com.google.android.gms.auth.api.Auth


/**
 * Created by renan on 07/09/17.
 */
open class LoginActivity : BaseActivity<LoginContract.View,
        LoginContract.Presenter>(),
        LoginContract.View {

    private val RC_SIGN_IN: Int = 1001

    override var mPresenter: LoginContract.Presenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPresenter.checksFirebaseAuthentication()
        findViewById(R.id.sign_in_button).setOnClickListener {
            mPresenter.doLoginWithGoogle()
        }
        setStatusBarColor()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorPrimary)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            mPresenter.handleSignInResult(result)
        }
    }

    override fun startGoogleLoginActivity(signInIntent: Intent) {
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun startMainActivity() {
        intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
    }

    override fun showProgressBar() {
        findViewById(R.id.sign_in_button).visibility = View.GONE
        findViewById(R.id.pb_login_activity).visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        findViewById(R.id.sign_in_button).visibility = View.VISIBLE
        findViewById(R.id.pb_login_activity).visibility = View.GONE
    }

}