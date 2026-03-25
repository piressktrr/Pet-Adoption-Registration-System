import Interface.InterfaceCLI;
import Repository.RepositoryInterface;
import Repository.RepositoryMemoryCLI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RepositoryInterface repository = new RepositoryMemoryCLI(); // só trocar a classe MemoryClI que não da erro
        InterfaceCLI interfaceSystem = new InterfaceCLI();
        interfaceSystem.menu();
    }
}
