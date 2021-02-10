package com.hrptech.expensemanager.moreapp;

/**
 * Created by faraz on 5/5/2016.
 */
public class NoteBookBean {
    private String id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String titleColor="";
    private String descriptionColor="";
    private String titleFont="";
    private String descriptionFont="";
    private String titleStyle="";
    private String descriptionStyle="";
    private String sticker="";
    private int ResImage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(String descriptionColor) {
        this.descriptionColor = descriptionColor;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(String titleFont) {
        this.titleFont = titleFont;
    }

    public String getDescriptionFont() {
        return descriptionFont;
    }

    public void setDescriptionFont(String descriptionFont) {
        this.descriptionFont = descriptionFont;
    }

    public String getTitleStyle() {
        return titleStyle;
    }

    public void setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
    }

    public String getDescriptionStyle() {
        return descriptionStyle;
    }

    public void setDescriptionStyle(String descriptionStyle) {
        this.descriptionStyle = descriptionStyle;
    }

    public int getResImage() {
        return ResImage;
    }

    public void setResImage(int resImage) {
        ResImage = resImage;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }
}
