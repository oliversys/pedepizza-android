/**
 * 
 */
package br.com.oliverapps.pedepizza;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.customlistviewadapter.PizzariaCustomAdapter;
import br.com.oliverapps.pedepizza.jersey.client.AppController;

/**
 * @author William
 *
 */
public class PizzariasActivity extends Activity {

	private ListView listViewPizzarias;
	private PizzariaCustomAdapter adapter;	
	private List<PizzariaRow> listaPizzarias = new ArrayList<PizzariaRow>();
	//private static final String DNS = "192.168.0.13:8080";
    private static final String DNS = "192.168.43.41:8080"; // DNS com roteamento android
    //private static final String DNS = "localhost:3000";
    public static final String DNS_PRODUCAO = "pedepizza.oliverapps.com.br";
    public static final String URL_GET_ALL_PIZZARIAS_JSON = "http://" + DNS + "/pedepizza-backend/rest/pizzarias/todas";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_pizzarias);

		Resources res = getResources();
		listViewPizzarias = (ListView)findViewById(R.id.list);
		adapter = new PizzariaCustomAdapter(this,listaPizzarias,res);
		listViewPizzarias.setAdapter(adapter);

        fetchPizzarias();
	}



	public void onItemClick(int mPosition)
    {
        PizzariaRow tempValues = (PizzariaRow) listaPizzarias.get(mPosition);
        
		// instanciar o intent do Cardapio
		
        Toast.makeText(this,
            tempValues.nome
            +(new Float(tempValues.avaliacao) / 1000)+"KM"
            +"minimo: R$"+tempValues.precoMinimo
            +"entrega: R$ "+tempValues.taxaEntrega
            +tempValues.tempoMedioEntrega+" minutos",
            Toast.LENGTH_LONG)
          .show();
    }

	@Override
	protected void onStop() {
        super.onStop();
		AppController.getInstance().getRequestQueue().cancelAll("GET ALL PIZZARIAS");
	}

    /*
     *  Cria o JsonRequest e popula uma lista de PizzariaRecord com a resposta json convertida.
     */
     private void fetchPizzarias() {
        AppController.getInstance().doJsonRequest(adapter,this);
     }

    public List<PizzariaRow> getPizzariasConsultadas()
    {
        return this.listaPizzarias;
    }

    public void setPizzariasConsultadas(List<PizzariaRow> lista)
    {
        this.listaPizzarias = lista;
    }
}
