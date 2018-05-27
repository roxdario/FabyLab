package seatIn.latoServer;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;



public interface SeatInServerInterface extends Remote {

	   public Object login(SeatInPeople person) throws RemoteException;

	    public boolean checkEmail(String email) throws RemoteException;

	    public boolean insertProfileIntoDatabase(SeatInPeople person) throws RemoteException;

	    public boolean resetPasswordRequest(SeatInPeople person) throws RemoteException;

	    public boolean updateProfileState(SeatInPeople person) throws RemoteException;

	    public Object profileInformation(SeatInPeople person) throws RemoteException, SQLException;

	    public ArrayList<String[]> showCourse() throws IOException, RemoteException;
	    public boolean courseSubscription (String email, String code, int year) throws MessagingException, RemoteException;
	    public void insertPersonByAdmin(HashMap<String, List> people) throws RemoteException;
	    public List<String[]> viewAllCourseInformation() throws RemoteException;
	    //corsi dal piano di studi a cui sei iscritto 
	    public List<String> showCourseFromStudyPlan(String email) throws SQLException,RemoteException;
	    //corsi docente
	    public List<String> getCoursesTeached (String email) throws SQLException, RemoteException;
	    //corsi student
	    public List<String> getCourseByStudent(String email) throws SQLException, RemoteException;
	    public boolean insertUserTeachingCourse(String[] row) throws SQLException, RemoteException;
	    public HashMap<String, List<? extends SeatInPeople>> showUserCSV() throws  RemoteException;
	    //al fine di inviare email da parte di un docente per la newsletter
	    public List<String> getStudentsEmailforNewsletter (String courseName, String year) throws SQLException, RemoteException;



	    //prova
	    public void uploadFileToServer(byte[] mybyte, String serverpath, int length) throws RemoteException;

	    public byte[] downloadFileFromServer(String servername) throws RemoteException;

	    public String[] listFiles(String serverpath) throws RemoteException;

	    public boolean createDirectory(String serverpath) throws RemoteException;

	    public boolean removeDirectoryOrFile(String serverpath) throws RemoteException;

	    public List<String[] > showChangeProfileRequest () throws SQLException, RemoteException;

	    public boolean changeProfileRequest(String email, String fieldToChange, String newParameter) throws RemoteException;

	    public void insertFile(String resourceCode, String sectionCode, String name, String description,File file, String fatherCode, String visibility ) throws IOException, RemoteException;

	        public void addCourseSubsection (String title, String description, String course, int editonYear, int fatherSection) throws RemoteException;

	    public void addCourseSection (String title, String description, String course, int editonYear) throws RemoteException;

	    public boolean updateChangeProfileRequest (SeatInPeople person, String oldEmail) throws SQLException, RemoteException;
	    public boolean changeSectionDescription (String description, String title, String courseName, int courseYear) throws RemoteException;
	    public boolean changeSectionName (String sectionName, String newTitle, String courseName, int courseYear) throws RemoteException;

	    public Node createCourseTree(String code, int year) throws SQLException, RemoteException;


	    public boolean deleteSection(String code) throws RemoteException;

	    public boolean deleteFileFromSection(String resourceCode) throws RemoteException;

	    public boolean changeVisibility(Node node, String visibility) throws RemoteException;
	    public void getFile(String fileName, int courseYear, String courseCode, String directory, String email) throws IOException, SQLException, RemoteException;

	    public int showResourceDownloadFromCourse (String courseCode, int courseYear) throws SQLException, RemoteException ;

	    public int showUserDownloadedInTime (Timestamp timeStart, Timestamp timeStop) throws RemoteException;

	    public int getUserConnected () throws RemoteException;//utenti connessi

	    public void  logout (String email) throws RemoteException;

	    public void registerUserLogoutFromCourse(String email, String code, int courseYear) throws RemoteException;

	    public void registerUserIsConsultingCourse(String email, String code, int courseYear, String profile) throws RemoteException;

	    public int statisticsUserActiveInACourse(int courseYear, String courseCode) throws SQLException, RemoteException;

	    public Object timeStatisticsForCourse(String courseCode, int courseYear) throws SQLException, RemoteException;
	    public Object timeStatisticsForAllCourses() throws SQLException, RemoteException;
	    public int totalAccessInACoursePage(String courseCode, int courseYear, Timestamp t1, Timestamp t2) throws SQLException, RemoteException;
	    
	   
}
