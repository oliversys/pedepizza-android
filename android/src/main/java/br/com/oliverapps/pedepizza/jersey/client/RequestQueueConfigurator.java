package br.com.oliverapps.pedepizza.jersey.client;

import android.app.Activity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by William on 4/16/2015.
 */
public class RequestQueueConfigurator {
    private static RequestQueueConfigurator instance;

    public static RequestQueueConfigurator getInstance(){
        if (instance == null){
            instance = new RequestQueueConfigurator();
        }
        return instance;
    }

    public RequestQueue config(RequestQueue mRequestQueue,Activity activity) {
// Instantiate the cache
        Cache cache = new DiskBasedCache(activity.getApplicationContext().getCacheDir()); // default 5 MB
// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
// Start the queue
        mRequestQueue.start();

        return mRequestQueue;
    }
}
