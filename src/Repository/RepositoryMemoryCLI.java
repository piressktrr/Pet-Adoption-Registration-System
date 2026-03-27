package Repository;

import models.Pet;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RepositoryMemoryCLI implements RepositoryInterface {

    // só se preocupar aqui em salvar, validações são todas no service
    private List<Pet> pets = new ArrayList<>();
    private int id = 1;

    @Override
    public void salvar(Pet pet) {
        pet.setId(this.id++);
        pets.add(pet);

    }

    @Override
    public List<Pet> listarPets() {
        return Collections.unmodifiableList(pets);
    }

    @Override
    public void atualizar(Pet pet) {

    }

    @Override
    public void deletar(int id) {

    }

    @Override
    public Pet buscarPetPorId(int id) {
        return null;
    }
}
