package latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class SeatInPeople implements Serializable {
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	
	static SeatInServerInterface stub;
	
	private static final long serialVersionUID = 1;

	public SeatInPeople(String iD, String name, String surname, String email, String password, String iDTemp,
			String stateProfile) {
		super();
		ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		IDTemp = iDTemp;
		this.stateProfile = stateProfile;
	
	}


	public SeatInPeople() {
		// TODO Auto-generated constructor stub
	}

    public  void connection() throws  RemoteException, NotBoundException{
        
        String host="10.24.2.33";
        int port=2099;
        Registry reg=LocateRegistry.getRegistry(host, port);
        stub=(SeatInServerInterface)reg.lookup("classeRemota");
        System.out.println("connesso");
       
    }

	public boolean subscription() throws NotBoundException, IOException{
		if(stub.insertProfileIntoDatabase(this)){
			return true;
		}else{
			return false;
		}
	}
    public String login(String email) throws RemoteException{
    	System.out.println("email:"+this.email);
    	System.out.println("psw:"+this.password);
    	Object x=stub.login(this);
    	SeatInPeople serverUser= (SeatInPeople) x;
    
		System.out.println(x);
		if(x.equals("utente bloccato")){
			return "utente bloccato";
		}else if(x.equals("utente inesistente")){
			return "utente inesistente";
		}else if(x.equals("password errata")){
			return "password errata";
		}else{
			this.getPerson(serverUser);
			
			if( serverUser instanceof SeatInStudent){
				this.getStudent((SeatInStudent) serverUser);
				return "student";
			}else if( serverUser instanceof SeatInTeacher){
				this.getTeacher((SeatInTeacher) serverUser);
				return "teacher";
			}else if( serverUser instanceof SeatInAdmin){
				this.getAdmin((SeatInAdmin) serverUser);
				return "admin";
			}
		}
		return null;	
	
    }
   
   
    public void blockProfile() throws RemoteException {
		stub.updateProfileState(this, "bloccato");
	}
    public boolean checkID(String id){
    	if(this.IDTemp.equals(id)){
    		return true;
    	}else{
    		return false;
    	}
    }
    public void updatePassword(String password) throws IOException {
    	this.setPassword(password);
    	stub.resetPasswordRequest(this);
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


	public void viewProfile() {}
	public  void sendEmail() {}

	public void getPerson(SeatInPeople serverUser) {
		this.setID(serverUser.getID());
		this.setName(serverUser.getName());
		this.setSurname(serverUser.getSurname());
		//this.setEmail(serverUser.getEmail());
		//this.setPassword(serverUser.getPassword());
		//this.setIDTemp(serverUser.getIDTemp());
		this.setStateProfile(serverUser.getStateProfile());
	}
	public void getStudent(SeatInStudent s) {
		
	}
	public void getAdmin(SeatInAdmin a) {
		
	}
	public void getTeacher(SeatInTeacher t) {
		
	}
	
	
	

}

