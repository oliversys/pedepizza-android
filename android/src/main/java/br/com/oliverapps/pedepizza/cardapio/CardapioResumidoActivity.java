/**
 * 
 */
package br.com.oliverapps.pedepizza.cardapio;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.Constants;
import br.com.oliverapps.pedepizza.IPedePizzaActivity;
import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.pedido.PedidoActivity;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableView;

/**
 * @author William
 *
 */
public class CardapioResumidoActivity extends Activity implements IPedePizzaActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public static final String URL_GET_CARDAPIO_JSON = Constants.DNS_CASA + "/pedepizza-backend/rest/cardapio/";

    private ListView listaMaisPedidas;
    private ListView listaPromocionais;

    private List<CardapioRow> pizzasPromocionais = new ArrayList<CardapioRow>();
    private ISwappableView adapter;
    private ViewFlipperAdapter flipperAdapter;
//    private ProgressBar bar;
    private Dialog progress;

    private String idCardapio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardapio);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.menuFlipper);
        flipper.setInAnimation(this, R.anim.abc_grow_fade_in_from_bottom);
        flipper.setOutAnimation(this, R.anim.abc_shrink_fade_out_from_bottom);
        flipper.setFlipInterval(1000);

        listaMaisPedidas = (ListView) findViewById(R.id.pizzasMaisPedidas);
        flipperAdapter = new ViewFlipperAdapter(this);
        listaMaisPedidas.setAdapter(flipperAdapter);

        idCardapio = getIntent().getStringExtra("idCardapio");

        listaPromocionais = (ListView) findViewById(R.id.pizzasPromocionais);
//        bar = (ProgressBar) findViewById(R.id.progressBar2);
//        bar.setVisibility(View.VISIBLE);
        progress = Utils.showProgressDialog(this);

        adapter = new CardapioListViewAdapter(this,pizzasPromocionais);
        listaPromocionais.setAdapter((ListAdapter) adapter);

        Button completo = (Button)findViewById(R.id.menuCompleto);
        completo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CardapioResumidoActivity.this, CardapioCompletoActivity.class);
                i.putExtra("idCardapio",idCardapio);
                startActivity(i);
            }
        });

        fetchCardapio();
	}

    private void fetchCardapio(){
       AppController.getInstance().doJsonRequest(adapter, this, progress,
               new CardapioRow(),URL_GET_CARDAPIO_JSON + idCardapio + "/PROMOCIONAL" );
    }

    public void onItemClick(int pos){
        CardapioRow pizzaSelecionada = (CardapioRow) ((ListAdapter)adapter).getItem(pos);//
        Gson gson = new Gson();
        String json = gson.toJson(pizzaSelecionada);

        Intent i = new Intent(CardapioResumidoActivity.this, PedidoActivity.class);
        i.putExtra("pizza", json);
        startActivity(i);
    }

}
