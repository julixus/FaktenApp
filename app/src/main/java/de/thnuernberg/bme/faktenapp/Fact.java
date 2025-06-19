package de.thnuernberg.bme.faktenapp;

public class Fact {
    public int id;
    public String title;
    public String text;
    public String imagePath;

    public Fact(int id, String title, String text, String imagePath) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
    }
}
