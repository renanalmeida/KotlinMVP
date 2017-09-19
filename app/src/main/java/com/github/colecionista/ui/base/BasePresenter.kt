package com.github.colecionista.ui.base

/**
 * Created by renan on 07/09/17.
 */
interface BasePresenter<in V : BaseView> {

    fun attachView(view: V)

    fun detachView()
}