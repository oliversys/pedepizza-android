package br.com.oliversys.mobilecommons.volleyjerseyclient;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.oliversys.volleyadapter.R;

public class AppController extends Application {
	 	 
    public static final String TAG = AppController.class.getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
 
    private static AppController mInstance;

    private Response.ErrorListener errorListener;
 
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

    public void doJsonRequestPedido(final Activity activity, final JSONObject voPreenchido,
                                    final Dialog progress,final String jsonURI)
    {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObj) {
                Log.i("JSON RESPONSE", "Pedido incluido com sucesso");
//                if (bar != null && bar.getVisibility() == View.VISIBLE) {
//                    bar.setVisibility(View.GONE);
//                }
                progress.dismiss();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                jsonURI,
                voPreenchido,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        Log.e("JSON RESPONSE", "Erro na recuperacao de " + activity.getLocalClassName()
                                + ": " + volleyError.getMessage());

                        Toast.makeText(activity, "Falha na recuperacao de " + activity.getLocalClassName()
                                , Toast.LENGTH_LONG).show();
                        activity.finish();
                    }
                });

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
        addToRequestQueue(request);
    }

    private Response.ErrorListener buildErrorListener(final Activity activity,final String jsonURI) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();

                String nomeEntidadeNegocio = activity.getClass().getSimpleName().replace("Activity", "");

                Log.e("JSON RESPONSE", "Erro na recuperacao de " + nomeEntidadeNegocio
                        + ": " + volleyError.getMessage());

                Toast.makeText(activity, "Falha na recuperacao de " + nomeEntidadeNegocio
                            , Toast.LENGTH_LONG).show();

                // mata a atividade corrente e volta para a MainActivity
                activity.finish();
            }
        };
    }

    public void doJsonRequestCardapioCompleto(final ISwappableExpandableView adapter,final Activity activity,
                              final Dialog progress,final IValueObject vo,final String jsonURI) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                Map<String,List<IValueObject>> pizzasClassificadas = Utils.jsonToMapaVO(json,vo);
                if (pizzasClassificadas != null) {
                    // popula o adapter com os valores do VO gravados no BANCO DE DADOS
                    adapter.swapRecords(pizzasClassificadas);
                    if (progress != null)
                        progress.dismiss();
                } else {
                    Log.e(AppController.class.getSimpleName(),
                            "Lista de " + vo.getClass().getSimpleName() + " retornou vazia");
                    activity.finish();
                }
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(
                jsonURI,
                null, // sem jsonObject como parametro (se aplica a HTTP.PUT)
                responseListener,
                buildErrorListener(activity,jsonURI)
                );
    }

    public void doJsonRequest(final ISwappableView adapter,final Activity activity,
                              final Dialog progress,final IValueObject vo,final String jsonURI)
    {
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List lista = Utils.fromJsonToVO(vo, jsonArray);
                if (lista != null) {
                    // popula o adapter com os valores do VO gravados no BANCO DE DADOS
                    adapter.swapRecords(lista);
                    if (progress != null)
                        progress.dismiss();
                }else {
                    Log.e(AppController.class.getSimpleName(),
                            "Lista de " + vo.getClass().getSimpleName() + " retornou vazia");
                    activity.finish();
                }
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(
            jsonURI,
            responseListener,
            buildErrorListener(activity,jsonURI));

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
        addToRequestQueue(request);
//  para aumentar o tempo de timeout
//        request.setRetryPolicy(
//                new DefaultRetryPolicy(
//                    (int) TimeUnit.SECONDS.toMillis(10000),//time out in 10second
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//DEFAULT_MAX_RETRIES = 1;
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void doJsonRequestStringList(final ArrayAdapter adapter, final Activity activity,
                                        final String jsonURI)
    {
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List<String> lista = (ArrayList<String>) new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<ArrayList<String>>() {}.getType());
                if (lista != null) {
                    // popula o adapter com os valores do VO gravados no BANCO DE DADOS
                    adapter.addAll(lista);
                    adapter.notifyDataSetChanged();
                }else {
                    Log.e(AppController.class.getSimpleName(),
                            "Lista de retornou vazia");
                    activity.finish();
                }
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(
                jsonURI,
                responseListener,
                buildErrorListener(activity,jsonURI));

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
        addToRequestQueue(request);
//  para aumentar o tempo de timeout
//        request.setRetryPolicy(
//                new DefaultRetryPolicy(
//                    (int) TimeUnit.SECONDS.toMillis(10000),//time out in 10second
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//DEFAULT_MAX_RETRIES = 1;
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    public void doJsonRequestPlacesFromWS(final ISwappableView adapter,final Activity activity,
                                final Dialog progress,final IValueObject vo,final String jsonURI)
    {
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List lista = Utils.fromGooglePlacesJsonToVO(vo, jsonArray);
                if (lista != null) {
                    adapter.swapRecords(lista);
                    if (progress != null)
                        progress.dismiss();
                }else {
                    Log.e(AppController.class.getSimpleName(),
                            "Lista de " + vo.getClass().getSimpleName() + " retornou vazia");
                    activity.finish();
                }
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(
                jsonURI,
                responseListener,
                buildErrorListener(activity,jsonURI));

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
        addToRequestQueue(request);
    }
}
