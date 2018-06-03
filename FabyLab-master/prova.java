package seatIn.latoServer;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class prova {
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
}
