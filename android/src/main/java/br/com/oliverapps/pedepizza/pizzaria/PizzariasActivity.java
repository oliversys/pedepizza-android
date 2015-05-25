/**
 * 
 */
package br.com.oliverapps.pedepizza.pizzaria;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.Constants;
import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.cardapio.CardapioResumidoActivity;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.PizzariaRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableView;

/**
 * @author William
 *
 */
public class PizzariasActivity extends Activity {

    //private ProgressBar spinner;
    private Dialog progress;
    private ISwappableView adapter;

    private ListView listViewPizzarias;
	private List<PizzariaRow> listaPizzarias = new ArrayList<PizzariaRow>();
    public static String URL_GET_ALL_PIZZARIAS_JSON = Constants.DNS_CASA+"/pedepizza-backend/rest/pizzarias/todas";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_listar_pizzarias);

        progress = Utils.showProgressDialog(this);

        listViewPizzarias = (ListView) findViewById(R.id.list);
        adapter = new PizzariaCustomAdapter(this, listaPizzarias);
        listViewPizzarias.setAdapter((ListAdapter) adapter);

        fetchPizzarias();
	}

    @Override
    protected void onPause() {
        super.onPause();
        if (progress != null)
            progress.dismiss();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (progress != null)
            progress.dismiss();
    }

    public void onItemClick(int mPosition)
    {
        PizzariaRow pizzariaSelecionada = (PizzariaRow) listViewPizzarias.getAdapter().getItem(mPosition);
        Intent i = new Intent(PizzariasActivity.this,CardapioResumidoActivity.class);
        i.putExtra("idCardapio", pizzariaSelecionada.getCardapioId());
        startActivity(i);
    }

	@Override
	protected void onStop() {
        super.onStop();
		AppController.getInstance().getRequestQueue().cancelAll("GET ALL PIZZARIAS");
        if (progress != null)
            progress.dismiss();
	}

    /*
     *  Cria o JsonRequest e popula uma lista de PizzariaRecord com a resposta json convertida.
     */
    private void fetchPizzarias() {
        AppController.getInstance().doJsonRequest(adapter, this, progress, new PizzariaRow(),URL_GET_ALL_PIZZARIAS_JSON);
     }
}
