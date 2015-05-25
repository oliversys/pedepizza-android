package br.com.oliverapps.pedepizza.valueobject;

import br.com.oliversys.mobilecommons.volleyjerseyclient.IValueObject;

public class CardapioRow implements IValueObject{

    private boolean isMaisPedida;

    private String ingredientes;
    private String categoriaCardapio;
    private String fotoURL;
    private String nome;
    private String descricao;
    private String avaliacao;
    private String preco;

    public CardapioRow(){}

    public boolean isMaisPedida(){
        return isMaisPedida;
    }

    public void setIsMaisPedida(boolean in){
        this.isMaisPedida = in;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getCategoriaCardapio() {
        return categoriaCardapio;
    }

    public void setCategoriaCardapio(String categoriaCardapio) {
        this.categoriaCardapio = categoriaCardapio;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "CardapioRow{" +
                "isMaisPedida=" + isMaisPedida +
                ", ingredientes='" + ingredientes + '\'' +
                ", categoriaCardapio='" + categoriaCardapio + '\'' +
                ", fotoURL='" + fotoURL + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", avaliacao='" + avaliacao + '\'' +
                ", preco='" + preco + '\'' +
                '}';
    }
}
