package com.hrptech.expensemanager.html;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HrptechWebViewDiv {
    String htmlCode ="";
    String categories="";
    String reports="";
    String years="";
    String headers="";
    String rows="";
    public void HrptechWebViewLoad(String categories,String reports,String years,String headers,String rows){
        this.categories = categories;
        this.reports = reports;
        this.years = years;
        this.headers = headers;
        this.rows = rows;
    }
    public String getHtmlCode(){
        htmlCode+=AttachedHtmlDoc();
        htmlCode+=AttachedHtmlCSSStyle();
        htmlCode+=AttachedHtmlFinal();
        return htmlCode;
    }

    public String AttachedHtmlDoc(){
        String htmlCode = "";
        htmlCode+="<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>\n" +
                "<title>Expense Manager - Track Your Daily Expense powered by Hatf Research Projects & Technologies</title>\n" +
                "<meta charset='utf-8'>\n" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>\n";
        return htmlCode;
    }

    public String AttachedHtmlCSSStyle(){
        String htmlCode = "";
        htmlCode+="<style>* {  box-sizing: border-box;\n" +
                "}" +
                "body {\n" +
                "  font-family: Arial, Helvetica, sans-serif; margin:0px;\n" +

                "}\n" +
                "/* Style the header */\n" +
                "header {\n" +
                "  background-color: #28512d;\n" +
                "  padding: 4px;\n" +
                "  text-align: center;\n" +
                "  font-size: 22px;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                "/* Create two columns/boxes that floats next to each other */\n" +
                "nav {\n" +
                "  float: left;\n" +
                "  width: 25%;\n" +
                "  height: 500px; /* only for demonstration, should be removed */\n" +
                "  background: #ccc;\n" +
                "  padding: 20px;\n" +
                "}\n" +
                "\n" +
                "/* Style the list inside the menu */\n" +
                "\n" +
                "article {\n" +
                "  float: left;\n" +
                "  padding: 20px;\n" +
                "  width: 75%;\n" +
                "  background-color: #f1f1f1;\n" +
                "  height: 500px; /* only for demonstration, should be removed */\n" +
                "}\n" +
                "\n" +
                "/* Clear floats after the columns */\n" +
                "section:after {\n" +
                "  content: \"\";\n" +
                "  display: table;\n" +
                "  clear: both;\n" +
                "}\n" +
                "\n" +
                "/* Style the footer */\n" +
                "footer {\n" +
                "  background-color: #777;\n" +
                "  padding: 10px;\n" +
                "  text-align: center;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                "/* Responsive layout - makes the two columns/boxes stack on top of each other instead of next to each other, on small screens */\n" +
                "@media (max-width: 600px) {\n" +
                "  nav, article {\n" +
                "    width: 100%;\n" +
                "    height: auto;\n" +
                "  }\n" +
                "}\n" +
                "#dropdown{\n" +
                " width:200px;   \n" +
                "}" +
                "#dropdown option{\n" +
                "  width:200px;   \n" +
                "}" +
                "\"table.table-style-one {\n" +
                "font-family: verdana,arial,sans-serif;\n" +
                "font-size:11px;\n" +
                "color:#333333;\n" +
                "border-width: 1px;\n" +
                "border-color: #3A3A3A;\n" +
                "border-collapse: collapse;\n" +
                "background-color: #00ff00;\n" +
                "}\n" +
                "#tr_header {\n" +
                "border-width: 1px;\n" +
                "padding: 8px;\n" +
                "border-style: solid;\n" +
                "border-color: #3A3A3A;\n" +
                "background-color: #B3B3B3;\n" +
                "}\n" +
                "#td_row {\n" +
                "border-width: 1px;\n" +
                "padding: 8px;\n" +
                "border-style: solid;\n" +
                "border-color: #3A3A3A;\n" +
                "background-color: #ffffff;\n" +
                "}\n" +
                "</style>\n";
        return htmlCode;
    }

    public String AttachedHtmlFinal(){
        String htmlCode = "";
        htmlCode+="</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<header>\n" +
                "  <h4>Expense Manager - Track your daily Income and Expense Transaction</h4>\n" +
                "</header>\n" +
                "\n" +
                ""+AttachedSection() +
                "\n" +
                "<footer>\n" +
                "  <p>Footer</p>\n" +
                "</footer>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        return htmlCode;
    }
    public String AttachedSection(){
        String htmlCode = "<section>\n" +
                "<nav>"+
                ""+AttachedDropDown()+""+
                "</nav>"+
                "  <article>\n";
                if(!headers.equalsIgnoreCase("")) {
                    htmlCode+="<table style='width: 100%;background-color: #c9c9c9;font-size: small;font-style: normal;font-family: cursive;'>\n" +

                            "" + getHeader() + "\n" +
                            "" + getRows() + "\n" +
                            "</table>";
                }
                htmlCode+="  </article>\n" +
                "</section>\n";
        return htmlCode;
    }

    public String AttachedDropDown(){

        String htmlCode = "";
        try {
            String categoriesCode = "";
            String list[] = categories.split("\\&");
            for(int index=0; index<list.length; index++) {
                String catBeans[] = list[index].split("\\|");
                categoriesCode += "<option value='"+catBeans[0]+"'>"+catBeans[1]+"</option>";

            }
            String reportList[] = reports.split("\\&");
            String reportCode = "";
            for(int index=0; index<reportList.length; index++) {
                String catBeans[] = reportList[index].split("\\|");
                reportCode += "<option value='"+catBeans[0].toUpperCase()+"'>"+catBeans[1].toUpperCase()+"</option>";
            }
            String yearsList[] = years.split("\\|");
            String yearsCode = "";
            for(int index=0; index<yearsList.length; index++) {
                yearsCode += "<option value='"+yearsList[index].toUpperCase()+"'>"+yearsList[index].toUpperCase()+"</option>";
            }
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            htmlCode = "<form action='?' method='GET'>\n" +
                    "Select a Report:&nbsp;<br/>\n" +
                    "<select id='dropdown' name=\"reports\">\n" +
                    ""+reportCode+"" +
                    "</select><br/>\n" +
                    "Select a Category:&nbsp;<br/>\n" +
                    "<select id='dropdown' name=\"category\">\n" +
                    ""+categoriesCode+"" +
                    "</select><br/>\n" +
                    "Select a Years:&nbsp;<br/>\n" +
                    "<select id='dropdown' name=\"Years\">\n" +
                    ""+yearsCode+"" +
                    "</select>\n" +
                    "<br/>\n" +
                    "From: <br/><input id='dropdown' type=\"date\" name=\"fromDate\" value="+currentDate+"> <br/>To: <br/><input id='dropdown' type=\"date\" name=\"toDate\" value="+currentDate+"><br/><br/>" +
                    "<input type=\"submit\" value=\"Submit\" />\n" +
                    "</form>";
        }catch(Exception e){

        }
        return htmlCode;
    }

    public String getHeader(){
        String htmlCode = "";
        htmlCode+="<tr style='background-color: #676363;text-align: left;height: 25px;margin: auto;font-size: 11px; color:white'>";
        String headerList[] = headers.split("\\|");
        for(int index=0; index<headerList.length; index++) {
            String style="";
            if(headerList[index].equalsIgnoreCase("Income")){
                style="width: 12%;";
            }else if(headerList[index].equalsIgnoreCase("Expense")){
                style="width: 12%;";
            }else if(headerList[index].equalsIgnoreCase("Balance")){
                style="width: 12%;";
            }else if(headerList[index].equalsIgnoreCase("Opening")){
                style="width: 12%;";
            }
            htmlCode += "<th style='"+style+"'>";
            htmlCode +=headerList[index].toUpperCase();
            htmlCode += "</th>";
        }
        htmlCode+="</tr>";
        return htmlCode;
    }

    public String table(){
        String htmlCode = "" +
                "<table class=\"table-style-one\">\n" +
                "\t<thead>\n" +
                "\t<tr>\n" +
                "\t\t<th>ID</th><th>First Name</th><th>Last Name</th>\n" +
                "\t</tr>\n" +
                "\t</thead>\n" +
                "\t<tbody>\n" +
                "\t<tr>\n" +
                "\t\t<td>235312</td>\n" +
                "\t\t<td>John</td>\n" +
                "\t\t<td>Doe</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>453123</td>\n" +
                "\t\t<td>Mark</td>\n" +
                "\t\t<td>Jones</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>998332</td>\n" +
                "\t\t<td>Jonathan</td>\n" +
                "\t\t<td>Smith</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>345612</td>\n" +
                "\t\t<td>Andrew</td>\n" +
                "\t\t<td>McArthur</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>453123</td>\n" +
                "\t\t<td>Adam</td>\n" +
                "\t\t<td>Fuller</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>998332</td>\n" +
                "\t\t<td>Tyler</td>\n" +
                "\t\t<td>Watt</td>\n" +
                "\t</tr>\n" +
                "\t</tbody>\n" +
                "</table>";
        return htmlCode;
    }

    public String getRows(){
        String htmlCode="";
        String rowsList[] = rows.split("\\$");
        for(int index=0; index<rowsList.length; index++) {
            String rowData[] = rowsList[index].split("\\|");
            htmlCode+="<tr>";
            for(int rowIndex=0; rowIndex<rowData.length; rowIndex++) {
                htmlCode += "<td>";
                htmlCode += rowData[rowIndex].toUpperCase();
                htmlCode += "</td>";
            }
            htmlCode+="</tr>";
        }
        return htmlCode;
    }

}
