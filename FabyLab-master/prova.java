package seatIn.latoServer;

public class prova {

    public JPanel statisticsAdmin() {
        List<String[]> coursesAvailable=administrator.allCourse();
        String[] comboCourse= new String[coursesAvailable.size()+1];
        int index=1;
        comboCourse[0]="-----";
        for (String[] c: coursesAvailable)
        {
            comboCourse[index]="nome:"+c[0]+"  codice:"+c[1]+"    anno:"+c[2];
            index++;
        }


        JPanel statsfinal = new JPanel();
        statsfinal.setLayout(new GridLayout(3,0));
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(3,2));


        JTextField timeConnctionAverage = new JTextField();
        JTextField numberOfConnectionToSeatIn =new JTextField();






        String[] dayVector= new String[32];
        dayVector[0]="gg";
        for (int i=1;i<10;i++)
            dayVector[i]="0"+String.valueOf(i);

        for (int i=10;i<32;i++)
            dayVector[i]=String.valueOf(i);

        String[] monthVector=new String[13];
        monthVector[0]="mm";
        for (int i=1;i<10;i++)
            monthVector[i]="0"+String.valueOf(i);

        for (int i=10;i<13;i++)
            monthVector[i]=String.valueOf(i);



        JComboBox day = new JComboBox(dayVector);

        JComboBox month = new JComboBox(monthVector);
        JComboBox dayTo= new JComboBox(dayVector);
        JComboBox monthTo= new JComboBox(monthVector);

        //combo dei corsi
        JComboBox contentsCourse = new JComboBox();
        for (String c: comboCourse)
            contentsCourse.addItem(c);


        //caricamento combo ora
        String[] time =new String[25];
        time[0]="seleziona ora";
        int in=1;
        for (int i=0;i<10;i++){
            time[in]="0"+String.valueOf(i);
            in++;
        }
        for (int i=10;i<24;i++){
            time[in]=String.valueOf(i);
            in++;
        }

