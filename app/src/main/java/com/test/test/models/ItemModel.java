package com.test.test.models;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * структура элемента
 */
public class ItemModel {
    private int id;
    private String name;
    private String imageUrl;
    private String description;
    private String time;
    private BmpModel bmpModel = new BmpModel();

    public ItemModel(int id, String name, String imageUrl, String description, String time) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime() {
        if (time != null) { // преобразовываем в формат даты
            try {
                Date d = new Date(Long.parseLong(time));
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
                return format.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BmpModel getBmpModel() {
        return bmpModel;
    }
}
