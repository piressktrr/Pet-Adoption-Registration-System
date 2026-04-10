package Interface;

import Repository.RepositoryInterface;
import Service.ServiceCLI;
import Models.*;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InterfaceCLI {
    // entradas e validações
    private Scanner input = new Scanner(System.in);
    RepositoryInterface bancoDeDados;
    private ServiceCLI serviceCLI;

    // atributos
    private final File formularioDeCadastro = new File("src/Repository/formulario");
    private final String NAO_INFORMADO = "NÃO INFORMADO";

    public InterfaceCLI(RepositoryInterface bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
        this.serviceCLI = new ServiceCLI(bancoDeDados);
    }

    private void imprimirMenu() {
        System.out.println();
        System.out.println("Menu: ");
        System.out.println("1. Cadastrar um novo pet");
        System.out.println("2. Alterar os dados do pet cadastrado");
        System.out.println("3. Deletar um pet cadastrado");
        System.out.println("4. Listar todos os pets cadastrados");
        System.out.println("5. Listar pets por algum critério (nome/raça/idade) ");
        System.out.println("6. Sair");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }

    public void menu() throws IOException {
        imprimirMenu();
        boolean rodando = true;
        while (rodando) {
            imprimirMenu();
            try {
                int opcaoA = input.nextInt();
                input.nextLine();
                switch (opcaoA) {
                    case 1:
                        System.out.println("Iniciando o cadastro de um novo pet... ");
                        cadastrarDef();
                        break;
                    case 2:
                        System.out.println("Selecione o pet que você quer alterar! ");
                        alterarDadosPet();
                        break;
                    case 3:
                        deletarPet();
                        break;
                    case 4:
                        System.out.println("Listando todos os pets...");
                        serviceCLI.mostrarApenasLista();
                        break;
                    case 5:
                        System.out.println("Diga o critério do qual você quer buscar: ");
                        selecionarPet();
                        break;
                    case 6:
                        System.out.println("Saindo da aplicação..! ");
                        rodando = false;
                        break;
                    default:
                        System.out.println("Número incorreto ou inválido, tente novamente");
                }
            } catch (InputMismatchException e) {
                System.out.println("Letras ou caracteres especiais não são validos, finalizando aplicação! ");
                input.nextLine();
                break;
            }
        }
    }

    private void cadastrarDef() throws IOException {
        PetDTO petTemporary = new PetDTO();
        Endereco endereco = new Endereco();

        try (BufferedReader lerFormulario = new BufferedReader(new FileReader(formularioDeCadastro))) {
            String linha;
            while ((linha = lerFormulario.readLine()) != null) {
                if (linha.isBlank()) {continue;}

                if (linha.contains("nome")) {
                    System.out.println(linha);
                    petTemporary.setNomeSobrenome(input.nextLine());

                } else if (linha.contains("tipo")) {

                    while (petTemporary.getTipoAnimal() != TipoAnimal.CACHORRO
                            && petTemporary.getTipoAnimal() != TipoAnimal.GATO) {
                        System.out.println(linha);
                        try {
                            petTemporary.setTipoAnimal(TipoAnimal.valueOf(input.nextLine().toUpperCase().trim()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite se o pet é um Gato ou Cachorro, outros não são aceitos!");
                        }

                    }
                } else if (linha.contains("sexo")) {
                    while (petTemporary.getSexo() != Sexo.MASCULINO &&  petTemporary.getSexo() != Sexo.FEMININO
                    && petTemporary.getSexo() != Sexo.FEMEA && petTemporary.getSexo() != Sexo.MACHO) {
                        System.out.println(linha);
                        try {
                            petTemporary.setSexo(Sexo.valueOf(input.nextLine().toUpperCase().trim()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Digite MASCULINO, FEMININO, MACHO ou FEMEA.");
                        }
                    }

                } else if (linha.contains("endereço")){
                    System.out.println(linha);
                    while (endereco.getNumeroCasa() <= 0) {
                        System.out.println("Numero: ");
                        try {
                            String resposta = input.nextLine();
                            if (resposta.isBlank()) {
                                break;
                            }
                            endereco.setNumeroCasa(Integer.parseInt(resposta));
                        } catch (NumberFormatException e) {
                            System.out.println("Digite somente números inteiros para o  Número da Casa!");
                        }
                    }

                    System.out.println("Cidade: ");
                    endereco.setCidade(input.nextLine().trim());

                    System.out.println("Rua: ");
                    endereco.setRua(input.nextLine().trim());

                    petTemporary.setEndereco(endereco);

                } else if (linha.contains("idade")) {
                    System.out.println(linha);
                    while (petTemporary.getIdade() < 0.1) {
                        try {
                            String resposta = input.nextLine();
                            petTemporary.setIdade(Double.parseDouble(resposta));
                        } catch (NumberFormatException e) {
                            System.out.println("Somente números para a idade!");
                            continue;
                        }
                        if (petTemporary.getIdade() < 0.1 ) {
                            System.out.println("Somente números POSITIVOS para a idade!");

                        }

                    }
                } else if (linha.contains("peso")) {
                    System.out.println(linha);
                    while (petTemporary.getPeso() < 0.5) {
                        try {
                            String  respostaNulaPeso = input.nextLine();
                            if (respostaNulaPeso.isBlank()) {
                                break;
                            }
                            petTemporary.setPeso(Double.parseDouble(respostaNulaPeso));
                        } catch (NumberFormatException | NullPointerException e) {
                            System.out.println("Somente números para o peso, " +
                                    "sem Caracteres, números negativos ou caracteres especiais!!");
                            continue;
                        }
                        if (petTemporary.getPeso() < 0.5) {
                            System.out.println("Somente números POSITIVOS para o peso!");
                        }
                    }

                } else if (linha.contains("raça")) {
                    System.out.println(linha);
                    petTemporary.setRaça(input.nextLine().trim());
                }

            }

            if (petTemporary.getNomeSobrenome() == null || petTemporary.getNomeSobrenome().isBlank()) {
                petTemporary.setNomeSobrenome(NAO_INFORMADO);
            }

            try {
                serviceCLI.cadastrarNovoPet(petTemporary);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private void selecionarPet () {

        System.out.println("Qual o tipo do animal? (CACHORRO / GATO)");
        String tipo = input.nextLine().trim().toUpperCase();
        serviceCLI.selecionarPetPorAtributo("tipoAnimal", tipo);

        System.out.println("\nDeseja fazer a busca com mais algum critério? (S/N)");
        String fazerBusca = input.nextLine().trim().toUpperCase();
        if (!fazerBusca.equals("S")) return;

        System.out.println("Critérios disponíveis: Nome | Sexo | Idade | Peso | Raça | Endereço");
        String criterio = input.nextLine().trim();

        System.out.println("Digite o valor para " + criterio + ":");
        String valor = input.nextLine().trim();

        serviceCLI.selecionarPetPorAtributo(criterio, valor);

    }

    private void alterarDadosPet () throws IOException {
        serviceCLI.mostrarListaEAlterarDados();
    }

    private void deletarPet() {
        serviceCLI.mostrarListaEDeletarPet();
    }
}
