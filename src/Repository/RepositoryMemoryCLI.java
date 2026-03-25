package Repository;

import models.Pet;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositoryMemoryCLI implements RepositoryInterface {

    // só se preocupar aqui em salvar, validações são todas no service
    private List<Pet> pets = new ArrayList<>();
    private int id = 1;

    @Override
    public void salvar(Pet petService) {

    }

    @Override
    public List<Pet> listarPets() {
        return List.of();
    }

    @Override
    public void atualizar(Pet pet) {

    }

    @Override
    public void deletar(int id) {

    }
}
