/**
 * 
 */
package br.com.oliverapps.pedepizza.pizzaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo.PizzariaRow;
import br.com.oliverapps.pedepizza.cardapio.CardapioResumidoActivity;
import br.com.oliverapps.pedepizza.customlistviewadapter.PizzariaCustomAdapter;
import br.com.oliverapps.pedepizza.jersey.client.AppController;

/**
 * @author William
 *
 */
public class PizzariasActivity extends Activity {

    private ProgressBar spinner;

    private ListView listViewPizzarias;
	private PizzariaCustomAdapter adapter;
	private List<PizzariaRow> listaPizzarias = new ArrayList<PizzariaRow>();
	private static final String DNS = "192.168.0.14:8080"; // CASA
//  private static final String DNS = "10.29.53.150:8080"; // SERPRO
//    private static final String DNS = "192.168.43.41:8080"; // DNS com roteamento android
    //private static final String DNS = "localhost:3000";
    public static final String DNS_PRODUCAO = "pedepizza.oliverapps.com.br";
    public static final String URL_GET_ALL_PIZZARIAS_JSON = "http://" + DNS + "/pedepizza-backend/rest/pizzarias/todas";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_listar_pizzarias);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
		listViewPizzarias = (ListView)findViewById(R.id.list);
		adapter = new PizzariaCustomAdapter(this,listaPizzarias);
		listViewPizzarias.setAdapter(adapter);

        fetchPizzarias();
	}

	public void onItemClick(int mPosition)
    {
        PizzariaRow pizzariaSelecionada = (PizzariaRow) adapter.getItem(mPosition);
        Intent i = new Intent(PizzariasActivity.this,CardapioResumidoActivity.class);
        i.putExtra("idPizzaria",pizzariaSelecionada.getId());
        Toast.makeText(getApplicationContext(),"ID: "+pizzariaSelecionada.getId(),Toast.LENGTH_LONG).show();
        startActivity(i);
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
        AppController.getInstance().doJsonRequest(adapter, this, this.spinner, new PizzariaRow(),URL_GET_ALL_PIZZARIAS_JSON);
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
