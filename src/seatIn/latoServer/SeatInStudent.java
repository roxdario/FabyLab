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
import java.util.Collection;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class SeatInStudent extends SeatInUser  implements Serializable {
	static final String user="S";
	private static final long serialVersionUID = 1;


	//private static final SeatInStudent instance = new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
	
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
	private String degreeCourse;

	public SeatInStudent(String iD, String name, String surname, String email, String password, String iDTemp,
						 String stateProfile, int enrollmentYear, int courseYear, String courseState, String degreeCourse) {
		super(iD, name, surname, email, password, iDTemp, stateProfile);
		this.enrollmentYear = enrollmentYear;
		this.courseYear = courseYear;
		this.courseState = courseState;
		this.degreeCourse = degreeCourse;
		this.ID = iD;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.IDTemp = iDTemp;
		this.stateProfile = stateProfile;

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
	         case "not found":
	           	 return "not found";
	         default:
	        	 SeatInStudent User= (SeatInStudent) serverUser;
	        	 this.setInformation(User);
 			return "student";
		 }
	}


	public void setInformation(SeatInStudent s) {
		this.setID(s.getID());
		this.setIDTemp(s.getIDTemp());
		this.setName(s.getName());
		this.setSurname(s.getSurname());
		this.setStateProfile(s.getStateProfile());
		this.setCourseState(s.getCourseState());
		this.setDegreeCourse(s.getDegreeCourse());
		this.setEnrollmentYear(s.getEnrollmentYear());
		this.setCourseYear(s.getCourseYear());
		
	}


	public int getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(int enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public int getCourseYear() {
		return courseYear;
	}

	public void setCourseYear(int courseYear) {
		this.courseYear = courseYear;
	}

	public String getCourseState() {
		return courseState;
	}

	public void setCourseState(String courseState) {
		this.courseState = courseState;
	}
	public String getDegreeCourse() {
		return degreeCourse;
	}

	public void setDegreeCourse(String degreeCourse) {
		this.degreeCourse = degreeCourse;
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
	
	

//	public Node createCourseTreesss(String info ,int year) throws RemoteException, SQLException{
	//	return stub.createCourseTree(info, year);
//	}


	public List<String[]> studentDegreeWithoutStudentOne() throws SQLException, RemoteException {
		return stub.courseDegreeWithoutStudentOne(this.getDegreeCourse());
	}


	public List<String[]> courseNotInStudyPlan(String code) throws SQLException, RemoteException {
		return stub.courseNotInStudyPlan(code,  this.enrollmentYear);
	}
	

	
	public  List<String> teacherEmailForStudentEmailSender(String code, int year) throws SQLException, RemoteException {
		// TODO Auto-generated method stub
		return stub.teacherEmailForStudentEmailSender(code,year);
	}
	
	public String isClass() {
		return "SeatInStudent";
	}
	public String[] compare (SeatInStudent profile)
	{
		//Check (campoDiModifica in ('nome', 'cognome', 'matricola', 'dipartimento', 'corsoDiLaurea', 'email', 'annoIscrizione', 'annoCorso', 'statoCorso')))
		//
		//
		//alter table richiestaDiModifica add column tipologia varchar(30) check (tipologia in ('studente','docente','amministratore'))

		String []temp=new String[2];
		temp[1]=temp[0]="NONE";
		if (!this.getName().equals(profile.getName())){
			temp[0]="nome";
			temp[1]=profile.getName();
		}
		if (!this.getSurname().equals(profile.getSurname())){
			temp[0]="cognome";
			temp[1]=profile.getSurname();
		}
		if (!this.getID().equals(profile.getID())){
			temp[0]="matricola";
			temp[1]=profile.getID();
		}
		if (!this.getEmail().equals(profile.getEmail())){
			temp[0]="email";
			temp[1]=profile.getEmail();

		}
		if (this.getEnrollmentYear()!=(profile.getEnrollmentYear())){
			temp[0]="annoIscrizione";
			temp[1]=String.valueOf(profile.getEnrollmentYear());
		}

		if (!this.getDegreeCourse().equals(profile.getDegreeCourse())){
			temp[0]="corsodilaurea";
			temp[1]=profile.getDegreeCourse();
		}
		if (!this.getCourseState().equals(profile.getCourseState())){
			temp[0]="statoCorso";
			temp[1]=profile.getCourseState();
		}
		if (this.getCourseYear()!=profile.getCourseYear()){
			temp[0]="annoCorso";
			temp[1]=String.valueOf(profile.getCourseYear());
		}
		return temp;
	}

}
