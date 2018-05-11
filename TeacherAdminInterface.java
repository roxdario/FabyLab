package latoServer;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
public class TeacherAdminInterface extends JLabel{

    public JPanel panelForRegistration() {

        JPanel panelTeacherAdmin= new JPanel();
        panelTeacherAdmin.setLayout(new GridLayout(2,2));;
        JTextField department = new JTextField();
        JButton ok = new JButton("REGISTRATI");
        panelTeacherAdmin.add(new JLabel ("Dipartimento :"));
        panelTeacherAdmin.add(department);
        panelTeacherAdmin.add(new JLabel(""));
        panelTeacherAdmin.add(ok);
        return panelTeacherAdmin;
    }
}