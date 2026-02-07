package model;

public class Citizen {
    public String id, name, healthRisk, registerDate, type;
    public int age;

    public Citizen(String id, String name, int age, String healthRisk,
                   String registerDate, String type) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.healthRisk = healthRisk;
        this.registerDate = registerDate;
        this.type = type;
    }

    public boolean isPriority() {
        return age <= 12 || age >= 60;
    }
}
