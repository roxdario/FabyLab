package seatIn.latoServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class SeatInPlatform {
	
  

	public SeatInPlatform() {
		// TODO Auto-generated constructor stub
	}
/*public static void main(String[] args) throws InterruptedException, IOException, NotBoundException { 
	  	
       
 
    	String host="10.24.136.165";
    	int port=1099;
    	Registry reg=LocateRegistry.getRegistry(host, port); 
 		SeatInPeople.stub = (SeatInServerInterface) reg.lookup("classeRemota"); 
 		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 		System.out.println("email:");
    	String tempEmail=in.readLine();
    	//login(tempEmail);
		SeatInStudent Student = new SeatInStudent("ciao1","ciao2","ciao3","ciao4","ciao5","ciao6","ciao7",2017,2018,"ciao8","ciao9");

    	Student.subscription();
    	
 	
 	
		
	   
	    
		System.out.println("Finito"); 
	}
	
	public static void login(String email){
		 
//	    if (email.equals("Student")) {
    		SeatInStudent Student = new SeatInStudent();
/*	    } else if (email.equals("Teacher")) {
	    	SeatInTeacher  Teacher = new SeatInTeacher(ID, email, password, host, port);
	    }else if (email.equals("Administrator")) {
	    	SeatInAdmin  Admin = new SeatInAdmin(ID, email, password, host, port);
	   }
	    
	}*/

}
