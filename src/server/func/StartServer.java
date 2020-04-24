package server.func;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartServer {


    public static void main(String[] args) {

        String REMOTE_CARD_OBJ_NAME = "CardToken";
        try {
            Tokenizable o = new TokenizableImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(REMOTE_CARD_OBJ_NAME, o);
            System.out.printf("Remote object named: %s is registered. Registry is running ...%n", REMOTE_CARD_OBJ_NAME);

        }
        catch (RemoteException ex) {
            System.out.printf("Error! Cannot register remote object: %s %n", REMOTE_CARD_OBJ_NAME);
        }
    }
}
