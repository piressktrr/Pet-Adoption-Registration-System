package Service;

import Repository.RepositoryInterface;
import models.Pet;
import models.PetDTO;

import java.io.*;
import java.util.Scanner;

public class ServiceCLI {
    // fazer todas as validações aqui
    private RepositoryInterface repository; // tipo da interface, pra facilitar no futuro e nao desacoplar o codigo
    private Scanner inputService = new Scanner(System.in);

    private File formulario = null;
    private FileReader formularioFileReader = null;

    private BufferedReader bufferedReader = null;

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

        Pet pet = new Pet(petTemporary.getNomeSobrenome(), petTemporary.getTipoAnimal(), petTemporary.getSexo(),
                petTemporary.getIdade(), petTemporary.getPeso(), petTemporary.getRaça());

        repository.salvar(pet);
    }


}
