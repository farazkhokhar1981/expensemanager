package com.hrptech.expensemanager.html;

public class HrptechWebView {

    String css = "<style type=\"text/css\">\n" +
            "table.table-style-one {\n" +
            "font-family: verdana,arial,sans-serif;\n" +
            "font-size:11px;\n" +
            "color:#333333;\n" +
            "border-width: 1px;\n" +
            "border-color: #3A3A3A;\n" +
            "border-collapse: collapse;\n" +
            "background-color: #00ff00;\n" +
            "}" +
            "table.table-style-one th {\n" +
            "border-width: 1px;\n" +
            "padding: 8px;\n" +
            "border-style: solid;\n" +
            "border-color: #3A3A3A;\n" +
            "background-color: #B3B3B3;\n" +
            "}\n" +
            "table.table-style-one td {\n" +
            "border-width: 1px;\n" +
            "padding: 8px;\n" +
            "border-style: solid;\n" +
            "border-color: #3A3A3A;\n" +
            "background-color: #ffffff;\n" +
            "}" +
            "body {\n" +
            "  margin: 0;\n" +
            "  padding: 0;\n" +
            "  background-color: #004882;\n" +
            "}\n" +
            "\n" +
            ".box {\n" +
            "  position: absolute;\n" +
            "  top: 50%;\n" +
            "  left: 50%;\n" +
            "  transform: translate(-50%, -50%);\n" +
            "}\n" +
            "\n" +
            ".box select {\n" +
            "  background-color: #0563af;\n" +
            "  color: white;\n" +
            "  padding: 12px;\n" +
            "  width: 250px;\n" +
            "  border: none;\n" +
            "  font-size: 20px;\n" +
            "  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);\n" +
            "  -webkit-appearance: button;\n" +
            "  appearance: button;\n" +
            "  outline: none;\n" +
            "}\n" +
            "\n" +
            ".box::before {\n" +
            "  content: \"\\f13a\";\n" +
            "  font-family: FontAwesome;\n" +
            "  position: absolute;\n" +
            "  top: 0;\n" +
            "  right: 0;\n" +
            "  width: 20%;\n" +
            "  height: 100%;\n" +
            "  text-align: center;\n" +
            "  font-size: 28px;\n" +
            "  line-height: 45px;\n" +
            "  color: rgba(255, 255, 255, 0.5);\n" +
            "  background-color: rgba(255, 255, 255, 0.1);\n" +
            "  pointer-events: none;\n" +
            "}\n" +
            ".box:hover::before {\n" +
            "  color: rgba(255, 255, 255, 0.6);\n" +
            "  background-color: rgba(255, 255, 255, 0.2);\n" +
            "}"+
            ".box select option {\n" +
            "  padding: 30px;\n" +
            "}" +
            "</style>";
    public String CreateReport(String title,String description,String dateTime,String reports,String categories,String headers,String rows){
        String htmlCode = "";
            htmlCode+="<html>";
            htmlCode+="<head>" +
                    "<title>Expense Manager - Track Your Daily Expense powered by Hatf Research Projects & Technologies</title>" +
                    "" +css+""+
                    "</head>";
            htmlCode+="<body>";
            htmlCode+="<table class='table-style-one'>";
            htmlCode+="<h1>";
            htmlCode+=title.toUpperCase();
            htmlCode+="</h1>";
            htmlCode+="<h2>";
            htmlCode+=description.toUpperCase();
            htmlCode+="</h2>";
            htmlCode+="<h5>";
            htmlCode+=dateTime.toUpperCase();
            htmlCode+="</h5>";
            htmlCode+="</table>";
            htmlCode+=getReports(reports);
            htmlCode+=getCategories(categories);

            htmlCode+="<table class='table-style-one'>";

                if(!headers.equalsIgnoreCase("")){

                    htmlCode+=getHeader(headers);
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

                }

            htmlCode+="</table>";
            htmlCode+="</body></html>";
        return htmlCode;
    }
    public String getHeader(String headers){
        String htmlCode = "";
        htmlCode+="<tr>";
        String headerList[] = headers.split("\\|");
        for(int index=0; index<headerList.length; index++) {
            htmlCode += "<td>";
            htmlCode +=headerList[index].toUpperCase();
            htmlCode += "</td>";
        }
        htmlCode+="</tr>";
        return htmlCode;
    }
    public String getReports(String reports){
        String htmlCode = "";
        htmlCode+="<select class='box'>";
        String reportList[] = reports.split("\\|");
        for(int index=0; index<reportList.length; index++) {
            htmlCode += "<option>";
            htmlCode +=reportList[index].toUpperCase();
            htmlCode += "</option>";
        }
        htmlCode+="</select>";
        return htmlCode;

    }

    public String getCategories(String categories){
        String htmlCode = "";
        htmlCode+="<select class='box'>";
        String list[] = categories.split("\\&");
        for(int index=0; index<list.length; index++) {
            String catBeans[] = list[index].split("\\|");
            htmlCode += "<option>";
                try {
                    htmlCode += "<table class='table-style-one'>";
                    htmlCode += "<tr>";
                    htmlCode += "<td>" + catBeans[0] + "</td>";
                    htmlCode += "<td>" + catBeans[1] + "</td>";
                    htmlCode += "<td>" + catBeans[2] + "</td>";
                    htmlCode += "</tr>";
                    htmlCode += "</table>";
                }catch(Exception e){

                }
            htmlCode += "</option>";
        }
        htmlCode+="</select>";
        return htmlCode;

    }
}
