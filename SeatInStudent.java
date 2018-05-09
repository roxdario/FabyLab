package latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SeatInStudent extends SeatInUser  implements Serializable {
	static final String user="S";
	private static final long serialVersionUID = 1;
	
	private String ID;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String IDTemp;
	private String stateProfile;
	private int enrollmentYear;
	private int courseYear;
	private String courseState;
	private String degreeCourse; //name+IDcourse
	
	
	

	
	



	public SeatInStudent(String iD, String name, String surname, String email, String password, String iDTemp,
			String stateProfile, int enrollmentYear, int courseYear, String courseState, String degreeCourse) {
		super();
		ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		IDTemp = iDTemp;
		this.stateProfile = stateProfile;
		this.enrollmentYear = enrollmentYear;
		this.courseYear = courseYear;
		this.courseState = courseState;
		this.degreeCourse = degreeCourse;
	}
	public SeatInStudent() {
		// TODO Auto-generated constructor stub
	}
	public void downloadFile(String pathName){}
	public void submit(String courseName){}
	


	@Override
	public void subscription() throws NotBoundException, IOException {
		super.subscription();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
/*		System.out.println("matricola:");
    	this.ID=in.readLine();
		System.out.println("nome:");
    	this.name=in.readLine();
    	System.out.println("cognome:");
    	this.surname=in.readLine();
		System.out.println("email:");
    	this.email=in.readLine();
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Anno di iscrizione:");
		this.enrollmentYear = Integer.parseInt(in.readLine());
		System.out.println("Anno corso");
		this.courseYear = Integer.parseInt(in.readLine());
		System.out.println("Sei in corso?");
    	this.courseState = in.readLine();
		System.out.println("Corso di laurea");
    	this.degreeCourse = in.readLine();*/
    	this.email="k";
    	this.password="ciao5";
    
    	
			System.out.println(stub.login(this));
		
  /*  	if(stub.insertProfileIntoDatabase(this)){
			System.out.println("Inserito correttamente");
		}else{
		System.err.println("Inserimento fallito!!");
		}
    	
    	SeatInAdmin admin=new SeatInAdmin("49","rr2", "r3", "zi4oo","5", "6", "7", "dista");
    	System.out.println(admin);
    	if(stub.insertProfileIntoDatabase(admin)){
			System.out.println("Inserito correttamente");
		}else{
			System.err.println("Inserimento fallito!!");
		}
    	SeatInTeacher t=new SeatInTeacher("6646","2", "3", "ziodocentemerda4","5", "6", "7", "dista");
    	System.out.println(admin);
    	if(stub.insertProfileIntoDatabase(t)){
			System.out.println("Inserito correttamente");
		}else{
			System.err.println("Inserimento fallito!!");
		}
    	*/
    	
    	//super.sendDate(this);
	}
	

}
