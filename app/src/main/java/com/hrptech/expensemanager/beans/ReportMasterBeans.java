package com.hrptech.expensemanager.beans;

import java.util.ArrayList;

public class ReportMasterBeans {
    private java.util.ArrayList<ReportRowBeans> listOfRow;
    private ArrayList<ReportHeaderBeans> headerBeansArrayList;
    private ReportInfoBeans reportInfoBeans;
    public ArrayList<ReportRowBeans> getListOfRow() {
        return listOfRow;
    }

    public void setListOfRow(ArrayList<ReportRowBeans> listOfRow) {
        this.listOfRow = listOfRow;
    }

    public ArrayList<ReportHeaderBeans> getHeaderBeansArrayList() {
        return headerBeansArrayList;
    }

    public void setHeaderBeansArrayList(ArrayList<ReportHeaderBeans> headerBeansArrayList) {
        this.headerBeansArrayList = headerBeansArrayList;
    }

    public ReportInfoBeans getReportInfoBeans() {
        return reportInfoBeans;
    }

    public void setReportInfoBeans(ReportInfoBeans reportInfoBeans) {
        this.reportInfoBeans = reportInfoBeans;
    }
}
