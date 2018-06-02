package seatIn.latoServer;

import java.util.*;
import java.io.*;
import java.rmi.RemoteException;
public class WrappedObserver implements Observer,Serializable {
	private static final long serialVersionUID = 1L;
	private RemoteObserver remoteClient = null;
	public WrappedObserver(RemoteObserver ro) { 
		this.remoteClient = ro;
	}

	public void update(Observable o, Object arg) {
		try {
			remoteClient.remoteUpdate(o.toString(), arg);
		}catch (RemoteException e) {
			  o.deleteObserver(this);
		}
	}
}
