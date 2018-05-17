package latoServer;
import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public interface SeatInServerInterface extends Remote {

	   public Object login(SeatInPeople person) throws RemoteException;
	   public boolean checkEmail(String email) throws RemoteException;
	   public boolean insertProfileIntoDatabase(SeatInPeople person) throws RemoteException;
	   public boolean resetPasswordRequest(SeatInPeople person) throws RemoteException;
	   public boolean updateProfileState(SeatInPeople person, String state) throws RemoteException;
	   public Object profileInformation (SeatInPeople person) throws RemoteException, SQLException;
}
