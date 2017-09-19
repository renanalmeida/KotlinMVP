package com.github.colecionista.ui.additem

import com.github.colecionista.ui.base.BasePresenterImpl
import com.github.colecionista.data.model.Item
import com.github.colecionista.data.source.firebase.FirebaseService
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference


/**
 * Created by renan on 09/09/17.
 */
class AddItemPresenter : BasePresenterImpl<AddItemContract.View>(), AddItemContract.Presenter {

    private var firebaseService = FirebaseService()

    override fun saveItem(item: Item, data: ByteArray) {
        var newItemRef = firebaseService.itemsRef.push()
        var itemId = newItemRef.key
        item.imageUrl = itemId


        newItemRef.setValue(item, { databaseError: DatabaseError?, databaseReference: DatabaseReference ->
            if (databaseError != null) {
                mView?.showError("Error ao salvar dados.")
            } else {
                uploadImage(itemId, data)
            }
        })
    }

    private fun uploadImage(name: String, data: ByteArray) {
        mView?.showLoading()

        val imageRef = firebaseService.imagesRef.child(name)

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener({
            // Handle unsuccessful uploads
            mView?.showError("Falha no envio.")
        }).addOnSuccessListener({
            mView?.hideLoading()
            mView?.dismiss()
            mView?.showMessage("Item salvo com succeso.")

        })
    }

}