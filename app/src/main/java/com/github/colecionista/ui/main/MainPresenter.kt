package com.github.colecionista.ui.main

import android.util.Log
import com.github.colecionista.ui.base.BasePresenterImpl
import com.github.colecionista.data.model.Item
import com.github.colecionista.data.source.firebase.FirebaseService
import com.github.colecionista.data.source.firebase.FirebaseUserService
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference


/**
 * Created by renan on 10/09/17.
 */
class MainPresenter : BasePresenterImpl<MainContract.View>(), MainContract.Presenter {

    private var firebaseService = FirebaseService()
    private var firebaseUserService = FirebaseUserService()

    override fun loadItems() {
        mView?.showLoading()
        val itemsListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mView?.hideLoading()
                val items = ArrayList<Item>()
                for (child in dataSnapshot.children) {
                    Log.d("MainPresenter", child.toString())
                    val item = child.getValue(Item::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }
                // Get Post object and use the values to update the UI
                mView?.showItems(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                mView?.hideLoading()
                mView?.showItems(ArrayList<Item>())
            }
        }

        firebaseService.itemsRef.addValueEventListener(itemsListener)

    }

    override fun getImagesRef(item: Item): StorageReference {
        return firebaseService.getImageRef(item.imageUrl!!)
    }

    override fun removeItem(itemId: String) {

        firebaseService.getItemRef(itemId).removeValue()
                .addOnSuccessListener {
                    mView?.showMessage("Item removido com succeso.")
                    deleteImage(itemId)
                }
                .addOnFailureListener { mView?.showMessage("Falha ao remover item.") }
    }

    private fun deleteImage(itemId: String) {
        Log.d("MainPreseter", "deleteImage: ma"+itemId)
        firebaseService.getImageRef(itemId).delete()
    }

    override fun logout() {
        Log.d("MainPreseter", "logout")

        firebaseUserService.logout()
        mView?.startLoginActivity()
    }

}