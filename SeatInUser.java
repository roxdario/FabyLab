package latoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SeatInUser extends SeatInPeople {
	private static final long serialVersionUID = 1;

	public SeatInUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SeatInUser(String iD, String name, String surname, String email, String password, String iDTemp,
			String stateProfile) {
		super(iD, name, surname, email, password, iDTemp, stateProfile);
		// TODO Auto-generated constructor stub
	}


	

	

}
