package br.com.oliverapps.pedepizza.br.com.oliverapps.pedepizza.vo;

/**
 * Created by William on 4/22/2015.
 */
public class CardapioRow implements IValueObject {

    private boolean inMaisPedida;

    private String fotoPizza;
    private String nomePizza;
    private String descricaoPizza;
    private String avaliacaoPizza;
    private String precoPizza;

    public CardapioRow(){}

    public boolean isMaisPedida(){
        return inMaisPedida;
    }

    public String getFotoPizza() {
        return fotoPizza;
    }

    public void setFotoPizza(String fotoPizza) {
        this.fotoPizza = fotoPizza;
    }

    public String getNomePizza() {
        return nomePizza;
    }

    public void setNomePizza(String nomePizza) {
        this.nomePizza = nomePizza;
    }

    public String getDescricaoPizza() {
        return descricaoPizza;
    }

    public void setDescricaoPizza(String descricaoPizza) {
        this.descricaoPizza = descricaoPizza;
    }

    public String getAvaliacaoPizza() {
        return avaliacaoPizza;
    }

    public void setAvaliacaoPizza(String avaliacaoPizza) {
        this.avaliacaoPizza = avaliacaoPizza;
    }

    public String getPrecoPizza() {
        return precoPizza;
    }

    public void setPrecoPizza(String precoPizza) {
        this.precoPizza = precoPizza;
    }
}
