package com.stutern.githublagos.utils;


import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .dontAnimate()
                .into(view);
    }
}
