package latoServer;

import java.awt.*;
import javax.swing.*;

import com.sun.xml.internal.ws.client.Stub;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SeatInInterface extends JFrame {
    private String UserEmail;
    private String mail;
    private static final long serialVersionUID = 1;

    public Container mainContainer= getContentPane();


    public SeatInInterface() {
        super("SeatIn");
        mainContainer.setLayout(new FlowLayout());
        mainContainer.setPreferredSize(new Dimension(555, 555));


    }

    public static void connection() throws  RemoteException, NotBoundException{
      if (System.getSecurityManager() == null)
        {
            System.setSecurityManager
                    (new RMISecurityManager());
        }
        String host="10.24.6.86";
        int port=1099;
        Registry reg=LocateRegistry.getRegistry(host, port);
        SeatInServerInterface stub=SeatInPeople.getStub();
        stub=(SeatInServerInterface)reg.lookup("classeRemota");
        System.out.println("connesso");
       
    }



    public void welcometoSeatIn() {


        mainContainer.removeAll();
        mainContainer.validate();
        mainContainer.repaint();
        mainContainer.setLayout(new BorderLayout());

        mainContainer.add(BorderLayout.CENTER, loginInterface());
        //mainContainer.add(BorderLayout.WEST, registerProfile());

        setVisible(true);
        setSize(600,300);

    }

    public JPanel loginInterface () {
   
        //  Component componentLogin[] = { new JTextField("nome@ununsubria.it", 20), new JPasswordField("Password", 20),
        //        new JButton("Accedi"), new JButton("Password dimenticata?")};
        JTextField email= new JTextField("nome@ununsubria.it", 20);
        JButton passwordForgot= new JButton("Password dimenticata");
        passwordForgot.setSize(60,20);
        JButton submit = new JButton("Registrati!");
        JPanel welcomeLogin = new JPanel();
        welcomeLogin.setLayout(new GridLayout(4, 5));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel("Benvenuti nella Piattaforma SeatIn  "));
        welcomeLogin.add(new JLabel(""));
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
        JPasswordField passwordField= new JPasswordField("Password", 20);
        passwordField.addFocusListener(new FocusListener() {


            //quando clicco sul textField Email scompare il suggerimento
            public void focusGained(FocusEvent e) {
                passwordField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setText("password");
            }
        }  );
        // pannello contenente email, pw, password dimenticata, tasto accesso
        JButton access = new JButton("Accedi");
        access.setSize(10,10);
        access.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.removeAll();
                mainContainer.validate();
                mainContainer.repaint();
                mainContainer.setLayout(new BorderLayout());
       
                if(whichUser(email.getText()).equals("s")){try {
					mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());
					 //connessione
			        connection();
			        SeatInStudent user= new SeatInStudent();
			        user.setEmail(email.getText());
			        user.login(user.getEmail());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
                else if(whichUser(email.getText()).equals("t")){try {
					mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());
				} catch (RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
                else if(whichUser(email.getText()).equals("a")){try {
					mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());
				} catch (RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
                
                //mainContainer.add(new JLabel("Benvenuto nella piattaforma SeatIn"), BorderLayout.CENTER);
                setVisible(true);
            }
        });
        welcomeLogin.add(passwordField);
        welcomeLogin.add(access);
        for (int i=0; i<7; i++)
            welcomeLogin.add(new JLabel(" "));
        welcomeLogin.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerProfile();
                welcomeLogin.setVisible(false);
            }
        });
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(passwordForgot);
        passwordForgot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordForgotten();
            }
        });
        //controlli input
       
        mail=email.getText();
        return welcomeLogin;
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

	public JPanel registerProfile() {
        Container container = getContentPane();
        JPanel submit=new JPanel();
        JTextField registerInformation[] = {
                //           nome                         cognome                 matricola       email             course degree              matricola           anno immatricolazione
                new JTextField(  20), new JTextField( 20), new JTextField(), new JTextField( 20), new JTextField(), new JTextField(), new JTextField(4)
        };

        String year[] = {"----", "1", "2", "3"};
        String kindYear[] = {"-----", "fuori corso", "in corso"};
        final String actor[] = { "----- ","studente", "docente", "amministratore"};
        final String[] departmentList={"-----", "DISTA", "DIMAT"};
        JComboBox comboBoxYear[] = { new  JComboBox<>(year), new JComboBox(kindYear), new JComboBox(actor), new JComboBox<String>(departmentList) };
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

        submit.setLayout(new GridLayout(14, 2));
        JLabel introducintToSubscription[] = {new JLabel("oppure ISCRIVITI!"), new JLabel(""), new JLabel("sono un: ")};
        for (JLabel label: introducintToSubscription)
            submit.add(label);
        JLabel labelForReg[] = { new JLabel("Nome"), new JLabel("Cognome"), new JLabel("Matricola"), new JLabel("Email"),  new JLabel ("Dipartimento :"),
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
                //bottone Registrati
                mail= registerInformation[4].getText();
             /*  try {
//                   stub.checkEmail(mail);
                   SeatInStudent user = new SeatInStudent();
                    stub.ciaoStringa(user);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }*/


              JOptionPane.showMessageDialog(submit, "Bottone Registrati");

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
       // mainContainer.add(submit);

        container.add(submit);

        setVisible(true);
        return submit;
    }

    public void passwordForgotten(){
        JButton backtoLogin = new JButton("<--");
        backtoLogin.setPreferredSize(new Dimension(5,5));
        backtoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //torno alla schermata iniziale
                welcometoSeatIn();
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
        JTextField email=new JTextField();
        email.setSize(new Dimension(10,10));
        panelForChangePassword.add(new JLabel("email:"));
        panelForChangePassword.add(email);
        panelForChangePassword.add(new JLabel(""));
        panelForChangePassword.add(ok);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SeatInInterface.this , "una email e' stata inviata all'indirizzo " + email.getText());
                welcometoSeatIn();

            }
        } ) ;
        panelForChangePw.add(panelForChangePassword);
        mainContainer.add(panelForChangePw);
        setSize(600,300);
        setVisible(true);


    }

    public  JPanel mainPanelAfterLoginStudent () throws RemoteException, NotBoundException {

        //Controllo Accesso


        JMenu menu[] = {new JMenu("Profilo"), new JMenu("Corsi"), new JMenu("Email")};
        JMenuItem menuItemForProfile[]={new JMenuItem("visualizza profilo"), new JMenuItem("Modifica password"),
                new JMenuItem("Richiedi modifica dei dati personali"), new JMenuItem("logout")};
       for (JMenuItem item: menuItemForProfile) {
           menu[0].add(item);
       }



                    JMenuItem menuItemForCourse[] ={
                new JMenuItem("visualizza corsi a cui sei iscritto/a"),
                new JMenuItem("iscriviti ad un corso")
        };
        for (JMenuItem item: menuItemForCourse)
            menu[1].add(item);



        JMenuItem menuItemForEmail[] ={
                new JMenuItem("invia email ad un docente")
        };

        JPanel panelAfterLogin= new JPanel();
        panelAfterLogin.setLayout(new BorderLayout());
        for (JMenuItem item: menuItemForEmail)
            menu[2].add(item);
        JMenuBar bar = new JMenuBar();
        setJMenuBar( bar );
        for (JMenu menuInVector: menu)
            bar.add(menuInVector);

        panelAfterLogin.add(BorderLayout.NORTH, bar);
        // panelAfterLogin.add(BorderLayout.CENTER, new JLabel("BENVENUTO/A in SEATIN. "));
        //panelAfterLogin.add(BorderLayout.CENTER,new JLabel("Visualizzazione della piattaforma in modalita': STUDENTE"));
        panelAfterLogin.add(BorderLayout.CENTER,viewProfile());
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

        for (int i=0;i<8; i++)
        {
            profileInformation.add(personalData[i]);
            profileInformation.add(personalDataTextField[i]);
            personalDataTextField[i].setEditable(false);
        }

        setVisible(true);
        return profileInformation;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        SeatInInterface sIn = new SeatInInterface();
        sIn.welcometoSeatIn();

    }




}
