package Repository;

import models.Pet;

import java.util.List;

public interface RepositoryInterface {
    void salvar(Pet petService);

    List<Pet> listarPets();

    void atualizar(Pet pet);
    void deletar(int id);
}
