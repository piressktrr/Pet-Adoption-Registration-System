package Service;

import Interface.InterfaceCLI;
import Repository.RepositoryInterface;
import exceptions.*;
import models.Endereco;
import models.Pet;
import models.PetDTO;
import models.PetDTOAtualizar;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceCLI {
    // todas as validações aqui
    private RepositoryInterface repository; // tipo da interface, pra facilitar no futuro e nao desacoplar o codigo
    private Scanner inputService = new Scanner(System.in);

    // todos os atributos aqui
    private File formulario = null;
    private FileReader formularioFileReader = null;
    private BufferedReader bufferedReader = null;
    private final String regexAZ = "(?i)[a-zà-ú]+(?:\\s[a-zà-ú]+)+";
    private final String regexSYMBOLS = "[!@#$%¨&*_]";
    private final String regexNUMBER = "\\d";
    private final Pattern patternSYMBOLS = Pattern.compile(regexSYMBOLS);
    private Endereco endereco = new Endereco();
    private List<String> petsAtributos = new ArrayList<>();

    public ServiceCLI(RepositoryInterface repository) {
        this.repository = repository; // quem decide qual interface vai usar é o main
    }

    public void cadastrarNovoPet(PetDTO petTemporary) {
        Matcher matcherSYMBOLS = patternSYMBOLS.matcher(petTemporary.getNomeSobrenome());
        if (matcherSYMBOLS.find()) {
            throw new NameOrLastNameSpecialCharException("Nome e sobrenome inválidos, contém caracteres especiais!");
        } else if (!petTemporary.getNomeSobrenome().trim().matches(regexAZ)) {
            throw new NameAndLastNameInvalidFormat("Somente o nome foi passado! É necessário um sobrenome!");
        }

        if (petTemporary.getPeso() > 60 || petTemporary.getPeso() < 0.5 ) {
            throw new WeightMoreOrLessThanAppropriatedException("Peso inválido, maior que 60kg ou menor que 0,5kg");
        }

        if (petTemporary.getIdade() > 20) {
            throw new IdadeException("Idade inválida, maior que 20 anos");
        }

        if (petTemporary.getRaça().trim().matches(regexSYMBOLS) || petTemporary.getRaça().trim().matches(regexNUMBER)) {
            // deixar como exceção por enquanto, mas acho que vou mudar pra validar na interface depois
            // assim como somente o nome ser passado, sem o sobrenome!
            throw new RacaInvalidaException("Raça inválida, contém números ou caracteres especiais!");
        }

        if (petTemporary.getIdade() < 1 && petTemporary.getIdade() >= 0.1) {
            // transformar meses em anos, se for menor que 1 ou maior igual a 0,1 ele assume automaticamente que são
            // meses, multiplica por 10 e divide por 12, para achar os anos!
            double tempIdade = (petTemporary.getIdade()*10) / 12;
            petTemporary.setIdade(tempIdade);
        }

        Pet pet = new Pet(petTemporary.getNomeSobrenome(), petTemporary.getTipoAnimal(), petTemporary.getEndereco(),
                petTemporary.getSexo(),
                petTemporary.getIdade(), petTemporary.getPeso(), petTemporary.getRaça());

        repository.salvar(pet);
    }

    public void selecionarPetPorAtributo(String opcao, String atributo) {
        int cont = 0;


        if (opcao.equalsIgnoreCase("nome")) {

                petsAtributos = repository.buscarPetPorNome(atributo);
                if (petsAtributos.isEmpty()) {
                    return;
                }
                for (String s : petsAtributos) {
                    System.out.println(cont + s); // ta imprimindo junto com o 1, nao sei pq
                    cont++;
                }
            } else if (opcao.equalsIgnoreCase("sexo")) {

                petsAtributos = repository.buscarPetPorSexo(atributo);
                if (petsAtributos.isEmpty()) {
                    return;
                }
                for (String s : petsAtributos) {
                    System.out.println(cont + s);
                    cont++;
                }
            } else if (opcao.equalsIgnoreCase("idade")){

                petsAtributos = repository.buscarPetPorIdade(Double.parseDouble(atributo));
                if (petsAtributos.isEmpty()) {
                    return;
                }
                for (String s : petsAtributos) {
                    System.out.println(cont + s);
                    cont++;
                }

            } else if (opcao.equalsIgnoreCase("peso")) {

                petsAtributos = repository.buscarPetPorPeso(Double.parseDouble(atributo));
                if (petsAtributos.isEmpty()) {
                return;
                }

                for (String s : petsAtributos) {
                    System.out.println(cont + s);
                    cont++;
                }
            } else if (opcao.equalsIgnoreCase("raça") || opcao.equalsIgnoreCase("raca")) {

                petsAtributos = repository.buscarPetPorRaca(atributo);
                if (petsAtributos.isEmpty()) {
                    return;
                }
                for (String s : petsAtributos) {
                    System.out.println(cont + s);
                    cont++;
                }
        }
    }

    public void mostrarApenasLista() {
        petsAtributos = repository.listarPetsString();
        if (petsAtributos.isEmpty()) {
            System.out.println("Nenhum pet encontrado!");
        }
        for (String s : petsAtributos) {
            System.out.println(s);
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        }
    }

    public void mostrarListaEAlterarDados() {
        int escolha;
        petsAtributos = repository.listarPetsString();
        if (petsAtributos.isEmpty()) {
            System.out.println("Nenhum pet encontrado!");
        }

        for (int i = 0; i < petsAtributos.size(); i++) {
            System.out.println("["+ (i) +"]" + petsAtributos.get(i));
            System.out.println();
        }

        escolha = Integer.parseInt(inputService.nextLine());
        while (escolha < 0 || escolha >= petsAtributos.size()) {
            System.out.println("Número de pet não encontrado, tente novamente!");
            escolha = Integer.parseInt(inputService.next());
        }


        System.out.println("Pet escolhido com sucesso!!");
        System.out.println("Pet: "+petsAtributos.get(escolha));
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        PetDTOAtualizar atualizacaoPet = new PetDTOAtualizar();

        System.out.print("Novo nome (Enter para manter): "); // por algum motivo não está mantendo os atributos
        String nome = inputService.nextLine();
        if (!nome.isBlank()) atualizacaoPet.setNomeSobrenome(nome);

        System.out.println("Novo endereço!! ");

        System.out.println("Novo número (Enter para manter):");
        String numero = inputService.nextLine();
        if(!numero.isBlank()) endereco.setNumeroCasa(Integer.parseInt(numero));

        System.out.println("Nova cidade (Enter para manter): ");
        String cidade = inputService.nextLine();
        if(!cidade.isBlank()) endereco.setCidade(cidade);

        System.out.println("Nova rua (Enter para manter): ");
        String rua = inputService.nextLine();
        if(!rua.isBlank()) endereco.setRua(rua);

        System.out.print("Nova idade (Enter para manter): ");
        String idade = inputService.nextLine();
        if (!idade.isBlank()) atualizacaoPet.setIdade(Double.parseDouble(idade));

        System.out.print("Novo peso (Enter para manter): ");
        String peso = inputService.nextLine();
        if (!peso.isBlank()) atualizacaoPet.setPeso(Double.parseDouble(peso));

        System.out.print("Nova raça (Enter para manter): ");
        String raca = inputService.nextLine();
        if (!raca.isBlank()) atualizacaoPet.setRaça(raca);

        repository.atualizar(escolha, atualizacaoPet, this.endereco);
        System.out.println("Pet atualizado com sucesso!!, encerrando programa!");
        // seleciono o numero
        // ai ele printa esse pet com esse numero na tela
        // ai o programa fala qual atributo ele quer mudar
        // passa pro repository, mudar o atributo, atualiza o txt
    }

    public void mostrarListaEDeletarPet() {
        petsAtributos = repository.listarPetsString();

        int escolha;
        String sn = "";

        if (petsAtributos.isEmpty()) {
            System.out.println("Nenhum pet encontrado!");
        }

        for (int i = 0; i < petsAtributos.size(); i++) {
            System.out.println("["+ (i) +"]" + petsAtributos.get(i));
            System.out.println();
        }

        System.out.println("Selecione o indíce do pet que você quer apagar: ");

        escolha = Integer.parseInt(inputService.nextLine());
        while (escolha < 0 || escolha >= petsAtributos.size()) {
            System.out.println("Número de pet não encontrado, tente novamente!");
            escolha = Integer.parseInt(inputService.next());
        }

        System.out.println("==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=---");
        System.out.println("Pet escolhido para DELETAR: ");
        System.out.println(petsAtributos.get(escolha));
        System.out.println("Quer mesmo exclui-lo? (S/N): ");
        while (!sn.equalsIgnoreCase("S") && !sn.equalsIgnoreCase("N")) {
            sn = inputService.nextLine();
        }

        if (sn.equalsIgnoreCase("S")) {
            boolean deletado = repository.deletar(escolha);
            if (deletado) {
                System.out.println("Pet deletado com sucesso!!");
            } else {
                System.out.println("Erro ao deletar!!");
            }
        } else {
            System.out.println("Exclusão cancelada!");
        }
    }
}
