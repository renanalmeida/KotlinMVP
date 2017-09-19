package com.github.colecionista.ui.additem

import com.github.colecionista.ui.base.BasePresenter
import com.github.colecionista.ui.base.BaseView
import com.github.colecionista.data.model.Item

/**
 * Created by renan on 09/09/17.
 */
object AddItemContract {

    interface View : BaseView {
        fun dismiss()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : BasePresenter<View> {
        fun saveItem(item: Item, data: ByteArray)
    }

}