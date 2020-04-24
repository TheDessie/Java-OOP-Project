package server.func;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Tokenizable extends Remote {

    String getCardToken(String cardNumber) throws RemoteException;

    String getCardNumber(String cardToken) throws RemoteException;

    boolean isAuthorised(String accessType) throws RemoteException;

    boolean registerUser(String name, String password, boolean canRegisterToken, boolean canGetCardNumber) throws RemoteException;

    boolean login(String name, String password) throws RemoteException;

    List<String> report(String orderBy) throws RemoteException;

}
