package Interface;

import Repository.RepositoryInterface;
import Repository.RepositoryMemoryCLI;
import Service.ServiceCLI;
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

    private PetDTO petTemporary = new PetDTO();

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
                        cadastrar();
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

        Map<String, Consumer<String>> setters = Map.of(
            "1 - Qual o nome e sobrenome do pet?", petTemporary::setNomeSobrenome,
            "2 - Qual o tipo do pet (Cachorro/Gato)?",
                v -> petTemporary.setTipoAnimal(TipoAnimal.valueOf(v.toUpperCase())),
            "3 - Qual o sexo do animal?", v -> petTemporary.setSexo(Sexo.valueOf(v.toUpperCase())),
            "4 - Qual a idade aproximada do pet?", v -> petTemporary.setIdade(Double.parseDouble(v)),
            "5 - Qual o peso aproximado do pet?", v -> petTemporary.setPeso(Double.parseDouble(v)),
            "6 - Qual a raça do pet?", v -> petTemporary.setRaça(v)
        );

        try (BufferedReader lerFormulario = new BufferedReader(new FileReader(formularioDeCadastro))) {
            String linha;
            while ((linha = lerFormulario.readLine()) != null) {
                if (linha.isBlank()) {continue;}

                String campo = linha;
                String resposta = input.nextLine();
                setters.get(campo).accept(resposta);
            }
        }



    }
}
