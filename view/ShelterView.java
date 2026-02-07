package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShelterView extends JPanel {

    public JTable shelterTable;
    public DefaultTableModel shelterTableModel;

    public JTable citizenTable;
    public DefaultTableModel citizenTableModel;

    public JButton addBtn = new JButton("Add Shelter");
    public JButton assignBtn = new JButton("Assign Citizen");
    public JButton unassignBtn = new JButton("Unassign Citizen");
    public JButton backBtn = new JButton("Back to Menu");

    public ShelterView() {
        setLayout(new BorderLayout(10, 10));

        String[] shelterColumns = {
            "Shelter ID",
            "Name",
            "Capacity",
            "Current Occupants",
            "Risk Level"
        };

        shelterTableModel = new DefaultTableModel(shelterColumns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        shelterTable = new JTable(shelterTableModel);
        shelterTable.setRowHeight(18);

        JScrollPane shelterScroll = new JScrollPane(shelterTable);

        String[] citizenColumns = {
            "Citizen ID",
            "Name",
            "Age",
            "Health",
            "Type",
            "Assigned Shelter"
        };

        citizenTableModel = new DefaultTableModel(citizenColumns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        citizenTable = new JTable(citizenTableModel);
        citizenTable.setRowHeight(18);

        JScrollPane citizenScroll = new JScrollPane(citizenTable);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                shelterScroll,
                citizenScroll
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);

        add(splitPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(addBtn);
        bottom.add(assignBtn);
        bottom.add(unassignBtn);
        bottom.add(backBtn);

        add(bottom, BorderLayout.SOUTH);
    }
}
