package seatIn.latoServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RmiObservableService extends Remote {
  void addObserver(RemoteObserver o)throws RemoteException;
}