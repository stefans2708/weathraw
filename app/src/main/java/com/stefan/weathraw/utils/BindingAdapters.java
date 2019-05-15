package com.stefan.weathraw.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindingAdapter("underline")
    public static void underlineText(TextView textView, Boolean underline) {
        SpannableString underlinedText = new SpannableString(textView.getText());
        underlinedText.setSpan(new UnderlineSpan(), 0, underlinedText.length(), 0);
        textView.setText(underlinedText);
    }
}
