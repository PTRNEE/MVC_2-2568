package view;

import javax.swing.*;

public class MenuPanel extends JPanel {
    public JButton citizenBtn = new JButton("Citizen Management");
    public JButton shelterBtn = new JButton("Shelter Allocation");
    public JButton reportBtn = new JButton("Report");

    public MenuPanel() {
        add(citizenBtn);
        add(shelterBtn);
        add(reportBtn);
    }
}
