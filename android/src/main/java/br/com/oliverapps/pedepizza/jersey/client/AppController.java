package br.com.oliverapps.pedepizza.jersey.client;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import br.com.oliverapps.pedepizza.PizzariaRow;
import br.com.oliverapps.pedepizza.PizzariasActivity;
import br.com.oliverapps.pedepizza.customlistviewadapter.PizzariaCustomAdapter;

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
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
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

    public void doJsonRequest(final PizzariaCustomAdapter adapter,final Activity activity){
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("JSON RESPONSE", jsonArray.toString());
                List<PizzariaRow> lista = null;
                try {
                    lista = parse(jsonArray);
                    ((PizzariasActivity)activity).setPizzariasConsultadas(lista);
                }
                catch(JSONException e) {
                    Log.e(AppController.class.getSimpleName(),
                            "Erro na conversao da resposta a chamada REST de recuperacao das pizzarias ");
                    e.printStackTrace();
                    activity.finish();
                }
                adapter.swapRecords(lista);
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(
            PizzariasActivity.URL_GET_ALL_PIZZARIAS_JSON,
            responseListener,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    Log.e("JSON RESPONSE", "Erro na recuperacao das pizzarias: " + volleyError.getMessage());
                    Toast.makeText(activity, "Falha na recuperacao das pizzarias", Toast.LENGTH_LONG).show();
                    // mata a atividade corrente e volta para a MainActivity
                    activity.finish();
                }
            })
            {
        };

        RequestQueueConfigurator.getInstance().config(getRequestQueue(),activity);
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(10000),//time out in 10second
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//DEFAULT_MAX_RETRIES = 1;
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request);
    }

    /*
    *  Converte a resposta json em lista de PizzariaRecord.
    */
    private List<PizzariaRow> parse(@NotNull JSONArray json) throws JSONException {
        List<PizzariaRow> pizzarias = new ArrayList<PizzariaRow>();
        JSONArray jsonPizzarias = json;

        for(int i =0; i < jsonPizzarias.length(); i++) {
            JSONObject jsonObj = jsonPizzarias.getJSONObject(i);
            PizzariaRow record = new PizzariaRow();

            if (!jsonObj.isNull("nome"))
                record.nome = jsonObj.getString("nome");

            if (!jsonObj.isNull("distanciaEmMetros"))
                record.distancia = jsonObj.getString("distanciaEmMetros");

            if (!jsonObj.isNull("precoMinimo"))
                record.precoMinimo = jsonObj.getString("precoMinimo");

            if (jsonObj.isNull("taxaEntrega"))
                record.taxaEntrega = jsonObj.getString("taxaEntrega");

            if (jsonObj.isNull("tempoMedioEntregaEmMinutos"))
                record.tempoMedioEntrega = jsonObj.getString("tempoMedioEntregaEmMinutos");

            if (jsonObj.isNull("logoURL"))
                record.logoPizzaria = jsonObj.getString("logoURL");

            if (jsonObj.isNull("numRatings"))
                record.avaliacao = jsonObj.getString("numRatings");

            pizzarias.add(record);
        }
        return pizzarias;
    }

}
