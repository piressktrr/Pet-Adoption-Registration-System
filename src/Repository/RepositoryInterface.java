package Repository;

import models.Pet;

import java.io.File;
import java.util.List;

public interface RepositoryInterface {
    void salvar(Pet pet);
    List<Pet> listarPets();
    void petsCadastradosPasta(Pet pet, String conteudo);

    Pet buscarPetPorId(int id);

    void atualizar(Pet pet);
    void deletar(int id);
}
