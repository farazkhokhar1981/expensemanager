package com.hrptech.expensemanager.beans;

import com.itextpdf.text.BaseColor;

public class PDFHeaderBeans {
    private String name;
    private int align;
    private int fontSize;
    private int fontStyle;
    private BaseColor fColor;
    private BaseColor bgColor;
    private BaseColor brColor;
    private float columnSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public BaseColor getfColor() {
        return fColor;
    }

    public void setfColor(BaseColor fColor) {
        this.fColor = fColor;
    }

    public BaseColor getBgColor() {
        return bgColor;
    }

    public void setBgColor(BaseColor bgColor) {
        this.bgColor = bgColor;
    }

    public BaseColor getBrColor() {
        return brColor;
    }

    public void setBrColor(BaseColor brColor) {
        this.brColor = brColor;
    }

    public float getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(float columnSize) {
        this.columnSize = columnSize;
    }
}
