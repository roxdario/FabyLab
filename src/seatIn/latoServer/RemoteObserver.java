package seatIn.latoServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RemoteObserver extends Remote {
  void remoteUpdate(Object observable, Object arg)throws RemoteException;
}


