package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public CardLayout layout = new CardLayout();
    public JPanel container = new JPanel(layout);

    public MainFrame() {
        setTitle("Emergency Shelter System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(container);
    }
}
