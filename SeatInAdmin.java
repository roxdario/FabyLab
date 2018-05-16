package latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
	


	public SeatInAdmin(String iD, String name, String surname, String email, String password, String iDTemp,
			String stateProfile, String department) {
		super();
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
	public void getAdmin(SeatInAdmin a) {
		super.getAdmin(a);
		this.setDepartment(a.getDepartment());
	}
	
	public void downloadFile(String pathName){}
	public void submit(String courseName){}
	public void uploadFile(String pathName){}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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
