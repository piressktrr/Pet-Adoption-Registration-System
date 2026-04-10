import Interface.InterfaceCLI;
import Repository.RepositoryInterface;
import Repository.RepositoryMemoryCLI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RepositoryInterface repository = new RepositoryMemoryCLI();
        InterfaceCLI interfaceSystem = new InterfaceCLI(repository);
        interfaceSystem.menu();

    }
}
