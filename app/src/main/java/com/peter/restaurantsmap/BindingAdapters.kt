package com.peter.restaurantsmap

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.peter.restaurantsmap.framwork.datasource.Utils.MAX_HEIGHT
import com.peter.restaurantsmap.framwork.datasource.Utils.MAX_WIDTH


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, reference: String?) {
    try {
        imgView.clipToOutline = true

//            var imgUri = imgUrl!!.toUri().buildUpon().scheme("https").build()
        var url =
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=$MAX_WIDTH&maxheight=$MAX_HEIGHT&photo_reference=$reference&key=${BuildConfig.MAPS_API_KEY}"
      Log.i("place_url",url)
        Glide.with(imgView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.mipmap.ic_launcher)
            )
            .into(imgView)

    } catch (e: Exception) {
        Log.e("BindingAdapter", e.message.toString())
        imgView.setImageResource(R.mipmap.ic_launcher)
    }
}

@BindingAdapter("status")
fun bindStatus(textView: TextView, status: Boolean?) {
    try {
        if (status == true) {
            textView.setText("Open Now")
        } else textView.setText("Closed")

    } catch (e: Exception) {
        Log.e("BindingAdapter", e.message.toString())
    }
}


