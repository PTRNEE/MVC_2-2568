package model;

public class Shelter {
    public String id, name, riskLevel;
    public int capacity, current;

    public Shelter(String id, String name, int capacity, String riskLevel) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.riskLevel = riskLevel;
        this.current = 0;
    }

    public boolean isFull() {
        return current >= capacity;
    }
}
