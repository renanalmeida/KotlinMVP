package com.github.colecionista.ui.main

import android.content.Context
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.github.colecionista.R
import com.github.colecionista.data.model.Item
import com.github.colecionista.utils.FirebaseImageLoader
import java.util.ArrayList
import com.bumptech.glide.load.resource.drawable.GlideDrawable

import com.bumptech.glide.request.target.Target
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidviewhover.BlurLayout
import com.google.firebase.storage.StorageReference

class ItemsAdapter(val mPresenter: MainContract.Presenter, val items: MutableList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup?, p1: Int): ViewHolder {
        mContext = parent?.context
        var inflater = LayoutInflater.from(mContext)
        var view = inflater.inflate(R.layout.colection_item, parent, false)
        var holder = ViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        setupHoverView(position, holder)

        val requestLister:RequestListener<StorageReference, GlideDrawable> = object : RequestListener<StorageReference, GlideDrawable> {
            override fun onException(e: Exception, model: StorageReference, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                holder?.pb?.visibility = View.GONE
                return false
            }

            override fun onResourceReady(resource: GlideDrawable, model: StorageReference, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                holder?.pb?.visibility = View.GONE
                return false
            }
        }

        if (items[position].imageUrl != null) {
            holder?.pb?.visibility = View.VISIBLE

            Glide.with(mContext)
                    .using(FirebaseImageLoader())
                    .load(mPresenter.getImagesRef(items[position])).listener(requestLister)
                    .into(holder?.image)
        }
    }

    private fun setupHoverView(position: Int, holder: ViewHolder?) {
        val hover = LayoutInflater.from(mContext).inflate(R.layout.hover_colection_item, null)
        var nameTextView = hover.findViewById(R.id.tv_name_colection_item) as TextView
        nameTextView.text = items[position].name

        var deleteCardButton = hover.findViewById(R.id.ib_delete_colection_item) as ImageButton
        deleteCardButton.setOnClickListener { mPresenter.removeItem(items[position].imageUrl!!) }

        holder?.blurLayout?.setHoverView(hover)
        holder?.blurLayout?.setBlurDuration(550)
        holder?.blurLayout?.enableZoomBackground(true)

        fun addChildAnimator(resId: Int) {
            holder?.blurLayout?.addChildAppearAnimator(hover, resId, Techniques.FlipInX, 550, 0)
            holder?.blurLayout?.addChildDisappearAnimator(hover, resId, Techniques.FlipOutX, 550, 500)
        }
        addChildAnimator(R.id.tv_name_colection_item)
        addChildAnimator(R.id.ib_delete_colection_item)
        addChildAnimator(R.id.ib_fav_colection_item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var blurLayout = itemView.findViewById(R.id.blur_layout_colection_item) as BlurLayout
        var image = itemView.findViewById(R.id.iv_colection_item) as ImageView
        var pb = itemView.findViewById(R.id.pb_downloading_image_colection_item) as ContentLoadingProgressBar
    }

}