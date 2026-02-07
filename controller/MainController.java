package controller;

import model.CSVHelper;
import view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    MainFrame frame;
    MenuPanel menu;
    CitizenView citizenView;
    ShelterView shelterView;
    ReportView reportView;

    public MainController() {
        frame = new MainFrame();
        menu = new MenuPanel();
        citizenView = new CitizenView();
        shelterView = new ShelterView();
        reportView = new ReportView();

        frame.container.add(menu, "menu");
        frame.container.add(citizenView, "citizen");
        frame.container.add(shelterView, "shelter");
        frame.container.add(reportView, "report");

        // menu
        menu.citizenBtn.addActionListener(e -> showCitizen());
        menu.shelterBtn.addActionListener(e -> showShelter());
        menu.reportBtn.addActionListener(e -> showReport());

        // back buttons
        citizenView.backBtn.addActionListener(e -> goMenu());
        shelterView.backBtn.addActionListener(e -> goMenu());
        reportView.backBtn.addActionListener(e -> goMenu());

        // citizen actions
        citizenView.addBtn.addActionListener(e -> addCitizen());
        citizenView.typeFilter.addActionListener(e -> showCitizen());

        // shelter action
        shelterView.addBtn.addActionListener(e -> addShelter());
        shelterView.assignBtn.addActionListener(e -> assignShelter());
        shelterView.unassignBtn.addActionListener(e -> unassignShelter());

        frame.layout.show(frame.container, "menu");
        frame.setVisible(true);
    }

    boolean hasUnassignedPriorityCitizen() {
        List<String[]> citizens = CSVHelper.read("data/citizens.csv");
        List<String[]> assigns = CSVHelper.read("data/assignments.csv");

        for (String[] c : citizens) {
            int age = Integer.parseInt(c[2]);
            boolean isPriority = (age < 12 || age >= 60);

            if (!isPriority) continue;

            boolean assigned = false;
            for (String[] a : assigns) {
                if (a[0].equals(c[0])) {
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                return true;
            }
        }
        return false;
    }

    String[] getCitizenById(String citizenId) {
        for (String[] c : CSVHelper.read("data/citizens.csv")) {
            if (c[0].equalsIgnoreCase(citizenId)) {
                return c;
            }
        }
        return null;
    }

    String[] getShelterById(String shelterId) {
        for (String[] s : CSVHelper.read("data/shelters.csv")) {
            if (s[0].equalsIgnoreCase(shelterId)) {
                return s;
            }
        }
        return null;
    }

    void goMenu() {
        frame.layout.show(frame.container, "menu");
    }

    void showCitizen() {
        List<String[]> data = CSVHelper.read("data/citizens.csv");

        String selectedType =
                citizenView.typeFilter.getSelectedItem().toString();

        DefaultTableModel m = new DefaultTableModel(
                new String[]{"ID","Name","Age","Health","Date","Type"}, 0
        );

        for (String[] r : data) {
            String type = r[5];

            if (selectedType.equals("ALL") || type.equals(selectedType)) {
                m.addRow(r);
            }
        }

        citizenView.table.setModel(m);
        frame.layout.show(frame.container, "citizen");
    }


    void addCitizen() {
        String id = JOptionPane.showInputDialog(
                null,
                "Enter Citizen ID",
                "Citizen Registration",
                JOptionPane.PLAIN_MESSAGE
        );

        if (id == null || id.isBlank()) return;

        if (CSVHelper.citizenExists(id)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Citizen ID already registered!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();

        JComboBox<String> healthBox =
                new JComboBox<>(new String[]{"LOW","MEDIUM","HIGH"});

        JComboBox<String> typeBox =
                new JComboBox<>(new String[]{"NORMAL","RISK","VIP"});

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Name"));
        panel.add(nameField);

        panel.add(new JLabel("Age"));
        panel.add(ageField);

        panel.add(new JLabel("Health Risk"));
        panel.add(healthBox);

        panel.add(new JLabel("Citizen Type"));
        panel.add(typeBox);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Citizen Details",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String health = healthBox.getSelectedItem().toString();
        String type = typeBox.getSelectedItem().toString();
        String date = LocalDate.now().toString();

        CSVHelper.append(
                "data/citizens.csv",
                id + "," + name + "," + age + "," + health + "," + date + "," + type
        );

        JOptionPane.showMessageDialog(
                null,
                "Citizen registered successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        showCitizen();
    }


    void updateCitizen() {
        int row = citizenView.table.getSelectedRow();
        if (row == -1) return;

        String newType = JOptionPane.showInputDialog("New Type:");
        citizenView.table.setValueAt(newType, row, 5);
    }

    void showShelter() {
        List<String[]> shelters = CSVHelper.read("data/shelters.csv");
        List<String[]> assigns = CSVHelper.read("data/assignments.csv");

        shelterView.shelterTableModel.setRowCount(0);

        for (String[] s : shelters) {
            String shelterId = s[0];
            String name = s[1];
            int capacity = Integer.parseInt(s[2]);
            String risk = s[4];

            int current = 0;
            for (String[] a : assigns) {
                if (a[1].equals(shelterId)) {
                    current++;
                }
            }

            shelterView.shelterTableModel.addRow(new Object[]{
                shelterId,
                name,
                capacity,
                current,
                risk
            });
        }

        loadCitizenForShelter();

        frame.layout.show(frame.container, "shelter");
    }

    void loadCitizenForShelter() {
        List<String[]> citizens = CSVHelper.read("data/citizens.csv");
        List<String[]> assigns = CSVHelper.read("data/assignments.csv");

        shelterView.citizenTableModel.setRowCount(0);

        for (String[] c : citizens) {
            String citizenId = c[0];
            String status = "UNASSIGNED";

            for (String[] a : assigns) {
                if (a[0].equals(citizenId)) {
                    status = "ASSIGNED";
                    break;
                }
            }

            shelterView.citizenTableModel.addRow(new Object[]{
                citizenId,
                c[1], // name
                c[2], // age
                c[3], // health
                c[5], // type
                status
            });
        }
    }


    void addShelter() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();

        JComboBox<String> riskBox =
                new JComboBox<>(new String[]{"LOW","MEDIUM","HIGH"});

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Shelter ID"));
        panel.add(idField);

        panel.add(new JLabel("Shelter Name"));
        panel.add(nameField);

        panel.add(new JLabel("Capacity"));
        panel.add(capacityField);

        panel.add(new JLabel("Risk Level"));
        panel.add(riskBox);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Add Shelter",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        String id = idField.getText();
        String name = nameField.getText();
        String capacityText = capacityField.getText();
        String risk = riskBox.getSelectedItem().toString();

        if (id.isBlank() || name.isBlank() || capacityText.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Capacity must be a number");
            return;
        }

        List<String[]> shelters = CSVHelper.read("data/shelters.csv");
        for (String[] s : shelters) {
            if (s[0].equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(null, "Shelter ID already exists");
                return;
            }
        }

        CSVHelper.append(
                "data/shelters.csv",
                id + "," + name + "," + capacity + ",0," + risk
        );

        JOptionPane.showMessageDialog(null, "Shelter added successfully");
        showShelter();
    }

    void assignShelter() {
        String citizenId = JOptionPane.showInputDialog("Citizen ID:");
        if (citizenId == null || citizenId.isBlank()) return;

        String[] citizen = getCitizenById(citizenId);
        if (citizen == null) {
            JOptionPane.showMessageDialog(null, "Citizen not found");
            return;
        }

        for (String[] a : CSVHelper.read("data/assignments.csv")) {
            if (a[0].equals(citizenId)) {
                JOptionPane.showMessageDialog(null, "Citizen already assigned");
                return;
            }
        }

        int age = Integer.parseInt(citizen[2]);
        String type = citizen[5]; // 🔥 ใช้ TYPE

        boolean isPriority = (age < 12 || age >= 60);
        if (!isPriority && hasUnassignedPriorityCitizen()) {
            JOptionPane.showMessageDialog(
                    null,
                    "There are unassigned children or elderly.\nPlease assign them first."
            );
            return;
        }

        String shelterId = JOptionPane.showInputDialog("Shelter ID:");
        if (shelterId == null || shelterId.isBlank()) return;

        String[] shelter = getShelterById(shelterId);
        if (shelter == null) {
            JOptionPane.showMessageDialog(null, "Shelter not found");
            return;
        }

        int capacity = Integer.parseInt(shelter[2]);
        String shelterRisk = shelter[4];

        if (type.equals("RISK") && !shelterRisk.equals("LOW")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Citizen with RISK type must be assigned to LOW risk shelter"
            );
            return;
        }

        int current = 0;
        for (String[] a : CSVHelper.read("data/assignments.csv")) {
            if (a[1].equals(shelterId)) current++;
        }

        if (current >= capacity) {
            JOptionPane.showMessageDialog(null, "Shelter is full");
            return;
        }

        CSVHelper.append(
                "data/assignments.csv",
                citizenId + "," + shelterId + "," + LocalDate.now()
        );

        JOptionPane.showMessageDialog(null, "Assigned successfully");
        showShelter();
    }


    void unassignShelter() {
        String citizenId = JOptionPane.showInputDialog("Citizen ID:");
        if (citizenId == null || citizenId.isBlank()) return;

        List<String[]> assigns = CSVHelper.read("data/assignments.csv");
        List<String> newData = new ArrayList<>();

        boolean found = false;

        for (String[] a : assigns) {
            if (a[0].equals(citizenId)) {
                found = true;
            } else {
                newData.add(String.join(",", a));
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Citizen not assigned");
            return;
        }

        CSVHelper.rewrite("data/assignments.csv", newData);

        JOptionPane.showMessageDialog(null, "Unassigned successfully");
        showShelter();
    }

    void showReport() {
        List<String[]> citizens = CSVHelper.read("data/citizens.csv");
        List<String[]> assigns = CSVHelper.read("data/assignments.csv");

        DefaultTableModel m = new DefaultTableModel(
            new String[]{"CitizenID","Age","Type","Health","Shelter","Status"}, 0
        );

        for (String[] c : citizens) {
            String citizenId = c[0];
            String shelterId = "-";
            String status = "UNASSIGNED";

            for (String[] a : assigns) {
                if (a[0].equals(citizenId)) {
                    shelterId = a[1];
                    status = "ASSIGNED";
                    break;
                }
            }

            m.addRow(new Object[]{
                citizenId,
                c[2],   // age
                c[5],   // type
                c[3],   // health
                shelterId,
                status
            });
        }

        reportView.table.setModel(m);
        frame.layout.show(frame.container, "report");
    }

}
