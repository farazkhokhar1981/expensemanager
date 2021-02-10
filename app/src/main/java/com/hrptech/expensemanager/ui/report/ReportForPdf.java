package com.hrptech.expensemanager.ui.report;

import android.app.Activity;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.PartiesBeans;
import com.hrptech.expensemanager.beans.ReportHeaderBeans;
import com.hrptech.expensemanager.beans.ReportInfoBeans;
import com.hrptech.expensemanager.beans.ReportMasterBeans;
import com.hrptech.expensemanager.beans.ReportRowBeans;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.itextpdf.text.Element;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportForPdf {
    Activity activity;
    TransactionDB transactionDB;
    CategoryDB categoryDB;
    String monthNumber[]={"Jan","Feb","Mar","Apr",
            "May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"};


    public ReportForPdf(Activity activity, TransactionDB transactionDB){
        this.activity = activity;
        this.transactionDB = transactionDB;
        this.categoryDB = new CategoryDB(activity);

    }


    public ReportMasterBeans getPartiesLedger(String reportName, String fDate,String tDate){
        ReportMasterBeans reportMasterBeans = new ReportMasterBeans();

        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDates(fDate,tDate);
        ArrayList<ReportRowBeans> listOfRow = new ArrayList<>();
        double dr=0;
        double cr=0;
        double balance = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beans = transactionBeans.get(index);
            ArrayList<ReportHeaderBeans> listOfHeaders = new ArrayList<>();
            ReportRowBeans pdfRowBeans = new ReportRowBeans();
            pdfRowBeans.setType(beans.getType());
            int color = R.color.colorBlack;
//            if(beans.getType().equalsIgnoreCase("cr")){
//                color = R.color.colorGreens;
//            }else {
//                color = R.color.colorRed;
//            }
            listOfHeaders.add(getRowBeans(""+(index+1), Element.ALIGN_LEFT,R.color.colorBlack));
            listOfHeaders.add(getRowBeans(beans.getName(), Element.ALIGN_LEFT,R.color.colorBlack));
            listOfHeaders.add(getRowBeans(beans.getIncome(), Element.ALIGN_RIGHT,R.color.colorGreens));
            listOfHeaders.add(getRowBeans(beans.getExpense(), Element.ALIGN_RIGHT,R.color.colorRed));
            dr+= Double.parseDouble(beans.getIncome());
            cr+= Double.parseDouble(beans.getExpense());
            if(dr>=cr){
                balance=dr-cr;
            }else if(cr>=dr){
                balance=cr-dr;
            }

            if(balance>0){
                listOfHeaders.add(getRowBeans(df.format(balance), Element.ALIGN_RIGHT,R.color.colorGreens));
            }else if(balance<0){
                listOfHeaders.add(getRowBeans(df.format(Math.abs(balance)), Element.ALIGN_RIGHT,R.color.colorRed));
            }else {
                listOfHeaders.add(getRowBeans(df.format(Math.abs(balance)), Element.ALIGN_RIGHT,R.color.colorGreens));
            }
            pdfRowBeans.setListOfStr(listOfHeaders);
            listOfRow.add(pdfRowBeans);
        }
        ArrayList<ReportHeaderBeans> headerBeansArrayList = new ArrayList<>();
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.S_No), Element.ALIGN_LEFT,1f, R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.Description), Element.ALIGN_LEFT,7f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.debit), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.credit), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.balance), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        reportMasterBeans.setHeaderBeansArrayList(headerBeansArrayList);
        reportMasterBeans.setListOfRow(listOfRow);
        reportMasterBeans.setReportInfoBeans(getReportInfoBeans(null,reportName.toUpperCase()+"("+fDate+")",""));
        return reportMasterBeans;
    }



    public ReportMasterBeans getPartiesLedgerDetail(String reportName, String pid, String pName, String fDate, String tDate){
        ReportMasterBeans reportMasterBeans = new ReportMasterBeans();
        ArrayList<ReportRowBeans> listOfRow = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        int rowCount=0;
        ArrayList<TransactionBeans> transactionTotalOpening = transactionDB.getTransactionRecordsToDatesCategoryOpeningAndClosing(fDate,pid);
        double balance = 1;
        for(int index=0; index<transactionTotalOpening.size(); index++){
            TransactionBeans beans = transactionTotalOpening.get(index);
            double dr = Double.parseDouble(beans.getIncome());
            double cr = Double.parseDouble(beans.getExpense());
            balance+=(dr-cr);


        }
        if(balance>0 || balance<0){
            ArrayList<ReportHeaderBeans> headerBeansArrayList = new ArrayList<>();
            ReportRowBeans pdfRowBeans = new ReportRowBeans();
            pdfRowBeans.setType("");
            headerBeansArrayList.add(getHeaderBeans(rowCount+"", Element.ALIGN_LEFT,1f, R.color.colorWhite));

            if(balance>0){
                headerBeansArrayList.add(getHeaderBeans("Opening Balance(DR)", Element.ALIGN_LEFT,7f,R.color.colorWhite));
                headerBeansArrayList.add(getHeaderBeans(decimalFormat.format(balance), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
                headerBeansArrayList.add(getHeaderBeans("0.00", Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
            }else {
                headerBeansArrayList.add(getHeaderBeans("Opening Balance(CR)", Element.ALIGN_LEFT,7f,R.color.colorWhite));
                headerBeansArrayList.add(getHeaderBeans("0.00", Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
                headerBeansArrayList.add(getHeaderBeans(decimalFormat.format(balance), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
            }
            reportMasterBeans.setHeaderBeansArrayList(headerBeansArrayList);
            reportMasterBeans.setListOfRow(listOfRow);
            rowCount++;
        }
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDatesCategoryDetail(fDate,tDate,pid);
        double totalDr = 0;
        double totalLineDr = 0;
        double totalCr = 0;
        double totalLineCr = 0;
        DecimalFormat dc = new DecimalFormat("0.00");
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beans = transactionBeans.get(index);
            ArrayList<ReportHeaderBeans> listOfHeaders = new ArrayList<>();
            ReportRowBeans pdfRowBeans = new ReportRowBeans();
            pdfRowBeans.setType(beans.getType());

            listOfHeaders.add(getRowBeans(""+(rowCount), Element.ALIGN_LEFT,R.color.colorBlack));
            listOfHeaders.add(getRowBeans(beans.getName(), Element.ALIGN_LEFT,R.color.colorBlack));
            double dr = Double.parseDouble(beans.getIncome());
            double cr = Double.parseDouble(beans.getExpense());
            totalDr+=dr;
            totalLineDr+=dr;
            totalCr+=cr;
            totalLineCr+=cr;
            listOfHeaders.add(getRowBeans(beans.getIncome(), Element.ALIGN_RIGHT,R.color.colorGreens));
            listOfHeaders.add(getRowBeans(beans.getExpense(), Element.ALIGN_RIGHT,R.color.colorRed));
            if(cr>0) {
                listOfHeaders.add(getRowBeans(dc.format(totalDr-totalCr), Element.ALIGN_RIGHT, R.color.colorRed));
                totalDr=0;
                totalCr=0;
            }else {
                listOfHeaders.add(getRowBeans("", Element.ALIGN_RIGHT, R.color.colorRed));
            }
            pdfRowBeans.setListOfStr(listOfHeaders);
            listOfRow.add(pdfRowBeans);
            rowCount++;
        }
        ArrayList<ReportHeaderBeans> totalArrayList = new ArrayList<>();
        totalArrayList.add(getRowBeans("", Element.ALIGN_LEFT,R.color.colorBlack));
        totalArrayList.add(getRowBeans("\tTotal", Element.ALIGN_LEFT,R.color.colorBlack));
        totalArrayList.add(getRowBeans(dc.format(totalLineDr), Element.ALIGN_RIGHT,R.color.colorGreens));
        totalArrayList.add(getRowBeans(dc.format(totalLineCr), Element.ALIGN_RIGHT,R.color.colorRed));
        totalArrayList.add(getRowBeans(dc.format(totalLineDr-totalLineCr), Element.ALIGN_RIGHT, R.color.colorRed));
        ReportRowBeans pdfRowBeans = new ReportRowBeans();
        pdfRowBeans.setListOfStr(totalArrayList);
        listOfRow.add(pdfRowBeans);
        ArrayList<ReportHeaderBeans> headerBeansArrayList = new ArrayList<>();
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.S_No), Element.ALIGN_LEFT,1f, R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.Description), Element.ALIGN_LEFT,7f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.debit), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.credit), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        headerBeansArrayList.add(getHeaderBeans(activity.getResources().getString(R.string.balance), Element.ALIGN_LEFT,2.4f,R.color.colorWhite));
        reportMasterBeans.setHeaderBeansArrayList(headerBeansArrayList);
        reportMasterBeans.setListOfRow(listOfRow);

        reportMasterBeans.setReportInfoBeans(getReportInfoBeans(null,reportName.toUpperCase()+"("+fDate+")",pName.toUpperCase()));
        return reportMasterBeans;
    }


    public ReportInfoBeans getReportInfoBeans(PartiesBeans partiesBeans, String reportName, String customerName){
        ReportInfoBeans reportInfoBeans = new ReportInfoBeans();

        reportInfoBeans.setCompanyName("");
        reportInfoBeans.setAddress("");
        reportInfoBeans.setContact("");
        reportInfoBeans.setEmail("");
        if(partiesBeans!=null) {
            reportInfoBeans.setCompanyName(partiesBeans.getName());
            reportInfoBeans.setAddress(partiesBeans.getAddress());
            reportInfoBeans.setContact(partiesBeans.getContact());
            reportInfoBeans.setEmail(partiesBeans.getEmail());
        }
        reportInfoBeans.setReportName(reportName);
        reportInfoBeans.setDateTime(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date()));
        reportInfoBeans.setCustomerName(customerName);
        return reportInfoBeans;
    }


    public ReportHeaderBeans getHeaderBeans(String name, int align, float columnSize, int color){
        ReportHeaderBeans beans = new ReportHeaderBeans();
        beans.setName(name);
        beans.setAlign(align);
        beans.setColumnSize(columnSize);
        beans.setColor(color);
        return beans;
    }
    public ReportHeaderBeans getRowBeans(String name, int align, int color){
        ReportHeaderBeans beans = new ReportHeaderBeans();
        beans.setName(name);
        beans.setAlign(align);
        beans.setColor(color);
        return beans;
    }
}
