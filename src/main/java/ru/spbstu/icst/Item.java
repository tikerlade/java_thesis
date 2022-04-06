package ru.spbstu.icst;

public class Item {
    String s1;
    String s2;

    public Item() {}

    public Item(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public String getS1() {
        return this.s1;
    }

    public String getS2() {
        return this.s2;
    }

    public void setS1(String s) {
        this.s1 = s;
    }

    public void setS2(String s) {
        this.s2 = s;
    }
}
