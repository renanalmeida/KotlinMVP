package com.github.colecionista.ui.additem

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.github.colecionista.R
import com.github.colecionista.data.model.Item
import com.github.colecionista.ui.base.BaseActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by renan on 15/09/17.
 */
class AddItemActivity : BaseActivity<AddItemContract.View, AddItemContract.Presenter>(), AddItemContract.View {

    private val SELECT_PHOTO: Int = 1001

    override var mPresenter: AddItemContract.Presenter = AddItemPresenter()

    private var mImageView: ImageView? = null

    private var mSaveItemButton: FloatingActionButton? = null

    private var mProgressBar: ContentLoadingProgressBar? = null

    private var selectedImage = false

    private var mTextView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        mProgressBar = findViewById(R.id.pb_upload_image_add_item) as ContentLoadingProgressBar?

        mImageView = findViewById(R.id.iv_photo_dialog_add_item) as ImageView?
        mImageView?.setOnClickListener {
            chooseImage()
        }

        mTextView = findViewById(R.id.et_name_dialog_add_item) as EditText
        mSaveItemButton = findViewById(R.id.bt_save_dialog_add_item) as FloatingActionButton
        mSaveItemButton?.setOnClickListener {
            if(saveItemValidation()){
                val item = Item()
                item.name = mTextView?.text.toString()
                //get image
                // Get the data from an ImageView as bytes
                mImageView?.setDrawingCacheEnabled(true)
                mImageView?.buildDrawingCache()
                val bitmap = mImageView?.getDrawingCache()
                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                mPresenter.saveItem(item, data)
            }else{
                showMessage(getString(R.string.item_validation_error))
            }

        }
    }

    fun saveItemValidation():Boolean{
        return selectedImage && mTextView?.text.toString().isNotBlank()
    }

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)

        if (imageReturnedIntent != null) {
            when (requestCode) {
                SELECT_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                    setImageViewPhoto(imageReturnedIntent)
                }
            }
        } else {
            showError(getString(R.string.no_image_selected))
        }
    }

    private fun setImageViewPhoto(imageReturnedIntent: Intent) {
        selectedImage = true
        val datifoto: Bitmap?
        mImageView?.setImageBitmap(null)
        var picUri: Uri? = imageReturnedIntent.data
        //<- get Uri here from data intent
        if (picUri != null) {
            try {
                datifoto = MediaStore.Images.Media.getBitmap(this.contentResolver, picUri)
                mImageView?.setImageBitmap(datifoto)

            } catch (e: FileNotFoundException) {
                showError(getString(R.string.image_not_found))
            } catch (e: IOException) {
                showError(getString(R.string.image_not_found))
            } catch (e: OutOfMemoryError) {
                showError(getString(R.string.very_large_image))
            }
        }
    }

    override fun dismiss() {
        finish()
    }

    override fun showLoading() {
        mImageView?.isEnabled = false
        mSaveItemButton?.isEnabled = false
        mProgressBar?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mImageView?.isEnabled = true
        mSaveItemButton?.isEnabled = true
        mProgressBar?.visibility = View.GONE
    }
}