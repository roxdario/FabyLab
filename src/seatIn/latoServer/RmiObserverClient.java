package seatIn.latoServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class RmiObserverClient extends UnicastRemoteObject implements RemoteObserver {
	private static final long serialVersionUID = 1L;
	protected RmiObserverClient() throws RemoteException {
		super();
	}
	public static void main(String[] args) {
	  try {
		  Registry registry = LocateRegistry.getRegistry(9999);
		  RmiObservableService remoteService =(RmiObservableService) registry.lookup("RmiService");
		  RmiObserverClient client = new RmiObserverClient();
		  remoteService.addObserver(client);
	  } catch (Exception ex) {
		  ex.printStackTrace();
	  }
	}
	public void remoteUpdate(Object observable, Object arg) throws RemoteException {
	  String updateMsg=null;
	  System.out.println("got message:" + updateMsg);
	}
}
