package seatIn.latoServer;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.*;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import jdk.nashorn.internal.scripts.JO;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeatInGui extends JFrame {
    private String userEmail= "empty";
    private String mail;
    private String IDTemp;
    private String psw;
    private static final long serialVersionUID = 1;
    private int counterPassword;
    public Container mainContainer= getContentPane();
    private SeatInPeople user;
    private SeatInStudent student;
    private SeatInTeacher teacher;
    private SeatInAdmin administrator;
    private String utenza;
    private static final int MLPSW=1;//lunghezza minima della psw
    private static final int MLS=1;//lunghezza minima stringhe

    public SeatInGui() {
        super("SeatIn");
        mainContainer.setLayout(new FlowLayout());
        mainContainer.setPreferredSize(new Dimension(600, 400));   
    }

    /** Contanier Rincipale
     * @throws NotBoundException
     * @throws RemoteException **/
    public void welcometoSeatIn() throws RemoteException, NotBoundException {
        counterPassword=0;
        mainContainer.removeAll();
        mainContainer.validate();
        mainContainer.repaint();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.add(BorderLayout.CENTER, loginInterface());
        //mainContainer.add(BorderLayout.WEST, registerProfile());
        setVisible(true);
        setSize(600,300);
    }

    private String whichUser(String text) {
        String[] temp= text.split("@");
        text=temp[1];
        if(text.startsWith("s")){
            return "s";
        }else if(text.startsWith("t")){
            return "t";
        }else if(text.startsWith("a")){
            return "a";
        }
        return "-1";
    }
 //controlli per login
    public boolean loginCheck(String email,String psw){
    	String[] temp= email.split("@");
    	if(temp.length!=2){
    		return false;
    	}else{
    		  email=temp[1];
    		  switch (utenza){
    		  case "Studente":
    			  if(email.equals("studenti.uninsubria.it")&&(psw.length()>=MLPSW)){
    				  return true;
    			  }else{
    				  return false;
    			  }	
    		  case "Docente":
    			  //TODO togliere lo studenti
    			  if(email.equals("uninsubria.it")||email.equals("studenti.uninsubria.it")&&(psw.length()>=MLPSW)){
    				  return true;
    			  }else{
    				  return false;
    			  }
    		  case "Admin":
    			  if((email.equals("uninsubria.it")||email.equals("studenti.uninsubria.it"))&&(psw.length()>=MLPSW)){
    				  return true;
    			  }else{
    				  return false;
    			  }	
    		  default:
    		  return false;
    		  } 
    	}
    }

    /** Panello di Login
     * @throws NotBoundException
     * @throws RemoteException **/   
    public JPanel loginInterface () throws RemoteException, NotBoundException {
        //connessione se non connesso bloccare pulsanti
       

        final JTextField email= new JTextField("nome@ununsubria.it", 20);
        JButton passwordForgot= new JButton("Password dimenticata");
        passwordForgot.setSize(60,20);
        JButton submit = new JButton("Registrati!");
        final JPanel welcomeLogin = new JPanel();
        welcomeLogin.setLayout(new GridLayout(6, 3));

        String[] users = {"Scegli il tipo di utente","Studente", "Docente", "Admin"};
        JComboBox type = new JComboBox(users);



        welcomeLogin.add(new JLabel("Piattaforma SeatIn  "));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(type);
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(email);
        email.addFocusListener(new FocusListener() {
            //quando clicco sul textField Email scompare il suggerimento
            public void focusGained(FocusEvent e) {
                email.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        }  );

        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        final JPasswordField passwordField= new JPasswordField("Password", 20);
        passwordField.addFocusListener(new FocusListener() {
            //quando clicco sul textField Email scompare il suggerimento
            public void focusGained(FocusEvent e) {
                passwordField.setText("password");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        }  );
        // pannello contenente email, pw, password dimenticata, tasto accesso
            JButton access = new JButton("Accedi");
            access.setSize(10,10);
            access.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    utenza=(String) type.getSelectedItem();
                    if(loginCheck(email.getText(),passwordField.getText())){
                        if(utenza.equals("Studente")){
    	                    student= new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
                        	
    	                    student.setEmail(email.getText());
    	                    student.setPassword(passwordField.getText());
    	                    try {
    	                        student.connection();
    	                    } catch (RemoteException | NotBoundException e2) {
    	                        // TODO Auto-generated catch block
    	                        e2.printStackTrace();
    	                    }
    	                    try {
    	                        switch(student.login()){
    	                        case "student":
    	                            System.out.println(student.getStateProfile());
    	                            switch(student.getStateProfile()){
    	                        	case"non attivo":
    	                        		JOptionPane.showMessageDialog( loginInterface(), "Utente non attivo, attiva ora il tuo profilo");
    	                        		activationUser();
    	                        		welcomeLogin.setVisible(false);	                               
    	                        		//nuova interfaccia
    	                                break;
    	                        	case "attivo":
    	                        		mainContainer.removeAll();
    	                                mainContainer.validate();
    	                                mainContainer.repaint();
    	                                mainContainer.setLayout(new BorderLayout());
    	                                mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());
    	                                loginInterface().setVisible(false);
    	                                break;
    	                        	case "bloccato":
    	                        		JOptionPane.showMessageDialog(loginInterface(),"Utente bloccato, un amministratore si occupera' di sbloccarlo");
    	                        		break;
    	                            }
    	                            break;
    	                        case "password errata":
    	                        	if(userEmail.equals("empty")){
    	                                JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+9+" tentativi"+student.getEmail());
    	                                counterPassword=1;
    	                                userEmail=student.getEmail();
    	                        	}else if(userEmail.equals(student.getEmail())){
    	                        		 counterPassword++;
    	                        		JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+(10-counterPassword)+" tentativi"+userEmail);
    	                                if(counterPassword >=10){
    	                                    try {
    	                                        student.blockProfile();
    	                                        JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
    	                                    } catch (RemoteException e1) {
    	                                        // TODO Auto-generated catch block
    	                                        e1.printStackTrace();
    	                                    }
    	                                }
    	                        	}                      
    	                            break;
    	                        case "utente inesistente":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
    	                        	break;
    	                        case "not found":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "User not found, check your status");
    	                        	break;
    	                        case "utente bloccato":
    	                        	JOptionPane.showMessageDialog(welcomeLogin,"Utente bloccato, fai richieta per sblocco profilo");
                            		break;
                                }
    	                      
    	                    } catch (HeadlessException | IOException | NotBoundException e1) {
    	                        // TODO Auto-generated catch block
    	                        e1.printStackTrace();
    	                    }
    	                }else if(utenza.equals("Docente")){
    	                	teacher = new SeatInTeacher(null, null, null, null, null, null, null,null);
    	                    teacher.setEmail(email.getText());
    	                    teacher.setPassword(passwordField.getText());
    	                    try {
    	                        teacher.connection();
    	                    } catch (RemoteException | NotBoundException e2) {
    	                        // TODO Auto-generated catch block
    	                        e2.printStackTrace();
    	                    }
    	                    try {
    	                        switch(teacher.login()){
    	                        case "teacher":
    	                            System.out.println(teacher.getStateProfile());
    	                            switch(teacher.getStateProfile()){
    	                        	case"non attivo":
    	                        		JOptionPane.showMessageDialog( loginInterface(), "Utente non attivo, attiva ora il tuo profilo");
    	                        		activationUser();
    	                        		welcomeLogin.setVisible(false);	                               
    	                        		//nuova interfaccia
    	                                break;
    	                        	case "attivo":
    	                        		 mainContainer.removeAll();
    	                                 mainContainer.validate();
    	                                 mainContainer.repaint();
    	                                 mainContainer.setLayout(new BorderLayout());
    	                                 mainContainer.add(BorderLayout.CENTER, teacherPanel());
    	                                 //welcomeLogin.setVisible(false);
    	                                 loginInterface().setVisible(false);
    	                                break;
    	                        	case "bloccato":
    	                        		JOptionPane.showMessageDialog(loginInterface(),"Utente bloccato, fai richieta per sblocco profilo");
    	                        		break;
    	                            }
    	                            break;
    	                        case "password errata":
    	                        	if(userEmail.equals("empty")){
    	                                JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+9+" tentativi"+teacher.getEmail());
    	                                counterPassword=1;
    	                                userEmail=teacher.getEmail();
    	                        	}else if(userEmail.equals(teacher.getEmail())){
    	                        		 counterPassword++;
    	                        		JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+(10-counterPassword)+" tentativi"+userEmail);
    	                                if(counterPassword >=10){
    	                                    try {
    	                                        teacher.blockProfile();
    	                                        JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
    	                                    } catch (RemoteException e1) {
    	                                        // TODO Auto-generated catch block
    	                                        e1.printStackTrace();
    	                                    }
    	                                }
    	                        	}                      
    	                            break;
    	                        case "utente inesistente":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
    	                        	break;
    	                        case "not found":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "User not found, check your status");
    	                        	break;
    	                        default:
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
    	                        }
    	                        
    	                    } catch (HeadlessException | IOException | NotBoundException e1) {
    	                        // TODO Auto-generated catch block
    	                        e1.printStackTrace();
    	                    }
    	                }else if(utenza.equals("Admin")){
    	                	administrator = new SeatInAdmin(null, null, null, null, null, null, null,null);
    	                    administrator.setEmail(email.getText());
    	                    administrator.setPassword(passwordField.getText());
    	                    try {
    	                        administrator.connection();
    	                    } catch (RemoteException | NotBoundException e2) {
    	                        // TODO Auto-generated catch block
    	                        e2.printStackTrace();
    	                    }
    	                    try {
    	                        switch(administrator.login()){
    	                        case "admin":
    	                            System.out.println(administrator.getStateProfile());
    	                            switch(administrator.getStateProfile()){
    	                        	case"non attivo":
    	                        		JOptionPane.showMessageDialog( loginInterface(), "Utente non attivo, attiva ora il tuo profilo");
    	                        		activationUser();
    	                        		welcomeLogin.setVisible(false);	                               
    	                        		//nuova interfaccia
    	                                break;
    	                        	case "attivo":
    	                        		 mainContainer.removeAll();
    	                                 mainContainer.validate();
    	                                 mainContainer.repaint();
    	                                 mainContainer.setLayout(new BorderLayout());
    	                                 mainContainer.add(BorderLayout.CENTER, adminPanel());
    	                                 //welcomeLogin.setVisible(false);
    	                                 loginInterface().setVisible(false);
    	                                break;
    	                        	case "bloccato":
    	                        		JOptionPane.showMessageDialog(loginInterface(),"Utente bloccato, fai richieta per sblocco profilo");
    	                        		break;
    	                            }
    	                            break;
    	                        case "password errata":
    	                        	if(userEmail.equals("empty")){
    	                                JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+9+" tentativi"+administrator.getEmail());
    	                                counterPassword=1;
    	                                userEmail=administrator.getEmail();
    	                        	}else if(userEmail.equals(teacher.getEmail())){
    	                        		 counterPassword++;
    	                        		JOptionPane.showMessageDialog(welcomeLogin, "Password errata, rimangono:"+(10-counterPassword)+" tentativi"+userEmail);
    	                                if(counterPassword >=10){
    	                                    try {
    	                                        administrator.blockProfile();
    	                                        JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
    	                                    } catch (RemoteException e1) {
    	                                        // TODO Auto-generated catch block
    	                                        e1.printStackTrace();
    	                                    }
    	                                }
    	                        	}                      
    	                            break;
    	                        case "utente inesistente":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
    	                        	break;
    	                        case "not found":
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "User not found, check your status");
    	                        	break;
    	                        default:
    	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
    	                        }
    	                        
    	                    } catch (HeadlessException | IOException | NotBoundException e1) {
    	                        // TODO Auto-generated catch block
    	                        e1.printStackTrace();
    	                    }
    	
    	                }else{
    	                	JOptionPane.showMessageDialog(welcomeLogin, "Seleziona una tipologia di utente");
    	                }
                    }else{
                    	JOptionPane.showMessageDialog(welcomeLogin, "Dati inseriti non correttamente");
                    }
    	       
                    setVisible(true);
                }
            });
        JButton unlockuser= new JButton("Sblocca utente");
          unlockuser.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  userBlocked();
                  setVisible(true);
                  welcomeLogin.setVisible(false);
                 
              }
          });
          welcomeLogin.add(passwordField);  //campo password
          welcomeLogin.add(new JLabel(""));
          welcomeLogin.add(new JLabel(""));
  
          welcomeLogin.add(access);  //bottone accedi
          welcomeLogin.add(new JLabel(""));
          welcomeLogin.add(unlockuser);


        welcomeLogin.add(passwordForgot);

        passwordForgot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordForgotten();
            }
        });
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerProfile();
                } catch (NotBoundException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                welcomeLogin.setVisible(false);
            }
        });
        //controlli input

        mail=email.getText();
        return welcomeLogin;
    }

    /**Pannello di Registrazione**/
    public JPanel registerProfile() throws NotBoundException, IOException {
        Container container = getContentPane();
        JPanel submit=new JPanel();
        final JTextField registerInformation[] = {
                //           nome                         cognome                 matricola       email             course degree              matricola           anno immatricolazione
                new JTextField(  20), new JTextField( 20), new JTextField(), new JTextField( 20), new JTextField(), new JTextField(), new JTextField(4)
        };

        final String year[] = {"----", "1", "2", "3"};
        final String kindYear[] = {"----", "fuori corso", "in corso"};
        final String actor[] = { "----","studente", "docente", "amministratore"};
        final String[] departmentList={"----", "DISTA", "DIMAT"};
        final JComboBox comboBoxYear[] = { new  JComboBox<>(year), new JComboBox(kindYear), new JComboBox(actor), new JComboBox<String>(departmentList) };
        JPanel panelForYear =new JPanel();
        panelForYear.setLayout(new GridLayout(1,2));
        for (JComboBox it : comboBoxYear)
            panelForYear.add(it);
        registerInformation[3].setText("nome@dominio.it");
        registerInformation[3].addFocusListener(new FocusListener() {
            //quando clicco sul textField Email scompare il suggerimento
            public void focusGained(FocusEvent e) {
                registerInformation[3].setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        }  );

        submit.setLayout(new GridLayout(15, 2));
        JLabel introducintToSubscription[] = {new JLabel("oppure ISCRIVITI!"), new JLabel(""), new JLabel("sono un: ")};
        for (JLabel label: introducintToSubscription)
            submit.add(label);
        final JLabel labelForReg[] = { new JLabel("Nome"), new JLabel("Cognome"), new JLabel("Matricola"), new JLabel("Email"),  new JLabel ("Dipartimento :"),
                new JLabel("Corso di laurea: "), new JLabel("Anno di immatricolazione: "),new JLabel("anno di corso:") , new JLabel("") };
        submit.add(comboBoxYear[2]);
        JButton ok = new JButton("REGISTRATI");
        Object objectReg[] = { registerInformation[0], registerInformation[1], registerInformation[2], registerInformation[3],
                comboBoxYear[3], registerInformation[4], registerInformation[5], panelForYear, ok};
        for (int i=0; i<9 ; i++)
        {
            submit.add(labelForReg[i]);
            submit.add(labelForReg[i]);
            submit.add((Component) objectReg[i]);
        }

        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	utenza=(String) comboBoxYear[2].getSelectedItem();
            	switch(utenza){
            	case"----":
            		JOptionPane.showMessageDialog(SeatInGui.this, "Seleziona una tipologia di utenza");
                    break;
            	case "studente":
            		switch(checkSubmitStudent(registerInformation[2].getText(), registerInformation[0].getText(), 
            				registerInformation[1].getText(),registerInformation[3].getText(),
            			registerInformation[5].getText(),(String) comboBoxYear[0].getSelectedItem(), 
            			(String)comboBoxYear[1].getSelectedItem(),registerInformation[4].getText())){
	            		case "ok":
	            			String tipoY = null;
	                        if(comboBoxYear[1].getSelectedIndex()==1){
	                            tipoY="fuoriCorso";
	                        }else if(comboBoxYear[1].getSelectedIndex()==2){
	                            tipoY="inCorso";
	                        }
	                      //  student= new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
	                       // student.getInstance();
	                        student.setID(registerInformation[2].getText());
	                        student.setName(registerInformation[0].getText());
	                        student.setSurname(registerInformation[1].getText());
	                        student.setEmail(registerInformation[3].getText());
	                        student.setEnrollmentYear(Integer.parseInt(registerInformation[5].getText()));
	                        student.setCourseYear(comboBoxYear[0].getSelectedIndex());
	                        student.setCourseState(tipoY);
	                        student.setDegreeCourse(registerInformation[4].getText());
	                        try {
	                        	
	                            student.connection();
	                        } catch (RemoteException | NotBoundException e2) {
	                            // TODO Auto-generated catch block
	                            e2.printStackTrace();
	                        }
	
	                        try {
	                            if(student.subscription()){
	                                JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
	                            }else{
	                                JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
	                            }
	                        } catch (HeadlessException | IOException e1) {
	                            // TODO Auto-generated catch block
	                            e1.printStackTrace();
	                        } catch (NotBoundException e1) {
	    						// TODO Auto-generated catch block
	    						e1.printStackTrace();
	    					} catch (MessagingException e1) {
	    						// TODO Auto-generated catch block
	    						e1.printStackTrace();
	    					}		
	            			 break;
		            	case "email":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "email non inserita correttamente");
		            		break;
		            	case "nome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "nome non inserito correttamente");
		            		break;
		            	case "cognome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "cognome non inserito correttamente");
		            		break;
		            	case "id":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "id non inserito correttamente");
		            		break;
		            	case "corso":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "corso non inserito correttamente");
		            		break;
		            	case "anno":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "anno non inserito correttamente");
		            		break;
		            	case "stato":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "stato corso non inserito correttamente");	  
		                    break;
		            	case "anno corso":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "anno corso non inserito correttamente");	  
		                    break;
            		}
            		break;
            	case "docente":
            		switch(checkSubmit(registerInformation[2].getText(), registerInformation[0].getText(), 
            				registerInformation[1].getText(),registerInformation[3].getText(), 
            			(String)comboBoxYear[3].getSelectedItem())){
	            		case "ok":
	            			 String department = null;
	                         if(comboBoxYear[3].getSelectedIndex()==1){
	                             department="DISTRA";
	                         }else if(comboBoxYear[3].getSelectedIndex()==2){
	                             department="DIMA";
	                         }
	                         teacher = new SeatInTeacher(null, null, null, null, null, null, null,null);
	                         teacher.setID(registerInformation[2].getText());
	                         teacher.setName(registerInformation[0].getText());
	                         teacher.setSurname(registerInformation[1].getText());
	                         teacher.setEmail(registerInformation[3].getText());
	                         teacher.setDepartment(department);
	                         try {
	                             if(teacher.subscription()){
	                                 JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
	                             }else{
	                                 JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
	                             }
	                         } catch (HeadlessException | NotBoundException | IOException | MessagingException e1) {
	                             // TODO Auto-generated catch block
	                             e1.printStackTrace();
	                         }
	           			 break;
		            	case "email":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "email non inserita correttamente");
		            		break;
		            	case "nome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "nome non inserito correttamente");
		            		break;
		            	case "cognome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "cognome non inserito correttamente");
		            		break;
		            	case "id":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "id non inserito correttamente");
		            		break;
		            	case "department":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "dipartimento non inserito correttamente");	  
		                    break;
            		}
            		
            		break;
            	case "amministratore":
            		switch(checkSubmit(registerInformation[2].getText(), registerInformation[0].getText(), 
            				registerInformation[1].getText(),registerInformation[3].getText(), 
            			(String)comboBoxYear[3].getSelectedItem())){
	            		case "ok":
		            		String departmen = null;
		                     if(comboBoxYear[3].getSelectedIndex()==1){
		                         departmen="DISTA";
		                     }else if(comboBoxYear[3].getSelectedIndex()==2){
		                         departmen="DIMAT";
		                     }
		                     administrator = new SeatInAdmin(null, null, null, null, null, null, null,null);  
		                     administrator.setID(registerInformation[2].getText());
		                     administrator.setName(registerInformation[0].getText());
		                     administrator.setSurname(registerInformation[1].getText());
		                     administrator.setEmail(registerInformation[3].getText());
		                     administrator.setDepartment(departmen);
		                     try {
		                         if(administrator.subscription()){
		                             JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
		                         }else{
		                             JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
		                         }
		                     } catch (HeadlessException | NotBoundException | IOException | MessagingException e1) {
		                         // TODO Auto-generated catch block
		                         e1.printStackTrace();
		                     }
		            		break;
		            	case "email":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "email non inserita correttamente");
		            		break;
		            	case "nome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "nome non inserito correttamente");
		            		break;
		            	case "cognome":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "cognome non inserito correttamente");
		            		break;
		            	case "id":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "id non inserito correttamente");
		            		break;
		            	case "department":
		            		JOptionPane.showMessageDialog(SeatInGui.this, "dipartimento non inserito correttamente");	  
		                    break;
		            		
            		}
            	}
        
            }
        });

        JButton back= new JButton("Indietro");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    welcometoSeatIn();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                submit.setVisible(false);
            }
        });

        comboBoxYear[2].addItemListener(new ItemListener() {
                                            @Override
                                            public void itemStateChanged(ItemEvent e) {
                                                if (e.getStateChange() == ItemEvent.SELECTED){
                                                    //System.out.print(iam.getSelectedIndex());
                                                    comboBoxYear[3].removeAllItems();
                                                    comboBoxYear[0].removeAllItems();
                                                    comboBoxYear[1].removeAllItems();
                                                    if (comboBoxYear[2].getSelectedIndex()==0) {
                                                        for (int i=4; i<=5; i++ ) {
                                                            registerInformation[i].setEditable(false);
                                                            registerInformation[i].setText("non disponibile per questo profilo");
                                                            registerInformation[i].setBackground((Color.LIGHT_GRAY));
                                                        }
                                                        for (int i=0; i<=1; i++)
                                                            comboBoxYear[i].setBackground((Color.LIGHT_GRAY));
                                                    }
                                                    if  (comboBoxYear[2].getSelectedIndex()==1) {
                                                        //if student
                                                        comboBoxYear[3].addItem("--------");
                                                        for (int i=4; i<=5; i++ ) {
                                                            registerInformation[i].setEditable(true);
                                                            registerInformation[i].setText("");
                                                            registerInformation[i].setBackground((Color.WHITE));
                                                        }
                                                        comboBoxYear[1].setBackground(Color.WHITE);
                                                        comboBoxYear[0].setBackground(Color.WHITE);
                                                        //String year[] = {"", "1", "2", "3"};
                                                        //String kindYear[] = {"", "fuori corso", "in corso"};
                                                        for (int i=0; i<3; i++) {
                                                            comboBoxYear[1].addItem(kindYear[i]);
                                                        }
                                                        for (int i =0; i<4 ; i++) {

                                                            comboBoxYear[0].addItem(year[i]);
                                                        }
                                                        registerInformation[3].setText("nome@studenti.uninsubria.it");

                                                    }
                                                    if (comboBoxYear[2].getSelectedIndex()==2 || comboBoxYear[2].getSelectedIndex()==3){
                                                        //if student
                                                        //   submit.remove(panelForStudent);
                                                        for (String i: departmentList)
                                                            comboBoxYear[3].addItem(i);
                                                        for (int i=4; i<=5; i++ ) {
                                                            registerInformation[i].setEditable(false);
                                                            registerInformation[i].setText("non disponibile per questo profilo");
                                                            registerInformation[i].setBackground((Color.LIGHT_GRAY));
                                                        }
                                                        comboBoxYear[0].setBackground((Color.LIGHT_GRAY));
                                                        comboBoxYear[1].setBackground((Color.LIGHT_GRAY));
                                                        registerInformation[3].setText("nome@uninsubria.it");
                                                    }
                                                }
                                            }
                                        }
        );
        submit.add(new JLabel(""));
        submit.add(back);

        mainContainer.add(submit);

        setVisible(true);

        return submit;
    }
    public String checkSubmitStudent(String iD, String name, String surname, String email,
			String enrollmentYear, String courseYear, String courseState,String degreeCourse){
    	String[] temp= email.split("@");
    	if(temp.length!=2){
    		return "email senza @";
    	}else{
    		  email=temp[1];
    		  if(!(email.equals("studenti.uninsubria.it")))
    				  return "email";
    		  if(name.length()<MLS)
    			  return "nome";
    		  if(surname.length()<MLS)
    			  return "cognome";
    		  if(iD.length()<MLS)
    			  return "id";
    		  if(degreeCourse.length()<MLS)
    			  return "corso";
    		  if(courseYear.equals("----"))
    			  return "anno";
    		  if(courseState.equals("----"))
    			  return "stato";
    		  if(enrollmentYear.length()<4)
    			  return "anno corso";
    		  return "ok"; 
    	}
    }
    public String checkSubmit(String iD, String name, String surname, String email,String department){
    	String[] temp= email.split("@");
    	if(temp.length!=2){
    		return "email senza @";
    	}else{
    		  email=temp[1];
    		  if(!(email.equals("uninsubria.it")))
    				  return "email";
    		  if(name.length()<MLS)
    			  return "nome";
    		  if(surname.length()<MLS)
    			  return "cognome";
    		  if(iD.length()<MLS)
    			  return "id";
    		  if(department.equals("----"))
    			  return "department";
    		  return "ok"; 
    	}
    }

    /** Pannello Password Dimenticata**/

    public void passwordForgotten(){
        JButton backtoLogin = new JButton("<--");
        backtoLogin.setPreferredSize(new Dimension(5,5));
        backtoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //torno alla schermata iniziale
                try {
                    welcometoSeatIn();
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        JPanel panelBackToLogin =new JPanel();
        panelBackToLogin.setLayout(new GridLayout(1,3));
        panelBackToLogin.add(backtoLogin);
        panelBackToLogin.add(new JLabel("Procedura di ripristino passsword"));

        JPanel panelForChangePw=new JPanel();
        panelForChangePw.setLayout(new BorderLayout());
        panelForChangePw.add(BorderLayout.NORTH,panelBackToLogin);
        JButton ok = new JButton("OK");
        mainContainer.removeAll();
        mainContainer.validate();
        mainContainer.repaint();
        JPanel panelForChangePassword =new JPanel();
        panelForChangePassword.setLayout(new GridLayout(2,2));
        final JTextField email=new JTextField();
        email.setSize(new Dimension(10,10));
        panelForChangePassword.add(new JLabel("email:"));
        panelForChangePassword.add(email);
        panelForChangePassword.add(new JLabel(""));
        panelForChangePassword.add(ok);
        panelForChangePw.add(panelForChangePassword);
        mainContainer.add(panelForChangePw);
        setSize(600, 300);
        setVisible(true);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    JOptionPane.showMessageDialog(SeatInGui.this, "una email e' stata inviata all'indirizzo " + email.getText());
                    user.connection();
                    user.passwordForgotten(email.getText());
                    welcometoSeatIn();


                } catch (RemoteException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

            }
        });
       


    }

    /** Pannello dopo Login: UTENTE**/
    public  JPanel mainPanelAfterLoginStudent () throws RemoteException, NotBoundException {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainContainer,
                        "Sei sicuro di voler chiuedere questa finestra?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        student.logout();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            }
        });
        //Controllo Accesso

        final JPanel panelAfterLogin = new JPanel();
        panelAfterLogin.setLayout(new BorderLayout());
        JMenuBar bar  = new JMenuBar();

        JMenu m1 = new JMenu("Utenti");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Email");


        JMenuItem m1a = new JMenuItem("Visualizza profilo Utente");
        m1a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelAfterLogin.add(BorderLayout.CENTER, viewProfile());
                try {
					EmailSenderStudent().setVisible(false);
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

                setSize(600,400);
            }
        });



        bar.add(m1);
        bar.add(m2);
        bar.add(m3);

        m1.add(m1a);
        JMenuItem m1b = new JMenuItem("Modifica Password");
        JMenuItem m1c = new JMenuItem("Richiedi modifica dei dati personali");
        JMenuItem m1d = new JMenuItem("Logout");
        JMenuItem m2a = new JMenuItem("Visualizza corsi");  /**pannello dei corsi**/
        JMenuItem m3a = new JMenuItem("Invia una mail");
        JMenuItem m2b = new JMenuItem("Iscriviti ad un Corso");
        m1.add(m1b);
        m1.add(m1c);
        m1.add(m1d);

        m2.add(m2a);
        m2.add(m2b);

        m3.add(m3a);


        panelAfterLogin.add(BorderLayout.NORTH, bar);



        m1b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply=JOptionPane.showConfirmDialog(panelAfterLogin, "Vuoi modificare la password?");
                if (reply == JOptionPane.YES_OPTION) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
                    mainContainer.add(changePassword(student));
                    setVisible(true);
                }

            }
        });

        /* invia richiesta all'admin */
        m1c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                mainContainer.add(profileRequest(student));
                setVisible(true);
            }
        });



        m1d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                try {
                    student.logout();
                    welcometoSeatIn();
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                panelAfterLogin.setVisible(false);
            }
        });



        m2a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	  try {
                        mainContainer.removeAll();
                        mainContainer.validate();
                        mainContainer.repaint();
                        mainContainer.add(courseInStudyPlan());
					    setVisible(true);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
                      e1.printStackTrace();
                  }

            }
        });


        m2b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					panelAfterLogin.add(addOptionalCourse(student));
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setVisible(true);
            }
        });


        m3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mainContainer.removeAll();
                    mainContainer.repaint();
                    mainContainer.validate();
                    mainContainer.add(EmailSenderStudent());
					//panelAfterLogin.add(BorderLayout.CENTER, EmailSenderStudent());
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                viewProfile().setVisible(false);
                setSize(600,400);

            }
        });

        Container container=getContentPane();
        container.add(panelAfterLogin);
        setVisible(true);
        return panelAfterLogin;

    }
    public JPanel viewProfile() {
        JPanel profileInformation = new JPanel();
        profileInformation.setLayout(new GridLayout(8,2));
        JLabel personalData[]= {new JLabel("nome"), new JLabel("cognome"), new JLabel("matricola"), new JLabel("email"), new JLabel("corso"), new JLabel("anno immatricolazione"), new JLabel("anno:"), new JLabel("tipologia" )};
        JTextField personalDataTextField[]= {new JTextField("nome"), new JTextField("cognome"),new JTextField("matricola"),
                new JTextField("email"), new JTextField("cdl"), new JTextField("anno immatricolazione"),new JTextField("anno"), new JTextField("tipologia")};

        personalDataTextField[0].setText(student.getName());
        personalDataTextField[1].setText(student.getSurname());
        personalDataTextField[2].setText(student.getID());
        personalDataTextField[3].setText(student.getEmail());
        personalDataTextField[4].setText(student.getDegreeCourse());
        personalDataTextField[5].setText(((Integer)student.getEnrollmentYear()).toString());
        personalDataTextField[6].setText(((Integer)student.getCourseYear()).toString());
        personalDataTextField[7].setText(student.getCourseState());

        for (int i=0;i<8; i++)
        {
            profileInformation.add(personalData[i]);
            profileInformation.add(personalDataTextField[i]);
            personalDataTextField[i].setEditable(false);
        }
        setVisible(true);
        return profileInformation;
    }

    /** PANNELLI NUOVI **/

    public JPanel AddOptionalCourse(SeatInStudent s) throws SQLException, RemoteException {
        JPanel addCoursePanel = new JPanel();
        addCoursePanel.setLayout(new FlowLayout());


        List<String[]> degreeAvailable = s.studentDegreeWithoutStudentOne();
        String[] degreeCourse = new String[degreeAvailable.size() + 1];
        degreeCourse[0] = "-------------";
        int i = 1;


        //in questo modo ottengo tutti i corsi di laurea registrati sulla piattaforma che sono differenti da quello dell'utente
        for (String[] degreeAv : degreeAvailable) {
            degreeCourse[i] = degreeAv[0];
            i++;
        }


        //significa che non ci sono corsiDiLaurea disponibili a parte quello per cui gia' appartiene l'utente
        if (degreeCourse.length == 1)
            JOptionPane.showMessageDialog(addCoursePanel, "ATTENZIONE: nessun corso di laurea disponibile");


        //ottengo tutte le info dei corsi per il corso di laurea prescelto
        JComboBox course = new JComboBox(degreeCourse);
        addCoursePanel.add(course);


        // JComboBox optionalCourse = new JComboBox();
        JLabel name = new JLabel();
        JLabel teacher = new JLabel();
        JLabel description = new JLabel();


        //addCoursePanel.add(optionalCourse);
        course.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ButtonGroup g = new ButtonGroup();
                    List<String[]> courses = new ArrayList<String[]>();
                    //optionalCourse.removeAllItems();
                    for (String[] d : degreeAvailable) {
                        if ((course.getSelectedItem().toString()).equals(d[0])) {
                            try {
                                courses = s.courseNotInStudyPlan(d[1]);
                                String[] courseInfoForSubscription = new String[courses.size()];
                                int i = 0;
                                for (String[] c : courses) {
                                    courseInfoForSubscription[i] = (c[1] + "anno accademico: " + c[2]);
                                    System.out.println((c[1] + "anno accademico: " + c[2]));
                                    //(optionalCourse.addItem(courseInfoForSubscription[i]);
                                    JRadioButton rb = new JRadioButton(courseInfoForSubscription[i]);
                                    g.add(rb);
                                    addCoursePanel.add(rb);
                                    //---> nome del corso

                                    addCoursePanel.add(new JLabel(c[1]));
                                    //---> descrizione
                                    addCoursePanel.add(new JLabel(c[3]));
                                    //--->docenti
                                    List<String[]> teachers = s.teacherTeachesCourse(c[0], Integer.parseInt(c[2]));
                                    System.out.println("list size" + teachers.size());
                                    if (teachers.size() > 1)
                                        //significa che ho piu docenti per un corso
                                        for (String[] t : teachers) {

                                            addCoursePanel.add(new JLabel("docenti per il corso: " + t[0] + t[1]));
                                        }
                                    if (teachers.isEmpty()) {
                                        addCoursePanel.add(new JLabel("nessun docente assegnato"));
                                        System.out.println("no docente");
                                    }

                                    if (teachers.size() == 1) {
                                        addCoursePanel.add(new JLabel("docente per il corso: " + teachers.get(0)[0] + teachers.get(0)[1]));
                                        System.out.println("nome:" + teachers.get(0)[0]);
                                    }

                                    i++;
                                }
                                addCoursePanel.setVisible(true);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }


                }
            }
        });


        JButton signUp = new JButton("Iscriviti");
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //String signupCourse = (String) optionalCourse.getSelectedItem();


                /** richiamare metodo Server **/
            }
        });

        JButton back = new JButton("Annulla");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    welcometoSeatIn();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                addCoursePanel.setVisible(false);
            }
        });
        addCoursePanel.add(signUp);
        addCoursePanel.add(back);


        Container container = getContentPane();
        container.add(addCoursePanel);
        setVisible(true);
        setSize(600, 400);

        return addCoursePanel;
    }

    /** Pannelo Teacher **/
    public JPanel teacherPanel() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainContainer,
                        "Sei sicuro di voler chiuedere questa finestra?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        teacher.logout();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            }
        });
        JPanel teacherp = new JPanel();
        teacherp.setLayout(new BorderLayout());
  

        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Profilo");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Email");
        JMenu statistics = new JMenu("Statistiche piattaforma");
        JMenuItem platformStatistics = new JMenuItem("Visualizza statistiche piattaforma");
        statistics.add(platformStatistics);
        JMenuItem m1a = new JMenuItem("Visualizza profilo ");
        m1a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	mainContainer.removeAll();
            	mainContainer.repaint();
            	mainContainer.validate();
            	mainContainer.add(viewProfileTeacher());
            	setVisible(true);
                setSize(600,400);
            }
        });
        JMenuItem m1b = new JMenuItem("Richiesta Modifica Profilo");
                m1b.addActionListener(new ActionListener() {
        	            @Override
        	            public void actionPerformed(ActionEvent e) {
        	                //comprarira un pannello
        	                // in cui il docente puo richiedere modifica del profilo. tale modifica verra' presa in considerazione
        	                //da un amministratore
        	                mainContainer.removeAll();
        	                mainContainer.validate();
        	                mainContainer.repaint();
        	                mainContainer.add(profileRequest(teacher));
        	                setVisible(true);
        	
        	            }
        	        });
        JMenuItem m1c = new JMenuItem("Logout");
        m1c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //faccio logout per salvare nella tabella database

                	mainContainer.removeAll();
 	                mainContainer.validate();
 	                mainContainer.repaint();
 	                welcometoSeatIn();
 	                
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem m2a = new JMenuItem("Iscriviti ai corsi");
        m2a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
					mainContainer.add(addOptionalCourseTeacher(teacher));
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setSize(600,400);
                setVisible(true);
            }
        });

        JMenuItem m3a = new JMenuItem("Invia Email");
        m3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {  mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                mainContainer.add(EmailSenderTeacher());
                setSize(600,400);
                setVisible(true);
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

            }
        });
        JMenuItem m2b = new JMenuItem("Visualizza i corsi in cui insegno");
        m2b.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                 	  try {
                             mainContainer.removeAll();
                             mainContainer.validate();
                             mainContainer.repaint();
                             mainContainer.add(mineCourseTeacher());
                             setSize(600,400);
                             setVisible(true);
     				} catch (RemoteException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (SQLException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (NotBoundException e1) {
                           e1.printStackTrace();
                       }

                 }
             });
        JMenuItem m2c = new JMenuItem("Visualizza corsi");
        m2c.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                 	  try {
                             mainContainer.removeAll();
                             mainContainer.validate();
                             mainContainer.repaint();
                             mainContainer.add(courseInStudyPlanTeacher());
                             setSize(600,400);
                             setVisible(true);
     				} catch (RemoteException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (SQLException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (NotBoundException e1) {
                           e1.printStackTrace();
                       }

                 }
             });
        JMenuItem m1d = new JMenuItem("Modifica password");
        m1d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply=JOptionPane.showConfirmDialog(teacherPanel(), "Vuoi modificare la password?");
                if (reply == JOptionPane.YES_OPTION) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
                    mainContainer.add(changePassword(teacher));
                    setVisible(true);
                }

            }
        });
        

        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        mb.add(statistics);
        m1.add(m1a);
        m1.add(m1d);
        m1.add(m1b);
        m1.add(m1c);
       
        
        m2.add(m2a);
        m2.add(m2b);
        m2.add(m2c);

        m3.add(m3a);
        platformStatistics.addActionListener(new ActionListener() {
        	            @Override
        	            public void actionPerformed(ActionEvent e) {
        	                mainContainer.removeAll();
        	                mainContainer.validate();
        	                mainContainer.repaint();
        	
        	                try {
        	                    mainContainer.add(statisticTeacher());
        	                } catch (RemoteException e1) {
        	                    e1.printStackTrace();
        	                } catch (SQLException e1) {
                            e1.printStackTrace();
        	                }
        	                setVisible(true);
        	            }
                });

        teacherp.add(mb, BorderLayout.NORTH);


        setVisible(true);
        return teacherp;
    }
    public JPanel courseInStudyPlanTeacher() throws RemoteException, SQLException, NotBoundException {
        class MyMouseHandler extends MouseAdapter {
        	@Override
            public void mouseClicked(MouseEvent evt) {
                // you can open a new frame here as
                // i have assumed you have declared "frame" as instance variable
                JLabel contains = (JLabel) evt.getSource();
                String[] data = contains.getText().split(":");
                //year[0] contiene l'anno del corso

                String[] year = data[2].split(Pattern.quote("["));
                System.out.println(year[0]);
                //code[0] contiene il codice del corso selezionato
                String[] code = data[3].split(Pattern.quote("] "));
                System.out.println(code[0]);
                Node tree = new Node("", "", "", "");
                try {
                    tree = teacher.createCourseTree(code[0], Integer.parseInt(year[0].replaceAll("\\s", "")));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                JPanel treepanel = null;
                try {
                    treepanel = viewTree(tree,"t","notEnabled");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                mainContainer.add(treepanel);
                setVisible(true);
            }
        }
      
        //ottengo i corsi per cui l'utente e' gia' iscritto
        List<String[]> courseInStudyPlan =   teacher.showCourseForStudyPlan();
        JPanel showCourses = new JPanel();
        //creo una JLabel per ogni corso!
        showCourses.setLayout(new GridLayout(courseInStudyPlan.size() + 1, 1));
        JLabel title = new JLabel("PAGINE DEI CORSI DISPONIBILI IN BASE AL TUO PROFILO:");
        showCourses.add(title);
        for (String[] c : courseInStudyPlan) {
            JLabel label = new JLabel("CORSO:" + c[0] + ". AA:" + c[2] + "        [codice corso:" + c[1] + "] ");
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
            MyMouseHandler handler = new MyMouseHandler();
            label.addMouseListener(handler);
            showCourses.add(label);
        }
        JButton back=new JButton("indietro");
        showCourses.add(back,BorderLayout.PAGE_END);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(teacherPanel());
                setVisible(true);
            }
        });
        
        return showCourses;
    }
    public JPanel mineCourseTeacher() throws RemoteException, SQLException, NotBoundException {

        class MyMouseHandler extends MouseAdapter {
        	@Override
            public void mouseClicked(MouseEvent evt) {
                // you can open a new frame here as
                // i have assumed you have declared "frame" as instance variable
                JLabel contains = (JLabel) evt.getSource();
                String[] data = contains.getText().split(":");
                //year[0] contiene l'anno del corso

                String[] year = data[2].split(Pattern.quote("["));
                System.out.println(year[0]);
                //code[0] contiene il codice del corso selezionato
                String[] code = data[3].split(Pattern.quote("] "));
                System.out.println(code[0]);
                Node tree = new Node("", "", "", "");
                try {
                    tree = teacher.createCourseTree(code[0], Integer.parseInt(year[0].replaceAll("\\s", "")));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                JPanel treepanel = null;
                try {
                    treepanel = viewTree(tree,"t","enabled");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                mainContainer.add(treepanel);
                setVisible(true);

            }
        }
        teacher.connection();
        //ottengo i corsi per cui l'utente e' gia' iscritto
        List<String[]> courseInStudyPlan = teacher.getInfoCoursesTeached();
        JPanel showCourses = new JPanel();
        //creo una JLabel per ogni corso!
        showCourses.setLayout(new GridLayout(courseInStudyPlan.size() + 1, 1));
        JLabel title = new JLabel("PAGINE DEI CORSI DISPONIBILI IN BASE AL TUO PROFILO:");
        showCourses.add(title);
        for (String[] c : courseInStudyPlan) {
            JLabel label = new JLabel("CORSO:" + c[0] + ". AA:" + c[2] + "        [codice corso:" + c[1] + "] ");
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
            MyMouseHandler handler = new MyMouseHandler();
            label.addMouseListener(handler);
            showCourses.add(label);
        }
        JButton back=new JButton("indietro");
        showCourses.add(back,BorderLayout.PAGE_END);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(teacherPanel()); 
                setVisible(true);
            }
        });
        setVisible(true);
        mainContainer.add(showCourses);
        return showCourses;
    }
    public JPanel addOptionalCourseTeacher(SeatInTeacher t) throws SQLException, RemoteException{
        JPanel addCoursePanel = new JPanel();
        addCoursePanel.setLayout(new FlowLayout());
        //tutti i corsi di laurea disponibili senza contare quello a cui è gia' iscritto lo studente
        List<String[]> degreeAvailable = t.courseNotInStudyPlanTeacher();
        String[] degreeCourse = new String[degreeAvailable.size()];
        int i = 0;
        JButton signUp = new JButton("Iscriviti");


        //in questo modo ottengo tutti i corsi di laurea registrati sulla piattaforma che sono differenti da quello dell'utente
        //degree[0]contiene nome
        //degree[1] contiene codice
        for (String[] degreeAv : degreeAvailable) {
            degreeCourse[i] = degreeAv[0];
            i++;
        }


        //significa che non ci sono corsiDiLaurea disponibili a parte quello per cui gia' appartiene l'utente
       // if (degreeCourse.length == 1)
         //   JOptionPane.showMessageDialog(addCoursePanel, "ATTENZIONE: nessun corso di laurea disponibile");


        JTextArea areatext = new JTextArea(10, 15);
        //prima combo box
        JComboBox course = new JComboBox(degreeCourse);

        JComboBox optionalCourse = new JComboBox();
        optionalCourse.removeAllItems();
        course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    optionalCourse.removeAllItems();
                    if (course.getSelectedIndex()==0)
                        areatext.removeAll();
                    List<String[]> coursesbyDegree = new ArrayList<>();
                    for (String[] degreeAv : degreeAvailable)
                        if (course.getSelectedItem().equals(degreeAv[0])) {

                            try {
                                coursesbyDegree = t.courseNotInStudyPlanTeacher();
                                System.out.println("courses by degree" + coursesbyDegree.size());
                                for (String[] x : coursesbyDegree) {
                                    optionalCourse.addItem("codice corso:" + x[0] + ".Anno accademico:" + x[2]);
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }

                        }

                    final List<String[]> courseBYdegree=coursesbyDegree;
                    optionalCourse.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (optionalCourse.getSelectedIndex() != -1) {
                                System.out.println(course.getSelectedIndex());
                                String x = optionalCourse.getSelectedItem().toString();
                                x = x.replaceAll("codice corso:", "");
                                x = x.replaceAll("Anno accademico:", "");
                                String[] y = x.split(Pattern.quote("."));
                                for (String[] info : courseBYdegree) {
                                    if (y[0].equals(info[0]) && y[1].equals(info[2])) {
                                        List<String[]> teachers = t.teacherTeachesCourse(info[0], Integer.parseInt(info[2]));
                                        areatext.setText("nome corso:" + info[1] + "\n" +
                                                "descrizione:" + info[3] + "\n" + "docenti del corso:");
                                        System.out.println("codice" + info[0] +
                                                "nome " + info[1] + "anno" + info[2] + "descrizione" + info[3]);
                                        for (String[] teacher : teachers)
                                            areatext.append(teacher[0] + " " + teacher[1]);
                                    } else
                                        System.out.println("non ho trovato ");
                                }


                            }

                        }


                    });
            }
        });



        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String x = optionalCourse.getSelectedItem().toString();
                x = x.replaceAll("codice corso:", "");
                x = x.replaceAll("Anno accademico:", "");
                String[] y = x.split(Pattern.quote("."));
                //y[0] codice
                // y1 anno
                List<String> teacherEmail=new ArrayList<>();
                try {
                    teacherEmail=t.teacherEmailForStudentEmailSender(y[0],Integer.parseInt(y[1]));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                try {
                    t.courseSubscription(teacherEmail, y[1],y[0]);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }


                int reply=JOptionPane.showConfirmDialog(addCoursePanel, "stai tentando di iscriverti al corso selezionato. Continuare?");
                if (reply == JOptionPane.YES_OPTION) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
               
                        mainContainer.add(teacherPanel());
                
                    setVisible(true);
                }

            }
        });

        JButton back = new JButton("Annulla");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // welcometoSeatIn();
                addCoursePanel.removeAll();
                addCoursePanel.validate();
                addCoursePanel.repaint();
                
                	teacherPanel();
               
            }
        });
        addCoursePanel.add(course);
        addCoursePanel.add(optionalCourse);
        addCoursePanel.add(signUp);
        addCoursePanel.add(back);
        addCoursePanel.add(areatext);


        Container container = getContentPane();
        container.add(addCoursePanel);
        setVisible(true);
        setSize(600, 400);
        return addCoursePanel;
    }

    /** pannello email
     * @throws SQLException 
     * @throws RemoteException **/
    public JPanel EmailSenderTeacher() throws RemoteException, SQLException{
    	  JComboBox objectComboBox= new JComboBox();
     	 JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());
        JTextField email = new JTextField(teacher.getEmail());
        //JPasswordField pasw = new JPasswordField();
        JLabel subject = new JLabel("inserisci oggetto della email");
       ///creazione lista corsi tenuti da docente
        List<String[]> courses = teacher.getInfoCoursesTeached();
        String vector[] = new String[courses.size() + 1];
        int i = 1;
        vector[0] = "------------------";
        for (String[] c : courses) {
            vector[i] = c[0] + "   annoAccademico" + c[2];
            i++;
        }
        final List<String[]> course = courses;
   
        // List<String[]> teacheremail = student.teacherEmailForStudentEmailSender();
        JComboBox teacherlist;

        JComboBox courseList = new JComboBox(vector);
        JComboBox emailStudent = new JComboBox();

        //evento lista studenti per corso
        courseList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    try {
                        if (courseList.getSelectedIndex() != 0) {
                            String [] x=courseList.getSelectedItem().toString().split("annoAccademico");
                            //x0 contiene nome
                            //x1 contiene anno
                            x[1].replaceAll("\\s","");
                            x[0].replaceAll("\\s","");
                            String code="";
                            for (String[] c: course)
                            {
                                System.out.print(x[0]+ "and" +c[0] + "   "+ x[1]+ "and" + c[2]);
                                if (x[0].equals(c[0]) && x[1].equals(c[2]))
                                    code=c[1];
                            }
                            
                       	 List<String> emailStudentForSend = teacher.getStudentsEmailforNewsletter(course.get(courseList.getSelectedIndex() - 1)[1],
                       			 Integer.parseInt((course.get(courseList.getSelectedIndex() - 1)[2])));
                            emailStudent.addItem("--------");
                            Node node=teacher.createCourseTree(code, Integer.parseInt(x[1]));
                            List<Node> nodeList =node.getChildren();
                            for (Node n: nodeList)
                                objectComboBox.addItem(n.getName());
                            int i = 0;
                            for (String m : emailStudentForSend) {
                                emailStudent.addItem(m);
                            }
                        } else
                            emailStudent.addItem("-------------");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
        String to = "";

        JTextArea body = new JTextArea(10, 40);

        //invio email a tutti gli studenti iscritti al corso
        JButton sendAll = new JButton("Invia a Tutti");
        sendAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();
              //  String teacherSelect = (String) courseList.getSelectedItem();
              //  String adminrSelect = (String) emailStudent.getSelectedItem();
                JPasswordField password = new JPasswordField();
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("inserisci password prima di inviare la mail"),
                        password};
                int result = JOptionPane.showConfirmDialog(password, inputs, "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String rootPass = new String(password.getPassword());
                    try {	
		                     if (courseList.getSelectedIndex() != 0) {
		                    	 List<String> emailStudentForSend = teacher.getStudentsEmailforNewsletter(course.get(courseList.getSelectedIndex() - 1)[1],
		                    			 Integer.parseInt((course.get(courseList.getSelectedIndex() - 1)[2])));
			                    	 teacher.sendEmailForNewsletter(teacher.getEmail(), rootPass, emailStudentForSend, "subject", bodyemail);
		                     }
						} catch (RemoteException | MessagingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                
                /** richiamare metodo email**/
                JOptionPane.showMessageDialog(emailp, "invio a tutti");
            }
   
        });
        
      ///creazione lista corsi a cui è iscritto docente
        List<String[]> coursesMine = teacher.showCourseForStudyPlan();
        String vectorMine[] = new String[coursesMine.size() + 1];
        int j = 1;
        vector[0] = "------------------";
        for (String[] cMine : coursesMine) {
            vector[j] = cMine[0] + "   annoAccademico" + cMine[2];
            j++;
        }
        final List<String[]> courseMine = coursesMine;
        JComboBox courseListMine = new JComboBox(vectorMine);
        JComboBox emailTeacherTo = new JComboBox();
        
        courseListMine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    try {
                        if (courseListMine.getSelectedIndex() != 0) {
                        	
                            List<String> emailTeacherForSend = (teacher.teacherEmailForStudentEmailSender(courseMine.get(courseListMine.getSelectedIndex() - 1)[1], Integer.parseInt((courseMine.get(courseListMine.getSelectedIndex() - 1)[2]))));//codcorso,//anno
                            emailTeacherTo.addItem("--------");
                            int i = 0;
                            for (String m : emailTeacherForSend) {
                                emailTeacherTo.addItem(m);
                            }
                        } else
                            emailTeacherTo.addItem("-------------");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
        //invio email a studente selezionato o docente selezionato
        JButton send = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();
                if (courseList.getSelectedIndex() != 0) {
               	 String teacherSelect = (String) courseList.getSelectedItem();
                    String adminrSelect = (String) emailStudent.getSelectedItem();
                    JPasswordField password = new JPasswordField();
                    final JComponent[] inputs = new JComponent[]{
                            new JLabel("inserisci password prima di inviare la mail"),
                            password};
                    int result = JOptionPane.showConfirmDialog(password, inputs, "", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String rootPass = new String(password.getPassword());
                        String to = emailStudent.getSelectedItem().toString();
                        try {
   						teacher.sendEmail(to, rootPass, "", body.getText());
   					} catch (RemoteException | MessagingException e1) {
   						// TODO Auto-generated catch block
   						e1.printStackTrace();
   					}
                    }
                    /** richiamare metodo email**/
                    JOptionPane.showMessageDialog(emailp, teacherSelect);
                }else if(courseListMine.getSelectedIndex() != 0){
               	 String teacherSelect = (String) courseListMine.getSelectedItem();
                    String adminrSelect = (String) emailTeacherTo.getSelectedItem();
                    JPasswordField password = new JPasswordField();
                    final JComponent[] inputs = new JComponent[]{
                            new JLabel("inserisci password prima di inviare la mail"),
                            password};
                    int result = JOptionPane.showConfirmDialog(password, inputs, "", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String rootPass = new String(password.getPassword());
                        String to = emailTeacherTo.getSelectedItem().toString();
                        try {
   						teacher.sendEmail(to, rootPass, "", body.getText());
   					} catch (RemoteException | MessagingException e1) {
   						// TODO Auto-generated catch block
   						e1.printStackTrace();
   					}
                    }
                    /** richiamare metodo email**/
                    JOptionPane.showMessageDialog(emailp, teacherSelect);
                }
                

            }
        });
        
        emailp.add(new JLabel("email"));
        emailp.add(email);
        JPanel panelAlignment =new JPanel();
        panelAlignment.setLayout(new GridLayout(5,2));

       panelAlignment.add(new JLabel("Seleziona Corso che insegno", (int) CENTER_ALIGNMENT));
       panelAlignment.add(courseList);
       panelAlignment.add(new JLabel("Seleziona Studente", (int) CENTER_ALIGNMENT));
       panelAlignment.add(emailStudent);
       panelAlignment.add(new JLabel("Seleziona Corso che seguo", (int) CENTER_ALIGNMENT));
       panelAlignment.add(courseListMine);
       panelAlignment.add(new JLabel("Seleziona Docente", (int) CENTER_ALIGNMENT));
       panelAlignment.add(emailTeacherTo);
       panelAlignment.add(new JLabel("Oggetto: ",(int) CENTER_ALIGNMENT));
       panelAlignment.add(objectComboBox);
       
       
       emailp.add(panelAlignment);

        emailp.add(body);
       
        
        //emailp.add() aggiungere oggetto

        emailp.add(send);
        emailp.add(sendAll);
        JButton back=new JButton("indietro");
        emailp.add(new JLabel(""));
        emailp.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(teacherPanel());
                setVisible(true);
            }
        });
        

        setSize(600, 400);
        setVisible(true);
        return emailp;
   }
    public JPanel EmailSenderStudent() throws RemoteException, SQLException{
    	 JPanel emailp = new JPanel();
         emailp.setLayout(new FlowLayout());
         JTextField email = new JTextField(student.getEmail());
        JComboBox objectComboBox= new JComboBox();

         List<String[]> courses = student.showCourseForStudyPlan();
         String vector[] = new String[courses.size() + 1];
         int i = 1;
         vector[0] = "------------------";
         for (String[] c : courses) {
             vector[i] = c[0] + "annoAccademico" + c[2];
             i++;
         }

         final List<String[]> course = courses;
         // List<String[]> teacheremail = student.teacherEmailForStudentEmailSender();
         JComboBox teacherlist;

         JComboBox courseList = new JComboBox(vector);
         JComboBox emailTeacher = new JComboBox();
         courseList.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 // for (int i = 0; i < course.size(); i++) {
                 //if (course.get(i)[0].equals(courseList.getSelectedIndex()+1))
                 //ho selezionato il corso, devo trovare il docente per quel corso.
                 //  System.out.println("ora cerco i docenti"+course.get(i)[0]);
                 {
                     try {
                         if (courseList.getSelectedIndex() != 0) {
                             String [] x=courseList.getSelectedItem().toString().split("annoAccademico");
                             //x0 contiene nome
                             //x1 contiene anno
                             x[1].replaceAll("\\s","");
                             x[0].replaceAll("\\s","");
                             String code="";
                             for (String[] c: course)
                             {
                                 System.out.print(x[0]+ "and" +c[0] + "   "+ x[1]+ "and" + c[2]);
                                 if (x[0].equals(c[0]) && x[1].equals(c[2]))
                                     code=c[1];
                             }


                             List<String> emailTeacherForSend = (student.teacherEmailForStudentEmailSender(code, Integer.parseInt(x[1])));//codcorso,//anno
                             Node node=student.createCourseTree(code, Integer.parseInt(x[1]));
                             List<Node> nodeList =node.getChildren();
                             for (Node n: nodeList)
                                 objectComboBox.addItem(n.getName());
                             emailTeacher.addItem("--------");
                             int i = 0;
                             for (String m : emailTeacherForSend) {
                                 emailTeacher.addItem(m);

                             }
                         } else
                             emailTeacher.addItem("-------------");
                     } catch (SQLException e1) {
                         e1.printStackTrace();
                     } catch (RemoteException e1) {
                         e1.printStackTrace();
                     }
                 }
             }

         });
         String to = "";

         JTextArea body = new JTextArea(10, 40);
         JButton send = new JButton("Invia Email");
         send.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {

                 String bodyemail = body.getText();
                 String teacherSelect = (String) courseList.getSelectedItem();
                 String adminrSelect = (String) emailTeacher.getSelectedItem();
                 JPasswordField password = new JPasswordField();
                 final JComponent[] inputs = new JComponent[]{
                         new JLabel("inserisci password prima di inviare la mail"),
                         password};
                 int result = JOptionPane.showConfirmDialog(password, inputs, "", JOptionPane.OK_CANCEL_OPTION);
                 if (result == JOptionPane.OK_OPTION) {
                     String rootPass = new String(password.getPassword());
                     String to = emailTeacher.getSelectedItem().toString();
                     try {
                         JOptionPane.showMessageDialog(emailp, "email inviata");

						mainContainer.removeAll();
						mainContainer.repaint();
						mainContainer.validate();
						mainContainer.add(mainPanelAfterLoginStudent());
                         student.sendEmail(to, rootPass, objectComboBox.getSelectedItem().toString(), body.getText());
						setVisible(true);
					} catch (RemoteException | MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotBoundException e1) {
                         e1.printStackTrace();
                     }
                 }

                 /** richiamare metodo email**/



             }
         });

         emailp.add(new JLabel("email"));
         emailp.add(email);
         JPanel panelAlignment =new JPanel();
         panelAlignment.setLayout(new GridLayout(3,2));

        panelAlignment.add(new JLabel("Seleziona Corso", (int) CENTER_ALIGNMENT));
        panelAlignment.add(new JLabel("Seleziona Docente", (int) CENTER_ALIGNMENT));
        panelAlignment.add(courseList);

        panelAlignment.add(emailTeacher);
        
      

        emailp.add(panelAlignment);
         emailp.add(body);
         panelAlignment.add(new JLabel("oggetto: "));

         panelAlignment.add(objectComboBox);
       
         //emailp.add() aggiungere oggetto

         emailp.add(send);
         JButton back=new JButton("indietro");
         emailp.add(new JLabel(""));
         emailp.add(back);
         back.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 mainContainer.removeAll();
                 mainContainer.repaint();
                 mainContainer.validate();
                 try {
					mainContainer.add(mainPanelAfterLoginStudent());
				} catch (RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 setVisible(true);
             }
         });
         
         setSize(600, 400);
         setVisible(true);
         return emailp;
    }

    public JPanel EmailSenderAdmin(){

        JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());

        JPanel insertinfo = new JPanel();

        insertinfo.setLayout(new GridLayout(6,2));
        JTextField administratorEmail= new JTextField(administrator.getEmail());
        administratorEmail.setEditable(false);
        insertinfo.add(new JLabel("da: "));
        insertinfo.add(administratorEmail);
        insertinfo.add(new JLabel("per:"));
        JTextField emailTo = new JTextField(30);
        insertinfo.add(emailTo);
        insertinfo.add(new JLabel("oggetto: "));
        JTextField object =new JTextField(10);
        insertinfo.add(object);

        JTextArea body = new JTextArea(10,40);
        JButton send  = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();
                //devo richiedere la password della email inserita
                JPasswordField password = new JPasswordField();
                final JComponent[] inputs = new JComponent[]{
                        new JLabel("inserisci password prima di inviare la mail"),
                        password};
                int result = JOptionPane.showConfirmDialog(password, inputs, "", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String obj = object.getText();
                    try {
                        administrator.sendEmail(emailTo.getText(), password.getText(), obj, bodyemail);


                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
                });

        emailp.add(insertinfo);

        emailp.add(body);
        emailp.add(send);
        JButton back=new JButton("indietro");
        emailp.add(new JLabel(""));
        emailp.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(adminPanel());
                setVisible(true);
            }
        });

        setSize(600,400);
        setVisible(true);
        return emailp;
    }

    /** Panello Admin **/
    public JPanel Viewcorsi(){
        JPanel corsi = new JPanel();
        corsi.setLayout(new FlowLayout());
        JComboBox checkCourse = new JComboBox();
        List<String> nameCourse = new ArrayList<String>(){};
        for(int i =0; i<10;i++) {
            nameCourse.add("Course"+i);
            checkCourse.addItem(nameCourse.get(i));
        }

        JTextArea area1 = new JTextArea(10,15);

        checkCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area1.append(checkCourse.getSelectedItem() + "\n");
                nameCourse.remove(checkCourse.getSelectedIndex());
                checkCourse.removeAllItems();
                for (int k=0; k<nameCourse.size();k++) {
                    checkCourse.addItem(nameCourse.get(k));
                }

            }
        });

        JLabel selectCourse = new JLabel("Seleziona Corso");


        JButton ok = new JButton("Abilita Corsi");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(corsi, area1.getText());
            }
        });
        JButton ko = new JButton("Deseleziona Corso");
        ko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // nameCourse.clear();
                //checkCourse.removeAllItems();
                if(area1.getSelectedText()!=null)

                    checkCourse.addItem(area1.getSelectedText());
                String from = area1.getSelectedText();
                int start = area1.getText().indexOf(from);

                if(start >=0 && from.length() > 0)
                    area1.replaceRange(" ", start, start + from.length());


                else{
                    JOptionPane.showMessageDialog(corsi, "seleziona il corso da  rimuovere");
                }



            }
        });
        corsi.add(selectCourse);

        corsi.add(checkCourse);
        corsi.add(new JLabel(""));
        corsi.add(ko);
        corsi.add(area1);
        corsi.add(ok);
        corsi.add(new JLabel(""));
        corsi.add(new JLabel(""));
        corsi.add(ko);


        setSize(600,400);
        pack();
        setVisible(true);

        return corsi;
    }
    public JPanel changePassword(SeatInPeople person) {
        final JPanel changePassword=new JPanel();
        changePassword.setLayout(new GridLayout(3,2));
        JTextField password=new JTextField();
        JTextField repeatPassword=new JTextField();
        JLabel insertPassword=new JLabel("inserisci password: ");
        JLabel insertPasswordtwice=new JLabel("re-inserisci password: ");
        JButton ok=new JButton("conferma");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (password.getText().equals(repeatPassword.getText()) && !password.getText().equals("")){
                    try {
                        person.setPassword(password.getText());
                        boolean flag=person.updatePassword();
                        if (flag){
                            JOptionPane.showMessageDialog(changePassword,"password modificata correttamente!");
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            if(utenza.equals("Studente")){
                            	 mainContainer.add(mainPanelAfterLoginStudent());
                            	 setVisible(true);
                            }else if(utenza.equals("Docente")){
                           	 	mainContainer.add(teacherPanel());
                           	 	setVisible(true);
                            }else if(utenza.equals("Admin")){
                              	 mainContainer.add(adminPanel());
                              	 setVisible(true);
                            }
                           

                        }
                    } catch (RemoteException | MessagingException e1) {
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        e1.printStackTrace();
                    }
                }
                else
                    JOptionPane.showMessageDialog(password,"Password non corretta. operazione non consentita");
            }
        });
        changePassword.add(insertPassword);
        changePassword.add(password);
        changePassword.add(insertPasswordtwice);
        changePassword.add(repeatPassword);
        changePassword.add(new JLabel(""));
        changePassword.add(ok);
        setVisible(true);
        setSize(600,400);
    return changePassword;
    }
    public JPanel adminPanel()  {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainContainer,
                        "Sei sicuro di voler chiuedere questa finestra?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        administrator.logout();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    System.exit(0);
                }
            }
        });
        final JPanel admin = new JPanel();
        admin.setLayout(new BorderLayout());

        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Profilo");
        JMenu m2 = new JMenu("Corsi");
        JMenu mm= new JMenu("Profili utente");
        JMenu m3 = new JMenu("Email");
        JMenu m4 = new JMenu("Statistiche piattaforma");
        JMenuItem statistics= new JMenuItem("Visualizza statistiche");
        m4.add(statistics);

        statistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(statisticsAdmin());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                setVisible(true);
            }
        });

        JMenuItem lockedProfile= new JMenuItem("Abilita profilo bloccato");
             JMenuItem people= new JMenuItem("Abilita utente");
        JMenuItem teacher= new JMenuItem("Associa ad un corso un insegnante");
         JMenuItem changeProfileRequest =new JMenuItem("Conferma una richiesta di modifica profilo");
        mm.add(lockedProfile);
        mm.add(people);
        m2.add(teacher);
        JMenuItem updateProfile= new JMenuItem("Modifica profilo");
        updateProfile.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                mainContainer.removeAll();
                                                mainContainer.validate();
                                                mainContainer.repaint();
                                                mainContainer.add(profileRequest(administrator));
                                                setVisible(true);
                                            }
                                        }
        );
        m1.add(updateProfile);
        people.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(enableUser());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                setVisible(true);
            }
        });
        lockedProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //abilita profilo bloccato
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(unlockedProfileUser());
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                setVisible(true);
            }
        });
        people.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //mi permette di abilitare un profilo che si trova all'interno del file csv.
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                //nome del pannello mainContainer.add()
            }
        });
        mm.add(changeProfileRequest);
        changeProfileRequest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(requestEditAdmin());
                    setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                setVisible(true);

            }
        });

        teacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(teacherCourseEnable());
                    setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
       changeProfileRequest.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               //permette di visualizzare tutte le richieste di modifica, ed in caso approvarle
           }
       });

        JMenuItem m1i = new JMenuItem("Visualizza il tuo profilo");
        m1i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                mainContainer.add(viewProfileAdmin());
             //   admin.add(BorderLayout.CENTER, viewProfileAdmin());
                setSize(600,400);
                setVisible(true);
            }
        });

        JMenuItem m3i = new JMenuItem("Abilita Corsi");
        m3i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    mainContainer.add(courseEnable());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                setVisible(true);

            }
        });
        JMenuItem m4i = new JMenuItem("Logout");
        m4i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    administrator.logout();
                    welcometoSeatIn();
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                admin.setVisible(false);
            }
        });
        JMenuItem m5i = new JMenuItem("Invia email");
        m5i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Viewcorsi().setVisible(false);
                admin.add(EmailSenderAdmin(), BorderLayout.CENTER);
                setSize(600,400);
                //Viewcorsi().setVisible(false);
            }
        });
        
        JMenuItem m2a = new JMenuItem("Visualizza corsi");
        m2a.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                 	  try {
                             mainContainer.removeAll();
                             mainContainer.validate();
                             mainContainer.repaint();
                             mainContainer.add(courseInStudyPlanAdmin());
                             setSize(600,400);
                             setVisible(true);
     				} catch (RemoteException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (SQLException e1) {
     					// TODO Auto-generated catch block
     					e1.printStackTrace();
     				} catch (NotBoundException e1) {
                           e1.printStackTrace();
                       }

                 }
             });
        JMenuItem m1d = new JMenuItem("Modifica password");
        m1d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply=JOptionPane.showConfirmDialog(teacherPanel(), "Vuoi modificare la password?");
                if (reply == JOptionPane.YES_OPTION) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
                    mainContainer.add(changePassword(administrator));
                    setVisible(true);
                }

            }
        });
        mb.add(m1);
        mb.add(m2);
        mb.add(mm);
        mb.add(m3);
        mb.add(m4);
        m1.add(m1i);
        m1.add(m1d);
        m2.add(m3i);
        m2.add(m2a);
        m1.add(m4i);
        m3.add(m5i);


        admin.add(mb,BorderLayout.NORTH);
        setVisible(true);
        setSize(600,400);
        return admin;
    }
    
    public JPanel profileRequest(SeatInPeople person) {
        String type = "";
        final String[] field = {""};
        JComboBox change;

        if (person instanceof SeatInStudent)
            type = "studente";
        if (person instanceof SeatInAdmin)
            type = "amministratore";
        if (person instanceof SeatInTeacher)
            type = "docente";
        final String t = type;
        final String[] fieldRequest = {""};
        JLabel text = new JLabel("Quale dato si desidera modificare?:");
        JPanel profileReq = new JPanel();
        profileReq.setLayout(new GridLayout(5, 1));
        profileReq.add(text);
        if (type.equals("studente")) {
            String[] student = {"-------", "nome", "cognome", "email", "matricola", "anno immatricolazione", "corso di laurea", "stato corso di laurea", "anno corso"};
            change = new JComboBox(student);
            profileReq.add(change);
        } else {
            String[] teacherAdmin = {"-------------", "nome", "cognome", "email", "matricola", "dipartimento"};
            change = new JComboBox(teacherAdmin);

            profileReq.add(change);

        }
        final String tipology=type;

        change.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    field[0] = change.getSelectedItem().toString();
                }

            }
        });

        JTextField textChange = new JTextField(70);
        profileReq.add(textChange);
        JButton ok = new JButton("richiedi modifica");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(profileReq, "ATTENZIONE! la richiesta verra' visionata ed eventualmente approvata da un amministratore. continuare?");
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        person.changeProfileRequest(person.getEmail(), field[0], textChange.getText(), t);
                        mainContainer.remove(profileReq);
                        mainContainer.removeAll();
                        mainContainer.repaint();
                        mainContainer.validate();
                        if (tipology.equals("studente")){
                        mainContainer.add(mainPanelAfterLoginStudent());
                        setVisible(true);}
                        if (tipology.equals("docente")){
                            mainContainer.add(teacherPanel());
                        setVisible(true);}
                        if (tipology.equals("amministratore")){
                            mainContainer.add(adminPanel());
                        setVisible(true);}
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        JButton back=new JButton("indietro");
        profileReq.add(new JLabel(""));
        profileReq.add(new JLabel(""));
        profileReq.add(new JLabel(""));
        profileReq.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                if (tipology.equals("studente")){
                    try {
						mainContainer.add(mainPanelAfterLoginStudent());
					} catch (RemoteException | NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    setVisible(true);}
                    if (tipology.equals("docente")){
                        mainContainer.add(teacherPanel());
                    setVisible(true);}
                    if (tipology.equals("amministratore")){
                        mainContainer.add(adminPanel());
                    setVisible(true);}
                setVisible(true);
            }
        });
        
        profileReq.add(ok);
        setSize(600, 400);
        setVisible(true);
        return profileReq;
    }
    public JPanel courseInStudyPlanAdmin() throws RemoteException, SQLException, NotBoundException {
        class MyMouseHandler extends MouseAdapter {
        	@Override
            public void mouseClicked(MouseEvent evt) {
                // you can open a new frame here as
                // i have assumed you have declared "frame" as instance variable
                JLabel contains = (JLabel) evt.getSource();
                String[] data = contains.getText().split(":");
                //year[0] contiene l'anno del corso

                String[] year = data[2].split(Pattern.quote("["));
                System.out.println(year[0]);
                //code[0] contiene il codice del corso selezionato
                String[] code = data[3].split(Pattern.quote("] "));
                System.out.println(code[0]);
                Node tree = new Node("", "", "", "");
                try {
                    tree = administrator.createCourseTree(code[0], Integer.parseInt(year[0].replaceAll("\\s", "")));
                    //devo salvare il fatto che un admin sta visualizzando/interagendo con un corso
                    administrator.registerUserIsConsultingCourse(code[0],Integer.parseInt(year[0].replaceAll("\\s", "")),"a");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                JPanel treepanel = null;
                try {
                    treepanel = viewTree(tree,"a","notEnabled");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                mainContainer.add(treepanel);
                setVisible(true);

            }
        }
        administrator.connection();
        //ottengo i corsi per cui l'utente e' gia' iscritto
        List<String[]> courseInStudyPlan =   administrator.allCourse();
        JPanel showCourses = new JPanel();
        //creo una JLabel per ogni corso!
        showCourses.setLayout(new GridLayout(courseInStudyPlan.size() + 1, 1));
        JLabel title = new JLabel("PAGINE DEI CORSI DISPONIBILI IN BASE AL TUO PROFILO:");
        showCourses.add(title);
        for (String[] c : courseInStudyPlan) {
            JLabel label = new JLabel("CORSO:" + c[0] + ". AA:" + c[2] + "        [codice corso:" + c[1] + "] ");
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
            MyMouseHandler handler = new MyMouseHandler();
            label.addMouseListener(handler);
            showCourses.add(label);
        }
        JButton back=new JButton("indietro");
        showCourses.add(back,BorderLayout.PAGE_END);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(adminPanel());
              
            }
        });
        setVisible(true);
        mainContainer.add(showCourses);
        return showCourses;
    }
 
    public JPanel courseInStudyPlan() throws RemoteException, SQLException, NotBoundException {
        class MyMouseHandler extends MouseAdapter {
        	@Override
            public void mouseClicked(MouseEvent evt) {
                // you can open a new frame here as
                // i have assumed you have declared "frame" as instance variable
                JLabel contains = (JLabel) evt.getSource();
                String[] data = contains.getText().split(":");
                //year[0] contiene l'anno del corso

                String[] year = data[2].split(Pattern.quote("["));
                System.out.println(year[0]);
                //code[0] contiene il codice del corso selezionato
                String[] code = data[3].split(Pattern.quote("] "));
                System.out.println(code[0]);
                Node tree = new Node("", "", "", "");
                try {
                    tree = student.createCourseTree(code[0], Integer.parseInt(year[0].replaceAll("\\s", "")));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                JPanel treepanel = null;
                try {
                    treepanel = viewTree(tree,"s","notEnabled");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
                mainContainer.add(treepanel);
                setVisible(true);

            }
        }
        student.connection();
        //ottengo i corsi per cui l'utente e' gia' iscritto
        List<String[]> courseInStudyPlan = student.showCourseForStudyPlan();
        JPanel showCourses = new JPanel();
        //creo una JLabel per ogni corso!
        showCourses.setLayout(new GridLayout(courseInStudyPlan.size() + 1, 1));
        JLabel title = new JLabel("PAGINE DEI CORSI DISPONIBILI IN BASE AL TUO PROFILO:");
        showCourses.add(title);
        for (String[] c : courseInStudyPlan) {
            JLabel label = new JLabel("CORSO:" + c[0] + ". AA:" + c[2] + "        [codice corso:" + c[1] + "] ");
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
            MyMouseHandler handler = new MyMouseHandler();
            label.addMouseListener(handler);
            showCourses.add(label);
        }
        JButton back=new JButton("indietro");
        showCourses.add(back,BorderLayout.PAGE_END);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                try {
                    mainContainer.add(mainPanelAfterLoginStudent());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setVisible(true);
        mainContainer.add(showCourses);
        return showCourses;
    }
  

    public JPanel viewTree(Node node, String profile, String way) throws IOException, NotBoundException {
        JButton delete = new JButton("modifica/elimina");
        JButton upload = new JButton("carica una risorsa");
        if (profile.equals("s"))
            student.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "s");
        if (profile.equals("a"))
            administrator.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "a");
        if (profile.equals("t"))
            teacher.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "t");
        if (profile.equals("t") && way.equals("enabled")) {
            teacher.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "t");

            upload.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
                    mainContainer.add(uploadResource(node));
                    upload.setVisible(true);
                }
            });
            JButton b2 = new JButton("rimuovi una risorsa");
        }
        System.out.print("user is having login ");

        JPanel tree = new JPanel();
        int i = node.treeLength();
        tree.setLayout(new GridLayout(i, 7));
        JTextField name = new JTextField();
        name.setText(node.getName());
        List<JLabel[]> labels = new ArrayList<>();

        JTextField description = new JTextField();
        description.setText(node.getDescription());
        List<Node> nodelist = node.getChildren(); //restituisce i figli del primo livello
        JLabel label = new JLabel();
        JPanel p = new JPanel(new GridLayout(3, 2));

        p.add(new JLabel("Nome Corso"));
        p.add(name);
        p.add(new JLabel("Descrizione"));
        p.add(description);
        p.add(new JLabel("SPECIFICARE DIRECTORY (usata per il download di file) : "));
        JTextField directory = new JTextField(30);
        p.add(directory);

        tree.add(p);


        class MyMouseHandler extends MouseAdapter {
            ArrayList<String> codeToZip = new ArrayList<>();

            @Override
            public void mouseClicked(MouseEvent evt) {
                JLabel source = (JLabel) evt.getSource();
                System.out.println(source);

                for (JLabel[] label : labels) {
                    if (label[0].equals(source)) {
                        if (directory.getText().equals(""))
                            JOptionPane.showMessageDialog(tree, "Il download delle risorse non verra' eseguito finche' non verra' specificata una directory!");

                        else {
                            //posso eseguire il download.
                            //caso unico file:
                            if (source.getText().contains("."))
                            //e' sicuramente un file
                            {


                                try {
                                    byte[] bytea = null;
                                    if (profile.equals("s"))
                                        bytea = student.getFile(label[1].getText(), node.getCode(), node.getYear());
                                    if (profile.equals("a"))
                                        bytea = administrator.getFile(label[1].getText(), node.getCode(), node.getYear());
                                    if (profile.equals("t"))
                                        bytea = teacher.getFile(label[1].getText(), node.getCode(), node.getYear());

                                    int response = JOptionPane.showConfirmDialog(tree, "Il file e' pronto per il download. Vuoi aggiungere un altro file?");
                                    if (response == JOptionPane.NO_OPTION) {
                                        if (codeToZip.size() == 0) {
                                            try (FileOutputStream fos = new FileOutputStream(directory.getText() + "/" + source.getText())) {
                                                //scrive nella cartella specificata dall'utente
                                                fos.write(bytea);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                        } else {
                                            codeToZip.add(label[1].getText());
                                            //posso zippare i contenuti scelti
                                            ArrayList<String> pathFiles = new ArrayList<String>();
                                            for (String l : codeToZip) {
                                                String fileName = "";
                                                try {

                                                    byte[] bytex = null;
                                                    if (profile.equals("s")) {
                                                        bytex = student.getFile(l, node.getCode(), node.getYear());
                                                        fileName = student.findFileName(l);
                                                    }
                                                    if (profile.equals("a")) {
                                                        bytex = administrator.getFile(l, node.getCode(), node.getYear());
                                                        fileName = administrator.findFileName(l);
                                                    }

                                                    if (profile.equals("t")) {
                                                        bytex = teacher.getFile(l, node.getCode(), node.getYear());
                                                        fileName = teacher.findFileName(l);
                                                    }
                                                    Utility.createDirectory(directory.getText() + "/Archivio");
                                                    FileOutputStream fos = new FileOutputStream(directory.getText() + "/Archivio/" + fileName);
                                                    fos.write(bytex);
                                                    pathFiles.add(directory.getText() + "/Archivio/" + fileName);
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                Utility.zip(pathFiles, directory.getText() + "/" + node.getName() + ".zip");

                                                //ricreo per uso successivo
                                                // createDirectory("/Users/gdelvecchio/Desktop/laboratorioB/Files");

                                            }
                                            //cancello tutti i file che avevo scritto su client
                                            Utility.deleteDir(new File(directory.getText() + "/Archivio/"));

                                        }
                                    }
                                    if (response == JOptionPane.YES_OPTION) {

                                        codeToZip.add(label[1].getText());
                                        System.out.println("lista da zippare" + codeToZip.get(0));
                                    }
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else
                            //è una cartella da zippare
                            {
                                //codice della cartella, da ricercare nel database
                                ArrayList<String> ls = new ArrayList<>();
                                try {
                                    if (profile.equals("s"))
                                        ls = student.findFolder(label[1].getText());
                                    if (profile.equals("a"))
                                        ls = administrator.findFolder(label[1].getText());
                                    if (profile.equals("t"))
                                        ls = teacher.findFolder(label[1].getText());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                ArrayList<String> pathFiles = new ArrayList<String>();
                                for (String l : ls) {
                                    String fileName = "";
                                    try {
                                        byte[] bytex = null;
                                        if (profile.equals("s")) {
                                            bytex = student.getFile(l, node.getCode(), node.getYear());
                                            fileName = student.findFileName(l);
                                        }
                                        if (profile.equals("a")) {
                                            bytex = administrator.getFile(l, node.getCode(), node.getYear());
                                            fileName = administrator.findFileName(l);
                                        }

                                        if (profile.equals("t")) {
                                            bytex = teacher.getFile(l, node.getCode(), node.getYear());
                                            fileName = teacher.findFileName(l);
                                        }

                                        Utility.createDirectory(directory.getText() + "/Archivio");
                                        FileOutputStream fos = new FileOutputStream(directory.getText() + "/Archivio/" + fileName);
                                        fos.write(bytex);
                                        pathFiles.add(directory.getText() + "/Archivio/" + fileName);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Utility.zip(pathFiles, directory.getText() + "/" + node.getName() + ".zip");

                                }
                                //cancello tutti i file che avevo scritto su server
                                Utility.deleteDir(new File(directory.getText() + "/Archivio/"));
                                JOptionPane.showMessageDialog(tree, "cartella zippata!");
                            }
                        }
                    }
                }
            }
        }
        MyMouseHandler handler = new MyMouseHandler();
        JLabel l = new JLabel();
        for (Node n : nodelist) {
            //aggiungo la sezione
            if (n.getVisibility().equals("pubblica") || way.equals("enabled")) {
                tree.add(new JLabel(n.getName()));
                //indentazione 0
                //se la sezione ha figli
                if (n.getChildren().size() != 0) {
                    //potrebbe avere file, cartelle o sottosezioni!
                    List<Node> list = n.getChildren();
                    for (Node nodo : list) {
                        if (nodo.getFile().equals("false") && (nodo.getVisibility().equals("pubblica") || way.equals("enabled"))) {
                            //e' una cartella senza sottosezioni!
                            {
                                //indentazione 1
                                tree.add(l);

                                label = new JLabel(nodo.getName());
                                Font font = label.getFont();
                                Map attributes = font.getAttributes();
                                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                                label.setFont(font.deriveFont(attributes));
                                tree.add(label);
                                JLabel[] temp = {label, new JLabel(nodo.getCode())};
                                labels.add(temp);
                                label.addMouseListener(handler);

                                List<Node> nodeFile = nodo.getChildren();

                                //print di tutti i file
                                for (Node files : nodeFile) {
                                    if (files.getVisibility().equals("pubblica") || way.equals("enabled")) {
                                        //file dentro una cartella
                                        //indentazione 2
                                        for (int z = 0; z < 3; z++)
                                            tree.add(l);
                                        label = new JLabel(files.getName());
                                        Font f = label.getFont();
                                        Map a = font.getAttributes();
                                        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                                        label.setFont(f.deriveFont(a));
                                        tree.add(label);
                                        JLabel[] tempr = {label, new JLabel(files.getCode())};
                                        labels.add(tempr);
                                        label.addMouseListener(handler);

                                    }
                                }
                            }
                            //se è privata non stampo ne la cartella ne i suoi file
                        }
                        if (nodo.getFile().equals("true") && (nodo.getVisibility().equals("pubblica") || way.equals("enabled")))
                        //e' un file senza sottosezioni, che ovviamente non puo' avere figli!!
                        {
                            //indentazione 1
                            //print di tutti i file
                            tree.add(l);

                            label = new JLabel((nodo.getName()));
                            Font font = label.getFont();
                            Map attributes = font.getAttributes();
                            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                            label.setFont(font.deriveFont(attributes));
                            tree.add(label);
                            JLabel[] tempr = {label, new JLabel(nodo.getCode())};
                            labels.add(tempr);
                            label.addMouseListener(handler);
                        } else
                        //significa che e' una sottosezione
                        {
                            if (nodo.getVisibility().equals("pubblica") || way.equals("enabled")) {
                                //indentazione 1
                                tree.add(l);
                                label = new JLabel(nodo.getName());
                                tree.add(label);
                                List<Node> nodes = nodo.getChildren();
                                for (Node nodoo : nodes) {

                                    if (nodoo.getFile().equals("false") && (nodoo.getVisibility().equals("pubblica") || way.equals("enabled")))
                                    //e' una cartella
                                    {
                                        for (int a = 0; a < 4; a++)
                                            tree.add(l);
                                        label = new JLabel(nodoo.getName());
                                        Font font = label.getFont();
                                        Map attributes = font.getAttributes();
                                        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                                        label.setFont(font.deriveFont(attributes));
                                        JLabel[] tempr = {label, new JLabel(nodoo.getCode())};
                                        labels.add(tempr);
                                        label.addMouseListener(handler);
                                        tree.add(label);
                                        List<Node> nodeFil = nodoo.getChildren();
                                        //print di tutti i file
                                        for (Node files : nodeFil) {

                                            if (files.getVisibility().equals("pubblica")) {
                                                //indentazione 5
                                                for (int a = 0; a < 6; a++)
                                                    tree.add(l);

                                                label = new JLabel(files.getName());
                                                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                                                label.setFont(font.deriveFont(attributes));
                                                tree.add(label);
                                                JLabel[] tmpr = {label, new JLabel(files.getCode())};
                                                labels.add(tmpr);
                                                label.addMouseListener(handler);
                                            }
                                        }

                                    }
                                    if (nodoo.getFile().equals("true") && (nodoo.getVisibility().equals("pubblica") || way.equals("enabled")))
                                    //e' un file senza cartella!
                                    {
                                        //indentazione 5
                                        for (int a = 0; a < 5; a++)
                                            tree.add(l);
                                        //print di tutti i file
                                        label = new JLabel(nodoo.getName());
                                        Font font = label.getFont();
                                        Map attributes = font.getAttributes();
                                        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                                        label.setFont(font.deriveFont(attributes));
                                        tree.add(label);
                                        JLabel[] tempr = {label, new JLabel(nodoo.getCode())};
                                        labels.add(tempr);
                                        label.addMouseListener(handler);
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
        JButton back = new JButton();
        tree.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                try {
                    if (profile.equals("s"))
                        student.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "s");
                    if (profile.equals("a"))
                        administrator.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "a");
                    if (profile.equals("t"))
                        teacher.registerUserIsConsultingCourse(node.getCode(), node.getYear(), "t");

                    System.out.print("user is having logout");
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                try {
                    mainContainer.add(mainPanelAfterLoginStudent());
                    setVisible(true);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        if (profile.equals("t") && way.equals("enabled")) {
            tree.add(upload);
            tree.add(delete);
        }
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(deleteInTree(node));
                setVisible(true);
            }
        });
        setVisible(true);
        setSize(600, 400);
        return tree;
    }
    
    public JPanel viewProfileAdmin(){
          JPanel profileInformation = new JPanel();
          profileInformation.setLayout(new GridLayout(8, 2));
          JLabel personalData[] = {new JLabel("nome"), new JLabel("cognome"), new JLabel("matricola"), new JLabel("email"), new JLabel("Dipartimento"), new JLabel("Stato Profilo")};
          JTextField personalDataTextField[] = {new JTextField("nome"), new JTextField("cognome"), new JTextField("matricola"),
                  new JTextField("email"), new JTextField("Dipartimento"), new JTextField("tipologia")};
  
          personalDataTextField[0].setText(administrator.getName());
          personalDataTextField[1].setText(administrator.getSurname());
          personalDataTextField[2].setText(administrator.getID());
          personalDataTextField[3].setText(administrator.getEmail());
          personalDataTextField[4].setText(administrator.getDepartment());
          personalDataTextField[5].setText(administrator.getStateProfile());
  
          for (int i = 0; i < 6; i++) {
              profileInformation.add(personalData[i]);
              profileInformation.add(personalDataTextField[i]);
              personalDataTextField[i].setEditable(false);
          }
          JButton back=new JButton("indietro");
          profileInformation.add(new JLabel(""));
          profileInformation.add(new JLabel(""));
          profileInformation.add(new JLabel(""));
          profileInformation.add(back);
          back.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  mainContainer.removeAll();
                  mainContainer.repaint();
                  mainContainer.validate();
                  mainContainer.add(adminPanel());
                  setVisible(true);
              }
          });
          
          setVisible(true);
          return profileInformation;
      }
 	public JPanel viewProfileTeacher(){
     JPanel profileInformation = new JPanel();
     profileInformation.setLayout(new GridLayout(8, 2));
     JLabel personalData[] = {new JLabel("nome"), new JLabel("cognome"), new JLabel("matricola"), new JLabel("email"), new JLabel("Dipartimento"), new JLabel("Stato Profilo")};
     JTextField personalDataTextField[] = {new JTextField("nome"), new JTextField("cognome"), new JTextField("matricola"),
             new JTextField("email"), new JTextField("cdl"), new JTextField("Dipartimento"), new JTextField("tipologia")};

     personalDataTextField[0].setText(teacher.getName());
     personalDataTextField[1].setText(teacher.getSurname());
     personalDataTextField[2].setText(teacher.getID());
     personalDataTextField[3].setText(teacher.getEmail());
     personalDataTextField[4].setText(teacher.getDepartment());
     personalDataTextField[5].setText(teacher.getStateProfile());

     for (int i = 0; i < 6; i++) {
         profileInformation.add(personalData[i]);
         profileInformation.add(personalDataTextField[i]);
         personalDataTextField[i].setEditable(false);
     }
     JButton back=new JButton("indietro");
     profileInformation.add(new JLabel(""));
     profileInformation.add(new JLabel(""));
     profileInformation.add(new JLabel(""));
     profileInformation.add(back);
     back.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             mainContainer.removeAll();
             mainContainer.repaint();
             mainContainer.validate();
             mainContainer.add(teacherPanel());
             setVisible(true);
         }
     });
     


     setVisible(true);
     return profileInformation;
 }
 	public JPanel userBlocked(){
 	   JPanel userblock = new JPanel();
 	   userblock.setLayout(new FlowLayout());
 	   String[] users = {"Scegli il tipo di utente","Studente", "Docente", "Admin"};
 	   JComboBox type = new JComboBox(users);
 	   userblock.add(type);
 	 
 	   JButton back = new JButton("Esci");
 	   back.addActionListener(new ActionListener() {
 	       @Override
 	       public void actionPerformed(ActionEvent e) {
 	           try {
 	               welcometoSeatIn();
 	           } catch (RemoteException e1) {
 	               e1.printStackTrace();
 	           } catch (NotBoundException e1) {
 	               e1.printStackTrace();
 	           }
 	           userblock.setVisible(false);

 	       }
 	   });
 	   JTextField email = new JTextField(15);
 	   JPasswordField psw = new JPasswordField(15);
 	   JButton send = new JButton("Invia");
 	   send.addActionListener(new ActionListener() {
 	       @Override
 	       public void actionPerformed(ActionEvent e) {
 	    	   utenza=(String) type.getSelectedItem();
 	    	   if(loginCheck(email.getText(),psw.getText())){ 
 	        	   switch (utenza){
 		               case "Studente":
 		            	//   student= new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
 		            	  //student.getInstance();
 		            	   student.setEmail(email.getText());
 		                   student.setPassword(psw.getText());
 		            	   try {
 		    				student.connection();
 		    			} catch (RemoteException | NotBoundException e2) {
 		    				// TODO Auto-generated catch block
 		    				e2.printStackTrace();
 		    			}
 		            	   try {
 		    				if(student.sendSblockRequest()){
 		    					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
 		    				}else{
 		    					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
 		    				}
 		    				
 		    			} catch (RemoteException e1) {
 		    				// TODO Auto-generated catch block
 		    				e1.printStackTrace();
 		    			}
 		            	   break;
 		               case "Docente":
 		            	   teacher = new SeatInTeacher(null, null, null, null, null, null, null,null);
 		            	   teacher.setEmail(email.getText());
 		                   teacher.setPassword(psw.getText());
 		            	   try {
 		    				teacher.connection();
 		    			} catch (RemoteException | NotBoundException e2) {
 		    				// TODO Auto-generated catch block
 		    				e2.printStackTrace();
 		    			}
 		            	  
 		            		   try {
 		           				if(teacher.sendSblockRequest()){
 		           					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
 		           				}else{
 		           					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
 		           				}
 		    					} catch (RemoteException e1) {
 		    						// TODO Auto-generated catch block
 		    						e1.printStackTrace();
 		    					}
 		            		   break;
 		          
 		               case "Admin":
 		            	   administrator = new SeatInAdmin(null, null, null, null, null, null, null,null);
 		            	   administrator.setEmail(email.getText());
 		                   administrator.setPassword(psw.getText());
 		            	   try {
 		    				administrator.connection();
 		    			} catch (RemoteException | NotBoundException e2) {
 		    				// TODO Auto-generated catch block
 		    				e2.printStackTrace();
 		    			}
 		            	   try {
 		          				if(administrator.sendSblockRequest()){
 		          					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
 		          				}else{
 		          					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
 		          				}
 		    					} catch (RemoteException e1) {
 		    						// TODO Auto-generated catch block
 		    						e1.printStackTrace();
 		    					} 
 		            	   break;
 	      		  	}
 	               
 	    	   }else{
 	    		   JOptionPane.showMessageDialog(userBlocked(), "Dati inseriti non correttamente");
 	    	   } 
 	       }
 	   });
 	   userblock.add(new JLabel("Inserisci email"));
 	   userblock.add(email);
 	   userblock.add(new JLabel("Inserisci password"));
 	   userblock.add(psw);
 	   userblock.add(send);
 	   userblock.add(back);

 	   setVisible(true);
 	   Container container = getContentPane();
 	   container.add(userblock);
 	   return userblock;
 	}
 	
 	public  JPanel activationUser(){
 	   JPanel activeUser = new JPanel();
 	   activeUser.setLayout(new FlowLayout());

 	   JTextField codAtt = new JTextField(10);
 	   JTextField paswd = new JTextField(10);
 	   JButton back = new JButton("Esci");
 	   back.addActionListener(new ActionListener() {
 	       @Override
 	       public void actionPerformed(ActionEvent e) {

 	           try {
 	               welcometoSeatIn();
 	           } catch (RemoteException e1) {
 	               e1.printStackTrace();
 	           } catch (NotBoundException e1) {
 	               e1.printStackTrace();
 	           }


 	           activeUser.setVisible(false);

 	       }
 	   });
 	   JButton send = new JButton("Invia");
 	   send.addActionListener(new ActionListener() {
 	       @Override
 	       public void actionPerformed(ActionEvent e) {
 	    	  
 	    	   String tempID = codAtt.getText();
 		       String password = paswd.getText();
 	    	   if (password.length()>=MLPSW && tempID.length()!=8 ){
 		         
 		          switch (utenza){
 		          case "Studente":
 		        	  if(student.checkID(tempID)){
 		           	   try {
 		   				student.updatePasswordandProfile(password);
 		   			} catch (IOException | MessagingException e1) {
 		   				// TODO Auto-generated catch block
 		   				e1.printStackTrace();
 		   			}
 		           	   JOptionPane.showMessageDialog(activationUser(),"password cambiata");
 		              }else{
 		           	   JOptionPane.showMessageDialog(activationUser(),"ID temporaneo non corretto");
 		              }
 		       	  break;
 		          case "Docente":
 		        	  if(teacher.checkID(tempID)){
 		              	   try {
 		      				teacher.updatePasswordandProfile(password);
 		      			} catch (IOException | MessagingException e1) {
 		      				// TODO Auto-generated catch block
 		      				e1.printStackTrace();
 		      			}
 		              	   JOptionPane.showMessageDialog(activationUser(),"password cambiata");
 		                 }else{
 		              	   JOptionPane.showMessageDialog(activationUser(),"ID temporaneo non corretto");
 		                 }
 		       	  
 		        	  break;
 		          case "Admin":
 		        	  if(administrator.checkID(tempID)){
 		              	   try {
 		      				administrator.updatePasswordandProfile(password);
 		      			} catch (IOException | MessagingException e1) {
 		      				// TODO Auto-generated catch block
 		      				e1.printStackTrace();
 		      			}
 		              	   JOptionPane.showMessageDialog(activationUser(),"password cambiata");
 		                 }else{
 		              	   JOptionPane.showMessageDialog(activationUser(),"ID temporaneo non corretto");
 		                 }
 		        	  break;
 		          }
 	 		 }else{
 	 			JOptionPane.showMessageDialog(activationUser(), "Password o Codice non inseriti correttamente");
 	 			
 	 		 }
 	    	   
 	           

 	       }
 	   });

 	   activeUser.add(new JLabel("Inserisci codice attivazione"), CENTER_ALIGNMENT);
 	   activeUser.add(codAtt);

 	   activeUser.add(new JLabel("Inserisci password"), CENTER_ALIGNMENT);
 	   activeUser.add(paswd);

 	   activeUser.add(send);
 	   activeUser.add(back);

 	   Container container = getContentPane();
 	   container.add(activeUser);

 	   setVisible(true);
 	   return activeUser;
  }
    public JPanel addOptionalCourse(SeatInStudent s) throws SQLException, RemoteException{
        JPanel addCoursePanel = new JPanel();
        addCoursePanel.setLayout(new FlowLayout());
        //tutti i corsi di laurea disponibili senza contare quello a cui è gia' iscritto lo studente
        List<String[]> degreeAvailable = s.studentDegreeWithoutStudentOne();
        String[] degreeCourse = new String[degreeAvailable.size()];
        int i = 0;
        JButton signUp = new JButton("Iscriviti");


        //in questo modo ottengo tutti i corsi di laurea registrati sulla piattaforma che sono differenti da quello dell'utente
        //degree[0]contiene nome
        //degree[1] contiene codice
        for (String[] degreeAv : degreeAvailable) {
            degreeCourse[i] = degreeAv[0];
            i++;
        }


        //significa che non ci sono corsiDiLaurea disponibili a parte quello per cui gia' appartiene l'utente
        if (degreeCourse.length == 1)
            JOptionPane.showMessageDialog(addCoursePanel, "ATTENZIONE: nessun corso di laurea disponibile");


        JTextArea areatext = new JTextArea(10, 15);
        //prima combo box
        JComboBox course = new JComboBox(degreeCourse);

        JComboBox optionalCourse = new JComboBox();
        optionalCourse.removeAllItems();
        course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    optionalCourse.removeAllItems();
                    if (course.getSelectedIndex()==0)
                        areatext.removeAll();
                    List<String[]> coursesbyDegree = new ArrayList<>();
                    for (String[] degreeAv : degreeAvailable)
                        if (course.getSelectedItem().equals(degreeAv[0])) {

                            try {
                                coursesbyDegree = student.courseNotInStudyPlan(degreeAv[1]);
                                System.out.println("courses by degree" + coursesbyDegree.size());
                                for (String[] x : coursesbyDegree) {

                                    optionalCourse.addItem("codice corso:" + x[0] + ".Anno accademico:" + x[2]);
                                }


                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }

                        }

                    final List<String[]> courseBYdegree=coursesbyDegree;
                    optionalCourse.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (optionalCourse.getSelectedIndex() != -1) {
                                System.out.println(course.getSelectedIndex());
                                String x = optionalCourse.getSelectedItem().toString();
                                x = x.replaceAll("codice corso:", "");
                                x = x.replaceAll("Anno accademico:", "");
                                String[] y = x.split(Pattern.quote("."));
                                for (String[] info : courseBYdegree) {
                                    if (y[0].equals(info[0]) && y[1].equals(info[2])) {
                                        List<String[]> teachers = student.teacherTeachesCourse(info[0], Integer.parseInt(info[2]));
                                        areatext.setText("nome corso:" + info[1] + "\n" +
                                                "descrizione:" + info[3] + "\n" + "docenti del corso:");
                                        System.out.println("codice" + info[0] +
                                                "nome " + info[1] + "anno" + info[2] + "descrizione" + info[3]);
                                        for (String[] teacher : teachers)
                                            areatext.append(teacher[0] + " " + teacher[1]);
                                    } else
                                        System.out.println("non ho trovato ");
                                }


                            }

                        }


                    });
            }
        });



        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String x = optionalCourse.getSelectedItem().toString();
                x = x.replaceAll("codice corso:", "");
                x = x.replaceAll("Anno accademico:", "");
                String[] y = x.split(Pattern.quote("."));
                //y[0] codice
                // y1 anno
                List<String> teacherEmail=new ArrayList<>();
                try {
                    teacherEmail=student.teacherEmailForStudentEmailSender(y[0],Integer.parseInt(y[1]));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                try {
                    student.courseSubscription(teacherEmail, y[1],y[0]);
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }


                int reply=JOptionPane.showConfirmDialog(addCoursePanel, "stai tentando di iscriverti al corso selezionato. Continuare?");
                if (reply == JOptionPane.YES_OPTION) {
                    mainContainer.removeAll();
                    mainContainer.validate();
                    mainContainer.repaint();
                    try {
                        mainContainer.add(mainPanelAfterLoginStudent());
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        e1.printStackTrace();
                    }
                    setVisible(true);
                }

            }
        });

        JButton back = new JButton("Annulla");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // welcometoSeatIn();
                addCoursePanel.removeAll();
                addCoursePanel.validate();
                addCoursePanel.repaint();
                try {
                    mainPanelAfterLoginStudent();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        addCoursePanel.add(course);
        addCoursePanel.add(optionalCourse);
        addCoursePanel.add(signUp);
        addCoursePanel.add(back);
        addCoursePanel.add(areatext);


        Container container = getContentPane();
        container.add(addCoursePanel);
        setVisible(true);
        setSize(600, 400);
        return addCoursePanel;
    }

    public JPanel courseEnable() throws IOException {
        JPanel panelCourse= new JPanel();
        // // il vettore che arrivera' dal server conterra' i seguenti campi nell'ordine elencato:
        //                // String idCode, name, activationYear, degreeCourse, description;
        ArrayList<String[]> course=administrator.showCourse();

        String[] courseNameVector= new String[course.size()+1];
        int i=1;
        courseNameVector[0]="----------";
        for (String[] c:course) {
            courseNameVector[i]=c[1];
             i++;
        }
        JComboBox courseName= new JComboBox(courseNameVector);
        JTextArea info=new JTextArea();
        info.setSize(300,300);
        info.setEditable(false);
        JPanel layout= new JPanel();
        layout.setLayout(new GridLayout(3,1));
        layout.add(courseName);
        layout.add(info);
        JPanel panelOk= new JPanel();
        panelOk.setLayout(new GridLayout(2,2));
        JButton ok=new JButton("abilita");
        panelOk.add(new JLabel(""));
        panelOk.add(new JLabel(""));
        panelOk.add(new JLabel(""));
        panelOk.add(ok);
        layout.add(panelOk);
        panelCourse.add(layout);
        courseName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseName.getSelectedIndex()!=0) {
                    String[] x = course.get(courseName.getSelectedIndex() - 1);
                    info.setText("nome corso: " + x[1] + "\n" + "codice corso:" + x[0] + "\n" + "anno: " + x[2] + "\n" + "corso di laurea:" + x[3] + "\n" + "descrizione: " + x[4]);
                }
                else
                    info.setText("Seleziona un corso da abilitare");
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //quando ok viene cliccato devo andare a salvare la modifica su database!
                if (courseName.getSelectedIndex()!=0) {
                    String[] x = course.get(courseName.getSelectedIndex() - 1);
                    try {
                        administrator.insertCourseByCsv(x);
                       int reply= JOptionPane.showConfirmDialog(panelCourse,"il corso e' stato abilitato. Desidera abilitarne un altro?");
                        if (reply == JOptionPane.NO_OPTION) {
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            mainContainer.add(adminPanel());
                            setVisible(true);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
                else
                    JOptionPane.showMessageDialog(panelCourse,"Impossibile validare il corso specificato");
            }
        });
        JButton back=new JButton("indietro");
        panelCourse.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(adminPanel());
                setVisible(true);
            }
        });
        
        setVisible(true);
        setSize(600, 400);
        return panelCourse;
    }

    public JPanel teacherCourseEnable() throws IOException {
        JPanel panelCourse= new JPanel();
        // // il vettore che arrivera' dal server conterra' i seguenti campi nell'ordine elencato:
        //                // String email, CourseCode, , year

        ArrayList<String[]> course=administrator.showTeachingCourse();


        String[] courseNameVector= new String[course.size()+1];
        int i=1;
        courseNameVector[0]="----------";
        for (String[] c:course) {
            courseNameVector[i]="codice: " +c[1]+ "anno: " + c[2];
            i++;
        }
        JComboBox courseName= new JComboBox(courseNameVector);
        JTextArea info=new JTextArea();
        info.setSize(300,300);
        info.setEditable(false);
        JPanel layout= new JPanel();
        layout.setLayout(new GridLayout(3,1));
        layout.add(courseName);
        layout.add(info);
        JPanel panelOk= new JPanel();
        panelOk.setLayout(new GridLayout(2,2));
        JButton ok=new JButton("assegna docente");
        panelOk.add(new JLabel(""));
        panelOk.add(new JLabel(""));
        panelOk.add(new JLabel(""));
        panelOk.add(ok);
        layout.add(panelOk);
        panelCourse.add(layout);
        courseName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseName.getSelectedIndex()!=0) {
                    String[] x = course.get(courseName.getSelectedIndex() - 1);
                    info.setText("codice corso: " + x[1] + "\n"  + "anno: " + x[2] + "\n" + "docente assegnabile:" + x[0]);
                }
                else
                    info.setText("Seleziona un corso a per vedere maggiori informazioni");
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //quando ok viene cliccato devo andare a salvare la modifica su database!
                if (courseName.getSelectedIndex()!=0) {
                    String[] x = course.get(courseName.getSelectedIndex() - 1);
                    try {
                        administrator.insertTeacherTeachesCourse(x);
                        int reply= JOptionPane.showConfirmDialog(panelCourse,"il docente e' stato assegnato al corso. Desidera abilitarne un altro?");
                        if (reply == JOptionPane.NO_OPTION) {
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            mainContainer.add(adminPanel());
                            setVisible(true);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
                else
                    JOptionPane.showMessageDialog(panelCourse,"Impossibile validare il corso specificato");
            }
        });
        setVisible(true);
        setSize(600, 400);
        return panelCourse;
    }

    public JPanel requestEditAdmin () throws SQLException, RemoteException {
        JPanel edit =new JPanel();
        //edit.setLayout(new GridLayout(4,2));
        edit.setLayout(new FlowLayout());
        JPanel edit1 = new JPanel();
        edit1.setLayout(new GridLayout(4,1));
        JPanel edit2 = new JPanel();


        edit2.setLayout(new GridLayout(10,2));
        Box horizontal = Box.createHorizontalBox();

        JLabel labelReq= new JLabel("Richieste di modifica");
        JLabel label = new JLabel("Tipologia"); //stamo lla tipologia d'utenza che richiede una modific al profilo
        JTextField typology = new JTextField(10);

        JLabel personalData[] = {new JLabel("nome"), new JLabel("cognome"), new JLabel("matricola"), new JLabel("email"), new JLabel("Corso di laurea"), new JLabel("Dipartimento"), new JLabel("anno iscrizione"),  new JLabel("anno corso"), new JLabel("stato corso")};


        //utilizzare questo vettore di JTextfield per caricare i valori da modificare
        JTextField personalDataTextField[] = {new JTextField(), new JTextField(), new JTextField(),
                new JTextField(), new JTextField(), new JTextField(),new JTextField(), new JTextField(), new JTextField()};



        //richieste modifica profilo:
        List<String[]> changeRequest= administrator.showChangeProfile();
        String[] inComboBox=new String[changeRequest.size()+1];
        int index=1;
        ////changes[0] = emailIdentificatore
        //        //changes[1] = campoDiModifica
        //        //changes[2] = valoreModifica
        //        //changes[3] = tipologiaProfilo
        inComboBox[0]="-------";
        for (String[] req: changeRequest)
        {
            inComboBox[index]=index + ":"  + req[0];
            index++;
        }
        JComboBox request = new JComboBox(inComboBox); //Carico tutte le richieste di modifica profilo
        request.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int i = request.getSelectedIndex() - 1;
                    if (i != -1) {
                        for (JTextField t : personalDataTextField) {
                            t.setText("");
                        }
                        String[] info = changeRequest.get(i);

                        typology.setText(info[3]);

                        // JTextField personalDataTextField[] = {new JTextField("nome"),
                        // new JTextField("cognome"), new JTextField("matricola"),
                        //                new JTextField("email"),
                        // new JTextField("cdl"), new JTextField("Dipartimento")};
                        //'nome', 'cognome', 'matricola', 'dipartimento', 'corsoDiLaurea', 'email', 'annoIscrizione', 'annoCorso', 'statoCorso')))
                        if (info[1].equals("nome")) {
                            personalDataTextField[0].setText(info[2]);

                        }
                        if (info[1].equals("cognome"))
                            personalDataTextField[1].setText(info[2]);
                        if (info[1].equals("email"))
                            personalDataTextField[3].setText(info[2]);
                        if (info[1].equals("matricola"))
                            personalDataTextField[2].setText(info[2]);
                        if (info[1].equals("dipartimento"))
                            personalDataTextField[5].setText(info[2]);
                        if (info[1].equals("corsoDiLaurea"))
                            personalDataTextField[4].setText(info[2]);
                        if (info[1].equals("annoIscrizione"))
                            personalDataTextField[6].setText(info[2]);
                        if (info[1].equals("annoCorso"))
                            personalDataTextField[7].setText(info[2]);
                        if (info[1].equals("statoCorso"))
                            personalDataTextField[8].setText(info[2]);

                    }
                }
            }

        });






        edit1.add(labelReq);
        edit1.add(request);

        edit1.add(label);
        edit1.add(typology);







        JButton ok = new JButton("Approva Modifiche");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Bottone Approva modifiche
                int i = request.getSelectedIndex() - 1;
                String[] info = changeRequest.get(i);

                ////changes[0] = emailIdentificatore
                //        //changes[1] = campoDiModifica
                //        //changes[2] = valoreModifica
                //        //changes[3] = tipologiaProfilo
                try {
                    boolean flag = false;
                    if (info[3].equals("studente")) {
                        SeatInStudent s = new SeatInStudent("", "", "", info[0], "", "", "", 0, 0, "", "");
                    	
                        Object sp = s.profileInfo();
                        SeatInStudent st = (SeatInStudent) sp;

                        if (info[1].equals("nome"))
                            st.setName(info[2]);
                        if (info[1].equals("cognome"))
                            st.setSurname(info[2]);
                        if (info[1].equals("email"))
                            st.setEmail(info[2]);
                        if (info[1].equals("matricola"))
                            st.setID(info[2]);
                        if (info[1].equals("corsoDiLaurea"))
                            st.setDegreeCourse(info[2]);
                        if (info[1].equals("annoIscrizione"))
                            st.setEnrollmentYear(Integer.parseInt(info[2]));
                        if (info[1].equals("annoCorso"))
                            st.setCourseYear(Integer.parseInt(info[2]));
                        if (info[1].equals("statoCorso"))
                            st.setCourseState(info[2]);

                        administrator.updateChangeProfileRequest(st, info[0]);
                    }
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                if (info[3].equals("docente")) {
                    SeatInTeacher s = new SeatInTeacher("", "", "", info[0], "", "", "", "");
                    Object sp = null;
                    try {
                        sp = s.profileInfo();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    SeatInTeacher st = (SeatInTeacher) sp;

                    if (info[1].equals("nome"))
                        st.setName(info[2]);
                    if (info[1].equals("cognome"))
                        st.setSurname(info[2]);
                    if (info[1].equals("email"))
                        st.setEmail(info[2]);
                    if (info[1].equals("matricola"))
                        st.setID(info[2]);
                    if (info[1].equals("dipartimento"))
                        st.setDepartment(info[2]);

                    try {
                        administrator.updateChangeProfileRequest(st, info[0]);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
                        if (info[3].equals("amministratore"))
                        {
                            SeatInAdmin s=new SeatInAdmin("","","",info[0],"","","","");
                            Object sp= null;
                            try {
                                sp = s.profileInfo();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            SeatInAdmin st=(SeatInAdmin)sp;

                            if (info[1].equals("nome"))
                                st.setName(info[2]);
                            if (info[1].equals("cognome"))
                                st.setSurname(info[2]);
                            if (info[1].equals("email"))
                                st.setEmail(info[2]);
                            if (info[1].equals("matricola"))
                                st.setID(info[2]);
                            if (info[1].equals("dipartimento"))
                                st.setDepartment(info[2]);

                            try {
                                administrator.updateChangeProfileRequest(st,info[0]);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                        }
                       int reply = JOptionPane.showConfirmDialog(request, "la richiesta di modifica e' andata a buon fine. Si desidera uscire dall'approvazione di richieste?");
                       if (reply == JOptionPane.YES_OPTION) {
                           mainContainer.removeAll();
                           mainContainer.validate();
                           mainContainer.repaint();
                           mainContainer.add(adminPanel());
                           setVisible(true);
                       }

            }
        });

        JButton ko = new JButton("Rifiuta Modifiche");
        ko.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Bottone Non Approva modifiche
                int i = request.getSelectedIndex() - 1;
                String[] info = changeRequest.get(i);
                try {
                administrator.unapproveRequest(info[0],info[1], info[2]);
                    JOptionPane.showMessageDialog(request,"richiesta disapprovata");
                    request.removeAllItems();
                    //aggiorno la combo box
                    List<String[]> changeRequest= administrator.showChangeProfile();
                    String[] inComboBox=new String[changeRequest.size()+1];
                    int index=1;
                    ////changes[0] = emailIdentificatore
                    //        //changes[1] = campoDiModifica
                    //        //changes[2] = valoreModifica
                    //        //changes[3] = tipologiaProfilo
                    inComboBox[0]="-------";
                    for (String[] req: changeRequest)
                    {
                        inComboBox[index]=index + ":"  + req[0];
                        index++;
                    }
                    for (String in: inComboBox)
                          request.addItem(in); //Carico tutte le richieste di modifica profilo


                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });
        JLabel l = new JLabel("Nome campo: ");
        JLabel lb = new JLabel("Valore da approvre: ");

        JButton back = new JButton("Esci");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(adminPanel());
                edit.setVisible(false);
                //edit.setVisible(false);
            }
        });

        edit2.add(l);
        edit2.add(lb);
        for (int i = 0; i < 9; i++) {
            edit2.add(personalData[i]);  //carica le label
            edit2.add(personalDataTextField[i]); //Carica le text
            //personalDataTextField[i].setEditable(false);
        }

        horizontal.add(ok);
        horizontal.add(ko);
        horizontal.add(back);


        edit.add(edit1);

        edit.add(edit2, CENTER_ALIGNMENT);

        //edit.add(ok);
        edit.add(horizontal);
        //edit.add(ko);
        //edit.add(back);
        Container container = getContentPane();
        container.add(edit);
        setVisible(true);
        setSize(600,400);
        return edit;
    }


    public JPanel unlockedProfileUser() throws SQLException, RemoteException {
        JPanel unlockprofile = new JPanel();
        unlockprofile.setLayout(new GridLayout(4,2));

        JLabel label = new JLabel("Seleziona profilo da sbloccare");
         List<String> lockedProfile= administrator.showLockedProfile();
         String[] arrayInCombo= new String[lockedProfile.size()+1];
         int i=1;
         arrayInCombo[0]="--------";
         for (String s: lockedProfile)
         {
             arrayInCombo[i]=s;
             i++;
         }


        JComboBox listprofileemail = new JComboBox(arrayInCombo); //Combobox che verrà caricata con le email dei profili bloccati


        JButton unlock = new JButton("Sblocca Profilo");
        unlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i=listprofileemail.getSelectedIndex()-1;
                String emailToUnlock=lockedProfile.get(i);
                String whichUser="";
                try {
                     whichUser=administrator.whicUser(emailToUnlock);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                if (whichUser.equals("student")) {
                	//student.getInstance();
                   // SeatInStudent s = new SeatInStudent("", "", "", emailToUnlock, "", "", "attivo", 0, 0, "", "");
                    try {
                        administrator.unlockProfile(student);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    int reply = JOptionPane.showConfirmDialog(unlockprofile, "profilo impostato ad attivo, si desidera sbloccare altri profili?");
                    if (reply == JOptionPane.NO_OPTION) {
                        mainContainer.removeAll();
                        mainContainer.validate();
                        mainContainer.repaint();
                        mainContainer.add(adminPanel());
                        setVisible(true);
                    }
                }
                if (whichUser.equals("teacher")){
                        SeatInTeacher t= new SeatInTeacher("","","", emailToUnlock,"","","attivo","");
                        try {
                            administrator.unlockProfile(t);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        int repl=JOptionPane.showConfirmDialog(unlockprofile,"profilo impostato ad attivo, si desidera sbloccare altri profili?");
                        if (repl == JOptionPane.NO_OPTION) {
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            mainContainer.add(adminPanel());
                            setVisible(true);
                    }
                }
                    if (whichUser.equals("admin")){
                        //it's admin
                        SeatInAdmin t= new SeatInAdmin("","","", emailToUnlock,"","","attivo","");
                        try {
                            administrator.unlockProfile(t);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        int repl=JOptionPane.showConfirmDialog(unlockprofile,"profilo impostato ad attivo, si desidera sbloccare altri profili?");
                        if (repl == JOptionPane.NO_OPTION) {
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            mainContainer.add(adminPanel());
                            setVisible(true);
                        }
                    }

                    //ripopolo la checkBox
                listprofileemail.removeAllItems();
                List<String> lockedProfile= null;
                try {
                    lockedProfile = administrator.showLockedProfile();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                String[] arrayInCombo= new String[lockedProfile.size()+1];
                int a=1;
                arrayInCombo[0]="--------";
                for (String s: lockedProfile)
                {
                    arrayInCombo[a]=s;
                    a++;
                }
                for (String array:arrayInCombo)
                    listprofileemail.addItem(array);


                }

        });
        JButton back=new JButton("indietro");
        unlockprofile.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.repaint();
                mainContainer.validate();
                mainContainer.add(adminPanel());
                setVisible(true);
            }
        });

        unlockprofile.add(label);
        unlockprofile.add(new JLabel(""));
        unlockprofile.add(listprofileemail);
        unlockprofile.add(new JLabel(""));
        unlockprofile.add(new JLabel(""));
        unlockprofile.add(new JLabel(""));
        unlockprofile.add(unlock);
        unlockprofile.add(back);


        Container container = getContentPane();
        container.add(unlockprofile);
        setVisible(true);
        setSize(500,300);
        return unlockprofile;
    }

    public JPanel enableUser() throws RemoteException {

        JPanel enablepanel = new JPanel();
       HashMap<String, List<? extends SeatInPeople>> hashMap=administrator.readUserCSV();
        List<SeatInStudent> studentList=new ArrayList<SeatInStudent>();
        studentList.addAll( (ArrayList<SeatInStudent>) hashMap.get("studente"));
        List< SeatInTeacher> teacherList=new ArrayList<>();
        teacherList.addAll((ArrayList<SeatInTeacher>) (hashMap.get("docente")));
        List<SeatInAdmin> adminList= new ArrayList<SeatInAdmin>();
        adminList.addAll((ArrayList<SeatInAdmin>) hashMap.get("amministratore"));
        int i=(studentList.size())+teacherList.size()+adminList.size()+1;
        String[] x=new String[i];
        x[0]="----";
        int index=1;
        for (SeatInPeople s: studentList) {
            x[index] = s.getEmail();
            index++;
        }
             for (SeatInAdmin ad: adminList){
               x[index]=ad.getEmail();
                  index++;}
        for (SeatInTeacher t: teacherList){
            x[index]=t.getEmail();
            index++;
         }


        enablepanel.setLayout(new FlowLayout());


        JComboBox user = new JComboBox(x);
        JTextArea userArea = new JTextArea(10,15);
        JScrollPane scroll = new JScrollPane(userArea);


        user.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (user.getSelectedIndex()!=0){
                         //devo aggiornare la textArea con i dati della persona
                         String email = user.getSelectedItem().toString();
                         //devo andare a scoprire la tipologia di profilo
                            for (SeatInAdmin ad : adminList) {
                                if (ad.getEmail().equals(email)) {
                                userArea.setText("nome:" + ad.getName() + "\n" + "cognome:" + ad.getSurname() + "\n" + "dipartimento: " + ad.getDepartment());
                             }
                             }
                            for (SeatInTeacher tc : teacherList) {
                                    if (tc.getEmail().equals(email)) {
                                    userArea.setText("nome:" + tc.getName() + "\n" + "cognome:" + tc.getSurname() + "\n" + "dipartimento: " + tc.getDepartment());
                                    }
                             }

                             for (SeatInStudent st : studentList) {
                                    if (st.getEmail().equals(email)) {
                                     userArea.setText("nome:" + st.getName() + "\n" + "cognome:" + st.getSurname() + "\n" + "matricola:" + st.getID() + "\n" + "corso di laurea: " + st.getDegreeCourse() +
                                    "\nanno iscrizione: " + st.getEnrollmentYear() + "\nanno corso:" + st.getCourseYear() + "\nStato corso:" + st.getCourseState());
                                      }
                                }

                     }
                 }

                }} );


        JButton enable = new JButton("Abilita");

        enable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.getSelectedIndex()==0)
                    JOptionPane.showMessageDialog(userArea, "non posso abilitare questo utente");
                else
                {
                    String email = user.getSelectedItem().toString();
                    System.out.print("la mail selezionata e'" + email);
                    //devo andare a scoprire la tipologia di profilo
                    for (SeatInAdmin ad : adminList) {
                        if (ad.getEmail().equals(email)) {
                            try {
                                HashMap<String,List<? extends SeatInPeople>> adh= new HashMap<>();
                                List<SeatInAdmin> adl=new ArrayList<SeatInAdmin>();
                                adl.add(ad);
                                administrator.insertProfileIntoDatabase(adh);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                                int reply = JOptionPane.showConfirmDialog(user, "utente inserito nel database, si desidera inserire un altro utente?");
                                if (reply == JOptionPane.NO_OPTION) {
                                    mainContainer.removeAll();
                                    mainContainer.validate();
                                    mainContainer.repaint();
                                    mainContainer.add(adminPanel());
                                    setVisible(true);
                            }
                        }
                    }
                    for (SeatInTeacher tc : teacherList) { ;
                        if (tc.getEmail().equals(email)) {
                            try {

                                HashMap<String,List<? extends SeatInPeople>> tch= new HashMap<>();
                                List<SeatInTeacher> tl=new ArrayList<>();
                                tl.add(tc);
                                tch.put("docente",tl);
                                administrator.insertProfileIntoDatabase(tch);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                                int reply= JOptionPane.showConfirmDialog(user, "utente inserito nel database, si desidera inserire un altro utente?");
                                if (reply == JOptionPane.NO_OPTION) {
                                    mainContainer.removeAll();
                                    mainContainer.validate();
                                    mainContainer.repaint();
                                    mainContainer.add(adminPanel());
                                    setVisible(true);
                            }
                               }
                    }

                    for (SeatInStudent st : studentList) {
                        if (st.getEmail().equals(email)) {
                            try {
                                HashMap<String,List<? extends SeatInPeople>> sdh= new HashMap<>();
                                ArrayList<SeatInStudent> sl=new ArrayList<>();
                                sl.add(st);
                                administrator.insertProfileIntoDatabase(sdh);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                                int reply= JOptionPane.showConfirmDialog(user, "utente inserito nel database, si desidera inserire un altro utente?");
                                if (reply == JOptionPane.NO_OPTION) {
                                    mainContainer.removeAll();
                                    mainContainer.validate();
                                    mainContainer.repaint();
                                    mainContainer.add(adminPanel());
                                    setVisible(true);
                                }

                        }
                    }
                    //finiti i cicli for devo andare ad aggiornare le liste
                    //se arrivo in questa riga di codice significa che l'utente vuole inserire un altro utente
                    HashMap<String, List<? extends SeatInPeople>> hashMap= null;
                    try {
                        hashMap = administrator.readUserCSV();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    List<SeatInStudent> studentList=new ArrayList<SeatInStudent>();
                    studentList.addAll( (ArrayList<SeatInStudent>) hashMap.get("studente"));
                    List< SeatInTeacher> teacherList=new ArrayList<>();
                    teacherList.addAll((ArrayList<SeatInTeacher>) (hashMap.get("docente")));
                    List<SeatInAdmin> adminList= new ArrayList<SeatInAdmin>();
                    adminList.addAll((ArrayList<SeatInAdmin>) hashMap.get("amministratore"));
                    int i=(studentList.size())+teacherList.size()+adminList.size()+1;
                    String[] x=new String[i];
                    x[0]="----";
                    int index=1;
                    for (SeatInPeople s: studentList) {
                        x[index] = s.getEmail();
                        index++;
                    }
                    for (SeatInAdmin ad: adminList){
                        x[index]=ad.getEmail();
                        index++;}
                    for (SeatInTeacher t: teacherList){
                        x[index]=t.getEmail();
                        index++;
                    }

                }
            }
        });
        Box listuser = Box.createVerticalBox();

        listuser.add(new JLabel("Lista utenti da abilitare"));
        listuser.add(user);

        enablepanel.add(listuser);
        enablepanel.add(scroll);

        enablepanel.add(enable);




        Container container = getContentPane();
        container.add(enablepanel);
        setVisible(true);
        setSize(500,300);
        return enablepanel;
    }



    public JPanel statisticsAdmin() throws RemoteException, SQLException {
        List<String[]> coursesAvailable = administrator.allCourse();
        String[] comboCourse = new String[coursesAvailable.size() + 1];
        int index = 1;
        comboCourse[0] = "-----";
        for (String[] c : coursesAvailable) {
            comboCourse[index] = "nome:" + c[0] + "  codice:" + c[1] + "    anno:" + c[2];
            index++;
        }


        JPanel statsfinal = new JPanel();
        statsfinal.setLayout(new GridLayout(2, 0));
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(3, 2));


        JTextField timeConnctionAverage = new JTextField();
        JTextField numberOfConnectionToSeatIn = new JTextField();


        Box downloadForCourse = Box.createHorizontalBox();
        JComboBox course3 = new JComboBox();
        JTextField textDownloadForCourse = new JTextField();

        downloadForCourse.add(course3);
        downloadForCourse.add(textDownloadForCourse);

        Box comboTime = Box.createHorizontalBox();
        String[] dayVector = new String[32];
        dayVector[0] = "gg";
        for (int i = 1; i < 10; i++)
            dayVector[i] = "0" + String.valueOf(i);

        for (int i = 10; i < 32; i++)
            dayVector[i] = String.valueOf(i);

        String[] monthVector = new String[13];
        monthVector[0] = "mm";
        for (int i = 1; i < 10; i++)
            monthVector[i] = "0" + String.valueOf(i);

        for (int i = 10; i < 13; i++)
            monthVector[i] = String.valueOf(i);


        JComboBox day = new JComboBox(dayVector);

        JComboBox month = new JComboBox(monthVector);
        JComboBox dayTo = new JComboBox(dayVector);
        JComboBox monthTo = new JComboBox(monthVector);


        JPanel timeForStat = new JPanel(new GridLayout(2, 4));
        timeForStat.add(new JLabel("da:"));
        timeForStat.add(day);
        timeForStat.add(month);
        timeForStat.add(new JLabel("a"));
        timeForStat.add(dayTo);
        timeForStat.add(monthTo);


        /** Box per numero complessivo di utenti connessi che stanno visualizzando /interagento con i contenuti del corso */
        Box seecontents = Box.createHorizontalBox();
        JComboBox contentsCourse = new JComboBox();
        for (String c : comboCourse)
            contentsCourse.addItem(c);
        JTextField textContentsCourse = new JTextField();


        seecontents.add(contentsCourse);
        seecontents.add(textContentsCourse);
        /*** aggunta dei componenti al panello stats*/

        stats.add(new JLabel("Numero complessivo di utenti che stanno visuliazzando/interagendo con i contenuti del corso"));
        stats.add(seecontents);
        stats.add(new JLabel("Tempo medio di connessione di studenti alle pagine offerte da SeatIn:"));
        stats.add(timeConnctionAverage);
        stats.add(new JLabel("Numero complessivo di utenti connessi a SeatIn:"));

        stats.add(numberOfConnectionToSeatIn);
        numberOfConnectionToSeatIn.setText(String.valueOf(administrator.getUserConnected()));


        JPanel stats2 = new JPanel();
        stats2.setLayout(new GridLayout(6, 2));


        JButton update = new JButton("Aggiorna Dati");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton back = new JButton("Esci");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(adminPanel());
                statsfinal.setVisible(false);
            }
        });
        Box horizontalbutton = Box.createHorizontalBox();  //Box per i due bottoni Update e Esci
        horizontalbutton.add(back);
        horizontalbutton.add(update);
        String[] time = new String[25];
        time[0] = "seleziona ora";
        int in = 1;
        for (int i = 0; i < 10; i++) {
            time[in] = "0" + String.valueOf(i);
            in++;
        }
        for (int i = 10; i < 24; i++) {
            time[in] = String.valueOf(i);
            in++;
        }


        /**Box per visualizzazzione numero complessivo di utenti che hanno effettuato il download
         * di una o piuù risorse ad intervalli di tempo**/
        Box downloadrisorse = Box.createHorizontalBox();
        JComboBox from = new JComboBox(time);
        JComboBox to = new JComboBox(time);
        JTextField textDownload = new JTextField(); // visualizzare il numero complessivo di utenti che hanno effetuato il
        // download nella faascia oraria selezionata.

        downloadrisorse.add(from);
        downloadrisorse.add(to);
        downloadrisorse.add(textDownload);


        /**Box per numero compelssivo di accessi x corso in una determinata fascia oraria */

        Box numberAccessForCourse = Box.createHorizontalBox();

        JTextField textAccesCourse = new JTextField();
        JComboBox course = new JComboBox();
        JComboBox from1 = new JComboBox(time);
        JComboBox to1 = new JComboBox(time);


        numberAccessForCourse.add(course);
        numberAccessForCourse.add(from1);
        numberAccessForCourse.add(to1);
        numberAccessForCourse.add(textAccesCourse);

        /** Box tempo medio di connessione degli studenti per ogni corso*/

        Box connectionForCourse = Box.createHorizontalBox();
        JComboBox course2 = new JComboBox();
        JTextField textForConnectioCourse = new JTextField();

        connectionForCourse.add(course2);
        connectionForCourse.add(textForConnectioCourse);

        /** Box per numero complessivo di download in base al corso selezionato */

        stats2.add(new JLabel("Numero complessivo di utenti che hanno effetuato il download in una determinata fascia oraria (info riguardante TUTTI i corsi):"));
        stats2.add(downloadrisorse);
        stats2.add(new JLabel("Numero complessivo di accessi per corso in una determinata fasia oraria:"));
        stats2.add(numberAccessForCourse);
        stats2.add(new JLabel("Tempo medio di connessione degli studenti in base al corso selezionato:"));
        stats2.add(connectionForCourse);
        stats2.add(new JLabel("Numero complessivo di download in base al corso selezionato:"));
        stats2.add(downloadForCourse);
        stats2.add(new JLabel(""));
        stats2.add(new JLabel(""));
        stats2.add(new JLabel(""));
        stats2.add(horizontalbutton);


        statsfinal.add(stats);
        statsfinal.add(stats2);

        contentsCourse.addItemListener(new ItemListener() {
                                           @Override
                                           public void itemStateChanged(ItemEvent e) {
                                               int i = contentsCourse.getSelectedIndex() - 1;
                                               //info sul corso selezionato
                                               String[] x = coursesAvailable.get(i);
                                               //contiene nome, codice, anno
                                               int userActiveInCourse = 0;
                                               try {
                                                   userActiveInCourse = administrator.userActiveInACourse(x[1], Integer.parseInt(x[2]));
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               textContentsCourse.setText(String.valueOf(userActiveInCourse));
                                               Object timeConnectionAverage = "";
                                               try {
                                                   timeConnectionAverage = administrator.timeStatisticsForAllCourses();
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               timeConnctionAverage.setText(String.valueOf(timeConnectionAverage));

                                               // administrator.totalAccessInACoursePage(x[1],Integer.parseInt(x[2]), new Timestamp(timeTo:00), new Timestamp(timeFrom:00) )
                                               int info = 0;
                                               try {
                                                   info = administrator.showResourceDownloadFromCourse(x[1], Integer.parseInt(x[2]));
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               textDownloadForCourse.setText(String.valueOf(info));
                                               Object averageConnectionByCourse = "";
                                               try {
                                                   averageConnectionByCourse = administrator.timeStatisticsForCourse(x[1], Integer.parseInt(x[2]));
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }

                                               textForConnectioCourse.setText(String.valueOf(averageConnectionByCourse));

                                               day.addItemListener(new ItemListener() {

                                                   @Override
                                                   public void itemStateChanged(ItemEvent e) {
                                                       textAccesCourse.setText("Seleziona data e ora");

                                                       month.addItemListener(new ItemListener() {
                                                           @Override
                                                           public void itemStateChanged(ItemEvent e) {

                                                               dayTo.addItemListener(new ItemListener() {
                                                                   @Override
                                                                   public void itemStateChanged(ItemEvent e) {

                                                                       monthTo.addItemListener(new ItemListener() {
                                                                           @Override
                                                                           public void itemStateChanged(ItemEvent e) {

                                                                               from.addItemListener(new ItemListener() {
                                                                                   @Override
                                                                                   public void itemStateChanged(ItemEvent e) {

                                                                                       to.addItemListener(new ItemListener() {
                                                                                           @Override
                                                                                           public void itemStateChanged(ItemEvent e) {
                                                                                               String timeTo = to.getSelectedItem().toString();
                                                                                               String timeFrom = from.getSelectedItem().toString();
                                                                                               String dayStart = String.valueOf((day.getSelectedItem()));

                                                                                               String monthStart = String.valueOf(month.getSelectedItem());
                                                                                               String monthEnd = String.valueOf(monthTo.getSelectedItem());
                                                                                               String dayEnd = String.valueOf(dayTo.getSelectedItem());


                                                                                               if (!dayStart.equals("gg") && !monthStart.equals("mm") && !dayEnd.equals("gg") && !monthEnd.equals("mm") && !timeFrom.equals("seleziona ora") && !timeTo.equals("seleziona ora")) {
                                                                                                   Timestamp to = Timestamp.valueOf("2018" + "-" + monthEnd + "-" + dayEnd + " " + timeFrom + ":" + "00" + ":" + "00");
                                                                                                   Timestamp from = Timestamp.valueOf("2018" + "-" + monthStart + "-" + dayStart + " " + timeTo + ":" + "00" + ":" + "00");

                                                                                                   System.out.print(from + "    " + to);

                                                                                                   int result = 0;
                                                                                                   try {
                                                                                                       result = administrator.totalAccessInACoursePage(x[1], Integer.parseInt(x[2]), from, to);

                                                                                                   } catch (SQLException e1) {
                                                                                                       e1.printStackTrace();
                                                                                                   } catch (RemoteException e1) {
                                                                                                       e1.printStackTrace();
                                                                                                   }
                                                                                                   textAccesCourse.setText(String.valueOf("da:" + from + "a:" + to + "sono stati eseguiti " + result + "accessi"));
                                                                                                   try {
                                                                                                       textDownload.setText(String.valueOf(administrator.showUserDownloadedInTime(from, to)));
                                                                                                   } catch (RemoteException e1) {
                                                                                                       e1.printStackTrace();
                                                                                                   }


                                                                                               }

                                                                                               from.setSelectedIndex(0);
                                                                                               to.setSelectedIndex(0);
                                                                                               day.setSelectedIndex(0);
                                                                                               dayTo.setSelectedIndex(0);
                                                                                               month.setSelectedIndex(0);
                                                                                               monthTo.setSelectedIndex(0);

                                                                                           }

                                                                                       });

                                                                                   }
                                                                               });


                                                                           }
                                                                       });
                                                                   }
                                                               });
                                                           }
                                                       });
                                                   }
                                               });

                                           }

                                       }
        );


        statsfinal.add(timeForStat);
        Container container = getContentPane();
        container.add(statsfinal);
        setVisible(true);
        setSize(1100, 400);
        return statsfinal;
    }
    public JPanel statisticTeacher() throws RemoteException, SQLException {
        JPanel statfinal = new JPanel();
        statfinal.setLayout(new GridLayout(3, 0));
        JPanel stat = new JPanel();
        stat.setLayout(new GridLayout(1, 2));
        JPanel stat2 = new JPanel();
        stat2.setLayout(new GridLayout(2, 2));

        JTextField timeConnctionAverage = new JTextField();
        JTextField numberOfDownload = new JTextField();


        JTextField textContentsCourse = new JTextField();


        String[] dayVector = new String[32];
        dayVector[0] = "gg";
        for (int i = 1; i < 10; i++)
            dayVector[i] = "0" + String.valueOf(i);

        for (int i = 10; i < 32; i++)
            dayVector[i] = String.valueOf(i);

        String[] monthVector = new String[13];
        monthVector[0] = "mm";
        for (int i = 1; i < 10; i++)
            monthVector[i] = "0" + String.valueOf(i);

        for (int i = 10; i < 13; i++)
            monthVector[i] = String.valueOf(i);


        JComboBox day = new JComboBox(dayVector);
        JComboBox month = new JComboBox(monthVector);
        JComboBox dayTo = new JComboBox(dayVector);
        JComboBox monthTo = new JComboBox(monthVector);

        String[] time = new String[25];
        time[0] = "seleziona ora";
        int in = 1;
        for (int i = 0; i < 10; i++) {
            time[in] = "0" + String.valueOf(i);
            in++;
        }
        for (int i = 10; i < 24; i++) {
            time[in] = String.valueOf(i);
            in++;
        }

        JComboBox from = new JComboBox(time);
        JComboBox to = new JComboBox(time);

        List<String[]> courseByTeacher = teacher.getInfoCoursesTeached();
        String[] comboTeached = new String[courseByTeacher.size() + 1];
        int i = 1;
        comboTeached[0] = "--------";
        for (String[] ct : courseByTeacher) {
            comboTeached[i] = "nome: " + ct[0] + "  codice: " + ct[1] + "  anno:" + ct[2];
            i++;
        }

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new GridLayout(3, 3));

        Box comboDay1 = Box.createHorizontalBox();
        comboDay1.add(day); //giorno1
        comboDay1.add(month);//  mese1

        Box comboDay2 = Box.createHorizontalBox();
        comboDay2.add(dayTo); // giorno2
        comboDay2.add(monthTo);//  mese2

        comboPanel.add(new JLabel("Da:"));
        comboPanel.add(comboDay1);
        comboPanel.add(from); //ora1
        comboPanel.add(new JLabel(""));
        comboPanel.add(new JLabel("a:"));
        comboPanel.add(comboDay2);
        comboPanel.add(to); //ora2
        comboPanel.add(new JLabel(""));
        comboPanel.add(new JLabel("Seleziona Corso: "));
        JComboBox contentsCourse = new JComboBox(comboTeached);
        comboPanel.add(contentsCourse); //corsi
        comboPanel.add(new JLabel(""));


        stat.add(new JLabel("Tempo medio di connessione di studenti alle pagine del corso:"));
        stat.add(timeConnctionAverage);


        stat2.add(new JLabel("Numero complessivo di utenti che stanno interagendo con i contenuti del corso"));
        stat2.add(textContentsCourse);
        stat2.add(new JLabel("Numero complessivo di utenti che hanno effettuato il download in un tempo prefissato"));
        stat2.add(numberOfDownload);


        JButton back = new JButton("Esci");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(teacherPanel());
                stat.setVisible(false);
            }
        });

        contentsCourse.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = contentsCourse.getSelectedIndex() - 1;
                String[] info = courseByTeacher.get(index);
                int userActiveInCourse = 0;
                try {
                    userActiveInCourse = teacher.userActiveInACourse(info[1], Integer.parseInt(info[2]));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                textContentsCourse.setText(String.valueOf(userActiveInCourse));
                Object averageConnectionByCourse = "";
                try {
                    averageConnectionByCourse = teacher.timeStatisticsForCourse(info[1], Integer.parseInt(info[2]));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

                timeConnctionAverage.setText(String.valueOf(averageConnectionByCourse));

                day.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        numberOfDownload.setText("Seleziona data e ora");

                        month.addItemListener(new ItemListener() {

                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                System.out.print("sono entrato in month");

                                dayTo.addItemListener(new ItemListener() {
                                    @Override
                                    public void itemStateChanged(ItemEvent e) {
                                        System.out.print("sono entrato in dayTo");

                                        monthTo.addItemListener(new ItemListener() {
                                            @Override
                                            public void itemStateChanged(ItemEvent e) {
                                                System.out.print("sono entrato in monthTo");

                                                from.addItemListener(new ItemListener() {
                                                    @Override
                                                    public void itemStateChanged(ItemEvent e) {
                                                        System.out.print("sono entrato in from");
                                                        to.addItemListener(new ItemListener() {
                                                            @Override
                                                            public void itemStateChanged(ItemEvent e) {
                                                                System.out.print("sono entrato in to");
                                                                String timeTo = to.getSelectedItem().toString();
                                                                String timeFrom = from.getSelectedItem().toString();
                                                                String dayStart = String.valueOf((day.getSelectedItem()));

                                                                String monthStart = String.valueOf(month.getSelectedItem());
                                                                String monthEnd = String.valueOf(monthTo.getSelectedItem());
                                                                String dayEnd = String.valueOf(dayTo.getSelectedItem());


                                                                if (!dayStart.equals("gg") && !monthStart.equals("mm") && !dayEnd.equals("gg") && !monthEnd.equals("mm") && !timeFrom.equals("seleziona ora") && !timeTo.equals("seleziona ora")) {
                                                                    Timestamp to = Timestamp.valueOf("2018" + "-" + monthEnd + "-" + dayEnd + " " + timeFrom + ":" + "00" + ":" + "00");
                                                                    Timestamp from = Timestamp.valueOf("2018" + "-" + monthStart + "-" + dayStart + " " + timeTo + ":" + "00" + ":" + "00");

                                                                    System.out.print(from + "    " + to);

                                                                    try {
                                                                        numberOfDownload.setText(String.valueOf(teacher.showUserDownloadedInTime(from, to)));
                                                                    } catch (RemoteException e1) {
                                                                        e1.printStackTrace();
                                                                    }


                                                                }

                                                            }

                                                        });

                                                    }
                                                });


                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });


        statfinal.add(stat);
        statfinal.add(comboPanel);
        statfinal.add(stat2);
        Container container = getContentPane();
        container.add(statfinal);
        setVisible(true);
        setSize(1000, 300);
        return statfinal;

    }


    public JPanel uploadResource(Node node) {
        JPanel upload = new JPanel();
        //upload.setLayout(new FlowLayout());
        JTextField resourceName = new JTextField(30);

        upload.setLayout(new GridLayout(8, 2));

        String[] resource = {"----", "Sezione", "Sottosezione", "Cartella", "File"};
        String[] typology = {"----", "pubblica", "privata"};
        JComboBox destination = new JComboBox();
        destination.addItem("--------");
        JButton back = new JButton("Esci");
        JButton up = new JButton("Carica");
        JTextField path = new JTextField(30);
        JComboBox type = new JComboBox(typology);
        JComboBox resourceType = new JComboBox(resource);
        String course = node.getCode();
        int year = node.getYear();
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setSize(50, 100);

        Box horizontalButton = Box.createHorizontalBox();
        horizontalButton.add(up);
        horizontalButton.add(back);

        upload.add(resourceName);
        upload.add(new JLabel("Seleziona il tipo di risorsa che vorresti caricare:   "));
        upload.add(resourceType);
        upload.add(new JLabel("Specifica il nome della risorsa:"));
        upload.add(resourceName);
        upload.add(new JLabel("Specificare una descrizione per la risorsa:"));

        upload.add(descriptionArea);
        upload.add(new JLabel("Selezionare visibilita'   "));
        upload.add(type);

        JPanel father = new JPanel(new GridLayout(2, 2));
        father.add(new JLabel("Specifica Risorsa Padre:   "));
        father.add(destination);
        JPanel sectionAvailablePanel = new JPanel();
        sectionAvailablePanel.setLayout(new GridLayout(1, 2));
        sectionAvailablePanel.add(new JLabel("nome sezione padre"));
        JComboBox section = new JComboBox();
        sectionAvailablePanel.add(section);

        father.add(new JLabel(""));
        father.add(sectionAvailablePanel);
        upload.add(father);
        sectionAvailablePanel.setVisible(false);

        upload.add(destination);
        upload.add(new JLabel("inserire il percorso della risorsa da caricare:    "));
        upload.add(path);
        upload.add(new JLabel(""));
        upload.add(new JLabel(""));
        upload.add(new JLabel(""));
        upload.add(horizontalButton);


        resourceType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destination.removeItem("Sezione");
                destination.removeItem("Sottosezione");
                destination.removeItem("Cartella");
                destination.removeItem("File");

                int i = resourceType.getSelectedIndex();
                switch (i) {
                    //sezione
                    case 1:
                        destination.addItem("Sezione");
                        path.setEditable(false);
                        break;
                    //sottosezione
                    case 2:
                        destination.addItem("Sezione");
                        path.setEditable(false);
                        break;
                    //cartella
                    case 3:
                        destination.addItem("Sottosezione");
                        destination.addItem("Sezione");
                        path.setEditable(false);
                        break;
                    //file
                    case 4:
                        destination.addItem("Sottosezione");
                        destination.addItem("Sezione");
                        destination.addItem("Cartella");
                        path.setEditable(true);
                        break;
                }
            }
        });


        //vado a creare il pannello che stampa i nomi delle sezioni disponibili:

        destination.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                List<Node> sectionAvailable = node.getChildren();
                section.removeAllItems();
                section.addItem("---");

                if (destination.getSelectedItem().equals("Sezione") && resourceType.getSelectedIndex() > 1 ) {
                    for (Node n : sectionAvailable)
                        section.addItem(n.getName());

                    sectionAvailablePanel.setVisible(true);
                }
                if (destination.getSelectedItem().equals("Sottosezione") && resourceType.getSelectedIndex() > 1) {
                    //devo trovare tutte le sottosezioni disponibili per un determinato CORSO
                    for (Node subsectionAvailable : sectionAvailable) {
                        {
                            List<Node> sectionChildren = subsectionAvailable.getChildren();
                            for (Node x : sectionChildren) {
                                if (x.getFile().equals("noFile")) {
                                    section.addItem(x.getName());
                                    System.out.print(x.getName());
                                }
                            }
                        }
                    }
                    sectionAvailablePanel.setVisible(true);
                }
                if (destination.getSelectedItem().equals("Cartella") && resourceType.getSelectedIndex()>1){
                    // devo mostrare tutte le cartelle
                    List<Node> subsection = new ArrayList<Node>();
                    for (Node subsectionAvailable : sectionAvailable){
                        subsection.addAll(subsectionAvailable.getChildren());
                    }

                    for (Node x : subsection) {
                        if (x.getFile().equals("false")) {
                            //e' una cartella.
                            section.addItem(x.getName());
                        }
                        if (x.getFile().equals("noFile")) {
                            //e' una sottosezione che puo' avere cartelle o file o entrambi
                            for (Node nod : x.getChildren()) {
                                if (nod.getFile().equals("false")) {
                                    //e' una cartella. guardo al suo interno
                                    section.addItem(x.getName());

                                }
                            }
                        }
                    }
                    sectionAvailablePanel.setVisible(true);
                }
            }
        });

        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<Node> s = node.getChildren();
                String visibility = "";
                String nodeCode = "";
                //padre di cartella o file messi direttamente nella sezione
                if (destination.getSelectedItem().equals("Sezione")) {
                    for (Node n : s) {
                        if (n.getName().equals(section.getSelectedItem().toString())) {
                            //significa che ho trovato il nodo
                            nodeCode = n.getCode();
                            visibility = n.getVisibility();
                        }
                    }
                }

                //padre di cartella o file in sottosezione
                if (destination.getSelectedItem().equals("Cartella")) {
                    List<Node> sectionChildren1 = new ArrayList<Node>();
                    for (Node a : s)
                        sectionChildren1.addAll(a.getChildren());

                    for (Node x : sectionChildren1) {
                        if (x.getFile().equals("false")) {
                            //e' una cartella. guardo al suo interno
                            nodeCode = x.getCode();
                            visibility = x.getVisibility();
                        }
                        if (x.getFile().equals("noFile")) {
                            //e' una sottosezione che puo' avere cartelle o file o entrambi
                            for (Node nod : x.getChildren()) {
                                if (nod.getFile().equals("false")) {
                                    //e' una cartella. guardo al suo interno
                                    nodeCode = nod.getCode();
                                    visibility = x.getVisibility();

                                }
                            }
                        }
                    }
                }
                if (destination.getSelectedItem().equals("Sottosezione")) {
                    for (Node c : s) {
                        {
                            List<Node> sectionChildren = c.getChildren();
                            for (Node x : sectionChildren) {
                                if (x.getFile().equals("noFile") && x.getName().equals(section.getSelectedItem().toString())) {
                                    nodeCode = x.getCode();
                                    visibility = x.getVisibility();
                                }
                            }
                        }
                    }
                }

                //padre di file in cartella (la quale puo' o meno appartenere a sottosezione)



                boolean flag = true;
                int i = resourceType.getSelectedIndex();
                if (resourceName.getText().equals("")) {
                    JOptionPane.showMessageDialog(resourceName, "attenzione! il nome della sezione/sottosezione/risorsa non può essere vuoto");
                    flag = false;

                }
                if (type.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(resourceName, "attenzione! Tipologia (pubblica o privata) non specificata");
                    flag = false;
                }
                if (flag) {
                    switch (i) {
                        //nessuna selezione
                        case 0:
                            //non posso aggiungere la tipologia di risorsa/sezione indicata
                            JOptionPane.showMessageDialog(resourceType, "ERRORE, controlla i campi inseriti");
                            break;
                        //pubblica
                        case 1:

                            // se premo up quando c'è selezionata la "sezione" devo richiamare metodo database:
                            boolean confirm = false;
                            try {
                                confirm = teacher.addCourseSection(resourceName.getText(), descriptionArea.getText(), course, year, type.getSelectedItem().toString());
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            if (confirm)
                                JOptionPane.showMessageDialog(resourceName, "Sezione aggiunta correttamente!");

                            break;
                        case 2:
                            //voglio inserire una sottosezione. Faccio la print di tutte le sezioni disponibili e faccio scegliere all'utente
                            //voglio andare a determinare tutte le proprieta del padre: possiedo il nodo.


                            if (visibility.equals(type.getSelectedItem()) || visibility.equals("pubblica") && type.getSelectedItem().equals("privata")) {
                                try {
                                    flag = teacher.addCourseSubsection(resourceName.getText(), descriptionArea.getText(), course, year, Integer.parseInt(nodeCode), type.getSelectedItem().toString());
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                                if (flag)
                                    JOptionPane.showMessageDialog(type, "La sottosezione e' stata inserita correttamente!");
                                else
                                    JOptionPane.showMessageDialog(type, "Problemi con inserimento sottosezione");
                            } else {
                                int reply = JOptionPane.showConfirmDialog(resourceName, "Attenzione: la visibilita' specificata è pubblica e la sezione ha visibilita' privata. Si intende propagare la modifica sulla visibilita' della sezione?  ");
                                if (reply == JOptionPane.YES_OPTION) {
                                    //propago la modifica

                                    //1 aggiungo
                                    //2 modifico
                                    try {
                                        flag = teacher.addCourseSubsection(resourceName.getText(), descriptionArea.getText(), course, year, Integer.parseInt(nodeCode), type.getSelectedItem().toString());
                                    } catch (RemoteException e1) {
                                        e1.printStackTrace();
                                    }
                                    if (flag)
                                    //se ho inserito correttamente la tupla, modifico la visibilita' del padre
                                    {
                                        try {
                                            flag = teacher.changeVisibility(nodeCode, "pubblica");
                                        } catch (RemoteException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    if (flag)
                                        //sia la modifica che upload sono andati a buon fine
                                        JOptionPane.showMessageDialog(resourceName, "modifica ed upload andati a buon fine");


                                }
                            }
                            break;

                        case 3:
                            //voglio aggiungere una cartella
                            //una cartella puo essere inserita in:
                            //-sezione -->fatto
                            //-sottosezione
                            //1 devo controllare se la risorsa padre specificata e' sezione o sottosezione. in questo ultimo caso
                            //modificare i valori della comboBox

                            byte[] byteFile = new byte[0];

                            if (destination.getSelectedItem().equals("Sezione")) {
                                if (visibility.equals(type.getSelectedItem()) || (visibility.equals("pubblica") && type.getSelectedItem().equals("privata"))) {
                                    //vado a controllare la correlazione tra visibilita' inserita e visibilita' della sezione
                                    //posso inserire la cartella
                                    {

                                        try {
                                            boolean confir=teacher.insertFile(nodeCode, resourceName.getText(), descriptionArea.getText(), byteFile, nodeCode, type.getSelectedItem().toString(), "cartella");
                                            System.out.println(""+nodeCode+ resourceName.getText()+ descriptionArea.getText()+ byteFile+ nodeCode+ type.getSelectedItem().toString());
                                            if (confir)
                                                JOptionPane.showMessageDialog(resourceName,"cartella caricata correttamente");
                                        } catch (IOException e1) {
                                           e1.printStackTrace();
                                        } catch (SQLException e1) {
                                            e1.printStackTrace();
                                        }
                                    }

                                }
                                //altrimenti devo propagare la modifica
                                else {

                                    int reply = JOptionPane.showConfirmDialog(resourceName, "Attenzione: la visibilita' specificata è pubblica e la risorsa ha visibilita' privata. Si intende propagare la modifica sulla visibilita' della sezione?  ");
                                    if (reply == JOptionPane.YES_OPTION) {
                                        try {
                                            //   flag=  teacher.insertFile(nodeCode, resourceName.getText(), descriptionArea.getText(), byteFile, nodeCode, type.getSelectedItem().toString(), "cartella");
                                            if (flag)
                                                flag = teacher.changeVisibility(nodeCode, "pubblica");
                                        } catch (RemoteException e1) {
                                            e1.printStackTrace();

                                            if (flag)
                                                JOptionPane.showMessageDialog(resourceName, "modifica ed upload andati a buon fine");

                                        }
                                    }


                                }
                            }
                            if (destination.getSelectedItem().equals("Sottosezione")){

                            }

                            break;
                        case 4:
                            //l'utente seleziona file
                            if (destination.getSelectedItem().toString().equals("Sezione")) {

                                try {
                                    byte[] array = Files.readAllBytes(new File(path.getText()).toPath());
                                    String [] extension=path.getText().split(Pattern.quote("."));
                                    //extension[1] contiene la tipologia di estensione
                                  flag= teacher.insertFile(nodeCode,resourceName.getText()+"."+extension[1],descriptionArea.getText(),array,nodeCode,type.getSelectedItem().toString(),"file");
                                  if (flag)
                                      JOptionPane.showMessageDialog(path,"file inserito correttamente!");
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }


                            }
                            if (destination.getSelectedItem().toString().equals("Sottosezione"))
                            break;
                    }

                }
            }

        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(teacherPanel());
                upload.setVisible(false);
            }
        });


        Container container = getContentPane();
        container.add(upload);


        setVisible(true);
        setSize(800, 400);
        return upload;
    }

    public JPanel deleteInTree(Node node) {
        JComboBox action = new JComboBox();
        action.addItem("-----");
        action.addItem("Modifica");
        action.addItem("Elimina");
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(action);
        JPanel modify = new JPanel();
        modify.setLayout(new GridLayout(5, 2));
        //1 modifica

        String[] modifyCombo = {"-----", "titolo", "descrizione", "visibilita'"};
        JComboBox what = new JComboBox(modifyCombo);
        modify.add(new JLabel("cosa si intende modificare/eliminare?"));
        modify.add(what);
        modify.add(new JLabel("indicare su cosa si vuole fare tale modifica/eliminazione :"));
        JComboBox resource = new JComboBox();
        modify.add(resource);
        modify.add(new JLabel("In base alla ricerca effettuata, e' possibile modificare/eliminare i seguenti campi:"));
        JComboBox modifyLabels = new JComboBox();
        modify.add(modifyLabels);
        modify.add(new JLabel("inserire nuovo valore:"));
        modify.add(new JLabel("nuova visibilita':"));
        String[] vis = {"----", "pubblica", "privata"};
        JComboBox visibility = new JComboBox(vis);
        modify.add(visibility);
        JTextField titleDescrVisibility = new JTextField();
        modify.add(titleDescrVisibility);
        modifyLabels.addItem("-------");
        resource.addItem("-----");
        mainPanel.add(modify);

        what.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                resource.removeItem("File");
                resource.removeItem("Cartella");
                resource.removeItem("Sezione");
                resource.removeItem("Sottosezione");

                switch (what.getSelectedItem().toString()) {
                    case "titolo":
                        resource.addItem("Sezione");
                        resource.addItem("Sottosezione");
                        break;

                    case "descrizione":
                        resource.addItem("Sezione");
                        resource.addItem("Sottosezione");
                        break;
                    case "visibilita'":
                        resource.addItem("Sezione");
                        resource.addItem("Sottosezione");
                        resource.addItem("File");
                        resource.addItem("Cartella");
                        break;
                }
            }
        });


        resource.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                List<Node> sectionTitle = node.getChildren();
                modifyLabels.removeAllItems();

                switch (resource.getSelectedIndex()) {

                    case 1:


                        for (Node n : sectionTitle) {
                            System.out.println(n.getName());
                            modifyLabels.addItem(n.getName());
                        }
                        break;
                    case 2:
                        List<Node> child = node.getChildren();
                        for (Node c : child) {
                            {
                                List<Node> sectionChildren = c.getChildren();
                                for (Node x : sectionChildren) {
                                    if (x.getFile().equals("noFile")) {
                                        modifyLabels.addItem(x.getName());
                                    }
                                }
                            }

                        }
                        break;
                    case 3:
                        //file
                        List<Node> sectionChildren = new ArrayList<Node>();
                        for (Node a : sectionTitle)
                            sectionChildren.addAll(a.getChildren());

                        for (Node x : sectionChildren) {
                            if ((x.getFile()).equals("true"))
                                //file che appartiene a sezione
                                modifyLabels.addItem(x.getName());
                            if (x.getFile().equals("false")) {
                                //e' una cartella. guardo al suo interno

                                for (Node nod : x.getChildren()) {
                                    if (nod.getFile().equals("true"))
                                        modifyLabels.addItem(nod.getName());
                                }
                            }
                            if (x.getFile().equals("noFile")) {
                                //e' una sottosezione che puo' avere cartelle o file o entrambi
                                for (Node nod : x.getChildren()) {
                                    if (nod.getFile().equals("true"))
                                        modifyLabels.addItem(nod.getName());

                                    if (nod.getFile().equals("false")) {
                                        //e' una cartella. guardo al suo interno

                                        for (Node node1 : nod.getChildren()) {
                                            if (node1.getFile().equals("true"))
                                                modifyLabels.addItem(node1.getName());
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    case 4:

                        //cartella
                        List<Node> sectionChildren1 = new ArrayList<Node>();
                        for (Node a : sectionTitle)
                            sectionChildren1.addAll(a.getChildren());

                        for (Node x : sectionChildren1) {
                            if (x.getFile().equals("false")) {
                                //e' una cartella. guardo al suo interno

                                modifyLabels.addItem(x.getName());
                            }
                            if (x.getFile().equals("noFile")) {
                                //e' una sottosezione che puo' avere cartelle o file o entrambi
                                for (Node nod : x.getChildren()) {
                                    if (nod.getFile().equals("false")) {
                                        //e' una cartella. guardo al suo interno
                                        modifyLabels.addItem(nod.getName());

                                    }
                                }
                            }
                        }
                        break;


                }
            }
        });

        JButton ok = new JButton("OK");
        mainPanel.add(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resourceCode = "";
                String ac = action.getSelectedItem().toString().toString();
                List<Node> n = node.getChildren();
                List<Node> child = node.getChildren();
                Node nodeToUpload = new Node("", "", "", "");
                String sectionCode = "";
                String whatToChange = what.getSelectedItem().toString();
                // determino il codice della sezione o sottosezione
                if (resource.getSelectedItem().toString().equals("Sezione")) {
                    for (Node x : n)
                        if (x.getName().equals(modifyLabels.getSelectedItem().toString())) {
                            sectionCode = x.getCode();
                            nodeToUpload = x;
                        }


                }
                if (resource.getSelectedItem().toString().equals("Sottosezione")) {

                    for (Node c : child) {
                        {
                            List<Node> sectionChildren = c.getChildren();
                            for (Node x : sectionChildren) {
                                if (x.getFile().equals("noFile") && x.getName().equals(modifyLabels.getSelectedItem().toString())) {
                                    sectionCode = x.getCode();
                                    nodeToUpload = x;
                                }
                            }
                        }
                    }
                }


                if (resource.getSelectedItem().toString().equals("File")) {
                    //devo cercare tutti i file nell'albero

                    List<Node> sectionChildren = new ArrayList<Node>();
                    for (Node a : n)
                        sectionChildren.addAll(a.getChildren());

                    for (Node x : sectionChildren) {
                        if ((x.getFile()).equals("true"))
                            //file che appartiene a sezione
                            resourceCode = x.getCode();
                        if (x.getFile().equals("false")) {
                            //e' una cartella. guardo al suo interno

                            for (Node nod : x.getChildren()) {
                                if (nod.getFile().equals("true")){
                                    resourceCode = nod.getCode();
                                nodeToUpload = x;
                                }
                            }
                        }
                        if (x.getFile().equals("noFile")) {
                            //e' una sottosezione che puo' avere cartelle o file o entrambi
                            for (Node nod : x.getChildren()) {
                                if (nod.getFile().equals("true")){
                                    resourceCode = nod.getCode();
                                    nodeToUpload = x;
                                }

                                if (nod.getFile().equals("false")) {
                                    //e' una cartella. guardo al suo interno

                                    for (Node node1 : nod.getChildren()) {
                                        if (node1.getFile().equals("true")){
                                            resourceCode = node1.getCode();
                                            nodeToUpload = x;
                                }
                                    }
                                }
                            }
                        }
                    }

                }
                if (resource.getSelectedItem().toString().equals("Cartella")) {
                    List<Node> sectionChildren1 = new ArrayList<Node>();
                    for (Node a : n)
                        sectionChildren1.addAll(a.getChildren());

                    for (Node x : sectionChildren1) {
                        if (x.getFile().equals("false")) {
                            //e' una cartella. guardo al suo interno
                            resourceCode = x.getCode();
                            nodeToUpload = x;
                        }
                        if (x.getFile().equals("noFile")) {
                            //e' una sottosezione che puo' avere cartelle o file o entrambi
                            for (Node nod : x.getChildren()) {
                                if (nod.getFile().equals("false")) {
                                    //e' una cartella. guardo al suo interno
                                    nodeToUpload = x;
                                    resourceCode = nod.getCode();

                                }
                            }
                        }
                    }
                }

                switch (ac) {
                    case "Modifica":

                        //devo cercare il codice del campo da modificare
                        String newField = titleDescrVisibility.getText();

                        if (whatToChange.equals("titolo") || whatToChange.equals("descrizione")) {
                            try {
                                boolean flag = teacher.changeSectionInformation(whatToChange, sectionCode, newField);
                                if (flag)
                                    JOptionPane.showMessageDialog(modifyLabels, whatToChange + " modificata/o correttamente con " + newField);


                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (whatToChange.equals("visibilita'")) {
                            int reply = JOptionPane.showConfirmDialog(titleDescrVisibility, "Attenzione: modificando la visibilita', tale modifica verra' propagata anche al contenuto di cio' che si vuole modificare. Continuare?");
                            if (reply == JOptionPane.YES_OPTION) {
                                boolean flag = false;
                                try {
                                    if (visibility.getSelectedIndex() != 0)
                                        flag = teacher.changeVisibility(nodeToUpload, visibility.getSelectedItem().toString());
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                                if (flag)
                                    JOptionPane.showMessageDialog(titleDescrVisibility, "operazione andata a buon fine");
                            }
                        }

                        break;

                    case "Elimina":
                        int reply = JOptionPane.showConfirmDialog(resource, "ATTENZIONE! Se la sezione/sottosezione contiene a sua volta risorse, anche esse verranno elimnate. Procedere?");
                        if (reply == JOptionPane.YES_OPTION) {
                            //vado a vedere cosa l'utente vuole eliminare
                            if (resource.getSelectedItem().equals("Sezione") || resource.getSelectedItem().equals("Sottosezione"))
                            //opero sulla stessa tabella
                            {
                                boolean b = false;
                                try {
                                    b = teacher.deleteSection(sectionCode);
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                                if (b)
                                    JOptionPane.showMessageDialog(resource, "Eliminazione avvenuta con successo.");


                            }
                            if (resource.getSelectedItem().equals("File") || resource.getSelectedItem().equals("Cartella")) {
                                boolean b = false;
                                try {
                                    System.out.print("resource code" + resourceCode);
                                    b = teacher.deleteFile(resourceCode);
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                                if (b)
                                    JOptionPane.showMessageDialog(resource, "Eliminazione avvenuta con successo.");
                            }
                        }


                        break;
                }

            }
        });

        setVisible(true);
        setSize(600, 400);
        return mainPanel;
    }
        
   

    public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {
        SeatInGui sIn = new SeatInGui();
        sIn.welcometoSeatIn();
    }



}

