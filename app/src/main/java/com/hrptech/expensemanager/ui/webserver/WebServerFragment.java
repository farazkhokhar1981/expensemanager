package com.hrptech.expensemanager.ui.webserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hrptech.expensemanager.MoreActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.html.HrptechWebViewDiv;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class WebServerFragment extends Activity {

    TextView ipServerTxt;
    Button startServerBen;
    Button startdesktopServerBen;
    ImageView back_Btn;
    Activity root;
    ServerSocket httpServerSocket;
    TransactionDB transactionDB;
    CategoryDB categoryDB;
    String carArr[];
    String year[];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_webserver);
        root = this;//inflater.inflate(R.layout.fragment_webserver, container, false);
        ipServerTxt = (TextView) root.findViewById(R.id.serverIp_txt);
        startServerBen = (Button) root.findViewById(R.id.serverStartBtn);
        startServerBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start server
                ipServerTxt.setText(getIpAddress() + ":"
                        + HttpServerThread.HttpServerPORT + "\n");

                HttpServerThread httpServerThread = new HttpServerThread();
                httpServerThread.start();
                //Load FullScreen Add
                //      AdsInit();
            }
        });

        startdesktopServerBen = (Button) root.findViewById(R.id.serverNoWebStartBtn);
        startdesktopServerBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start server
                ipServerTxt.setText(getIpAddress() + ":"
                        + HttpServerThread.HttpServerPORT + "\n");

                DesktopServerThread httpServerThread = new DesktopServerThread();
                httpServerThread.start();
                //Load FullScreen Add
                //      AdsInit();
            }
        });

        back_Btn = (ImageView) root.findViewById(R.id.back_btn);
        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToMain();
            }
        });
        transactionDB = new TransactionDB(this.getActivity());
        categoryDB = new CategoryDB(this.getActivity());
        carArr = getResources().getStringArray(R.array.report_arrays);
        year = getResources().getStringArray(R.array.year_arrays);
        //ads of Admob
        mInterstitialAd = new InterstitialAd(this.getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
       // AdsInit();
        LoadBanner();
    }
