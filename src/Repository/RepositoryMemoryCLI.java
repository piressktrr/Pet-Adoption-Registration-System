package Repository;

import Models.Endereco;
import Models.Pet;
import Models.PetDTOAtualizar;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;

public class RepositoryMemoryCLI implements RepositoryInterface {

    private List<Pet> pets = new ArrayList<>();

    @Override
    public void petsCadastradosPasta(Pet pet, String conteudo) {
        File pastaPetsCadastrados = new File("petsCadastrados");
        pastaPetsCadastrados.mkdirs();

        String petTemp = pet.getNomeSobrenome().toUpperCase().replaceAll(" ", "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String time = LocalDateTime.now().format(formatter);

        File arquivoPet = new File(pastaPetsCadastrados,  time + "-" + petTemp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoPet, true))) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.out.println("Erro! "+e.getMessage());
        }

    }

    @Override
    public void salvar(Pet pet) {
        pets.add(pet);

        String conteudo = "1 - " + pet.getNomeSobrenome() +
                "\n2 - " + pet.getTipoAnimal() +
                "\n3 - " + pet.getSexo() +
                "\n4 - " + pet.getEndereco().getRua() +
                ", " + pet.getEndereco().getNumeroCasa() +
                ", " + pet.getEndereco().getCidade() +
                "\n5 - " + pet.getIdade() + " anos \n" +
                "6 - " + pet.getPeso() + "KG\n" +
                "7 - " +pet.getRaça();

        petsCadastradosPasta(pet, conteudo);
    }

    @Override
    public List<Pet> listarPets() {
        for (Pet pet : this.pets) {
            System.out.println(pet);
        }
        return this.pets;
    }

    @Override
    public List<String> listarPetsString() {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultado = new ArrayList<>();

        if (arquivos == null) {
            return resultado;
        }

        for (File file : arquivos) {
            if (file.isFile())
                resultado.add(lerArquivo(file));
        }
        return resultado;
    }

    @Override
    public void atualizar(int indice, PetDTOAtualizar atualizar, Endereco endereco) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        if (arquivos == null) {
            System.out.println("Erro: pasta petsCadastrados não encontrada ou vazia.");
            return;
        }

        File file = arquivos[indice];

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String nome     = br.readLine().replaceFirst("^\\d+ - ", "");
            String tipo     = br.readLine().replaceFirst("^\\d+ - ", "");
            String sexo     = br.readLine().replaceFirst("^\\d+ - ", "");
            String enderecoA = br.readLine().replaceFirst("^\\d+ - ", "");
            String idade    = br.readLine().replaceFirst("^\\d+ - ", "");
            String peso     = br.readLine().replaceFirst("^\\d+ - ", "");
            String raca     = br.readLine().replaceFirst("^\\d+ - ", "");

            if (atualizar.getNomeSobrenome()  != null) nome  = atualizar.getNomeSobrenome();

            if (atualizar.getEndereco() != null
                    && endereco.getNumeroCasa() != 0
                    && endereco.getCidade() != null
                    && endereco.getRua() != null) {
                enderecoA = atualizar.getEndereco().getRua() + ", "
                        + atualizar.getEndereco().getNumeroCasa() + ", "
                        + atualizar.getEndereco().getCidade();
            }

            if (atualizar.getIdade() != 0) idade = String.valueOf(atualizar.getIdade());
            if (atualizar.getPeso()  != 0) peso  = String.valueOf(atualizar.getPeso());
            if (atualizar.getRaça()  != null) raca  = atualizar.getRaça();


            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("1 - " + nome);     bw.newLine();
                bw.write("2 - " + tipo);     bw.newLine();
                bw.write("3 - " + sexo);     bw.newLine();
                bw.write("4 - " + enderecoA);     bw.newLine();
                bw.write("5 - " + idade + " ANOS");    bw.newLine();
                bw.write("6 - " + peso + "KG");     bw.newLine();
                bw.write("7 - " + raca);     bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar arquivo.: "  + e.getMessage());
        }
    }

    @Override
    public boolean deletar(int indice) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        if (arquivos == null) {
            return false;
        }
        return arquivos[indice].delete();
    }

    @Override
    public List<String> buscarPetPorNome(String nomePet) {
        StringBuilder stringBuilder;
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return null;

        for (File arquivo : arquivos) {
            if (!arquivo.isFile()) continue;
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String primeiraLinha = br.readLine();
                    if (primeiraLinha != null && primeiraLinha.toLowerCase().contains(nomePet.toLowerCase())) {
                        resultadoTerminal.add(lerArquivo(arquivo));
                    }
            } catch (IOException e) {
                System.out.println("Erro! "+e.getMessage());
            }
        }
        return resultadoTerminal;
    }

    @Override
    public List<String> buscarPetPorTipoAnimal(String tipoAnimal) {
        File[] arquivos = new File("petsCadastrados").listFiles();
        List<String> resultadoTerminal = new ArrayList<>();
        if (arquivos == null) return resultadoTerminal;

        for (File arquivo : arquivos) {
            if (!arquivo.isFile()) continue;
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                br.readLine();
                String tipo = br.readLine();
                if (tipo != null && tipo.toUpperCase().contains(tipoAnimal.toUpperCase())) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println("Erro! " + e.getMessage());
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
                sb.append("\n");
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
        if (arquivos == null) return resultadoTerminal;

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new  BufferedReader(new FileReader(arquivo))) {
                bufferedReader.readLine();
                bufferedReader.readLine();
                String sexo = bufferedReader.readLine();
                if (sexo != null && sexo.toUpperCase().contains(sexoPet.toUpperCase())) {
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
        if (arquivos == null) return resultadoTerminal;

        for (File arquivo : arquivos) {
            if (!arquivo.isFile()) continue;

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
        if (arquivos == null) return resultadoTerminal;

        for (File arquivo : arquivos) {
            if (!arquivo.isFile()) continue;

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
        if (arquivos == null) return resultadoTerminal;

        for (File arquivo : arquivos) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivo))) {
                for(int i = 0; i < 6; i++) {
                    bufferedReader.readLine();
                }
                String raca = bufferedReader.readLine();
                if (raca != null && raca.toLowerCase().contains(racaPet.toLowerCase())) {
                    resultadoTerminal.add(lerArquivo(arquivo));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return resultadoTerminal;
    }


}
