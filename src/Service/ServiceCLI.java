package Service;

import java.io.*;
import java.util.Scanner;

public class ServiceCLI {
    private Scanner inputService = new Scanner(System.in);
    private File formulario = null;
    private FileReader formularioFileReader = null;

    private BufferedReader bufferedReader = null;

    public  void readFileInteger(File file) {
        // esse aqui é so pra teste e ler o formulario inteiro, sem usar nada para as perguntas
        formulario = new File(file.getAbsolutePath());

        try {
            formularioFileReader = new FileReader(formulario);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        bufferedReader = new BufferedReader(formularioFileReader);

        String linha;

        try {
            while ((linha = bufferedReader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao pegar uma linha do cadastro no formulário ");
        }

    }


    public void cadastrarNovoPet(File file) {
        formulario = new File(file.getAbsolutePath());
        try {
            formularioFileReader = new FileReader(formulario);
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao ler o formulario com as perguntas!! ");
        }

        bufferedReader = new BufferedReader(formularioFileReader);

        String linha;

        try {
            while ((linha = bufferedReader.readLine()) != null) {
                if (linha.startsWith("=") || linha.isEmpty()) {
                    continue;
                }
                System.out.println(linha);
                inputService.next();
            }
        } catch (IOException e) {
            System.out.println("Erro ao pegar uma linha do cadastro no formulário ");
        }

    }


}
