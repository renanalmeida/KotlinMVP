package com.github.colecionista.ui.base

/**
 * Created by renan on 07/09/17.
 */
open class BasePresenterImpl<V : BaseView> : BasePresenter<V>{

    protected var mView: V? = null

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}