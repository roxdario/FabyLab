package latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SeatInTeacher extends SeatInUser implements Runnable{
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
	
	
	


	public SeatInTeacher(String iD, String name, String surname, String email, String password, String iDTemp,
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

	public void run() {
		try{
			//this.login(user);
		} catch(Exception e) {}
	}
	
	public void downloadFile(String pathName){}
	public void submit(String courseName){}
	public void uploadFile(String pathName){}
	
	

}
