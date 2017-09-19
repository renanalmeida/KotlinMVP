package com.github.colecionista.ui.main

import com.github.colecionista.ui.base.BasePresenter
import com.github.colecionista.ui.base.BaseView
import com.github.colecionista.data.model.Item
import com.google.firebase.storage.StorageReference

/**
 * Created by renan on 10/09/17.
 */
object MainContract {

    interface View : BaseView {
        fun showItems(items: MutableList<Item>?)
        fun showLoading()
        fun hideLoading()
        fun startLoginActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun loadItems()
        fun getImagesRef(item: Item): StorageReference
        fun removeItem(itemId: String)
        fun logout()
    }
}