        JComboBox from = new JComboBox(time);
        JComboBox to = new JComboBox(time);

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new GridLayout(3,3));

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
        comboPanel.add(contentsCourse); //corsi
        comboPanel.add(new JLabel(""));



        /*** aggunta dei componenti al panello stats*/

        stats.add(new JLabel("Tempo medio di connessione di studenti alle pagine offerte da SeatIn:"));
        stats.add(timeConnctionAverage);
        stats.add(new JLabel("Numero complessivo di utenti connessi a SeatIn:"));
        stats.add(numberOfConnectionToSeatIn);
        numberOfConnectionToSeatIn.setText(String.valueOf(administrator.getUserConnected()));



        JPanel stats2 = new JPanel();
        stats2.setLayout(new GridLayout(7,2));

        JButton back = new JButton("Esci");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainContainer.add(adminPanel());
                statsfinal.setVisible(false);
            }
        });



        /**numero complessivo di utenti connessi che stanno visualizzando /interagento con i contenuti del corso */


        JTextField textContentsCourse = new JTextField();




        /**visualizzazzione numero complessivo di utenti che hanno effettuato il download
         * di una o piuù risorse ad intervalli di tempo**/


        JTextField textDownload = new JTextField(); // visualizzare il numero complessivo di utenti che hanno effetuato il
        // download nella faascia oraria selezionata.


        /** per numero compelssivo di accessi x corso in una determinata fascia oraria */



        JTextField textAccesCourse = new JTextField();



        /** Box tempo medio di connessione degli studenti per ogni corso*/



        JTextField textForConnectioCourse = new JTextField();

        /**  per numero complessivo di download in base al corso selezionato */


        JTextField textDownloadForCourse = new JTextField();


        stats2.add(new JLabel("Numero complessivo di utenti che stanno visuliazzando/interagendo con i contenuti del corso"));
        stats2.add(textContentsCourse);
        stats2.add(new JLabel("Numero complessivo di utenti che hanno effetuato il download in una determinata fascia oraria (info riguardante TUTTI i corsi):"));
        stats2.add(textDownload);
        stats2.add(new JLabel("Numero complessivo di accessi per corso in una determinata fasia oraria:"));
        stats2.add(textAccesCourse);
        stats2.add(new JLabel("Tempo medio di connessione degli studenti in base al corso selezionato:"));
        stats2.add(textForConnectioCourse);
        stats2.add(new JLabel("Numero complessivo di download in base al corso selezionato:"));
        stats2.add(textDownloadForCourse);
        stats2.add(new JLabel(""));
        stats2.add(new JLabel(""));
        stats2.add(new JLabel(""));
        stats2.add(back);


        statsfinal.add(stats);
        statsfinal.add(comboPanel);
        statsfinal.add(stats2);

        contentsCourse.addItemListener(new ItemListener() {
                                           @Override
                                           public void itemStateChanged(ItemEvent e) {
                                               int i=contentsCourse.getSelectedIndex()-1;
                                               //info sul corso selezionato
                                               String[] x=coursesAvailable.get(i);
                                               //contiene nome, codice, anno
                                               int userActiveInCourse=0;
                                               try {
                                                   userActiveInCourse=administrator.userActiveInACourse(x[1], Integer.parseInt(x[2]));
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               textContentsCourse.setText(String.valueOf(userActiveInCourse));
                                               Object timeConnectionAverage="";
                                               try {
                                                   timeConnectionAverage=administrator.timeStatisticsForAllCourses();
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               timeConnctionAverage.setText(String.valueOf(timeConnectionAverage));

                                               // administrator.totalAccessInACoursePage(x[1],Integer.parseInt(x[2]), new Timestamp(timeTo:00), new Timestamp(timeFrom:00) )
                                               int info=0;
                                               try {
                                                   info=administrator.showResourceDownloadFromCourse(x[1],Integer.parseInt(x[2]));
                                               } catch (SQLException e1) {
                                                   e1.printStackTrace();
                                               } catch (RemoteException e1) {
                                                   e1.printStackTrace();
                                               }
                                               textDownloadForCourse.setText(String.valueOf(info));
                                               Object averageConnectionByCourse="";
                                               try {
                                                   averageConnectionByCourse=administrator.timeStatisticsForCourse(x[1],Integer.parseInt(x[2]));
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
                                                                                               String timeTo=to.getSelectedItem().toString();
                                                                                               String timeFrom= from.getSelectedItem().toString();
                                                                                               String dayStart = String.valueOf((day.getSelectedItem()));

                                                                                               String monthStart=String.valueOf(month.getSelectedItem());
                                                                                               String monthEnd=String.valueOf(monthTo.getSelectedItem());
                                                                                               String dayEnd=String.valueOf(dayTo.getSelectedItem());



                                                                                               if (!dayStart.equals("gg") && !monthStart.equals("mm") && !dayEnd.equals("gg") && !monthEnd.equals("mm") &&!timeFrom.equals("seleziona ora") && !timeTo.equals("seleziona ora") )
                                                                                               {
                                                                                                   Timestamp to=Timestamp.valueOf("2018"+"-"+monthEnd+"-"+dayEnd+" "+timeFrom+":"+"00"+":"+"00");
                                                                                                   Timestamp from=Timestamp.valueOf("2018"+"-"+monthStart+"-"+dayStart+" "+timeTo+":"+"00"+":"+"00");

                                                                                                   System.out.print(from+ "    "+ to);

                                                                                                   int result=0;
                                                                                                   try {
                                                                                                       result= administrator.totalAccessInACoursePage(x[1],Integer.parseInt(x[2]),from,to);

                                                                                                   } catch (SQLException e1) {
                                                                                                       e1.printStackTrace();
                                                                                                   } catch (RemoteException e1) {
                                                                                                       e1.printStackTrace();
                                                                                                   }
                                                                                                   textAccesCourse.setText(String.valueOf("da:"+from + "a:"+to+"sono stati eseguiti "+result+ "accessi"));
                                                                                                   try {
                                                                                                       textDownload.setText(String.valueOf(administrator.showUserDownloadedInTime(from,to)));
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




        Container container= getContentPane();
        container.add(statsfinal);
        setVisible(true);
        setSize(1100,400);
        return statsfinal;
    }
    
}
