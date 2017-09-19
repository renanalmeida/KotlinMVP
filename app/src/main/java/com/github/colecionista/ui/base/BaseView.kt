package com.github.colecionista.ui.base

import android.content.Context

/**
 * Created by renan on 07/09/17.
 */
interface BaseView {

    fun getContext(): Context

    fun showError(error: String?)

    fun showMessage(message: String?)
}