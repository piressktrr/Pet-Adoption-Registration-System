package Repository;

import models.Pet;

import java.io.File;
import java.util.List;

public interface RepositoryInterface {
    void salvar(Pet pet);
    List<Pet> listarPets();
    List<String> listarPetsString();
    void petsCadastradosPasta(Pet pet, String conteudo);

    String lerArquivo(File arquivo);

    List<String> buscarPetPorNome(String nomePet);
    List<String> buscarPetPorIdade(double idadePet);
    List<String> buscarPetPorPeso(double pesoPet);
    List<String> buscarPetPorRaca(String racaPet);
    List<String> buscarPetPorSexo(String sexoPet);


    void atualizar(Pet pet);
    void deletar(int id);
}
