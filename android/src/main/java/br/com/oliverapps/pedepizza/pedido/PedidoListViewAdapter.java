package br.com.oliverapps.pedepizza.pedido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliverapps.pedepizza.valueobject.PedidoItemRow;

/**
 * Created by William on 5/7/2015.
 */
public class PedidoListViewAdapter extends ArrayAdapter{

    private List<PedidoItemRow> pedidoItens;
    private int layout;
    private Context ctx;
    private CardapioRow pizzaAdicionadaAoCarrinho;

    public PedidoListViewAdapter(Context context, int resource,CardapioRow pizza,List<PedidoItemRow> p) {
        super(context, resource);

        this.pizzaAdicionadaAoCarrinho = pizza;
        this.layout = resource;
        this.pedidoItens = p;
        this.ctx = context;
    }

    public View getView(int position, View listRow, ViewGroup parent) {

        ViewHolder holder = null;
        if (listRow == null){
            listRow = LayoutInflater.from(getContext()).inflate(this.layout,parent,false);

            holder = new ViewHolder();
            listRow.setTag(holder);
        }
        else {
            holder = (ViewHolder) listRow.getTag();
        }

        holder.picker = (CustomNumberPicker) listRow.findViewById(R.id.qtd);
        holder.nomePizza = (TextView) listRow.findViewById(R.id.nomePizza);
        holder.precopizza = (TextView) listRow.findViewById(R.id.precoPizza);

        if (pedidoItens.get(position) != null) {
            PedidoItemRow ped = pedidoItens.get(position);
            //ped.setQtd(String.valueOf(holder.picker.getValue()));
            holder.nomePizza.setText(ped.getNomePizza());
            holder.precopizza.setText(ped.getPrecoPizza());
        }
        else {
            PedidoItemRow pedidoItem = new PedidoItemRow();
            pedidoItem.setQtd("1");
            pedidoItem.setNomePizza(pizzaAdicionadaAoCarrinho.getNome());
            pedidoItem.setPrecoPizza(pizzaAdicionadaAoCarrinho.getPreco());
            super.add(pedidoItem);
        }

        return listRow;
    }

    public class ViewHolder {
        public CustomNumberPicker picker;
        public TextView nomePizza;
        public TextView precopizza;
    }
}
