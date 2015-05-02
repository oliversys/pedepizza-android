/**
 * 
 */
package br.com.oliverapps.pedepizza.cardapio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo.CardapioRow;
import br.com.oliverapps.pedepizza.customlistviewadapter.CardapioListViewAdapter;
import br.com.oliverapps.pedepizza.customlistviewadapter.ViewFlipperAdapter;

/**
 * @author William
 *
 */
public class CardapioResumidoActivity extends Activity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private static final String DNS = "192.168.0.14:8080"; // CASA
    //  private static final String DNS = "10.29.53.150:8080"; // SERPRO
//    private static final String DNS = "192.168.43.41:8080"; // DNS com roteamento android
    //private static final String DNS = "localhost:3000";
    public static final String URL_GET_CARDAPIO_JSON = "http://" + DNS + "/pedepizza-backend/rest/cardapio/";

    private ListView listaMaisPedidas;
    private ListView listaPromocionais;

    private List<CardapioRow> pizzasPromocionais = new ArrayList<CardapioRow>();
    private CardapioListViewAdapter adapter;
    private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.menuFlipper);
        flipper.setInAnimation(this, R.anim.abc_grow_fade_in_from_bottom);
        flipper.setOutAnimation(this, R.anim.abc_shrink_fade_out_from_bottom);
        flipper.setFlipInterval(1000);

        String idPizzaria = getIntent().getStringExtra("idPizzaria");

        listaMaisPedidas = (ListView) findViewById(R.id.pizzasMaisPedidas);
        listaPromocionais = (ListView) findViewById(R.id.pizzasPromocionais);
        bar = (ProgressBar) findViewById(R.id.progressBar1);

        listaMaisPedidas.setAdapter(new ViewFlipperAdapter(this));
        listaPromocionais.setAdapter(new CardapioListViewAdapter(this,pizzasPromocionais));

        fetchCardapio(URL_GET_CARDAPIO_JSON + idPizzaria);
	}

    private void fetchCardapio(String uri){
       // AppController.getInstance().doJsonRequest(adapter, this, bar, new CardapioRow(),URL_GET_CARDAPIO_JSON);
    }

    public void onItemClick(int pos){
//        CardapioRow pizzaSelecionada = (CardapioRow) adapter.getItem(pos);//
//        Gson gson = new Gson();
//        String json = gson.toJson(pizzaSelecionada);
//
//        Intent i = new Intent(CardapioResumidoActivity.this, PedidoActivity.class);
//        i.putExtra("pizza",json);
//        startActivity(i);

//        Toast.makeText(this.getApplicationContext(), pizzaSelecionada.getNomePizza(), Toast.LENGTH_LONG);
    }

}
