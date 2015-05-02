package br.com.oliverapps.pedepizza.jersey.client;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.List;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo.IValueObject;
import br.com.oliverapps.pedepizza.customlistviewadapter.PizzariaCustomAdapter;
import br.com.oliverapps.pedepizza.util.Utils;

public class AppController extends Application {
	 	 
    public static final String TAG = AppController.class.getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
 
    private static AppController mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
 
    public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        } 
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void doJsonRequest(final PizzariaCustomAdapter adapter,final Activity activity,
                              final ProgressBar bar,final IValueObject vo,final String jsonURI)
    {
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("JSON RESPONSE", jsonArray.toString());
                List lista = null;
                try {
                    lista = Utils.fromJsonToVO(vo,jsonArray); //Utils.toPizzariaRows(jsonArray);//Utils.fromJsonToVO(vo, jsonArray);
                    if (lista != null)
                        // popula o adapter com os valores do VO gravados no BANCO DE DADOS
                        adapter.swapRecords(lista,bar);
                    else {
                        Log.e(AppController.class.getSimpleName(),
                                "Lista de "+vo.getClass().getSimpleName()+" retornou vazia");
                        activity.finish();
                    }
                }
                catch(Exception e) {
                    Log.e(AppController.class.getSimpleName(),
                            "Erro na conversao da resposta REST: "+e.getMessage());
                    e.printStackTrace();
                    // mata a atividade corrente e volta para a MainActivity
                    activity.finish();
                }
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(
            jsonURI,
            responseListener,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    String packageName = activity.getPackageName().substring(activity.getPackageName().lastIndexOf("."));
                    Log.e("JSON RESPONSE", "Erro na recuperacao de " + packageName
                            + ": " + volleyError.getMessage());

                    Toast.makeText(activity, "Falha na recuperacao de " + packageName
                            , Toast.LENGTH_LONG).show();
                    // mata a atividade corrente e volta para a MainActivity
                    activity.finish();
                }
            })
            {
        };

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
//  para aumentar o tempo de timeout

//        request.setRetryPolicy(
//                new DefaultRetryPolicy(
//                    (int) TimeUnit.SECONDS.toMillis(10000),//time out in 10second
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//DEFAULT_MAX_RETRIES = 1;
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request);
    }

    public ImageLoader.ImageContainer doImageRequest(String url,final ImageView mImageView){
        ImageLoader.ImageContainer img = getImageLoader().get(url,
            new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mImageView.setImageResource(R.drawable.image_load_error);
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    mImageView.setImageBitmap(response.getBitmap());
                }
            });
        return img;
    }
}
