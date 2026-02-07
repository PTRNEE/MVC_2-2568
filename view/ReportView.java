package view;

import javax.swing.*;
import java.awt.*;

public class ReportView extends JPanel {
    public JTable table;
    public JButton backBtn = new JButton("Back to Menu");

    public ReportView() {
        setLayout(new BorderLayout());

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }
}
