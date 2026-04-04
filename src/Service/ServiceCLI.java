package Service;

import Repository.RepositoryInterface;
import exceptions.*;
import models.Pet;
import models.PetDTO;


import java.io.*;
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
            System.out.println(e.getMessage());
        }

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


        if (opcao.equalsIgnoreCase("nome")) {
                List<String> petsTextoNome = repository.buscarPetPorNome(atributo);
                if (petsTextoNome.isEmpty()) {
                    return;
                }
                for (String s : petsTextoNome) {
                    System.out.println(s);
                }
            } else if (opcao.equalsIgnoreCase("sexo")) {
                List<String> petsTextoSexo = repository.buscarPetPorSexo(atributo);
                if (petsTextoSexo.isEmpty()) {
                    return;
                }
                for (String s : petsTextoSexo) {
                    System.out.println(s);
                }
            }
    }
}
