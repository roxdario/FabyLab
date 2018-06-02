package seatIn.latoServer;
import java.util.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
public class RmiObserverServer extends Observable implements RmiObservableService, Runnable {
	private static final long serialVersionUID = 1L;
	public RmiObserverServer() {
		new Thread(this).start();
	}
	public void addObserver(RemoteObserver o) throws RemoteException {
		WrappedObserver mo = new WrappedObserver(o);
		addObserver(mo);
		System.out.println("Added observer:" + mo);
	}
	public void run() {
		  while (true) {
			  try { Thread.sleep(5 * 1000);
			  } catch (InterruptedException e) { }
			  setChanged();
			  notifyObservers(new Date());
		  }
	}
	public static void main(String[] args) {
		try {
			RmiObserverServer obj = new RmiObserverServer();
			RmiObservableService stub = (RmiObservableService)
		    UnicastRemoteObject.exportObject(obj, 0);
			Registry registry=LocateRegistry.createRegistry(9999);
			registry.rebind("RmiService", stub);
			System.err.println("Server ready");
		} catch (Exception ex) { }
	}
}