package seatIn.latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public abstract class  SeatInPeople implements Serializable {
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	
	static SeatInServerInterface stub;
	private static final long serialVersionUID = 1;


	public SeatInPeople(){
	}
	public SeatInPeople(String iD, String name, String surname, String email,
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


    public  void connection() throws  RemoteException, NotBoundException{
        String host="192.168.1.83";
        int port=2099;
        Registry reg=LocateRegistry.getRegistry(host, port);
        stub=(SeatInServerInterface)reg.lookup("classeRemota");
        System.out.println("connesso");
       
    }
	
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
 			return "people";
		 }
     }
	public void setInformation(SeatInPeople s) {
		this.setID(s.getID());
	//	this.setIDTemp(s.getIDTemp());
		this.setName(s.getName());
		this.setSurname(s.getSurname());
	//	this.setEmail(s.getEmail());
	//	this.setPassword(s.getPassword());
		this.setStateProfile(s.getStateProfile());	
	}

	public boolean subscription() throws NotBoundException, IOException{
		if(stub.insertProfileIntoDatabase(this)){
			return true;
		}else{
			return false;
		}
	}
    
   
   
    public void blockProfile() throws RemoteException {
    	this.setStateProfile("bloccato");
		stub.updateProfileState(this);
	}
    public boolean checkID(String id){
    	if(this.IDTemp.equals(id)){
    		return false;
    	}else{
    		return true;
    	}
    }
    public void updatePassword(String password) throws IOException {
    	this.setPassword(password);
    	stub.resetPasswordRequest(this);
    	this.setStateProfile("attivo");
    	stub.updateProfileState(this);
	}
	

    public String getStateProfile() {
        return stateProfile;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIDTemp(String IDTemp) {
        this.IDTemp = IDTemp;
    }
    protected void setStateProfile(String stateProfile) {
  		this.stateProfile=stateProfile;
  		
  	}

    public String getID() {

        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIDTemp() {
        return IDTemp;
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
	
	
	public void updatePassword() throws RemoteException {
		stub.resetPasswordRequest(this);
		
	}
	
	
	

}

