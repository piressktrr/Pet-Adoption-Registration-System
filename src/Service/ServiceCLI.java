package Service;

import Repository.RepositoryInterface;
import exceptions.*;
import models.Pet;
import models.PetDTO;


import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceCLI {
    // fazer todas as validações aqui
    private RepositoryInterface repository; // tipo da interface, pra facilitar no futuro e nao desacoplar o codigo
    private Scanner inputService = new Scanner(System.in);

    private File formulario = null;
    private FileReader formularioFileReader = null;
    private BufferedReader bufferedReader = null;

    // apagar esses de cima depois
    private final String regexAZ = "(?i)[a-zà-ú]+(?:\\s[a-zà-ú]+)+";
    private final String regexSYMBOLS = "[!@#$%¨&*_]";
    private final String regexNUMBER = "\\d";
    private final Pattern patternSYMBOLS = Pattern.compile(regexSYMBOLS);

    public ServiceCLI(RepositoryInterface repository) {
        this.repository = repository; // quem decide qual interface vai usar é o main
    }

    public  void readFileInteger(File file) {
        if (!file.exists()) {
            return;
        }

        // esse aqui é so pra teste e ler o formulario inteiro, sem usar nada para as perguntas
        formulario = new File(file.getAbsolutePath());

        try {
            formularioFileReader = new FileReader(formulario);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        bufferedReader = new BufferedReader(formularioFileReader);

        String linha;

        try {
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao pegar uma linha do cadastro no formulário ");
        }

    }



    public void cadastrarNovoPet(PetDTO petTemporary) {
        Matcher matcherSYMBOLS = patternSYMBOLS.matcher(petTemporary.getNomeSobrenome());
        if (matcherSYMBOLS.find()) {
            throw new NameOrLastNameSpecialCharException("Nome e sobrenome inválidos, contém caracteres especiais!");
        } else if (!petTemporary.getNomeSobrenome().trim().matches(regexAZ)) {
            throw new NameAndLastNameInvalidFormat("Somente o nome foi passado! É necessário um sobrenome!");
        }

        if (Double.parseDouble(petTemporary.getPeso()) > 60 || Double.parseDouble(petTemporary.getPeso()) < 0.5 ) {
            throw new WeightMoreOrLessThanAppropriatedException("Peso inválido");
        }

        if (petTemporary.getIdade() > 20) {
            throw new IdadeException("Idade inválida");
        }

        if (petTemporary.getRaça().trim().matches(regexSYMBOLS) || petTemporary.getRaça().trim().matches(regexNUMBER)) {
            throw new RacaInvalidaException("Raça está contendo números ou caracteres especiais!");
        }

        Pet pet = new Pet(petTemporary.getNomeSobrenome(), petTemporary.getTipoAnimal(), petTemporary.getSexo(),
                petTemporary.getIdade(), petTemporary.getPeso(), petTemporary.getRaça());

        repository.salvar(pet);
    }


}
