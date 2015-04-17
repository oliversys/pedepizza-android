/**
 * 
 */
package br.com.oliverapps.pedepizza;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import java.util.ArrayList;

import br.com.oliverapps.pedepizza.bean.entity.Cardapio;
import br.com.oliverapps.pedepizza.customlistviewadapter.CardapioCustomAdapter;

/**
 * @author William
 *
 */
public class MenuCompletoActivity extends ListActivity {

	private ListView listViewPizzarias;
	private CardapioCustomAdapter adapter;
	private ArrayList<Cardapio> listaPizzas = new ArrayList<Cardapio>();
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_completo);
		
		RatingBar ratingBar = (RatingBar) findViewById(R.id.barra_avaliacao); 
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float avaliacao, boolean fromUser) {
				
			}
		});
	}
}
