package com.example.stefan.weathraw.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {

    @BindingAdapter(value = {"imageUrl", " errorImage"}, requireAll = false)
    public static void loadImage(ImageView imageView, String imageUrl, Drawable errorPlaceholderResId) {

        if (imageUrl == null) {
            imageView.setImageDrawable(errorPlaceholderResId);
        } else {
            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(errorPlaceholderResId)
                    .error(errorPlaceholderResId);

            Glide.with(imageView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
