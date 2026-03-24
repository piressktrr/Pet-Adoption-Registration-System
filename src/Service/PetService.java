package Service;

import models.Endereco;
import models.Sexo;
import models.TipoAnimal;

public class PetService {
    private String nome; // somente caracteres de A-Z
    private String sobrenome; // somente caracteres de A-Z
    private TipoAnimal tipo;
    private Sexo sexo;
    private Endereco endereço; // usando uma classe de endereço pra nao ficar fazendo atributo aq
    private String bairro;
    private double idade;
    // se idade > 20, lançar exceçao
    // se idade menor que < 1 (meses), transformar em 0.x anos
    private double peso; // pode digitar com , ou . - arrumar isso
    // se o peso for maior que 60 ou menor que 0.5 lançar a exceçao personalizada

    private String raça;
    // sem numeros ou caracteres especiais

    private final String NAO_INFORMADO = "NÃO INFORMADO"; // pra caso o usuario nao informar
}
