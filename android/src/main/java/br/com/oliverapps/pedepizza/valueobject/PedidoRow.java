package br.com.oliverapps.pedepizza.valueobject;

import java.util.List;

import br.com.oliversys.mobilecommons.volleyjerseyclient.IValueObject;

/**
 * Created by William on 5/7/2015.
 */
public class PedidoRow implements IValueObject{

    private List<String> itens;
    private String valor;
    private String endereco;
    private String pagamento;

    public List<String> getItens() {
        return itens;
    }

    public void setItens(List<String> itens) {
        this.itens = itens;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }
}
