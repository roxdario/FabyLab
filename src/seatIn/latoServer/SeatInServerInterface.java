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
	public String findFileName (String code) throws SQLException, RemoteException;
	public ArrayList<String[]> showTeachingCourse() throws IOException, RemoteException;
	public boolean sendEmailForNewsletter(String user, String password, List<String> toList, String subject, String body) throws RemoteException;
	public Object login(SeatInPeople person) throws RemoteException;
	public boolean checkEmail(String email) throws RemoteException;
	public boolean insertProfileIntoDatabase(SeatInPeople person) throws RemoteException;
	public boolean resetPasswordRequest(SeatInPeople person) throws RemoteException;
	public boolean updateProfileState(SeatInPeople person) throws RemoteException;
	public Object profileInformation(SeatInPeople person) throws RemoteException, SQLException;
	public ArrayList<String[]> showCourse() throws IOException, RemoteException;
	public boolean courseSubscription(String email, String code, int year) throws MessagingException, RemoteException;
	public void insertPersonByAdmin(HashMap<String, List<? extends SeatInPeople>> people) throws RemoteException;
	public List<String[]> viewAllCourseInformation() throws RemoteException;
	public List<String[]> showCourseFromStudyPlan(String email) throws SQLException, RemoteException;
	public List<String[]> getCoursesTeached(String email) throws SQLException, RemoteException;
	public List<String> getCourseByStudent(String email) throws SQLException, RemoteException;
	public boolean insertUserTeachingCourse(String[] row) throws SQLException, RemoteException;
	public HashMap<String, List<? extends SeatInPeople>> showUserCSV() throws RemoteException;
	//al fine di inviare email da parte di un docente per la newsletter
	public List<String> getStudentsEmailforNewsletter(String courseName, int year) throws SQLException, RemoteException;
	//prova
	public void uploadFileToServer(byte[] mybyte, String serverpath, int length) throws RemoteException;
	public byte[] downloadFileFromServer(String servername) throws RemoteException;
	public String[] listFiles(String serverpath) throws RemoteException;
	public boolean createDirectory(String serverpath) throws RemoteException;
	public boolean removeDirectoryOrFile(String serverpath) throws RemoteException;
	public List<String[]> showChangeProfileRequest() throws SQLException, RemoteException;
	public boolean changeProfileRequest(String email, String fieldToChange, String newParameter, String tipology) throws RemoteException;
	public boolean insertFile(String resourceCode, String sectionCode, String name, String description, File file, String fatherCode, String visibility, String tipology) throws IOException, RemoteException;
	public boolean addCourseSubsection(String title, String description, String course, int editonYear, int fatherSection, String visibility) throws RemoteException;
	public boolean addCourseSection(String title, String description, String course, int editonYear, String visibility) throws RemoteException;
	public Object updateChangeProfileRequest(SeatInPeople person, String oldEmail) throws SQLException, RemoteException;
	public Node createCourseTree(String code, int year) throws SQLException, RemoteException;
	public boolean deleteSection(String code) throws RemoteException;
	public boolean deleteFile(String resourceCode) throws RemoteException;
	public boolean changeVisibility(Node node, String visibility) throws RemoteException;
	public int showResourceDownloadFromCourse(String courseCode, int courseYear) throws SQLException, RemoteException;
	public int showUserDownloadedInTime(Timestamp timeStart, Timestamp timeStop) throws RemoteException;
	public int getUserConnected() throws RemoteException;
	public void logout(String email) throws RemoteException;
	public void registerUserLogoutFromCourse(String email, String code, int courseYear) throws RemoteException;
	public void registerUserIsConsultingCourse(String email, String code, int courseYear, String profile) throws RemoteException;
	public int statisticsUserActiveInACourse(int courseYear, String courseCode) throws SQLException, RemoteException;
	public Object timeStatisticsForCourse(String courseCode, int courseYear) throws SQLException, RemoteException;
	public Object timeStatisticsForAllCourses() throws SQLException, RemoteException;
	public int totalAccessInACoursePage(String courseCode, int courseYear, Timestamp t1, Timestamp t2) throws SQLException, RemoteException;
	public int getResourceCode() throws RemoteException, SQLException;
	public List<String> teacherEmailForStudentEmailSender (String courseCode, int courseYear) throws SQLException, RemoteException;
	public List<String[]> courseDegreeWithoutStudentOne (String courseName) throws SQLException,RemoteException;
	public List<String[]> courseNotInStudyPlan(String degreeCode, int enrollmentYear) throws SQLException, RemoteException;
	public boolean sendEmail (String from, String to, String password, String object, String body) throws RemoteException;
	public List<String[]> teacherTeachesCourse (String code, int year) throws SQLException ,RemoteException;
	public Object passwordForgotten(String email) throws SQLException, RemoteException;
	public ArrayList<String> findInFolder(String code) throws SQLException, RemoteException;
	public byte[] getFile(String fileCode, String email, int courseYear, String courseCode) throws IOException, SQLException, RemoteException;
	public boolean courseSubscription(String email, String code, int year,List<String> teacherEmail) throws MessagingException, RemoteException;
	public void insertCourseByAdmin(String[] courses) throws SQLException, RemoteException;

	public boolean unapproveRequest(String oldEmail,String fieldRequest, String newField) throws RemoteException;
	public List<String> showLockedProfileForAdmin() throws SQLException, RemoteException;
	public String whichUser(String email) throws RemoteException, SQLException;
    public List<String[]> allCourse() throws RemoteException, SQLException; 
    public List<String[]> getInfoCoursesTeached(String email) throws SQLException,RemoteException ;
    public List<String[]> courseNotInStudyPlanTeacher(String email) throws SQLException, RemoteException ;



}
