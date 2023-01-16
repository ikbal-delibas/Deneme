package com.example.deneme;

import java.util.List;

public class GroupModel {
    private String name, aciklama, image, uid;
    private List<String> numbers;
    public GroupModel() {
    }

    public GroupModel(String name, String aciklama, String image, List<String> numbers, String uid) {
        this.name = name;
        this.aciklama = aciklama;
        this.image = image;
        this.numbers = numbers;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return aciklama;
    }

    public void setDescription(String description) {
        this.aciklama = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
