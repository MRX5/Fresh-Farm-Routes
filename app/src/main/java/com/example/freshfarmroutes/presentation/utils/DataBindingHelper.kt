package com.example.freshfarmroutes.presentation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        Picasso.get().load(url).into(imageView)
    }
}
