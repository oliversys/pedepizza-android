package br.com.oliverapps.pedepizza.customlistviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo.CardapioRow;
import br.com.oliverapps.pedepizza.cardapio.CardapioResumidoActivity;
import br.com.oliverapps.pedepizza.jersey.client.AppController;
import br.com.oliverapps.pedepizza.jersey.client.volley.BitmapLruCache;
import br.com.oliverapps.pedepizza.util.Utils;

/**
 * Created by William on 4/30/2015.
 */
public class CardapioListViewAdapter extends ArrayAdapter<CardapioRow>{

    private ImageLoader imgLoader;
    private CardapioResumidoActivity activity;
    private List<CardapioRow> listItens;

    public CardapioListViewAdapter(CardapioResumidoActivity a, List<CardapioRow> promocionais)
    {
        super(a, R.layout.cardapio_row, promocionais);
        activity = a;
        listItens = promocionais;
        imgLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    public void swapRecords(List<CardapioRow> objects,ProgressBar bar) {
        super.clear();

        for(CardapioRow obj : objects) {
            super.add(obj);
        }
        super.notifyDataSetChanged();

        if (bar != null && bar.getVisibility() == View.VISIBLE) {
            bar.setVisibility(View.GONE);
        }
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
            Toast.makeText(this.activity, "Nenhuma pizza encontrada no cardapio desta pizzaria", Toast.LENGTH_LONG).show();
        }
        else
        {
            CardapioRow tempValues = (CardapioRow)getItem(position);
            v.nomePizza.setText(tempValues.getNomePizza());
            v.descPizza.setText(tempValues.getDescricaoPizza());
            v.precoPizza.setText("R$ " + tempValues.getPrecoPizza());
            v.fotoPizza.setImageUrl(Utils.getFotoPizzaURL(position), imgLoader);
            v.avaliacaoPizza.setRating(new Float(tempValues.getAvaliacaoPizza()));
           // Set Item Click Listener for each row
            row.setOnClickListener(new OnItemClickListener(position));
        }

        return row;
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
            ((CardapioResumidoActivity)activity).onItemClick(mPosition);
        }
    }
}
