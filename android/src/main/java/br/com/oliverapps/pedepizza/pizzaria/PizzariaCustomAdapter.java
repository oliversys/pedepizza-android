package br.com.oliverapps.pedepizza.pizzaria;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.PizzariaRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.BitmapLruCache;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableView;

import static br.com.oliverapps.pedepizza.R.id;
import static br.com.oliverapps.pedepizza.R.layout;

public class PizzariaCustomAdapter extends ArrayAdapter<PizzariaRow> implements ISwappableView<PizzariaRow> {

    private ImageLoader imgLoader;
    private Activity activity;
    private static LayoutInflater inflater;
    private List<PizzariaRow> itensDaLista;

    public PizzariaCustomAdapter(Activity actv,List<PizzariaRow> itens) {
        super(actv,layout.pizzarias_row,itens);

        this.activity = actv;
        this.itensDaLista = itens;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

// JsonRequest exclusivo para imagens (logos de pizzarias)
        imgLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    public void swapRecords(List<PizzariaRow> objects) {
        super.clear();

        for(PizzariaRow obj : objects) {
            super.add(obj);
        }
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ViewHolder holder = null;
        if(row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(layout.pizzarias_row, parent, false);

            holder = new ViewHolder();

            holder.nome = (TextView) row.findViewById(id.nomePizzaria);
            holder.distancia = (TextView)row.findViewById(id.distancia);
            holder.precoMinCardapio = (TextView)row.findViewById(id.precoPizza);
            holder.taxaEntrega = (TextView)row.findViewById(id.taxaEntrega);
            holder.tempoMedioEntrega = (TextView)row.findViewById(id.tempoEntrega);
            holder.logoPizzaria = (NetworkImageView) row.findViewById(id.logoPizzaria);
            holder.avaliacao = (RatingBar)row.findViewById(id.barra_avaliacao);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        if(itensDaLista.isEmpty())
        {
            Toast.makeText(this.activity,"Nenhuma pizzaria encontrada perto de voce",Toast.LENGTH_LONG).show();
        }
        else
        {
            PizzariaRow tempValues = itensDaLista.get(position);
// atribui os valores informados pelo usuario aos controles do HOLDER
            holder.nome.setText(tempValues.getNome());
            holder.distancia.setText(tempValues.getDistanciaEmKM()+" KM");
            if (tempValues.getPrecoMinimo() != null)
                holder.precoMinCardapio.setText("R$ "+tempValues.getPrecoMinimo());
            holder.taxaEntrega.setText("R$ "+tempValues.getTaxaEntrega());
            holder.tempoMedioEntrega.setText(tempValues.getTempoMedioEntregaEmMinutos()+"MIN.");
// atribui a URL da imagem do logo da pizzaria armazenada no servidor ao Holder
            holder.logoPizzaria.setImageUrl(Utils.getLogoURL(position), imgLoader);
            holder.avaliacao.setRating(Float.valueOf(tempValues.getAvaliacao()));
            row.setOnClickListener(new OnItemClickListener(position));
        }
        return row;
    }

    public class ViewHolder{
        public TextView nome;
        public TextView distancia;
        public TextView precoMinCardapio;
        public TextView taxaEntrega;
        public TextView tempoMedioEntrega;
        public NetworkImageView logoPizzaria;
        public RatingBar avaliacao;
    }

    private class OnItemClickListener implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            PizzariasActivity act = (PizzariasActivity)activity;
            act.onItemClick(mPosition);
        }
    }
}
