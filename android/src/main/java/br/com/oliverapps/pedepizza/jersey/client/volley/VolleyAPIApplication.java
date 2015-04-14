package br.com.oliverapps.pedepizza.jersey.client.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by 30258767863 on 02/03/15.
 */
public class VolleyAPIApplication extends Application{
    private static VolleyAPIApplication sInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        sInstance = this;
    }

    public synchronized static VolleyAPIApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
