package io.shace.app.api.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import io.shace.app.api.cache.BitmapLruCache;

/**
 * Created by melvin on 8/28/14.
 */
public class ApiImageLoader implements ImageLoader.ImageCache {
    private final LruCache<String, Bitmap> mCache = new BitmapLruCache();
    private static ApiImageLoader sInstance = null;
    private ImageLoader mImageLoader;

    private ApiImageLoader() {
        mImageLoader = new ImageLoader(RequestQueue.getInstance().get(), this);
    }

    public static ApiImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ApiImageLoader();
        }

        return sInstance;
    }

    public static ImageLoader get() {
        return getInstance().mImageLoader;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
