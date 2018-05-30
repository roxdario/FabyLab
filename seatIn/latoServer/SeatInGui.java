package seatIn.latoServer;

import java.awt.*;
import javax.swing.*;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private static final long MLPSW=1;//lunghezza minima della psw

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
	                        		activationUser();
	                        		welcomeLogin.setVisible(false);	                                //nuova interfaccia
	                               
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
	                        		String block = JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
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
	                        default:
	                        	JOptionPane.showMessageDialog(welcomeLogin, "Utente inesistente");
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
	                        		 //nuova interfaccia
	                        		activationUser();
	                        		welcomeLogin.setVisible(false);
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
	                        		String block = JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
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
	                        		 //nuova interfaccia
	                        		activationUser();
	                        		welcomeLogin.setVisible(false);
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
	                        		String block = JOptionPane.showInputDialog ("Utente bloccato, fai richieta per sblocco profilo");
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
        final String kindYear[] = {"-----", "fuori corso", "in corso"};
        final String actor[] = { "----- ","studente", "docente", "amministratore"};
        final String[] departmentList={"-----", "DISTA", "DIMAT"};
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
            	/*
                System.out.println("0"+registerInformation[0].getText());
                System.out.println("1"+registerInformation[1].getText());
                System.out.println("2"+registerInformation[2].getText());
                System.out.println("3"+registerInformation[3].getText());
                System.out.println("4"+registerInformation[4].getText());
                System.out.println("5"+registerInformation[5].getText());
                System.out.println("6"+registerInformation[6].getText());


                System.out.println("year "+comboBoxYear[0].getSelectedIndex());
                System.out.println("tipoY "+comboBoxYear[1].getSelectedIndex());
                System.out.println("actor "+comboBoxYear[2].getSelectedIndex());
                System.out.println("dipartimento "+comboBoxYear[3].getSelectedIndex());
            	 */
            	

                if(comboBoxYear[2].getSelectedIndex()==1){
                    String tipoY = null;
                    if(comboBoxYear[1].getSelectedIndex()==1){
                        tipoY="fuoriCorso";
                    }else if(comboBoxYear[1].getSelectedIndex()==2){
                        tipoY="inCorso";
                    }

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

                }else if(comboBoxYear[2].getSelectedIndex()==2){
                    String department = null;
                    if(comboBoxYear[3].getSelectedIndex()==1){
                        department="DISTRA";
                    }else if(comboBoxYear[3].getSelectedIndex()==2){
                        department="DIMA";
                    }
                    SeatInTeacher t= new SeatInTeacher(registerInformation[2].getText(), registerInformation[0].getText(),
                            registerInformation[1].getText(), registerInformation[3].getText(),null, null,null,department);
                    try {
                        if(t.subscription()){
                            JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
                        }else{
                            JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
                        }
                    } catch (HeadlessException | NotBoundException | IOException | MessagingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }else if(comboBoxYear[2].getSelectedIndex()==3){
                    String department = null;
                    if(comboBoxYear[3].getSelectedIndex()==1){
                        department="DISTA";
                    }else if(comboBoxYear[3].getSelectedIndex()==2){
                        department="DIMAT";
                    }
                    SeatInAdmin a= new SeatInAdmin(registerInformation[2].getText(), registerInformation[0].getText(),
                            registerInformation[1].getText(), registerInformation[3].getText(),null, null,null,department);
                    try {
                        if(a.subscription()){
                            JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
                        }else{
                            JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
                        }
                    } catch (HeadlessException | NotBoundException | IOException | MessagingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                }

                JOptionPane.showMessageDialog(submit, "Bottone Registrati");

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

    /** Pannello Password Dimemnticata**/

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
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.setEmail(email.getText());
                    user.updatePassword();
                } catch (RemoteException | MessagingException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                JOptionPane.showMessageDialog(SeatInGui.this , "una email e' stata inviata all'indirizzo " + email.getText());
                try {
                    welcometoSeatIn();
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        } ) ;
        panelForChangePw.add(panelForChangePassword);
        mainContainer.add(panelForChangePw);
        setSize(600,300);
        setVisible(true);


    }

    /** Pannello dopo Login: UTENTE**/
    public  JPanel mainPanelAfterLoginStudent () throws RemoteException, NotBoundException {

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
        JMenuItem m1b = new JMenuItem("Modifica Password");
        m1b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply=JOptionPane.showConfirmDialog(panelAfterLogin, "Vuoi modificare la password?");
                if (reply == JOptionPane.YES_OPTION) {
                   mainContainer.add(changePassword(student));
                }

            }
        });
        JMenuItem m1c = new JMenuItem("Richiedi modfica dei dati personali");
        /* invia richiesta all'admin */
        m1c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(profileRequest(student));
            }
        });


        JMenuItem m1d = new JMenuItem("Logout");
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


        JMenuItem m2a = new JMenuItem("Visualizza corsi");  /**pannello dei corsi**/
        m2a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 SeatInStudent s = new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
                 try {
					Node tree= s.createCourseTreesss("1info",2016);
					panelAfterLogin.add(viewTree(tree));
					setVisible(true);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 
            }
        });

        JMenuItem m2b = new JMenuItem("Iscriviti ad un Corso");
        m2b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelAfterLogin.add(AddOptionalCourse());
                setVisible(true);
            }
        });

        JMenuItem m3a = new JMenuItem("Invia una mail");
        m3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					panelAfterLogin.add(BorderLayout.CENTER, EmailSenderStudent());
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                viewProfile().setVisible(false);
                setSize(600,400);

            }
        });

        bar.add(m1);
        bar.add(m2);
        bar.add(m3);

        m1.add(m1a);
        m1.add(m1b);
        m1.add(m1c);
        m1.add(m1d);

        m2.add(m2a);
        m2.add(m2b);

        m3.add(m3a);


        panelAfterLogin.add(BorderLayout.NORTH, bar);


        Container container = getContentPane();
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

    public JPanel ViewCourse() {
        JPanel viewCoursePanel = new JPanel();

        /** collegato al download **/

        Container container = getContentPane();
        container.add(viewCoursePanel);
        return viewCoursePanel;
    }
    public JPanel AddOptionalCourse(){
        JPanel addCoursePanel = new JPanel();
        addCoursePanel.setLayout(new FlowLayout());

        String [] degreeCourse ={"---", "Informatica", "Medicina", "Giurisprudenza"};

        String[] OptionalCourseA = {"----", "Android", "Sicurezza"};

        String [] OptionalCourseB = {"----", "Anatomia"};

        String [] OptionalCourseC = {"----", "Diritto Privato"};


        JComboBox course = new JComboBox(degreeCourse);
        JComboBox optionalCourse = new JComboBox();
        course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (course.getSelectedItem().equals("Informatica")){
                    optionalCourse.removeAllItems();
                    for(int k =0; k<OptionalCourseA.length;k++) {
                        optionalCourse.addItem(OptionalCourseA[k]);
                    }

                }else if (course.getSelectedItem().equals("Medicina")){
                    optionalCourse.removeAllItems();
                    for(int k =0; k<OptionalCourseB.length;k++) {
                        optionalCourse.addItem(OptionalCourseB[k]);
                    }
                } else if(course.getSelectedItem().equals("Giurisprudenza")) {
                    optionalCourse.removeAllItems();
                    for(int k =0; k<OptionalCourseC.length;k++) {
                        optionalCourse.addItem(OptionalCourseC[k]);
                    }
                }
            }
        });

        JButton signUp = new JButton("Iscriviti");
        signUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String signupCourse = (String) optionalCourse.getSelectedItem();


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
        addCoursePanel.add(course);
        addCoursePanel.add(optionalCourse);
        addCoursePanel.add(signUp);
        addCoursePanel.add(back);


        Container container = getContentPane();
        container.add(addCoursePanel);
        setVisible(true);
        setSize(600,400);
        return addCoursePanel;
    }



    /** Pannelo Teacher **/
    public JPanel teacherPanel(){
        JPanel teacher = new JPanel();
        teacher.setLayout(new BorderLayout());

        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Profilo");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Email");

        JMenuItem m1a = new JMenuItem("Visualizza profilo ");
        m1a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	teacher.add(BorderLayout.CENTER, viewProfileTeacher());
                try {
					EmailSenderStudent().setVisible(false);
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setSize(600,400);
            }
        });
        JMenuItem m1b = new JMenuItem("Richiesta Modifica Profilo");
        JMenuItem m1c = new JMenuItem("Logout");
        m1c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //faccio logout per salvare nella tabella database

                    welcometoSeatIn();
                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                teacher.setVisible(false);
            }
        });

        JMenuItem m2a = new JMenuItem("Iscriviti ai corsi");

        JMenuItem m3a = new JMenuItem("Invia Email");
        m3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					teacher.add(EmailSenderTeacher(),BorderLayout.CENTER);
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

            }
        });

        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        m1.add(m1a);
        m1.add(m1b);
        m1.add(m1c);

        m2.add(m2a);

        m3.add(m3a);


        teacher.add(mb,BorderLayout.NORTH);


        setVisible(true);
        return teacher;
    }


    /** pannello email
     * @throws SQLException 
     * @throws RemoteException **/
    public JPanel EmailSenderTeacher() throws RemoteException, SQLException{
   //   SeatInTeacher teacher = new SeatInTeacher();

        JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());//new GridLayout(5,1));


        /** Cose da fare:
         *
         *  1)Caricare i dati dei corsi in un vettore di Stringhe
         * 

         */



        List<String> courses= new ArrayList<String>();
        courses=teacher.getCoursesTeached(teacher.getEmail());
        final List<String> course = courses;
        
        List<String> studentemail= new ArrayList<String>();
        courses=teacher.getCoursesTeached(teacher.getEmail());
        final List<String> studentEmail = studentemail;
        String[] nomeCorso = new String[0];
    /*
            ----chiamarareun altro metodo---
        while(teacher.getStudentsEmailforNewsletter().size()!=0){
            course = teacher.getStudentsEmailforNewsletter()
        }


        for(int j = 0; j< teacher.getStudentsEmailforNewsletter().size();j++){

                nomeCorso[j] =

        }
        */


        JComboBox courseList = new JComboBox(nomeCorso);
        JComboBox emailStudent = new JComboBox();
        courseList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < course.size(); i++) {
                    if (course.get(i).equals(courseList.getSelectedItem())){
                    	String courseName= "Anatomina";
                    	String y="2016";
                    	int year=Integer.parseInt(y);
                    	
                    	try {
							studentEmail.add(teacher.getStudentsEmailforNewsletter(courseName, year).toString());//codcorso,//anno
						} catch (RemoteException | SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
							emailStudent.addItem(studentEmail);
                    }
                }
            }
        });
        JTextField emailTo = new JTextField(40);
        emailTo.setText("seleziona destinatari o inserisci manualmente email");
        JTextField pasw = new JTextField();
        JTextArea body = new JTextArea(10,40);
        JButton send  = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();

                /** richiamare metodo email**/
                try {
					teacher.sendEmail(teacher.getEmail(), emailTo.getText(), teacher.getPassword(), "oggetto", body.getText());
				} catch (RemoteException | MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                JOptionPane.showMessageDialog(emailp,bodyemail);

            }
        });
        JButton sendAll  = new JButton("Invia a tutti");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();

                /** richiamare metodo email**/
                try {
                	teacher.sendEmailForNewsletter(teacher.getEmail(), pasw.getText(), studentEmail, "oggetto", body.getText());
					//teacher.sendEmail(teacher.getEmail(), emailTo.getText(), teacher.getPassword(), "oggetto", body.getText());
				} catch (RemoteException | MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                JOptionPane.showMessageDialog(emailp,bodyemail);

            }
        });

        emailp.add(new JLabel("email destinatario"));
        emailp.add(emailTo);
        emailp.add(new JLabel("Password email"));
        emailp.add(pasw);
        emailp.add( new JLabel("Seleziona Corso", (int) CENTER_ALIGNMENT));
        emailp.add(courseList);
        emailp.add( new JLabel("Seleziona Studente", (int) CENTER_ALIGNMENT));
        emailp.add(emailStudent);
        emailStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 for (int i = 0; i < studentEmail.size(); i++) {
                     if (studentEmail.get(i).equals(emailStudent.getSelectedItem())){
                     	
                     	emailTo.setText(studentEmail.get(i).toString());
                   
                     }
                 }
            }
        });
        emailp.add(new JLabel(""));
        emailp.add(body);
        emailp.add(new JLabel(""));

        emailp.add(send);
        emailp.add(new JLabel(""));
        emailp.add(sendAll);
        emailp.add(new JLabel(""));



        setSize(600,400);
        setVisible(true);
        return emailp;
    }
    public JPanel EmailSenderStudent() throws RemoteException, SQLException{

        JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());

        JTextField emailTo = new JTextField();
        JTextField pasw = new JTextField();


        List<String[]> courses = new ArrayList<String[]>();
        courses = student.showCourseForaStudyPlan(emailTo.getText());
        final List<String[]> course = courses;
    
        List<String> teacheremail = new ArrayList<String>();
        final List<String> teacherEmail = teacheremail;
        //teacheremail.addAll(teacher.teacherEmailForStudentEmailSender());
        JComboBox teacherlist;

        JComboBox courseList = new JComboBox();
        JComboBox emailTeacher = new JComboBox();

        courseList.addItem(course);
        courseList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < course.size(); i++) {
                    if (course.get(i).equals(courseList.getSelectedItem())){
                    	String courseName= "Anatomina";
                    	String y="2016";
                    	int year=Integer.parseInt(y);
                    	teacherEmail.add(student.teacherTeachesCourse(courseName, year).toString());//codcorso,//anno
                    	emailTeacher.addItem(teacherEmail); 
                  
                    }
                }
            }
        });

        JTextArea body = new JTextArea(10, 40);
        JButton send = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();
                String teacherSelect =(String) emailTeacher.getSelectedItem();
                try {
					student.sendEmail(student.getEmail(), emailTo.getText(), pasw.getText(), "oggetto", body.getText());
				} catch (RemoteException | MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                /** richiamare metodo email**/

                JOptionPane.showMessageDialog(emailp, teacherSelect);

            }
        });

        emailp.add(new JLabel("email destinatario"));
        emailp.add(new JLabel("Password email"));
        emailp.add(pasw);

        emailp.add(new JLabel("Seleziona Corso", (int) CENTER_ALIGNMENT));
        emailp.add(courseList);
        emailp.add(new JLabel("Seleziona Docente", (int) CENTER_ALIGNMENT));
        emailp.add(emailTeacher);
        emailTeacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 for (int i = 0; i < teacherEmail.size(); i++) {
                     if (teacherEmail.get(i).equals(emailTeacher.getSelectedItem())){
                    	 emailTo.setText(teacherEmail.get(i).toString());
                   
                     }
                 }
            }
        });

        emailp.add(new JLabel(""));
        emailp.add(body);
        emailp.add(new JLabel(""));

        emailp.add(send);
        emailp.add(new JLabel(""));


        setSize(600, 400);
        setVisible(true);
        return emailp;
    }

    public JPanel EmailSenderAdmin(){

        JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());

        JPanel insertinfo = new JPanel();
        insertinfo.setLayout(new GridLayout(4,2));

        JTextField emailTo = new JTextField(30);
        emailTo.setText("inserisce email del destinatario");
        //emailadmin.setEditable(false);
        JPasswordField paswd = new JPasswordField(10);

       // JTextField emailDest = new JTextField(10);
        JTextField object =new JTextField(10);

        JTextArea body = new JTextArea(10,40);
        JButton send  = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();
             //   String emailAdmin = emailadmin.getText();
            //    String emailto = emailDest.getText();
             //   String password = paswd.getText();
                String obj = object.getText();
                try {
					administrator.sendEmail(administrator.getEmail(), emailTo.getText() , paswd.getText(), obj, bodyemail);
				} catch (RemoteException | MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                /** richiamare metodo email**/

         //       admin.sendEmail (emailAdmin,emailto,passoword, obj, bodyemail);


            }
        });

        emailp.add(insertinfo);

        insertinfo.add(emailTo);
        insertinfo.add(new JLabel(""));
        insertinfo.add(new JLabel("Password email"));
        insertinfo.add(paswd);
       // insertinfo.add(new JLabel("Email destinatario"));
        insertinfo.add(new JLabel("Oggeto Email"));
        insertinfo.add(object);

        emailp.add(body);
        emailp.add(send);

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
                        if (flag)
                            JOptionPane.showMessageDialog(changePassword,"password modificata correttamente!");
                    } catch (RemoteException | MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
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
    public JPanel adminPanel(){
        final JPanel admin = new JPanel();
        admin.setLayout(new BorderLayout());


        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Utenti");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Opzioni");
       

        JMenuItem m1i = new JMenuItem("Visualizza profilo Utente");
        m1i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	admin.add(BorderLayout.CENTER, viewProfileAdmin());
                try {
					EmailSenderStudent().setVisible(false);
				} catch (RemoteException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                setSize(600,400);
            }
        });

        JMenuItem m3i = new JMenuItem("Abilita Corsi");
        m3i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                admin.add(Viewcorsi(),BorderLayout.CENTER);
                // profilepanel().setVisible(false);

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
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        m1.add(m1i);
        m2.add(m3i);
        m3.add(m4i);
        m3.add(m5i);


        admin.add(mb,BorderLayout.NORTH);
        setVisible(true);
        setSize(600,400);
        return admin;
    }
    
    
    public JPanel viewTree(Node node){
    	JPanel tree = new JPanel();
    	tree.setLayout(new FlowLayout());

    	JLabel l1 = new JLabel("Nome Corso");
    	JLabel l2 = new JLabel("Descrizione");
    	
    	JTextField name = new JTextField();
    	name.setText(node.getName());

    	JTextField description = new JTextField();
    	description.setText(node.getDescription());
    	
    	List<Node> nodelist = node.getChildren(); //restituisce i figli del primo livello
    	
    	tree.add(l1);
    	tree.add(name);
    	tree.add(l2);
    	tree.add(description);
    	
    	for(Node n: nodelist){
    		tree.add(new JLabel(n.getName()));	
             //se la sezione ha figli
            if (n.getChildren().size() != 0) {
                //potrebbe avere file, cartelle o sottosezioni!
                List<Node> list = n.getChildren();
                for (Node nodo : list) {
                    if (nodo.getFile().equals("false"))
                    //e' una cartella senza sottosezioni!
                    {
                       tree.add(new JLabel(nodo.getName()));
                        List<Node> nodeFile = nodo.getChildren();
                        //print di tutti i file
                        for (Node files : nodeFile) {
                            tree.add(new JLabel(files.getName()));
                        }
                    } if (nodo.getFile().equals("true"))
                        //e' un file senza sottosezioni, che ovviamente non puo' avere figli!!
                    {
                        //print di tutti i file
                           
                            tree.add(new JLabel(nodo.getName()));
                    }
                    else
                    //significa che e' una sottosezione
                    {
                       tree.add(new JLabel(nodo.getName()));
                        List<Node> nodes = nodo.getChildren();
                        for (Node nodoo : nodes) {
                            if (nodoo.getFile().equals("false"))
                            //e' una cartella senza sottosezioni!
                            {
                            
                                tree.add(new JLabel(nodoo.getName()));
                                List<Node> nodeFil = nodoo.getChildren();
                                //print di tutti i file
                                for (Node files : nodeFil) {
                                   
                                    tree.add(new JLabel(files.getName()));
                                }
                            }
                            if (nodoo.getFile().equals("true"))
                            //e' un file senza cartella!
                            {
                                //print di tutti i file
                                   
                                    tree.add(new JLabel(nodoo.getName()));
                                }
                            }

                        }
                    }

                }
            }
    	setVisible(true);
    	setSize(600,400);
    	return tree;
        }

 public JPanel profileRequest(SeatInPeople person) {
        String type="";
            final String[] field = {""};
        JComboBox change;

        if (person instanceof SeatInStudent)
            type="studente";
        if (person instanceof SeatInAdmin)
                type="amministratore";
        if (person instanceof SeatInTeacher)
                type="docente";
            final String t=type;
            final String[] fieldRequest = {""};
            JLabel text=new JLabel("SPECIFICARE IL CAMPO CHE SI DESIDERA MODIFICARE:");
            JPanel profileReq=new JPanel();
            profileReq.setLayout(new GridLayout(3,2));
            profileReq.add(text);
            profileReq.add(new JLabel(""));
            if (type.equals("studente"))
            {
                String [] student = {"-------", "nome", "cognome", "email" , "matricola", "anno immatricolazione", "corso di laurea", "stato corso di laurea", "anno corso"};
                change = new JComboBox(student);
                profileReq.add(change);
            }
           else
            {String[] teacherAdmin = {"-------------","nome", "cognome", "email", "matricola","dipartimento"};
                change = new JComboBox(teacherAdmin);

                profileReq.add(change);

            }

            change.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED){
                        field[0]=change.getSelectedItem().toString();
                    }

                }
            });

            JTextField textChange= new JTextField();
            profileReq.add(textChange);
            JButton ok= new JButton("richiedi modifica");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                 int response=   JOptionPane.showConfirmDialog(profileReq, "ATTENZIONE! la richiesta verra' visionata ed eventualmente approvata da un amministratore. continuare?");
                 if (response==JOptionPane.YES_OPTION)
                 {
                     try {
                         person.changeProfileRequest(person.getEmail(), field[0],textChange.getText(), t);
                     } catch (RemoteException e1) {
                         e1.printStackTrace();
                     }
                 }
                }
            });
            profileReq.add(ok);
            setSize(600,400);
            setVisible(true);
            return profileReq;
        }

 public JPanel viewProfileAdmin(){
          JPanel profileInformation = new JPanel();
          profileInformation.setLayout(new GridLayout(6, 2));
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
  
  
          setVisible(true);
          return profileInformation;
      }
 public JPanel viewProfileTeacher(){
     JPanel profileInformation = new JPanel();
     profileInformation.setLayout(new GridLayout(6, 2));
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
 //  JPasswordField psw = new JPasswordField(15);
   JButton send = new JButton("Invia");
   send.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
    	  String  utenza1=(String) type.getSelectedItem();
    	   switch (utenza1){
           case "Studente":
        	   student= new SeatInStudent(null, null, null, null, null, null, null, 0, 0, null, null);
        	   try {
				student.connection();
			} catch (RemoteException | NotBoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	   try {
				if(student.sendSblockRequest( email.getText(), "statoprofilo", "attivo","studente" )){
					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
				}else{
					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           case "Docente":
        	   teacher = new SeatInTeacher(null, null, null, null, null, null, null,null);
        	   try {
				teacher.connection();
			} catch (RemoteException | NotBoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	  
        		   try {
       				if(teacher.sendSblockRequest( email.getText(), "statoprofilo", "attivo","studente" )){
       					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
       				}else{
       					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
       				}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        	  
      
           case "Admin":
        	   administrator = new SeatInAdmin(null, null, null, null, null, null, null,null);
        	   try {
				administrator.connection();
			} catch (RemoteException | NotBoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
        	   try {
      				if(administrator.sendSblockRequest( email.getText(), "statoprofilo", "attivo","studente" )){
      					JOptionPane.showMessageDialog(userBlocked(),"richiesta inviata");
      				}else{
      					JOptionPane.showMessageDialog(userBlocked(),"richiesta non inviata");
      				}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
           
  		 }
           

       }
   });
   userblock.add(new JLabel("Inserisci email"));
   userblock.add(email);
   userblock.add(new JLabel("Inserisci password"));
 //  userblock.add(psw);
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
    	

    public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {
        SeatInGui sIn = new SeatInGui();
        sIn.welcometoSeatIn();
       
        

    }

}

