package latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public abstract class SeatInPeople implements Serializable {
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
        
        String host="10.24.5.233";
        int port=1099;
        Registry reg=LocateRegistry.getRegistry(host, port);
        
      //  SeatInServerInterface stub=SeatInPeople.getStub();
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
    public boolean login(String email) throws RemoteException{
    	System.out.println("email:"+this.email);
    	System.out.println("psw:"+this.password);
    	Object x=stub.login(this);

		System.out.println(x);
		if(x.equals("utente bloccato")){
			return false;
		}else if(x.equals("utente inesistente") || x.equals("password errata")){
			return false;
		}else{
			return true;
		}
		
	
    }
    
    public void blockProfile() throws RemoteException {
		stub.updateProfileState(this, "bloccato");
	}
    public void updatePassword() throws IOException {
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 		System.out.println("password:");
    	String tempPassword=in.readLine();
    	this.setPassword(tempPassword);
    	System.out.println("codice di attivazione:");
    	String tempCode=in.readLine();
    	if(this.IDTemp.equals(tempCode)){
    		stub.resetPasswordRequest(this);
    	}else
    		System.out.println("ritenta");
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
	
	
	

}

