package Interface;

import Service.ServiceCLI;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InterfaceCLI {
    private Scanner input = new Scanner(System.in);
    private ServiceCLI serviceCLI = new ServiceCLI();
    private final File formularioDeCadastro = new File("src/Repository/formulario");
    private int opcao;

    public void lerFormulario() {
        serviceCLI.readFileInteger(formularioDeCadastro);
    }

    public void menu() {
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
                        serviceCLI.cadastrarNovoPet(formularioDeCadastro);
                        break;
                    case 2:

                    case 3:

                    case 4:
                        System.out.println("4");
                        break;
                    case 5:

                    case 6:
                        opcaoValida = false;
                        break;
                    default:
                        System.out.println("Número incorreto ou inválido");
                }
            } catch (InputMismatchException e) {
                System.out.println("Letras ou caracteres especiais nao sao validos " + e.getLocalizedMessage());
                break;
            }
        }
    }

}
