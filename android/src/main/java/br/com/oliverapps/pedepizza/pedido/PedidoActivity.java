package br.com.oliverapps.pedepizza.pedido;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.oliverapps.pedepizza.Constants;
import br.com.oliverapps.pedepizza.R;
import br.com.oliverapps.pedepizza.pagamento.PagamentoActivity;
import br.com.oliverapps.pedepizza.util.Utils;
import br.com.oliverapps.pedepizza.valueobject.CardapioRow;
import br.com.oliverapps.pedepizza.valueobject.PedidoItemRow;
import br.com.oliverapps.pedepizza.valueobject.PedidoRow;
import br.com.oliversys.mobilecommons.volleyjerseyclient.AppController;

public class PedidoActivity extends Activity {

    private List<PedidoItemRow> pedidoItens = new ArrayList<PedidoItemRow>();
    private TextView totalPedido;
    private ListView pedidos;
    private PedidoListViewAdapter adapter;
    private PedidoRow pedidoRow;
    //private ProgressBar bar;
    private Dialog progress;

    public final String URL_INCLUIR_PEDIDO_JSON = Constants.DNS_CASA+"/pedepizza-backend/rest/pedidos/incluir";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        String pizzaJson = getIntent().getStringExtra("pizza");
        Gson gson = new Gson();
        CardapioRow pizza = gson.fromJson(pizzaJson, CardapioRow.class);

        adapter = new PedidoListViewAdapter(this,R.layout.pedido_row,pizza,pedidoItens);

        calcularTotalPedido(pizza);

        Button finalizar = (Button) findViewById(R.id.finalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamarTelaFinalizarPedido();
            }
        });

//        bar = (ProgressBar) findViewById(R.id.pedidoBar);

        pedidos = (ListView) findViewById(R.id.list);
        pedidos.setAdapter(adapter);
    }

    // Chama a tela para o user escolher se quer pagar online ou na entrega
    private void chamarTelaFinalizarPedido()
    {
        String pedidoJson = incluirPedido(pedidoRow);

        Intent i = new Intent(PedidoActivity.this, PagamentoActivity.class);
        i.putExtra("pedido",pedidoJson);
        startActivity(i);
    }

    // Chamado a cada itemPedido adicionado a tela pelo adapter
    private void calcularTotalPedido(CardapioRow pizza) {
        totalPedido = (TextView) findViewById(R.id.totalPedido);

        // SE ja existem itens na tela, atualiza o TOTAL com o preco do item novo
        if (!((String)totalPedido.getText()).isEmpty()){
            String totaStr = ((String)totalPedido.getText());
            Double totalAtual = Double.valueOf(totaStr);
            Double total = totalAtual + Double.valueOf(pizza.getPreco());
            totalPedido.setText(String.valueOf(total));
        }
        else{ // SE primeiro item do pedido, TOTAL = ITEM.PRECO
            totalPedido.setText(String.valueOf(Double.valueOf(pizza.getPreco())));
        }
    }

    private String incluirPedido(PedidoRow pedido){
        // popula o pedido com os itens da tela
        pedidoRow = new PedidoRow();
        List<String> itensPedidoStr = new ArrayList<String>();
        for(PedidoItemRow item:pedidoItens){
            itensPedidoStr.add(item.toString());
        }
        pedidoRow.setItens(itensPedidoStr);
        pedidoRow.setValor(this.totalPedido.getText().toString());
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        String enderecoEntrega = settings.getString("ENDERECO_ENTREGA", "ENDERECO NAO ENCONTRADO");
        pedidoRow.setEndereco(enderecoEntrega);

        // converte o pedido em JSONObject para ser incluido via Volley
        JSONObject pedidoJsonObj = new JSONObject();
        for(Field field:pedidoRow.getClass().getDeclaredFields() ){
            Object valor = null;
            try {
                valor = field.get(pedidoRow);
                field.setAccessible(true);
                pedidoJsonObj.put(field.getName(),valor);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        bar.setVisibility(View.VISIBLE);
        progress = Utils.showProgressDialog(this);
        AppController.getInstance().doJsonRequestPedido(this, pedidoJsonObj, progress, URL_INCLUIR_PEDIDO_JSON);

        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedidoRow);
        return pedidoJson;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedido, menu);
        return true;
    }
}
