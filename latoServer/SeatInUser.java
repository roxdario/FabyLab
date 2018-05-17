package latoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SeatInUser extends SeatInPeople {
	private static final long serialVersionUID = 1;
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	




	private SeatInUser(String iD, String name, String surname, String email,
			String password, String iDTemp, String stateProfile) {
		super();
		ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		IDTemp = iDTemp;
		this.stateProfile = stateProfile;
	}

	public SeatInUser() {
		// TODO Auto-generated constructor stub
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIDTemp() {
		return IDTemp;
	}

	public void setIDTemp(String iDTemp) {
		IDTemp = iDTemp;
	}

	public String getStateProfile() {
		return stateProfile;
	}

	public void setStateProfile(String stateProfile) {
		this.stateProfile = stateProfile;
	}



	

	

}