public Activity getActivity(){
        return root;
}
    public void AdsInit() {
        mInterstitialAd = new InterstitialAd(this.getActivity());

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
                    LoadBanner();
//                }
//            }
//        });
    }

    public void LoadBanner() {
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FrameLayout frameLayout =findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
    }

    InterstitialAd mInterstitialAd;


    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    private class HttpServerThread extends Thread {

        static final int HttpServerPORT = 8080;

        @Override
        public void run() {
            Socket socket = null;

            try {

                httpServerSocket = new ServerSocket(HttpServerPORT);

                while (true) {
                    socket = httpServerSocket.accept();

                    HttpResponseThread httpResponseThread =
                            new HttpResponseThread(
                                    socket,
                                    "");
                    httpResponseThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

    private class DesktopServerThread extends Thread {

        static final int HttpServerPORT = 8080;

        @Override
        public void run() {
            Socket socket = null;

            try {

                httpServerSocket = new ServerSocket(HttpServerPORT);

                while (true) {
                    socket = httpServerSocket.accept();

                    DesktopResponseThread httpResponseThread =
                            new DesktopResponseThread(
                                    socket,
                                    "");
                    httpResponseThread.start();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

    private class HttpResponseThread extends Thread {

        Socket socket;
        String h1;

        HttpResponseThread(Socket socket, String msg) {
            this.socket = socket;
            h1 = msg;
        }

        String msgLog = "";

        @Override
        public void run() {
            BufferedReader is;
            PrintWriter os;
            String request;
//HrptechWebView webView = new HrptechWebView();
            HrptechWebViewDiv webViewDiv = new HrptechWebViewDiv();

            try {
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                request = is.readLine();
                String resHtml = "";
                String transactionRecord = "";

                    for (int inRp = 0; inRp < carArr.length; inRp++) {
                        transactionRecord += (inRp + 1) + "|" + carArr[inRp] + "&";
                    }

                transactionRecord += "#";
                ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecords();
                for (int inRp = 0; inRp < categoryBeans.size(); inRp++) {

                    transactionRecord += categoryBeans.get(inRp).getId() + "|" + categoryBeans.get(inRp).getName() + "|" + categoryBeans.get(inRp).getType() + "&";
                }
                transactionRecord += "#";

                    for (int inRp = 0; inRp < year.length; inRp++) {
                        transactionRecord += year[inRp] + "|";
                    }
                    transactionRecord += "#";
                if(categoryBeans.size()<1){
                    transactionRecord="";
                }

                resHtml+=transactionRecord;
                os = new PrintWriter(socket.getOutputStream(), true);
                try {
                    if (!request.equalsIgnoreCase("null")) {
                        if (request.contains("?")) {
                            String[] urlParts = request.split("\\?");
                            String report_Value = "";
                            String category_Value = "";
                            String years_Value = "";
                            String fromDate_Value = "";
                            String toDate_Value = "";
                            if (urlParts.length > 1) {
                                String query = urlParts[1];
                                for (String param : query.split("&")) {
                                    String[] pair = param.split("=");
                                    String key = URLDecoder.decode(pair[0], "UTF-8");
                                    String value = "";
                                    if (pair.length > 1) {
                                        value = URLDecoder.decode(pair[1], "UTF-8");
                                    }
                                    if (key.equalsIgnoreCase("reports")) {
                                        report_Value = value;
                                    } else if (key.equalsIgnoreCase("category")) {
                                        category_Value = value;
                                    } else if (key.equalsIgnoreCase("Years")) {
                                        years_Value = value;
                                    } else if (key.equalsIgnoreCase("fromDate")) {
                                        fromDate_Value = value;
                                    } else if (key.equalsIgnoreCase("toDate")) {
                                        toDate_Value = value;
                                    }

                                }

                                String reportName = getReportName(Integer.parseInt(report_Value) - 1);
                                resHtml += getReport(reportName, category_Value, years_Value, fromDate_Value, toDate_Value);
                            }
                        }
                    }
                }catch(Exception e){

                }

//
                os.print("HTTP/1.0 200" + "\r\n");
                os.print("Content type: text/html" + "\r\n");
                os.print("\r\n");
                if (resHtml.equalsIgnoreCase("")) {
                      os.print("No Data Found First Maintain records");
                } else {
                    int length = resHtml.split("\\#").length;
                    if (resHtml.split("\\#").length == 1) {
                        String reports = resHtml.split("\\#")[0];
                        //  os.print(webView.CreateReport("Report","To Dates Report","2019-09-24 05:33:50 PM",reports,"","","")+"\r\n");
                    } else if (resHtml.split("\\#").length == 3) {
                        String reports = resHtml.split("\\#")[0];
                        String categories = resHtml.split("\\#")[1];
                        String years = resHtml.split("\\#")[2];
                        webViewDiv.HrptechWebViewLoad(categories, reports, years, "", "");
                        os.print(webViewDiv.getHtmlCode());
                    } else {
                        try {
                            String reports = resHtml.split("\\#")[0];
                            String categories = resHtml.split("\\#")[1];
                            String years = resHtml.split("\\#")[2];
                            String header = resHtml.split("\\#")[3];
                            String rows = resHtml.split("\\#")[4];
                            webViewDiv.HrptechWebViewLoad(categories, reports, years, header, rows);
                            os.print(webViewDiv.getHtmlCode());
                        }catch (Exception e){

                        }
                        //  os.print(webView.CreateReport("Report","To Dates Report","2019-09-24 05:33:50 PM",reports,categories,header,rows)+"\r\n");
                    }

                }

                os.flush();
                socket.close();

                msgLog += "Request of " + request
                        + " from " + socket.getInetAddress().toString() + "\n";
                try {
                    WebServerFragment.this.getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            ipServerTxt.setText(msgLog);
                        }
                    });
                }catch(Exception e){

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return;
        }
    }

    private class DesktopResponseThread extends Thread {

        Socket socket;
        String h1;

        DesktopResponseThread(Socket socket, String msg) {
            this.socket = socket;
            h1 = msg;
        }

        String msgLog = "";
        boolean isStart = true;
        @Override
        public void run() {
            ObjectOutputStream is;
            ObjectInputStream os;
            String request = "";
//HrptechWebView webView = new HrptechWebView();
            HrptechWebViewDiv webViewDiv = new HrptechWebViewDiv();

            try {
               // os = new ObjectInputStream(socket.getInputStream());
                is = new ObjectOutputStream(socket.getOutputStream());
                String transactionRecord = "";
                String resHtml="";
                for (int inRp = 0; inRp < carArr.length; inRp++) {
                    transactionRecord += (inRp + 1) + "|" + carArr[inRp] + "&";
                }

                transactionRecord += "#";
                ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecords();
                for (int inRp = 0; inRp < categoryBeans.size(); inRp++) {

                    transactionRecord += categoryBeans.get(inRp).getId() + "|" + categoryBeans.get(inRp).getName() + "|" + categoryBeans.get(inRp).getType() + "&";
                }
                transactionRecord += "#";

                for (int inRp = 0; inRp < year.length; inRp++) {
                    transactionRecord += year[inRp] + "|";
                }
                transactionRecord += "#";
                if(categoryBeans.size()<1){
                    transactionRecord="";
                }

                resHtml+=transactionRecord;
                is.writeObject("started#"+resHtml);
                os = new ObjectInputStream(socket.getInputStream());
                while (isStart) {
                    if (os != null) {

                        try {
                            request = (String) os.readObject();
                        } catch (Exception exc) {
                            isStart = false;
                            exc.printStackTrace();
                        }
                        if(request.contains("request")){
                            //request#report$To dates Detail Report#categoryId$1#year$2020#fdate$2020-11-02#tdate$2020-11-02
                            String reportName = request.split("\\#")[1].split("\\$")[1];
                            String categoryId = request.split("\\#")[2].split("\\$")[1];
                            String year = request.split("\\#")[3].split("\\$")[1];
                            String fDate = request.split("\\#")[4].split("\\$")[1];
                            String tDate = request.split("\\#")[5].split("\\$")[1];
                            is.writeObject(reportName+"#"+getReport(reportName,categoryId,year,fDate,tDate));

                        }

                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return;
        }
    }

    public String getReport(String report, String category, String years, String fromDate, String toDate) {
        if (report.equalsIgnoreCase("To dates Detail Report")) {
            return DailyTransactionReport(fromDate, toDate);
        } else if (report.equalsIgnoreCase("To dates Summary Report")) {
            return MonthlyTransactionReport(fromDate, toDate);
        } else if (report.equalsIgnoreCase("Yearly Report")) {
            return YearlyReport(years);
        } else if (report.equalsIgnoreCase("To dates Category Detail Report")) {
            return LoadToDateCategoryReport("detail", fromDate, toDate, category);
        } else if (report.equalsIgnoreCase("To dates Category Summary Report")) {
            return LoadToDateCategoryReport("sum", fromDate, toDate, category);
        } else if (report.equalsIgnoreCase("Yearly Category Report")) {
            return YearlyCategoryReport(years, category);
        } else if (report.equalsIgnoreCase("Monthly Budget Report")) {

        } else if (report.equalsIgnoreCase("Yearly Budget Report")) {

        }
        return "";
    }

    public String getReportName(int index) {
        String report = "";
        for (int inReport = 0; inReport < carArr.length; inReport++) {
            return carArr[index];
        }
        return report;
    }

    public String DailyTransactionReport(String fDate, String tDate) {


        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDates(fDate, tDate);
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        double opening = transactionDB.getTransactionByOpeningBalance(fDate);
        double balance = 0;
        double lastBalance = 0;
        String htmlCode = "";
        htmlCode += "Descrption|";
        htmlCode += "Date|";
        htmlCode += "Opening|";
        htmlCode += "Income|";
        htmlCode += "Expense|";
        htmlCode += "Balance#";
        for (int index = 0; index < transactionBeans.size(); index++) {
            TransactionBeans beans = transactionBeans.get(index);
            double income = Double.parseDouble(beans.getIncome());
            double expense = Double.parseDouble(beans.getExpense());
            double balances = income - expense;
            if (index == 0) {
                balance += balances + opening;
                lastBalance = balance;

                htmlCode += beans.getDescription() + "|";
                htmlCode += beans.getDate() + "|";
                htmlCode += new DecimalFormat("0.00").format(opening) + "|";
                htmlCode += beans.getIncome() + "|";
                htmlCode += beans.getExpense() + "|";
                htmlCode += new DecimalFormat("0.00").format(balance) + "$";
            } else {
                balance = balances + lastBalance;
                htmlCode += beans.getDescription() + "|";
                htmlCode += beans.getDate() + "|";
                htmlCode += new DecimalFormat("0.00").format(lastBalance) + "|";
                htmlCode += beans.getIncome() + "|";
                htmlCode += beans.getExpense() + "|";
                htmlCode += new DecimalFormat("0.00").format(balance) + "$";
                lastBalance += balances;
            }
        }
        if(transactionBeans.size()>0){
            return htmlCode;
        }else {
            return "";
        }

    }

    public String MonthlyTransactionReport(String fDate, String tDate) {

        String htmlCode = "";
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDatesSummary(fDate, tDate);
        double opening = transactionDB.getTransactionByOpeningBalance(fDate);
        double balance = 0;
        double lastBalance = 0;
        htmlCode += "Description|";
        htmlCode += "Date|";
        htmlCode += "Opening|";
        htmlCode += "Income|";
        htmlCode += "Expense|";
        htmlCode += "Balance#";
        for (int index = 0; index < transactionBeans.size(); index++) {
            TransactionBeans beans = transactionBeans.get(index);
            double income = Double.parseDouble(beans.getIncome());
            double expense = Double.parseDouble(beans.getExpense());
            double balances = income - expense;
            if (index == 0) {
                balance += balances + opening;
                lastBalance = balance;

                htmlCode += beans.getDescription() + "|";
                htmlCode += beans.getDate() + "|";
                htmlCode += new DecimalFormat("0.00").format(opening) + "|";
                htmlCode += beans.getIncome() + "|";
                htmlCode += beans.getExpense() + "|";
                htmlCode += new DecimalFormat("0.00").format(balance) + "$";

            } else {
                balance = balances + lastBalance;
                htmlCode += beans.getDescription() + "|";
                htmlCode += beans.getDate() + "|";
                htmlCode += new DecimalFormat("0.00").format(lastBalance) + "|";
                htmlCode += beans.getIncome() + "|";
                htmlCode += beans.getExpense() + "|";
                htmlCode += new DecimalFormat("0.00").format(balance) + "$";
                lastBalance += balances;
            }
        }
        if(transactionBeans.size()>0){
            return htmlCode;
        }else {
            return "";
        }
    }

    String monthNumber[] = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    public String YearlyReport(String year) {
        String htmlCode = "";
        // header for Table
        htmlCode += "Month|";
        htmlCode += "Income|";
        htmlCode += "Expense|";
        htmlCode += "Balance#";
        for (int index = 0; index < monthNumber.length; index++) {
            String month = monthNumber[index];
            TransactionBeans mTransaction = transactionDB.getTransactionRecordsYear("" + (Utilities.getIndexZero((index + 1))), year);
            htmlCode += year + "," + month + "|";
            htmlCode += mTransaction.getIncome() + "|";
            htmlCode += mTransaction.getExpense() + "|";
            htmlCode += mTransaction.getBalance() + "$";
        }
        return htmlCode;

    }

    public String LoadToDateCategoryReport(String type, String fDate, String tDate, String categoryId) {
        String htmlCode = "";
        ArrayList<TransactionBeans> transactionBeans = new ArrayList<>();
        if (type.equalsIgnoreCase("detail")) {
            transactionBeans = transactionDB.getTransactionRecordsToDatesCategoryDetail(fDate, tDate, categoryId);
        } else if (type.equalsIgnoreCase("sum")) {
            transactionBeans = transactionDB.getTransactionRecordsToDatesCategorySummary(fDate, tDate, categoryId);
        }
        htmlCode += "Date|";
        htmlCode += "Description|";
        htmlCode += "Amount#";
        for (int index = 0; index < transactionBeans.size(); index++) {
            TransactionBeans beansTr = transactionBeans.get(index);
            htmlCode += beansTr.getDate() + "|";
            htmlCode += beansTr.getDescription() + "|";
            htmlCode += beansTr.getBalance() + "$";
        }
        if(transactionBeans.size()>0){
            return htmlCode;
        }else {
            return "";
        }
    }

    public String YearlyCategoryReport(String year, String categoryID) {
        String htmlCode = "";
        CATEGORY beans = categoryDB.getCategoryRecordSingle(categoryID);
        if (beans != null) {
            String categoryName = beans.getName();
            htmlCode += "Month|";
            htmlCode += "Description|";
            htmlCode += "Amount#";
            for (int index = 0; index < monthNumber.length; index++) {
                String month = monthNumber[index];
                TransactionBeans mTransaction = transactionDB.getTransactionRecordsYearCategory("" + (Utilities.getIndexZero((index + 1))), year, categoryID);
                htmlCode+=year + "," + month + "|";
                htmlCode+=categoryName + "|";
                htmlCode+=mTransaction.getBalance() + "$";
            }

        }
        return htmlCode;
    }

    @Override
    public void onBackPressed() {
        BackToMain();

    }

    public void BackToMain(){
        Intent intent = new Intent(this, MoreActivity.class);
        startActivity(intent);
        finish();
    }
}