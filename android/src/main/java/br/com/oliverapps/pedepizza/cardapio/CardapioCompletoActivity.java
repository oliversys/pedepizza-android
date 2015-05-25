/**
 * 
 */
package br.com.oliverapps.pedepizza.cardapio;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.oliverapps.pedepizza.Constants;
import br.com.oliverapps.pedepizza.IPedePizzaActivity;
import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.pedido.PedidoActivity;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableExpandableView;

/**
 * @author William
 *
 */
public class CardapioCompletoActivity extends Activity implements IPedePizzaActivity {

	private ExpandableListView menuCompleto;
	private ISwappableExpandableView adapter;
	private List<String> groupHeader = new ArrayList<String>();
	private Map<String,List<CardapioRow>> childData = new HashMap<String,List<CardapioRow>>();
	//private ProgressBar bar;
	private Dialog progress;
	private String idCardapio;

	public static final String URL_GET_CARDAPIO_COMPLETO_JSON = Constants.DNS_CASA + "/pedepizza-backend/rest/cardapio/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_completo);

		idCardapio = getIntent().getStringExtra("idCardapio");

		progress = Utils.showProgressDialog(this);

		menuCompleto = (ExpandableListView) findViewById(R.id.menuCompleto);
		adapter = new CardapioCompletoListAdapter(this,groupHeader,childData);
		menuCompleto.setAdapter((BaseExpandableListAdapter)adapter);

		// Listview on child click listener
		menuCompleto.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
										int groupPosition, int childPosition, long id) {
				chamarTelaPedido(groupPosition,childPosition);
				return false;
			}
		});

		fetchCardapio();
	}

	private void fetchCardapio(){
		AppController.getInstance().doJsonRequestCardapioCompleto(this.adapter,this,this.progress,
				new CardapioRow(),URL_GET_CARDAPIO_COMPLETO_JSON + idCardapio);
	}

	private void chamarTelaPedido(int childPos,int groupPos){
		CardapioRow pizzaSelecionada = (CardapioRow) ((CardapioCompletoListAdapter)adapter).getChild(groupPos,childPos);//
		Gson gson = new Gson();
		String json = gson.toJson(pizzaSelecionada);

		Intent i = new Intent(CardapioCompletoActivity.this, PedidoActivity.class);
		i.putExtra("pizza",json);
		startActivity(i);
	}

	@Override
	public void onItemClick(int pos) {
	}
}
