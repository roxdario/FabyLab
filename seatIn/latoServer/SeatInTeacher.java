package seatIn.latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;



public class SeatInTeacher extends SeatInUser {
	static final String user="T";
	private static final long serialVersionUID = 1;
	
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	private String department;

	public SeatInTeacher(String iD, String name, String surname, String email, String password, String IDTemp, String state, String department) {
		super(iD, name, surname, email, password, IDTemp, state);
		this.department = department;
		this.ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.IDTemp = IDTemp;
		this.stateProfile = state;

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
	 public void setDepartment(String department) {
	        this.department = department;
	    }
	  public String getDepartment() {

	        return department;
	    }

	@Override
	public String login() throws RemoteException {
		System.out.println("email:"+this.email);
    	System.out.println("psw:"+this.password);
    	Object serverUser=stub.login(this);
    	System.out.println(serverUser);
	
		 switch (serverUser.toString()) {
		 case "utente bloccato":
             return "bloccato";
         case "utente inesistente":
             return "utente inesistente";
         case "password errata":
             return "password errata";
         case "not found":
        	 return "not found";
         default:
        	 SeatInTeacher User= (SeatInTeacher) serverUser;
        	 this.setInformation(User);
        	 return "teacher";
		}
	}
	
	public void setInformation(SeatInTeacher serverUser) {
		this.setID(serverUser.getID());
		this.setIDTemp(serverUser.getIDTemp());
		this.setName(serverUser.getName());
		this.setSurname(serverUser.getSurname());
		this.setStateProfile(serverUser.getStateProfile());
		this.setDepartment(serverUser.getDepartment());	
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

	public List<String> teacherEmailForStudentEmailSender(String courseCode, int courseYear) throws RemoteException, SQLException {
		// TODO Auto-generated method stub
		return stub.teacherEmailForStudentEmailSender(courseCode, courseYear);
	}

	public List<String> getCoursesTeached() throws RemoteException, SQLException {
		return stub.getCoursesTeached(this.getEmail());
	}
	public List<String>getStudentsEmailforNewsletter(String courseName, int year) throws RemoteException, SQLException{
		return stub.getStudentsEmailforNewsletter( courseName, year);
	}

	public String isClass() {
		return "SeatInTeacher";
	}
	

}
