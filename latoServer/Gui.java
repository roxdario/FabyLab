package latoServer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SeatInGui extends JFrame {
    private String userEmail;
    private String mail;
    private static final long serialVersionUID = 1;
    private int counterPassword;
    private SeatInStudent user;
    public Container mainContainer= getContentPane();

    public SeatInGui() {
        super("SeatIn");
        mainContainer.setLayout(new FlowLayout());
        mainContainer.setPreferredSize(new Dimension(600, 400));
    }

    /** Contanier Rincipale**/
    public void welcometoSeatIn() {
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

    /** Panello di Login**/
    public JPanel loginInterface () {
        //  Component componentLogin[] = { new JTextField("nome@ununsubria.it", 20), new JPasswordField("Password", 20),
        //        new JButton("Accedi"), new JButton("Password dimenticata?")};
        JTextField email= new JTextField("nome@ununsubria.it", 20);
        JButton passwordForgot= new JButton("Password dimenticata");
        passwordForgot.setSize(60,20);
        JButton submit = new JButton("Registrati!");
        JPanel welcomeLogin = new JPanel();
        welcomeLogin.setLayout(new GridLayout(5, 4));

        welcomeLogin.add(new JLabel("Piattaforma SeatIn  "));
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
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        JPasswordField passwordField= new JPasswordField("Password", 20);
        passwordField.addFocusListener(new FocusListener() {
            //quando clicco sul textField Email scompare il suggerimento
            public void focusGained(FocusEvent e) {
                passwordField.setText("");
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


                userEmail= email.getText();

                if(whichUser(email.getText()).equals("s")){try {
                    //connessione
                    SeatInStudent user= new SeatInStudent(null, null, null, email.getText(), passwordField.getText(), null, null, 0,0, null, null);
                    user.connection();

                    //login
                    if(user.login(user.getEmail())){
                        if(user.getStateProfile().equals("non attivo")){
                            //nuova interfaccia
                            user.updatePassword();

                        }else{
                            mainContainer.removeAll();
                            mainContainer.validate();
                            mainContainer.repaint();
                            mainContainer.setLayout(new BorderLayout());
                            mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());
                        }
                    }else{
                        JOptionPane.showMessageDialog(welcomeLogin, "Errore");
                        if(userEmail.equals(email.getText())){
                            counterPassword++;
                            if(counterPassword >=10){
                                user.blockProfile();
                            }
                        }else{
                            counterPassword=1;
                        }
                    }
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }}
                else if(whichUser(email.getText()).equals("t")){try {
                    mainContainer.add(BorderLayout.CENTER, mainPanelAfterLoginStudent());

                } catch (RemoteException | NotBoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }}
                else if(whichUser(email.getText()).equals("a")){
                    mainContainer.add(BorderLayout.CENTER,adminPanel());
                    welcomeLogin.setVisible(false);
                }

               /**
                * Chiamata al Pannello Admin (Mettila dove ti serve)!!
                *
                *  mainContainer.add(BorderLayout.CENTER,adminPanel());
                *
                *  **/
                setVisible(true);
            }
        });
        welcomeLogin.add(passwordField);  //campo password
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(access);  //bottone accedi
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(new JLabel(""));
        welcomeLogin.add(passwordForgot);
        welcomeLogin.add(new JLabel(""));
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

    /**Pannello di Registrazione**/
    public JPanel registerProfile() throws NotBoundException, IOException {
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
                   latoServer.SeatInStudent user = new latoServer.SeatInStudent();
                    stub.ciaoStringa(user);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }*/


                JOptionPane.showMessageDialog(submit, "Bottone Registrati");
                SeatInStudent user= new SeatInStudent("7285355","dario","ross", "drossini@studenti.uninsubria.it", null, null, "attivo", 2015,3, "inCorso", "informatica");
                try {
                    user.connection();
                } catch (RemoteException | NotBoundException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }



                try {
                    if(user.subscription()){
                        JOptionPane.showMessageDialog(SeatInGui.this, "Inserito");
                    }else{
                        JOptionPane.showMessageDialog(SeatInGui.this, "Non Inserito");
                    }
                } catch (HeadlessException | NotBoundException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

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
       /* user.setName(registerInformation[0].getText());
        user.setSurname(registerInformation[1].getText());
        user.setID(registerInformation[2].getText());
        user.setEmail(registerInformation[3].getText());
        user.setDegreeCourse(registerInformation[5].getText());
        user.setEnrollmentYear(Integer.parseInt(registerInformation[6].getText()));
        user.setCourseState(registerInformation[7].getText());
        user.setCourseYear(3);
       */

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
                JOptionPane.showMessageDialog(SeatInGui.this , "una email e' stata inviata all'indirizzo " + email.getText());
                welcometoSeatIn();

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

        JPanel panelAfterLogin = new JPanel();
        panelAfterLogin.setLayout(new BorderLayout());
        JMenuBar bar  = new JMenuBar();

        JMenu m1 = new JMenu("Utenti");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Email");


        JMenuItem m1a = new JMenuItem("Visualizza profilo Utente");
        JMenuItem m1b = new JMenuItem("Modifica Paswword");
        m1b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordForgotten();
            }
        });
        JMenuItem m1c = new JMenuItem("Richiedi modfica dei dati personali");
        /** invia mail all'admin**/

        JMenuItem m1d = new JMenuItem("Logout");
        m1d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcometoSeatIn();
                panelAfterLogin.setVisible(false);
            }
        });


        JMenuItem m2a = new JMenuItem("Visualizza corsi");  /**pannello dei corsi**/

        JMenuItem m2b = new JMenuItem("Iscriviti ad un Corso");

        JMenuItem m3a = new JMenuItem("Invia una mail");

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

        panelAfterLogin.add(BorderLayout.CENTER, viewProfile());
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

        for (int i=0;i<8; i++)
        {
            profileInformation.add(personalData[i]);
            profileInformation.add(personalDataTextField[i]);
            personalDataTextField[i].setEditable(false);
        }

        setVisible(true);
        return profileInformation;
    }

    /** Panello Admin **/
    public JPanel Viewcorsi() throws IOException {
        SeatInAdmin admin = new SeatInAdmin();
        Box box = Box.createHorizontalBox();
        JPanel corsi = new JPanel();
        JTextArea area1;
        JTextArea area2;

        area1 = new JTextArea(10,15);
        box.add(new JScrollPane(area1));



        /** Lettura da CSV **/
        ArrayList<String[]> arraylistCourse = new ArrayList<>();


        while (admin.showCourse().size()!=0) {  //
            arraylistCourse = admin.showCourse();
        }

        //caricamento dati TextArea
        for(int i =0;i<arraylistCourse.size();i++){
            area1.append(arraylistCourse.get(i) + "\n");
        }
        /**fine Lettura => dati caricati in un ArrayList<String[]> **/

        JButton select = new JButton("->");
        box.add(select);
        box.add(new JLabel(""));
        JButton back = new JButton("<-");
        box.add(back);

        area2 = new JTextArea(10,15);
        box.add(new JScrollPane(area2));




        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area2.append(area1.getSelectedText() + "\n");


                String from = area1.getSelectedText();



                int start = area1.getText().indexOf(from);

                if(start >=0 && from.length() > 0)

                    //area1.append(csvCorsi + "\n");

                    area1.replaceRange(" ", start, start + from.length());


            }


        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                area1.append(area2.getSelectedText() + "\n");


                String from = area2.getSelectedText();



                int start = area2.getText().indexOf(from);

                if(start >=0 && from.length() > 0)



                    area2.replaceRange(" ", start, start + from.length());
            }
        });

        JButton check = new JButton("Abilita");
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                //salvare i dati dell'area2 in un ArrayList<String[]> e richiamre il metodo del Server

            }
        });
        JButton backmain = new JButton("Esci");
        backmain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminPanel();
                corsi.setVisible(false);
            }
        });

        corsi.add(check, BorderLayout.CENTER);
        corsi.add(backmain, BorderLayout.SOUTH);




        corsi.add(box);

        setVisible(true);
        return corsi;
    }
    public JPanel adminPanel(){
        JPanel admin = new JPanel();
        admin.setLayout(new BorderLayout());


        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Utenti");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Opzioni");

        JMenuItem m1i = new JMenuItem("Visualizza profilo Utente");

        JMenuItem m3i = new JMenuItem("Abilita Corsi");
        m3i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    admin.add(Viewcorsi(),BorderLayout.CENTER);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // profilepanel().setVisible(false);

            }
        });
        JMenuItem m4i = new JMenuItem("Logout");
        m4i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcometoSeatIn();
                admin.setVisible(false);
            }
        });
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);

        m1.add(m1i);


        m2.add(m3i);
        m3.add(m4i);

        admin.add(mb,BorderLayout.NORTH);
        setVisible(true);
        setSize(600,400);
        return admin;
    }


    /** Pannelo Teacher **/
    public JPanel teacherpanel(){
        JPanel teacher = new JPanel();
        teacher.setLayout(new BorderLayout());

        JMenuBar mb  = new JMenuBar();

        JMenu m1 = new JMenu("Profilo");
        JMenu m2 = new JMenu("Corsi");
        JMenu m3 = new JMenu("Email");

        JMenuItem m1a = new JMenuItem("Visualizza profilo ");
        JMenuItem m1b = new JMenuItem("Richiesta Modifica Profilo");
        JMenuItem m1c = new JMenuItem("Logout");
        m1c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcometoSeatIn();
                teacher.setVisible(false);
            }
        });

        JMenuItem m2a = new JMenuItem("Iscriviti ai corsi");

        JMenuItem m3a = new JMenuItem("Invia Email");
        m3a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teacher.add(EmailSender(),BorderLayout.CENTER);

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


    /** pannello emailSender **/
    public JPanel EmailSender(){

        SeatInTeacher teacher = new SeatInTeacher();

        JPanel emailp = new JPanel();
        emailp.setLayout(new FlowLayout());//new GridLayout(5,1));


        /** Cose da fare:
         *
         *  1)Caricare i dati dei corsi in un vettore di Stringhe

         */



        List<String> courseList = new ArrayList<String>();

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


        JComboBox course = new JComboBox(nomeCorso);
        JTextField email = new JTextField(10);
        email.setText(mail);
        JTextArea body = new JTextArea(10,40);
        JButton send  = new JButton("Invia Email");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bodyemail = body.getText();

                /** richiamare metodo email**/

                JOptionPane.showMessageDialog(emailp,bodyemail);

            }
        });

        emailp.add(new JLabel("Email Docente", (int) CENTER_ALIGNMENT));
        emailp.add(email);
        emailp.add( new JLabel("Seleziona Corso", (int) CENTER_ALIGNMENT));
        emailp.add(course);
        emailp.add(new JLabel(""));
        emailp.add(body);
        emailp.add(new JLabel(""));

        emailp.add(send);
        emailp.add(new JLabel(""));



        setSize(600,400);
        setVisible(true);
        return emailp;
    }


    public static void main(String[] args) throws RemoteException, NotBoundException {
        SeatInGui sIn = new SeatInGui();
        sIn.welcometoSeatIn();

    }




}
