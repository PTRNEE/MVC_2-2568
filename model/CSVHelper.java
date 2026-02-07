package model;

import java.io.*;
import java.util.*;

public class CSVHelper {

    public static List<String[]> read(String path) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                list.add(parts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void append(String path, String line) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(line + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean citizenExists(String citizenId) {
        List<String[]> data = read("data/citizens.csv");
        for (String[] row : data) {
            if (row[0].equalsIgnoreCase(citizenId)) {
                return true;
            }
        }
        return false;
    }

    public static void rewrite(String path, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {

            if (path.contains("assignments")) {
                pw.println("citizenId,shelterId,date");
            } else if (path.contains("citizens")) {
                pw.println("citizenId,name,age,health,date,type");
            } else if (path.contains("shelters")) {
                pw.println("shelterId,name,capacity,riskLevel");
            }

            for (String line : lines) {
                pw.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
