/**
 * 
 */
package br.com.oliverapps.pedepizza;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.bean.entity.Pizzaria;
import br.com.oliverapps.pedepizza.customlistviewadapter.PizzariaCustomAdapter;
import br.com.oliverapps.pedepizza.jersey.client.AppController;
import br.com.oliverapps.pedepizza.jersey.client.volley.VolleyAPIApplication;

/**
 * @author William
 *
 */
public class PizzariasActivity extends Activity {

	private ListView listViewPizzarias;
	private PizzariaCustomAdapter adapter;	
	private List<Pizzaria> listaPizzarias = new ArrayList<Pizzaria>();
	private final String DNS = "localhost:8080";
	
	private final String URL_GET_ALL_PIZZARIAS_JSON = "http://" + DNS + "/pedepizza-backend/rest/pizzarias/todas";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_pizzarias);

		Resources res = getResources();
		listViewPizzarias = (ListView)findViewById(R.id.list);
		adapter = new PizzariaCustomAdapter(this,listaPizzarias,res);
		listViewPizzarias.setAdapter(adapter);

//		setListViewData();
		
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        fetch();
	}
//		
//    private void setListViewData()
//    {	
//    	for(Pizzaria p:pizzariasRecuperadas)
//    	{
//    		final PizzariaListModel pizzaria = new PizzariaListModel();
//            /******* Firstly take data in model object ******/
//    		pizzaria.setNome(p.getNome());
//    		pizzaria.setDistancia(p.getDistanciaEmMetros().toString());
//    		pizzaria.setPrecoMinimo(p.getCardapio().getPrecoMinimo().toString());
//    		pizzaria.setTaxaEntrega(p.getTaxaEntrega().toString());
//    		pizzaria.setTempoMedioEntrega(p.getTempoMedioEntregaEmMinutos().toString());
//    		pizzaria.setLogoPizzaria(getResources().getDrawable(R.drawable.icon));
//    		pizzaria.setAvaliacao(p.getNumRatings());
//    		
//    		listaPizzarias.add(pizzaria);    		
//    	}
//    }
	
	public void onItemClick(int mPosition)
    {
		Pizzaria tempValues = (Pizzaria) listaPizzarias.get(mPosition);
        
		// instanciar o intent do Cardapio
		
        Toast.makeText(this,
            tempValues.getNome()
            +(tempValues.getDistanciaEmMetros().floatValue() / 1000)+"KM"
            +"minimo: R$"+tempValues.getCardapios().get(0).getPrecoMinimo().toString()
            +"entrega: R$ "+tempValues.getTaxaEntrega()
            +tempValues.getTempoMedioEntregaEmMinutos().toString()+" minutos",
            Toast.LENGTH_LONG)
          .show();
    }

	@Override
	protected void onStop() {
		AppController.getInstance().getRequestQueue().cancelAll("GET ALL PIZZARIAS");
	}
		
	/*
	 *  Converte a resposta json em lista de PizzariaRecord.
	 */
/*
    private List<Pizzaria> parse(@NotNull JSONObject json) throws JSONException {
    	List<Pizzaria> pizzarias = new ArrayList<Pizzaria>();
        JSONArray jsonPizzarias = json.getJSONArray("pizzarias");

        for(int i =0; i < jsonPizzarias.length(); i++) {
            JSONObject jsonObj = jsonPizzarias.getJSONObject(i);
            Pizzaria record = new Pizzaria();
    		record.nome = jsonObj.getString("nome");
    		record.distancia = jsonObj.getString("distanciaEmMetros");    		
    		Cardapio c = (Cardapio)jsonObj.get("cardapio");    		
    		record.valorMinimo = c.getPrecoMinimo().toString();    		
    		record.taxaEntrega = jsonObj.getString("taxaEntrega");
    		record.tempoMedioEntrega = jsonObj.getString("tempoMedioEntregaEmMinutos");
    		record.logoPizzaria = jsonObj.getString("logoURL");
    		record.avaliacao = jsonObj.getString("numRatings");        
                        
            pizzarias.add(record);
        }
        return pizzarias;
    }
*/

    /*
     *  Cria o JsonRequest e popula uma lista de PizzariaRecord com a resposta json convertida.
     */
    private void fetch() {
        JsonObjectRequest request = new JsonObjectRequest(
            URL_GET_ALL_PIZZARIAS_JSON,
            null,
                new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    List<Pizzaria> lista = null;
                   // try {
                        Type collectionType = new TypeToken<List<Pizzaria>>(){}.getType();
                        lista = new Gson().fromJson(jsonObject.toString(),collectionType);
                        //lista = parse(jsonObject);
                    }
                    /*catch(JSONException e){
                        Log.e(getClass().getSimpleName(),
                           "Erro na conversao da resposta a chamada REST de recuperacao das pizzarias ");}
                    adapter.swapRecords(lista);
                }*/
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(PizzariasActivity.this, "Unable to fetch data: "
                        + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            VolleyAPIApplication.getInstance().getRequestQueue().add(request);
        }

}
