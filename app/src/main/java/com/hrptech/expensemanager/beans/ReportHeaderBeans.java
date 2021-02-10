package com.hrptech.expensemanager.beans;

public class ReportHeaderBeans {
private String name;
private int align;
private float columnSize;
private int color;

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

    public float getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(float columnSize) {
        this.columnSize = columnSize;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
