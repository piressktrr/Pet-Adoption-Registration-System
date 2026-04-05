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
    private final String NAO_INFORMADO = "NÃO INFORMADO";

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
                        System.out.println("Selecione o pet que você quer alterar! ");
                        alterarDadosPet();
                        opcaoValida = false;
                        break;
                    case 3:
                        deletarPet();
                        opcaoValida = false;
                        break;
                    case 4:
                        System.out.println("Listando todos os pets...");
                        serviceCLI.mostrarApenasLista();
                        opcaoValida = false;
                        break;
                    case 5:
                        System.out.println("Diga o critério do qual você quer listar os pets: ");
                        selecionarPet();
                        opcaoValida = false;
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

    // lembrar de arrumar quando nao escrever pra colocar NAO_INFORMADO
    private void cadastrarDef() throws IOException {
        // cadastrar com um método de validação baseado em if e else e mais verboso e menos fácil de entender
        // elaborando o projeto inteiro baseado nesse cadastrar, deixar o outro
        // com hashmap para servir de evolução no projeto
        // futuramente.
        PetDTO petTemporary = new PetDTO();

        try (BufferedReader lerFormulario = new BufferedReader(new FileReader(formularioDeCadastro))) {
            String linha;
            while ((linha = lerFormulario.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                if (linha.contains("nome")) {
                    System.out.println(linha);
                    input.nextLine();
                    petTemporary.setNomeSobrenome(input.nextLine());

                } else if (linha.contains("tipo")) {

                    while (petTemporary.getTipoAnimal() != TipoAnimal.CACHORRO
                            && petTemporary.getTipoAnimal() != TipoAnimal.GATO) {
                        System.out.println(linha);
                        try {
                            petTemporary.setTipoAnimal(TipoAnimal.valueOf(input.next().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite se o pet é um Gato ou Cachorro, outros não são aceitos!");
                        }

                    }

                } else if (linha.contains("sexo")) {
                    // vou ter que voltar e fazer a validação para quando a pessoa digitar espaço e dar enter/ só dar enter
                    // para o programa falar que só aceita masculino/feminino/macho/femea
                    // mesma coisa em cima no tipo
                    while (petTemporary.getSexo() != Sexo.MASCULINO &&  petTemporary.getSexo() != Sexo.FEMININO
                    && petTemporary.getSexo() != Sexo.FEMEA && petTemporary.getSexo() != Sexo.MACHO) {
                        System.out.println(linha);
                        try {
                            petTemporary.setSexo(Sexo.valueOf(input.next().toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite se o pet é Masculino ou feminino (Fêmea ou Macho também são " +
                                    " aceitos!!)");
                        }
                    }

                } else if (linha.contains("endereço")){

                    System.out.println(linha);
                    input.nextLine();
                    while (endereco.getNumeroCasa() <= 0) {
                        System.out.println("Numero: ");
                        try {
                            String respostaNulaEndereco = input.nextLine();
                            if (respostaNulaEndereco.isBlank()) {
                                break;
                            }
                            endereco.setNumeroCasa(Integer.parseInt(respostaNulaEndereco));
                        } catch (NumberFormatException e) {
                            System.out.println("Digite somente números para o  Número da Casa! (e inteiros!)");
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
                    while (petTemporary.getIdade() < 0.1) {
                        // fazer validação depois para quando o espaço for em branco/só deu enter
                        // ai provavelmente vou ter que transformar o atributo em 'String' e tirar de double
                        // mesma coisa para peso
                        try {
                            String respostaNulaIdade = input.nextLine();
                            petTemporary.setIdade(Double.parseDouble(respostaNulaIdade));
                        } catch (NumberFormatException e) {
                            System.out.println("Somente números para a idade, " +
                                    "sem Caracteres, números negativos ou caracteres especiais!!");
                            continue;
                        }
                        if (petTemporary.getIdade() < 0.1 ) {
                            System.out.println("Somente números POSITIVOS para a idade!");

                        }

                    }
                } else if (linha.contains("peso")) {
                    while (petTemporary.getPeso() < 0.5) {
                        System.out.println(linha);
                        try {
                            String  respostaNulaPeso = input.nextLine();
                            if (respostaNulaPeso.isBlank()) {
                                break;
                            }
                            petTemporary.setPeso(Double.parseDouble(respostaNulaPeso));
                        } catch (NumberFormatException | NullPointerException e) {
                            System.out.println("Somente números POSITIVOS para o peso, " +
                                    "sem Caracteres, números negativos ou caracteres especiais!!");
                            continue;
                        }
                        if (petTemporary.getPeso() < 0.5) {
                            System.out.println("Somente números POSITIVOS para o peso!");
                        }
                    }

                } else if (linha.contains("raça")) {
                    System.out.println(linha);
                    petTemporary.setRaça(input.next());
                }


                if (petTemporary.getNomeSobrenome().isBlank()) {
                    petTemporary.setNomeSobrenome(NAO_INFORMADO);
                }
            }

            try {
                serviceCLI.cadastrarNovoPet(petTemporary);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void selecionarPet () throws IOException {

         // FAZER CRITERIO DE TIPO ANIMAL AQ DEPOIS

        String menu = "Quais critérios você quer usar para buscar o pet?: \n" + "-> Nome. \n" +
                "-> Sexo. \n" + "-> Idade. \n" + "-> Peso. \n" + "-> Raça. \n" + "-> Endereço.";

        System.out.println(menu);
        String inputEscolhaMenu = input.next();
        System.out.println("Digite o " +inputEscolhaMenu);
        String atributoEscolhaDoMenu = input.next();
        serviceCLI.selecionarPetPorAtributo(inputEscolhaMenu, atributoEscolhaDoMenu);

    }

    private void alterarDadosPet () throws IOException {
        serviceCLI.mostrarListaEAlterarDados();
    }

    private void deletarPet() {
        serviceCLI.mostrarListaEDeletarPet();
    }
}
