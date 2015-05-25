package br.com.oliverapps.pedepizza.cardapio;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import java.util.Map;

import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;
import br.com.oliversys.mobilecommons.volleyjerseyclient.BitmapLruCache;
import br.com.oliversys.mobilecommons.volleyjerseyclient.ISwappableExpandableView;

/**
 * Created by William on 5/5/2015.
 */
public class CardapioCompletoListAdapter extends BaseExpandableListAdapter implements ISwappableExpandableView<CardapioRow> {

    private Context ctx;
    private List<String> listHeader;
    private Map<String, List<CardapioRow>> mapHeaderToListChild;
    private ImageLoader imgLoader;

    public CardapioCompletoListAdapter(Context context, List<String> listDataHeader,
                                 Map<String, List<CardapioRow>> listChildData) {
        this.ctx = context;
        this.listHeader = listDataHeader;
        this.mapHeaderToListChild = listChildData;
        imgLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapLruCache());
    }

    public void popular(Map<String,List<CardapioRow>> pizzasPorCategoria){
        mapHeaderToListChild = pizzasPorCategoria;
        listHeader.addAll(pizzasPorCategoria.keySet());
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPos) {
        return mapHeaderToListChild.get(listHeader.get(groupPos)).size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return listHeader.get(groupPos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return this.mapHeaderToListChild.get(this.listHeader.get(groupPos)).get(childPos);
    }

    @Override
    public long getGroupId(int groupPos) {
        return groupPos;
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPos, boolean isExpanded, View row, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPos);
        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView headerText = (TextView) row.findViewById(R.id.categoria);
        headerText.setTypeface(null, Typeface.BOLD);
        headerText.setText(headerTitle);

        return row;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild,
                             View row, ViewGroup parent)
    {
        ViewHolder v = null;
        if (row == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = infalInflater.inflate(R.layout.cardapio_row, null);
            v = new ViewHolder(row);
            row.setTag(v);
        }else{
            v = (ViewHolder) row.getTag();
        }

        if(mapHeaderToListChild.isEmpty()) {
            Toast.makeText(this.ctx, "Nenhuma pizza encontrada no cardapio desta pizzaria", Toast.LENGTH_LONG).show();
        }
        else
        {
            CardapioRow tempValues = (CardapioRow)getChild(groupPos,childPos);

            v.nomePizza.setText(tempValues.getNome());
            v.descPizza.setText(tempValues.getDescricao());
            v.precoPizza.setText("R$ " + tempValues.getPreco());
            v.fotoPizza.setImageUrl(Utils.getFotoPizzaURL(groupPos), imgLoader);
            v.avaliacaoPizza.setRating(Float.valueOf(tempValues.getAvaliacao()));
        }
        return row;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void swapRecords(Map<String,List<CardapioRow>> pizzasClassificadas) {
        this.mapHeaderToListChild.clear();
        for(Map.Entry<String,List<CardapioRow>> entry:pizzasClassificadas.entrySet()){
            this.mapHeaderToListChild.put(entry.getKey(),entry.getValue());
        }
        notifyDataSetChanged();
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

}
