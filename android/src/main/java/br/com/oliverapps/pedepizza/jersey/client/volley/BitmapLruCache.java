package br.com.oliverapps.pedepizza.jersey.client.volley;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 30258767863 on 02/03/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class BitmapLruCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache{
    static int getMaxCacheSize() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    public BitmapLruCache() {
        super(getMaxCacheSize());
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
