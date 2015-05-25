package br.com.oliverapps.pedepizza.cardapio;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.com.oliverapps.pedepizza.IPedePizzaActivity;
import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.BitmapLruCache;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableView;

/**
 * Created by William on 4/30/2015.
 */
public class CardapioListViewAdapter extends ArrayAdapter<CardapioRow> implements ISwappableView<CardapioRow> {

    private ImageLoader imgLoader;
    private IPedePizzaActivity activity;
    private List<CardapioRow> listItens;

    public CardapioListViewAdapter(Activity a, List<CardapioRow> promocionais)
    {
        super(a, R.layout.cardapio_row, promocionais);
        activity = (IPedePizzaActivity)a;
        listItens = promocionais;
        imgLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder v = null;
        if(row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(R.layout.cardapio_row, parent, false);

            v = new ViewHolder(row);
            row.setTag(v);
        }else{
            v = (ViewHolder)row.getTag();
        }

        if(listItens.isEmpty())
        {
            Toast.makeText((Activity)this.activity, "Nenhuma pizza encontrada no cardapio desta pizzaria", Toast.LENGTH_LONG).show();
        }
        else
        {
            CardapioRow tempValues = getItem(position);
            v.nomePizza.setText(tempValues.getNome());
            v.descPizza.setText(tempValues.getDescricao());
            v.precoPizza.setText("R$ " + tempValues.getPreco());
            v.fotoPizza.setImageUrl(Utils.getFotoPizzaURL(position), imgLoader);
            v.avaliacaoPizza.setRating(Float.valueOf(tempValues.getAvaliacao()));
           // Set Item Click Listener for each row
            row.setOnClickListener(new OnItemClickListener(position));
        }

        return row;
    }

    @Override
    public void swapRecords(List<CardapioRow> objects) {
        super.clear();

        for(CardapioRow obj : objects) {
            super.add(obj);
        }
        super.notifyDataSetChanged();
    }

    private class ViewHolder{
        private TextView nomePizza;
        private TextView descPizza;
        private TextView precoPizza;
        private TextView taxaEntrega;
        private NetworkImageView fotoPizza;
        private RatingBar avaliacaoPizza;

        ViewHolder(View listViewItem) {
            nomePizza = (TextView) listViewItem.findViewById(R.id.nomePizza);
            descPizza = (TextView) listViewItem.findViewById(R.id.descPizza);
            precoPizza = (TextView) listViewItem.findViewById(R.id.precoPizza);
            fotoPizza = (NetworkImageView) listViewItem.findViewById(R.id.fotoPizza);
            avaliacaoPizza = (RatingBar) listViewItem.findViewById(R.id.barra_avaliacao_pizza);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            this.mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            activity.onItemClick(mPosition);
        }
    }
}
