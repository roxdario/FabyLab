package seatIn.latoServer;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class StudentInterface extends JFrame {
    public JPanel panelForRegistration() {
        List<String> listWithStudentInformation= new ArrayList<String>();
        JPanel panelStudent= new JPanel();
        panelStudent.setLayout(new GridLayout(4,2));
        Container containerStudent = getContentPane();
        containerStudent.setLayout(new GridLayout(4, 2));
        JTextField courseDegree = new JTextField();
        JTextField matriculation = new JTextField();
        String year[] = {"", "1", "2", "3"};
        String kindYear[] = {"", "fuori corso", "in corso"};
        JComboBox<String> comboBoxYear = new JComboBox<String>(year);
        JComboBox<String> comboBoxKindYear = new JComboBox<String>(kindYear);
        JPanel panelForYear =new JPanel();
        panelForYear.setLayout(new GridLayout(1,2));
        panelForYear.add(comboBoxYear);
        panelForYear.add(comboBoxKindYear);
        JLabel courseYear = new JLabel("anno di corso:");
        JButton ok = new JButton("REGISTRATI");
        panelStudent.add(new JLabel("Corso di laurea: "));
        panelStudent.add(courseDegree);
        panelStudent.add(new JLabel("Anno di immatricolazione: "));
        panelStudent.add(matriculation);
        panelStudent.add(new JLabel("anno di corso:"));
        panelStudent.add(panelForYear);
        panelStudent.add(new JLabel(""));
        panelStudent.add(ok);
        containerStudent.add(panelStudent);
        // containerStudent.add(panelStudent);
        //  listWithStudentInformation.add (new String(matriculation.getText()));
        // listWithStudentInformation.add( new String(courseDegree.getText()));
        //  listWithStudentInformation.add( (String) comboBoxYear.getSelectedItem());
        //listWithStudentInformation.add((String) comboBoxKindYear.getSelectedItem());
        setVisible(true);

        return panelStudent;
    }
}