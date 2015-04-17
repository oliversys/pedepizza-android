package br.com.oliverapps.pedepizza.customlistviewadapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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

import br.com.oliverapps.pedepizza.PizzariaRow;
import br.com.oliverapps.pedepizza.PizzariasActivity;
import br.com.oliverapps.pedepizza.jersey.client.AppController;
import br.com.oliverapps.pedepizza.jersey.client.volley.BitmapLruCache;

import static br.com.oliverapps.pedepizza.R.id;
import static br.com.oliverapps.pedepizza.R.layout;

public class PizzariaCustomAdapter extends ArrayAdapter<PizzariaRow> implements OnClickListener {

	private ImageLoader imgLoader;
	private Activity activity;
    private static LayoutInflater inflater;
    private List<PizzariaRow> itensDaLista;
    public Resources res;
    int i=0;
    
    public PizzariaCustomAdapter(Activity actv,List<PizzariaRow> itens,Resources resLocal) {
    	super(actv, layout.pizzarias_row);
    	
    	this.activity = actv;
    	this.itensDaLista = itens;
    	this.res = resLocal;    	
    	this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
// Request exclusivo para imagens (logos de pizzarias)
            imgLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
        }catch (Throwable t){ t.printStackTrace();}
	}
    
    public void swapRecords(List<PizzariaRow> objects) {
        super.clear();
        for(PizzariaRow obj : objects) {
            super.add(obj);
        }    
    	super.notifyDataSetChanged();
    }
    
	@Override
	public void onClick(View v) {

	}
	
	@Override
	public View getView(int position, View row, ViewGroup parent) {
        if(row == null) {
            row = LayoutInflater.from(getContext())
                 .inflate(layout.pizzarias_row, parent, false);
        }

        PizzariaRow tempValues = getItem(position);
        View pizzariasRow = row;
        ViewHolder holder = new ViewHolder();
        
		row.setTag(holder);

        // atribui os controles da tela ao HOLDER
        if(tempValues == null){
/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            holder.nome = (TextView) row.findViewById(id.nomePizzaria);
            holder.distancia = (TextView)row.findViewById(id.distancia);
            holder.precoMinCardapio = (TextView)row.findViewById(id.precoPizza);
            holder.taxaEntrega = (TextView)row.findViewById(id.taxaEntrega);
            holder.tempoMedioEntrega = (TextView)row.findViewById(id.tempoEntrega);
            holder.logoPizzaria = (NetworkImageView) row.findViewById(id.logoPizzaria);
            holder.avaliacao = (RatingBar)row.findViewById(id.barra_avaliacao);

            pizzariasRow = row;
/************  Set holder with LayoutInflater ************/            
            pizzariasRow.setTag(holder);
        }
        else 
            holder = (ViewHolder)pizzariasRow.getTag();
         
        if(itensDaLista.isEmpty())
        {
            Toast.makeText(this.activity,"Nenhuma pizzaria encontrada perto de voce",Toast.LENGTH_LONG).show();             
        }
        else
        {
/***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = itensDaLista.get(position);
             
// atribui os valores informados pelo usuario aos controles do HOLDER
             holder.nome.setText(tempValues.nome);
             holder.distancia.setText(tempValues.distancia);
             holder.precoMinCardapio.setText(tempValues.precoMinimo);
             holder.taxaEntrega.setText(tempValues.taxaEntrega);
             holder.tempoMedioEntrega.setText(tempValues.tempoMedioEntrega);

// atribui a URL da imagem do logo da pizzaria armazenada no servidor ao Holder             
             holder.logoPizzaria.setImageUrl(tempValues.logoPizzaria,imgLoader);
             
             holder.avaliacao.setRating(new Float(tempValues.avaliacao));
/******** Set Item Click Listner for LayoutInflater for each row *******/

             pizzariasRow.setOnClickListener(new OnItemClickListener(position));
        }
        return pizzariasRow;
	}
	
/********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{         
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