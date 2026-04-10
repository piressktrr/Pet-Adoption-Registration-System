package Service;

import Repository.RepositoryInterface;
import Exceptions.*;
import Models.Endereco;
import Models.Pet;
import Models.PetDTO;
import Models.PetDTOAtualizar;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceCLI {
    private RepositoryInterface repository; // tipo da interface, pra facilitar no futuro e nao desacoplar o codigo
    private Scanner inputService = new Scanner(System.in);

    private final String regexAZ = "(?i)[a-zà-ú]+(?:\\s[a-zà-ú]+)+";
    private final String regexSYMBOLS = "[!@#$%¨&*_]";
    private final String regexNUMBER = "\\d";
    private final Pattern patternSYMBOLS = Pattern.compile(regexSYMBOLS);

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

        Matcher racaSymbolMatcher = patternSYMBOLS.matcher(petTemporary.getRaça().trim());
        Pattern patternNumber = Pattern.compile(regexNUMBER);
        Matcher racaNumberMatcher = patternNumber.matcher(petTemporary.getRaça().trim());

        if (racaSymbolMatcher.find() || racaNumberMatcher.find()) {
            throw new RacaInvalidaException("Raça inválida, contém números ou caracteres especiais!");
        }

        if (petTemporary.getIdade() < 1 && petTemporary.getIdade() >= 0.1) {
            double tempIdade = (petTemporary.getIdade()*10) / 12;
            petTemporary.setIdade(tempIdade);
        }

        Pet pet = new Pet(petTemporary.getNomeSobrenome(),
                petTemporary.getTipoAnimal(),
                petTemporary.getEndereco(),
                petTemporary.getSexo(),
                petTemporary.getIdade(),
                petTemporary.getPeso(),
                petTemporary.getRaça());

        repository.salvar(pet);
    }

    public void selecionarPetPorAtributo(String opcao, String atributo) {
        int cont = 0;

        if (opcao.equalsIgnoreCase("tipoAnimal")) {
            petsAtributos = repository.buscarPetPorTipoAnimal(atributo);
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado para o tipo: " + atributo);
                return;
            }
            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s);
                cont++;
            }

        } else if (opcao.equalsIgnoreCase("nome")) {
            petsAtributos = repository.buscarPetPorNome(atributo);
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado com nome: " + atributo);
                return;
            }
            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s); // ta imprimindo junto com o 1, nao sei pq
                cont++;
            }
        } else if (opcao.equalsIgnoreCase("sexo")) {
            petsAtributos = repository.buscarPetPorSexo(atributo);
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado com sexo: " + atributo);
                return;
            }
            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s);
                cont++;
            }
        } else if (opcao.equalsIgnoreCase("idade")){
            try {
                petsAtributos = repository.buscarPetPorIdade(Double.parseDouble(atributo));
            } catch (NumberFormatException e) {
                System.out.println("Valor de idade inválido. Digite somente números.");
                return;
            }
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado com idade: " + atributo);
                return;
            }
            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s);
                cont++;
            }
        } else if (opcao.equalsIgnoreCase("peso")) {
            try {
                petsAtributos = repository.buscarPetPorPeso(Double.parseDouble(atributo));
            } catch (NumberFormatException e) {
                System.out.println("Valor de peso inválido. Digite somente números.");
                return;
            }
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado com peso: " + atributo);
                return;
            }

            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s);
                cont++;
            }

        } else if (opcao.equalsIgnoreCase("raça") || opcao.equalsIgnoreCase("raca")) {
            petsAtributos = repository.buscarPetPorRaca(atributo);
            if (petsAtributos.isEmpty()) {
                System.out.println("Nenhum pet encontrado com raça: " + atributo);
                return;
            }
            for (String s : petsAtributos) {
                System.out.println("[" + cont + "]" + s);
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
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }
    }

    public void mostrarListaEAlterarDados() {
        petsAtributos = repository.listarPetsString();

        if (petsAtributos.isEmpty()) {
            System.out.println("Nenhum pet encontrado!");
            return;
        }

        for (int i = 0; i < petsAtributos.size(); i++) {
            System.out.println("["+ (i) +"]" + petsAtributos.get(i));
            System.out.println();
        }

        int escolha = lerIndiceValido(petsAtributos.size());

        System.out.println("Pet escolhido com sucesso!!");
        System.out.println("Pet: "+petsAtributos.get(escolha));
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

        PetDTOAtualizar atualizacaoPet = new PetDTOAtualizar();
        Endereco endereco = new Endereco();

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

        atualizacaoPet.setEndereco(endereco);

        repository.atualizar(escolha, atualizacaoPet, endereco);
        System.out.println("Pet atualizado com sucesso!!, encerrando programa!");
    }

    public void mostrarListaEDeletarPet() {
        petsAtributos = repository.listarPetsString();

        if (petsAtributos.isEmpty()) {
            System.out.println("Nenhum pet encontrado!");
            return;
        }

        for (int i = 0; i < petsAtributos.size(); i++) {
            System.out.println("["+ (i) +"]" + petsAtributos.get(i));
            System.out.println();
        }

        System.out.println("Selecione o indíce do pet que você quer apagar: ");
        int escolha = lerIndiceValido(petsAtributos.size());

        System.out.println("Pet escolhido para DELETAR: ");
        System.out.println(petsAtributos.get(escolha));
        System.out.println("Quer mesmo exclui-lo? (S/N): ");

        String sn = "";
        while (!sn.equalsIgnoreCase("S") && !sn.equalsIgnoreCase("N")) {
            sn = inputService.nextLine();
        }

        if (sn.equalsIgnoreCase("S")) {
            boolean deletado = repository.deletar(escolha);
            System.out.println(deletado ? "Pet deletado com sucesso!" : "Erro ao deletar!");
        } else {
            System.out.println("Exclusão cancelada!");
        }
    }

    private int lerIndiceValido(int tamanhoLista) {
        int escolha = -1;
        while (escolha < 0 || escolha >= tamanhoLista) {
            try {
                String entrada = inputService.nextLine();
                escolha = Integer.parseInt(entrada.trim());
                if (escolha < 0 || escolha >= tamanhoLista) {
                    System.out.println("Índice fora do intervalo, tente novamente (0 a " + (tamanhoLista - 1) + "):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite somente números inteiros!");
            }
        }
        return escolha;
    }
}
