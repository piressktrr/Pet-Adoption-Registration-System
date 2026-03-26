package Repository;

import models.Pet;

import java.util.List;

public interface RepositoryInterface {
    void salvar(Pet pet);

    List<Pet> listarPets();
    Pet buscarPetPorId(int id);
    void atualizar(Pet pet);
    void deletar(int id);
}
