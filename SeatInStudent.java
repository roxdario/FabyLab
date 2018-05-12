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

	public void downloadFile(String pathName){}
	public void submit(String courseName){}
	


	

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

	

	
	

}
