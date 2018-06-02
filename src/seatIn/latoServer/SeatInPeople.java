package seatIn.latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;


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
    public SeatInPeople(String iD, String name, String surname, String email, String password, String iDTemp,
                        String stateProfile) {
        super();
        this.ID = iD;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.IDTemp = iDTemp;
        this.stateProfile = stateProfile;
    }

    public  void connection() throws  RemoteException, NotBoundException{
        String host="192.168.1.9";
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
        case "not found":
       	 return "not found";
        default:
			return "errore";
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

	public boolean subscription() throws NotBoundException, IOException, MessagingException{
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
    		return true;
    	}else{
    		return false;
    	}
    }
    public void updatePasswordandProfile(String password) throws IOException, MessagingException {
    	this.setPassword(password);
    	stub.resetPasswordRequest(this);
    	this.setStateProfile("attivo");
    	stub.updateProfileState(this);
	}
	

	public void getPerson(SeatInPeople serverUser) {
		this.setID(serverUser.getID());
		this.setName(serverUser.getName());
		this.setSurname(serverUser.getSurname());
		//this.setEmail(serverUser.getEmail());
		//this.setPassword(serverUser.getPassword());
		//this.setIDTemp(serverUser.getIDTemp());
		this.setStateProfile(serverUser.getStateProfile());
	}
	
	
	public boolean updatePassword() throws RemoteException, MessagingException {
		return stub.resetPasswordRequest(this);
		
	}

	public void logout() throws RemoteException {
		stub.logout(this.getEmail());
	}

	public void changeProfileRequest(String email, String fieldToChange, String newParameter, String tipology) throws RemoteException {
		stub.changeProfileRequest(email, fieldToChange, newParameter, tipology);
	}
	public boolean sendSblockRequest() throws RemoteException {
		this.setStateProfile("non attivo");
    	stub.updateProfileState(this);
    	if(stub.resetPasswordRequest(this)){
    		return true;
    	}else{
    		return false;
    	}
		
	}
	
	public List<String[]> teacherTeachesCourse (String code, int year)  {
		try {
			return stub.teacherTeachesCourse(code,year);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object passwordForgotten(String email) throws SQLException, RemoteException, MessagingException{
		return stub.passwordForgotten(email);
	}
	public boolean sendEmailForNewsletter(String user, String password, List<String> toList, String subject, String body) throws RemoteException, MessagingException{
		return stub.sendEmailForNewsletter(user, password, toList, subject, body);
	}
	public boolean sendEmail (String to, String password, String object, String body) throws RemoteException, MessagingException{
		return stub.sendEmail(this.getEmail(), to, password, object, body);
		
	}
	public byte[] getFile( String fileCode, String courseCode, int courseYear) throws IOException, SQLException, RemoteException{
	    return  stub.getFile(fileCode, this.getEmail() , courseYear,  courseCode);

    }
    public ArrayList<String> findFolder(String code) throws SQLException, RemoteException {
	    return stub.findInFolder(code);
    }

    public  String findFileName(String code) throws SQLException, RemoteException {
	    return stub.findFileName(code);
    }
    public List<String[]> viewAllCourseInformation() throws RemoteException {
		return stub.viewAllCourseInformation();
	}
    public List<String[]> showCourseForStudyPlan() throws RemoteException, SQLException {
		return stub.showCourseFromStudyPlan(this.getEmail());
	}

    public Object profileInfo() throws RemoteException, SQLException {
	    return stub.profileInformation(this);

    }
	public boolean sendSblockRequest(String email, String fieldToChange, String newParameter, String tipology) throws RemoteException {
		 if(stub.changeProfileRequest(email, fieldToChange, newParameter, tipology)){
			 return true;
		 }else{
			 return false;
		 }
		 
	}
	public Node createCourseTree(String code, int year) throws SQLException, RemoteException {
		return stub.createCourseTree(code,year);
	}
	public List<String[]> allCourse() throws RemoteException, SQLException {
		return stub.allCourse();
	}

    public abstract String isClass();
 
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
    public void setStateProfile(String stateProfile) {
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
    public String getStateProfile() {
        return stateProfile;
    }
	
  

	public void courseSubscription(List<String> teacherEmail,String courseYear, String courseCode) throws MessagingException, RemoteException {
		stub.courseSubscription(this.getEmail(),courseCode,Integer.parseInt(courseYear),teacherEmail);
	}

	public void registerUserIsConsultingCourse( String code, int courseYear, String profile) throws RemoteException {
	stub.registerUserIsConsultingCourse(this.getEmail(),code,courseYear,profile);}

	public void registerUserLogoutFromCourse( String code, int courseYear) throws RemoteException{
	stub.registerUserLogoutFromCourse(this.getEmail(),code,courseYear);
	}
	public List<String[]> getInfoCoursesTeached()throws RemoteException, SQLException{
		return stub.getInfoCoursesTeached(this.getEmail());
	}
    public List<String[]> courseNotInStudyPlanTeacher() throws SQLException, RemoteException {
    	return stub.courseNotInStudyPlanTeacher(this.getEmail());
    }


}

