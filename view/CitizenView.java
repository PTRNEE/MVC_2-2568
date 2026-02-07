package view;

import javax.swing.*;
import java.awt.*;

public class CitizenView extends JPanel {
    public JTable table;
    public JButton addBtn = new JButton("Add Citizen");
    public JComboBox<String> typeFilter =
            new JComboBox<>(new String[]{"ALL","NORMAL","RISK","VIP"});
    public JButton backBtn = new JButton("Back to Menu");

    public CitizenView() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(addBtn);
        bottom.add(new JLabel("Filter by Type:"));
        
        bottom.add(typeFilter);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }
}
