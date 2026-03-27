package Interface;

import Repository.RepositoryInterface;
import Repository.RepositoryMemoryCLI;
import Service.ServiceCLI;
import models.*;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class InterfaceCLI {
    // entradas e validações
    private Scanner input = new Scanner(System.in);
    RepositoryInterface bancoDeDados = new RepositoryMemoryCLI();
    private ServiceCLI serviceCLI = new ServiceCLI(bancoDeDados);

    // atributos
    private final File formularioDeCadastro = new File("src/Repository/formulario");
    private Endereco endereco = new Endereco();

    public void lerFormulario() { // só pra teste pra ler o arquivo, apagar depois
        serviceCLI.readFileInteger(formularioDeCadastro);
    }

    public void menu() throws IOException {
        System.out.println();
        System.out.println("Menu: ");
        System.out.println("1. Cadastrar um novo pet");
        System.out.println("2. Alterar os dados do pet cadastrado");
        System.out.println("3. Deletar um pet cadastrado");
        System.out.println("4. Listar todos os pets cadastrados");
        System.out.println("5. Listar pets por algum critério (nome/raça/idade) ");
        System.out.println("6. Sair");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        int opcaoSwitchCase;
        boolean opcaoValida = true;
        while (opcaoValida) {
            try {
                opcaoSwitchCase = input.nextInt();
                switch (opcaoSwitchCase) {
                    case 1:
                        System.out.println("Iniciando o cadastro de um novo pet... ");
                        cadastrarDef();
                        opcaoValida = false;
                        break;
                    case 2:
                        System.out.println("Selecione o ID do pet que você quer alterar: ");

                        break;
                    case 3:
                        System.out.println("Selecione o ID do pet que você quer apagar: ");

                        break;
                    case 4:
                        System.out.println("Listando todos os pets...");
                        break;
                    case 5:
                        System.out.println("Diga o critério do qual você quer listar os pets: ");

                        break;
                    case 6:
                        System.out.println("Saindo da aplicação..! ");
                        opcaoValida = false;
                        break;
                    default:
                        System.out.println("Número incorreto ou inválido, tente novamente");
                }
            } catch (InputMismatchException e) {
                System.out.println("Letras ou caracteres especiais não são validos, finalizando aplicação! ");
                break;
            }
        }
    }

    private void cadastrar() throws IOException {
        PetDTO petTemporary = new PetDTO();
        // cadastrar usando hashmap e map, algo que eu ainda nao tenho conhecimento, mas vou ir atrás
        Map<String, Consumer<String>> setters = Map.of(
            "1 - Qual o nome e sobrenome do pet?", petTemporary::setNomeSobrenome,
            "2 - Qual o tipo do pet (Cachorro/Gato)?",
                v -> petTemporary.setTipoAnimal(TipoAnimal.valueOf(v.toUpperCase())),
            "3 - Qual o sexo do animal?", v -> petTemporary.setSexo(Sexo.valueOf(v.toUpperCase())),
            "4 - Qual a idade aproximada do pet?", v -> petTemporary.setIdade(Double.parseDouble(v)),
            "5 - Qual o peso aproximado do pet?", v -> petTemporary.setPeso(v.toUpperCase()),
            "6 - Qual a raça do pet?", v -> petTemporary.setRaça(v)
        );

        try (BufferedReader lerFormulario = new BufferedReader(new FileReader(formularioDeCadastro))) {
            String linha;
            while ((linha = lerFormulario.readLine()) != null) {
                if (linha.isBlank()) {continue;}

                String campo = linha.trim();
                String resposta = input.nextLine();
                setters.get(campo).accept(resposta);
            }
        }

        try {
            serviceCLI.cadastrarNovoPet(petTemporary);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar na interface!!");
        }

    }

    private void cadastrarDef() throws IOException {
        // cadastrar com um metodo de validação baseado em if e else e mais verboso e menos facil de entender
        // fazendo o projeto inteiro baseado nesse cadastrar, deixar o com hashmap pra servir de evolução pro projeto
        // futuramente.
        PetDTO petTemporary = new PetDTO();

        try (BufferedReader lerFormulario = new BufferedReader(new FileReader(formularioDeCadastro))) {
            String linha;
            while ((linha = lerFormulario.readLine()) != null) {
                if (linha.isBlank() || linha.isEmpty()) {
                    continue;
                }
                if (linha.contains("nome")) {
                    System.out.println(linha);
                    input.nextLine();
                    petTemporary.setNomeSobrenome(input.nextLine());

                } else if (linha.contains("tipo")) {

                    // acho que o certo seria fazer isso dentro do Service, mas não encontrei um jeito de fazer
                    // então to fazendo essa "lógica" pra validar aqui mesmo, infelizmente
                    // a mesma coisa aconteceu com o sexo ali em baixo

                    for (int i = 0; i < 9999; i++) {
                        System.out.println(linha);
                        try {
                            petTemporary.setTipoAnimal(TipoAnimal.valueOf(input.next().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite se o pet é um Gato ou Cachorro, outros não são aceitos!");
                            continue;
                        }


                        if (petTemporary.getTipoAnimal() == TipoAnimal.CACHORRO ||
                                petTemporary.getTipoAnimal() == TipoAnimal.GATO) {
                            break;
                        }
                    }

                } else if (linha.contains("sexo")) {
                    for (int i = 0; i < 9999; i++) {

                        System.out.println(linha);
                        try {
                            petTemporary.setSexo(Sexo.valueOf(input.next().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite se o pet é Masculino ou feminino (Femea ou Macho também são " +
                                    " aceitos!!)");
                            continue;
                        }

                        if (petTemporary.getSexo() == Sexo.MASCULINO ||
                                petTemporary.getSexo() == Sexo.FEMININO ||
                                petTemporary.getSexo() == Sexo.FEMEA ||
                                petTemporary.getSexo() == Sexo.MACHO) {
                                     break;
                        }
                    }
                } else if (linha.contains("endereço")){
                    // mesma coisa aqui, sinto que essa lógica devia ir pra service
                    // até pensei em um jeito de fazer isso, mas aqui parece mais facil
                    System.out.println(linha);
                    System.out.println("Numero: ");
                    while (true) {
                        try {
                            endereco.setNumeroCasa(Integer.parseInt(input.next()));
                        } catch (NumberFormatException e) {
                            System.out.println("Digite somente números para o  Número da Casa! (e inteiros!)");
                            continue;
                        }
                        if (endereco.getNumeroCasa() >= 0) {
                            break;
                        }
                    }
                    System.out.println("Cidade: ");
                    endereco.setCidade(input.next());

                    input.nextLine();
                    System.out.println("Rua: ");
                    endereco.setRua(input.nextLine());

                    petTemporary.setEndereco(endereco);

                } else if (linha.contains("idade")) {
                    System.out.println(linha);
                    while (true) {
                        try {
                            petTemporary.setIdade(Double.parseDouble(input.next()));
                        } catch (NumberFormatException | NullPointerException e) {
                            System.out.println("Somente números POSITIVOS, sem Caracteres ou números negativos!");
                            continue;
                        }
                        if (petTemporary.getIdade() >= 0.1) {
                            break;
                        }
                    }
                } else if (linha.contains("peso")) {
                    System.out.println(linha);
                    petTemporary.setPeso(input.next());

                } else if (linha.contains("raça")) {
                    System.out.println(linha);
                    petTemporary.setRaça(input.next());
                }

            }
            try {
                serviceCLI.cadastrarNovoPet(petTemporary);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void testeParaEndereco() {
        // else if linha.contains "endereco"
        // qual endereço e bairro que ele foi encontrado?
            // numero:
                    //endereco.setNumero().inputNext()
            // cidade:
                    //
            // bairro:

    }
}
