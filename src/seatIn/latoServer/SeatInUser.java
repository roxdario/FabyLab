package seatIn.latoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class SeatInUser extends SeatInPeople implements Serializable{
	private static final long serialVersionUID = 1;
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;

	//public SeatInTeacher(String iD, String name, String surname, String email,
	//			String password, String iDTemp, String stateProfile,
	//			String department) {
	//		super(iD, name, surname, email, password, iDTemp, stateProfile);
	public SeatInUser(String iD, String name, String surname, String email, String password, String IDTemp, String stateProfile) {
		super(iD, name, surname, email, password, IDTemp, stateProfile);

		this.ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.IDTemp = IDTemp;
		this.stateProfile = stateProfile;


	}
	@Override
	public String login() throws RemoteException {
		return super.login();
	}
	

	@Override
	public void setInformation(SeatInPeople s) {
		// TODO Auto-generated method stub
		super.setInformation(s);
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return super.getID();
	}



	@Override
	public void setID(String iD) {
		// TODO Auto-generated method stub
		super.setID(iD);
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}



	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		super.setName(name);
	}



	@Override
	public String getSurname() {
		// TODO Auto-generated method stub
		return super.getSurname();
	}



	@Override
	public void setSurname(String surname) {
		// TODO Auto-generated method stub
		super.setSurname(surname);
	}



	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return super.getEmail();
	}



	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		super.setEmail(email);
	}



	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}



	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		super.setPassword(password);
	}



	@Override
	public String getIDTemp() {
		// TODO Auto-generated method stub
		return super.getIDTemp();
	}



	@Override
	public void setIDTemp(String iDTemp) {
		// TODO Auto-generated method stub
		super.setIDTemp(iDTemp);
	}



	@Override
	public String getStateProfile() {
		// TODO Auto-generated method stub
		return super.getStateProfile();
	}



	@Override
	public void setStateProfile(String stateProfile) {
		// TODO Auto-generated method stub
		super.setStateProfile(stateProfile);
	}

	
	public String isClass() {
		return "SeatInUser";
	}




}




