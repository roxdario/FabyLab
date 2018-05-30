package seatIn.latoServer;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class SeatInAdmin extends SeatInPeople {

	static final String user="A";
	private static final long serialVersionUID = 1;
	
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	private String department;

	public SeatInAdmin(String iD, String name, String surname, String email,
			String password, String iDTemp, String stateProfile,
			String department) {
		 super(iD, name, surname, email, password, iDTemp, stateProfile);
		ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		IDTemp = iDTemp;
		this.stateProfile = stateProfile;
		this.department = department;
	}


	@Override
	public String login() throws RemoteException {
		System.out.println("email:"+this.email);
    	System.out.println("psw:"+this.password);
    	Object serverUser=stub.login(this);
    	System.out.println(serverUser);
	
		 switch (serverUser.toString()) {
         case "utente bloccato":
             return "utente bloccato";
         case "utente inesistente":
             return "utente inesistente";
         case "password errata":
             return "password errata";
         default:
        	 SeatInAdmin User= (SeatInAdmin) serverUser;
        	 this.setInformation(User);
        	 return "admin";
		}
	}
	
	public void setInformation(SeatInAdmin serverUser) {
		this.setID(serverUser.getID());
		this.setIDTemp(serverUser.getIDTemp());
		this.setName(serverUser.getName());
		this.setSurname(serverUser.getSurname());
	//	this.setEmail(serverUser.getEmail());
	//	this.setPassword(serverUser.getPassword());
		this.setStateProfile(serverUser.getStateProfile());
		this.setDepartment(serverUser.getDepartment());	
	}
	public    ArrayList<String[]> showCourse() {
		// TODO Auto-generated method stub
		return null;
	}

	public void downloadFile(String pathName){}
	public void submit(String courseName){}
	public void uploadFile(String pathName){}
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
	 public void setDepartment(String department) {
	        this.department = department;
	    }
	  public String getDepartment() {

	        return department;
	    }

}
