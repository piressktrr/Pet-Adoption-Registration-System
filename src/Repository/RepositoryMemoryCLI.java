package Repository;

import models.Pet;


import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        for (Pet pet : this.pets) {
            System.out.println(pet);
        }
        return null;
    }

    @Override
    public List<String> listarPetsString() {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultado = new ArrayList<>();

        if (arquivos == null) {
            return null;
        }

        for (File file : arquivos) {
            resultado.add(lerArquivo(file));
        }
        return resultado;
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
                if (sexo != null && sexo.contains(sexoPet.toUpperCase())) {
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

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivo))) {
                for(int i = 0; i < 4; i++) {
                    bufferedReader.readLine();
                }
                String idade = bufferedReader.readLine();
                if (idade != null && idade.contains(Double.toString(idadePet))) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return resultadoTerminal;
    }

    @Override
    public List<String> buscarPetPorPeso(double pesoPet) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return null;

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivo))) {
                for(int i = 0; i < 5; i++) {
                    bufferedReader.readLine();
                }
                String peso = bufferedReader.readLine();
                if (peso != null && peso.contains(Double.toString(pesoPet))) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return resultadoTerminal;
    }

    @Override
    public List<String> buscarPetPorRaca(String racaPet) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return null;

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivo))) {
                for(int i = 0; i < 6; i++) {
                    bufferedReader.readLine();
                }
                String raca = bufferedReader.readLine();
                if (raca != null && raca.contains(racaPet)) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return resultadoTerminal;
    }


}
