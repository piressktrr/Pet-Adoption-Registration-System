package models;

public class Endereco {
    private int numeroCasa;
    private String cidade;
    private String rua;

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int  numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String bairro) {
        this.rua = bairro;
    }
}
