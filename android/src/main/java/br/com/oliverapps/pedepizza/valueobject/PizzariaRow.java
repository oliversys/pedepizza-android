package br.com.oliverapps.pedepizza.valueobject;

import br.com.oliversys.mobilecommons.volleyjerseyclient.IValueObject;

/**
 * Created by William on 4/14/2015.
 *
 *  Representa cada linha da lista de pizzarias exibida na tela
 */
public class PizzariaRow implements IValueObject{

    private String cardapioId;

    private String nome;
    private String distanciaEmKM;
    private String precoMinimo;
    private String taxaEntrega;
    private String tempoMedioEntregaEmMinutos;
    private String logoPizzaria;
    private String avaliacao;

    public PizzariaRow(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDistanciaEmKM() {
        return distanciaEmKM;
    }

    public void setDistanciaEmKM(String distanciaEmKM) {
        this.distanciaEmKM = distanciaEmKM;
    }

    public String getPrecoMinimo() {
        return precoMinimo;
    }

    public void setPrecoMinimo(String precoMinimo) {
        this.precoMinimo = precoMinimo;
    }

    public String getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(String taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public String getTempoMedioEntregaEmMinutos() {
        return tempoMedioEntregaEmMinutos;
    }

    public void setTempoMedioEntregaEmMinutos(String tempoMedioEntregaEmMinutos) {
        this.tempoMedioEntregaEmMinutos = tempoMedioEntregaEmMinutos;
    }

    public String getLogoPizzaria() {

        return logoPizzaria;
    }

    public void setLogoPizzaria(String logoPizzaria) {
        this.logoPizzaria = logoPizzaria;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getCardapioId() {
        return cardapioId;
    }

    public void setCardapioId(String cardapioId) {
        this.cardapioId = cardapioId;
    }

    @Override
    public String toString() {
        return "PizzariaRow{" +
                "cardapioId='" + cardapioId + '\'' +
                ", nome='" + nome + '\'' +
                ", distanciaEmKM='" + distanciaEmKM + '\'' +
                ", precoMinimo='" + precoMinimo + '\'' +
                ", taxaEntrega='" + taxaEntrega + '\'' +
                ", tempoMedioEntregaEmMinutos='" + tempoMedioEntregaEmMinutos + '\'' +
                ", logoPizzaria='" + logoPizzaria + '\'' +
                ", avaliacao='" + avaliacao + '\'' +
                '}';
    }
}
