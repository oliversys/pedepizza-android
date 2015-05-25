package br.com.oliverapps.pedepizza.valueobject;

import br.com.oliversys.mobilecommons.volleyjerseyclient.IValueObject;

/**
 * Created by William on 5/7/2015.
 */
public class PedidoItemRow implements IValueObject{

    private String qtd;
    private String nomePizza;
    private String precoPizza;

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getNomePizza() {
        return nomePizza;
    }

    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }

    public String getPrecoPizza() {
        return precoPizza;
    }

    public void setPrecoPizza(String precoPizza) {
        this.precoPizza = precoPizza;
    }

    @Override
    public String toString() {
        return "PedidoItemRow{" +
                "qtd='" + qtd + '\'' +
                ", nomePizza='" + nomePizza + '\'' +
                ", precoPizza='" + precoPizza + '\'' +
                '}';
    }
}
