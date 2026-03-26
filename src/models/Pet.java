package models;

public class Pet {
    private String nomeSobrenome;
    private TipoAnimal tipoAnimal;
    private Sexo sexo;
    private double idade;
    private String peso;
    private String raça;
    private double id;
    private static final String NAO_INFORMADO = "NÃO_INFORMADO";

    public Pet(String nomeSobrenome, TipoAnimal tipoAnimal, Sexo sexo, double idade, String peso, String raça) {
        this.nomeSobrenome = nomeSobrenome;
        this.tipoAnimal = tipoAnimal;
        this.sexo = sexo;
        this.idade = idade;
        this.peso = peso;
        this.raça = raça;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNomeSobrenome() {
        return nomeSobrenome;
    }

    public void setNomeSobrenome(String nomeSobrenome) {
        this.nomeSobrenome = nomeSobrenome;
    }

    public TipoAnimal getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(TipoAnimal tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public double getIdade() {
        return idade;
    }

    public void setIdade(double idade) {
        this.idade = idade;
    }

    public String getPeso() {
        return this.peso;
    }

    public void setPeso(String peso) {

        this.peso = peso;
    }

    public String getRaça() {
        return raça;
    }

    public void setRaça(String raça) {
        this.raça = raça;
    }
}
