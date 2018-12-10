package com.jimmy.rx_flat_concat_map.ui.adapter

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jimmy.rx_flat_concat_map.data.models.Ticket

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setLogo(view: ImageView, url: String ) {

        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(view)

    }
}
