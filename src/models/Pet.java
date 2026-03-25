package models;

public class Pet {
    private String nomeSobrenome;
    private TipoAnimal tipoAnimal;
    private Sexo sexo;
    private double idade;
    private double peso;
    private String raça;

    public Pet(String nomeSobrenome, TipoAnimal tipoAnimal, Sexo sexo, double idade, double peso, String raça) {
        this.nomeSobrenome = nomeSobrenome;
        this.tipoAnimal = tipoAnimal;
        this.sexo = sexo;
        this.idade = idade;
        this.peso = peso;
        this.raça = raça;
    }
}
