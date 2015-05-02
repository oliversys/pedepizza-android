package br.com.oliverapps.pedepizza.customlistviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.cardapio.CardapioResumidoActivity;
import br.com.oliverapps.pedepizza.jersey.client.AppController;
import br.com.oliverapps.pedepizza.util.Utils;

public class ViewFlipperAdapter extends ArrayAdapter<NetworkImageView>
        implements OnClickListener
{
    private static ImageLoader imgLoader;
    private CardapioResumidoActivity activity;

    public ViewFlipperAdapter(CardapioResumidoActivity a)
    {
        super(a, R.layout.cardapio_row);
        this.activity = a;
        try {
// Request exclusivo para imagens
            imgLoader = AppController.getInstance().getImageLoader();
        }catch (Throwable t){ t.printStackTrace();}
    }

    // obrigatorio essa declaracao
    @Override
    public void onClick(View view) {

    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (row == null) {
            row = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_flipper_row, parent, false);
        }

        NetworkImageView imgView = (NetworkImageView) row.findViewById(R.id.fotoPizzaFlipper);
        imgView.setImageUrl(Utils.getFotoPizzaURL(position), imgLoader);

        row.setOnClickListener(new OnItemClickListener(position,this.activity));

        return row;
    }

    private class OnItemClickListener implements OnClickListener {
        private int mPosition;
        private CardapioResumidoActivity activity;

        OnItemClickListener(int position,CardapioResumidoActivity act) {
            mPosition = position;
            activity = act;
        }

        @Override
        public void onClick(View arg0) {
            activity.onItemClick(mPosition);
        }
    }
}
