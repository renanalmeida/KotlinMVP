package com.github.colecionista.data.source.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Created by renan on 15/09/17.
 */
class FirebaseService {

    var itemsRef: DatabaseReference
    var imagesRef: StorageReference

    init {
        val database = FirebaseDatabase.getInstance()
        /*TODO create singleton*/ try{ database.setPersistenceEnabled(true)}catch (e:Exception){}
        itemsRef = database.getReference("items")

        var storage = FirebaseStorage.getInstance()
        imagesRef = storage.getReference("images")
    }

    fun getImageRef(imageName: String): StorageReference{
        return imagesRef.child(imageName)
    }

    fun getItemRef(id: String): DatabaseReference{
        return itemsRef.child(id)
    }
}