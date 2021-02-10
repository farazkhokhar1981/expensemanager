package com.hrptech.expensemanager.beans;

import java.util.ArrayList;

public class ReportRowBeans {
    private ArrayList<ReportHeaderBeans> listOfStr = new ArrayList<>();
    private int align;
    private String type;
    private float columSize = 0;

    public ArrayList<ReportHeaderBeans> getListOfStr() {
        return listOfStr;
    }

    public void setListOfStr(ArrayList<ReportHeaderBeans> listOfStr) {
        this.listOfStr = listOfStr;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getColumSize() {
        return columSize;
    }

    public void setColumSize(float columSize) {
        this.columSize = columSize;
    }
}
