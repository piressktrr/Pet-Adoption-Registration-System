package Repository;

import models.Pet;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class RepositoryMemoryCLI implements RepositoryInterface {

    // só se preocupar aqui em salvar, validações são todas no service
    private List<Pet> pets = new ArrayList<>();
    private int id = 1;


    @Override
    public void petsCadastradosPasta(Pet pet, String conteudo) {

        File pastaPetsCadastrados = new File("petsCadastrados");
        pastaPetsCadastrados.mkdirs();

        String petTemp = pet.getNomeSobrenome().toUpperCase().replaceAll(" ", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        File arquivoPet = new File(pastaPetsCadastrados,  LocalDate.now().format(formatter)
                + "T"
                + LocalTime.now().getHour()
                + LocalTime.now().getMinute()
                + petTemp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoPet, true))) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.out.println("Erro! "+e.getMessage());
        }

    }

    @Override
    public void salvar(Pet pet) {
        pet.setId(this.id++);
        pets.add(pet);
        String conteudo = "1 - " + pet.getNomeSobrenome() + "\n2 - " + pet.getTipoAnimal() +
                "\n3 - " + pet.getSexo() +
                "\n4 - " + pet.getEndereco().getRua() + ", " + pet.getEndereco().getNumeroCasa() +
                ", " + pet.getEndereco().getCidade() + "\n5 - " + pet.getIdade() + " anos \n" +
                "6 - " + pet.getPeso() + "KG\n" + "7 - " +pet.getRaça();

        petsCadastradosPasta(pet, conteudo);
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
