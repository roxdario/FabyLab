package latoServer;
import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public interface SeatInServerInterface extends Remote {
	/*  public  String generaCodice() throws RemoteException ;
	   public  boolean checkEmail(String email) throws RemoteException, SQLException;
	   public void inserisciUtenteDB(String nome, String cognome, String CF, String email, String inquad, String telefono, String pw, String codiceAttivazione)
	           throws RemoteException, SQLException;
	   public boolean controlloCodFiscale (String codFiscale) throws RemoteException, SQLException;
	   public void inviaMail(String to) throws RemoteException,
	           AccessException, MessagingException;
	   public int CheckLogin(String email, String password) throws RemoteException;
*/
	public String ciaoStringa(SeatInPeople user) throws RemoteException;
	public boolean checkEmail(String email) throws RemoteException;
	public boolean insertProfileIntoDatabase(SeatInPeople user) throws RemoteException;
	public String login(SeatInPeople user) throws RemoteException;
}
