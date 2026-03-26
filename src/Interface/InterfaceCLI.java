package Interface;

import Repository.RepositoryInterface;
import Repository.RepositoryMemoryCLI;
import Service.ServiceCLI;
import models.Pet;
import models.PetDTO;
import models.Sexo;
import models.TipoAnimal;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class InterfaceCLI {
    private Scanner input = new Scanner(System.in);
    RepositoryInterface bancoDeDados = new RepositoryMemoryCLI();

    private ServiceCLI serviceCLI = new ServiceCLI(bancoDeDados);
    private final File formularioDeCadastro = new File("src/Repository/formulario");


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
                        cadastrar2();
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

    private void cadastrar2() throws IOException {
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
                    System.out.println(linha);
                    // dar um jeito de fazer com que o loop volte quando o cara digitar algo que nao seja cachorro/gato
                    // mesma coisa com o sexo
                    petTemporary.setTipoAnimal(TipoAnimal.valueOf(input.next().toUpperCase()));

                } else if (linha.contains("sexo")) {
                    System.out.println(linha);
                    petTemporary.setSexo(Sexo.valueOf(input.next().toUpperCase()));
                } else if (linha.contains("idade")) {
                    System.out.println(linha);
                    try {
                        petTemporary.setIdade(Double.parseDouble(input.next()));
                    } catch (NumberFormatException e) {
                        petTemporary.setIdade(0);
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
}
