package Repository;

import models.Pet;


import java.io.*;
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
        return pets;
    }

    @Override
    public void atualizar(Pet pet) {

    }

    @Override
    public void deletar(int id) {

    }

    @Override
    public List<String> buscarPetPorNome(String nomePet) {
        StringBuilder stringBuilder;
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();

        if (arquivos == null) return null;

        for (File arquivo : arquivos) {
                try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                    String primeiraLinha = br.readLine();

                    if (primeiraLinha.contains(nomePet)) {
                        stringBuilder = new StringBuilder(primeiraLinha);
                        String linha;
                        while ((linha = br.readLine()) != null) {
                            stringBuilder.append(linha.replaceAll("[(0-9)]+ - ", " - "));
                        }
                        resultadoTerminal.add(stringBuilder.toString());
                    }
                } catch (IOException e) {
                     System.out.println("Erro! "+e.getMessage());
                }
            }

            return resultadoTerminal;
    }

    @Override
    public String lerArquivo(File arquivo) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha.replaceAll("[(0-9)]+ - ", " - "));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }


    @Override
    public List<String> buscarPetPorSexo(String sexoPet) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return null;

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new  BufferedReader(new FileReader(arquivo))) {
                bufferedReader.readLine();
                bufferedReader.readLine();
                String sexo = bufferedReader.readLine();
                if (sexo.equalsIgnoreCase(sexoPet)) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return resultadoTerminal;

    }

    @Override
    public List<String> buscarPetPorIdade(double idadePet) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return null;


        return List.of();
    }

    @Override
    public List<String> buscarPetPorPeso(double pesoPet) {
        return List.of();
    }

    @Override
    public List<String> buscarPetPorRaca(String racaPet) {
        return List.of();
    }


}
