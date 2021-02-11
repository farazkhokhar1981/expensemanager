package com.hrptech.expensemanager.ui.report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.PDFHeaderBeans;
import com.hrptech.expensemanager.beans.PartiesBeans;
import com.hrptech.expensemanager.beans.ReportHeaderBeans;
import com.hrptech.expensemanager.beans.ReportInfoBeans;
import com.hrptech.expensemanager.beans.ReportMasterBeans;
import com.hrptech.expensemanager.beans.ReportRowBeans;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.report.DailyReportViewAdapter;
import com.hrptech.expensemanager.report.YearlyReportViewAdapter;
import com.hrptech.expensemanager.report.toDateCategoryReportViewAdapter;
import com.hrptech.expensemanager.ui.budget.CategoryListAdapter;
import com.hrptech.expensemanager.utility.Utilities;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Activity {

    Spinner report_spr;
    TextView lblYear;
    Spinner year_spr;
    TextView lblMonth;
    Spinner month_spr;
    Spinner category_spr;

    TextView fromDate_txt;
    TextView toDate_txt;
    TextView errorMsg_txt;
    ImageView fromDate_btn;
    ImageView toDate_btn;
    ImageView addBtn;
    RecyclerView reportList_rec;
    DatePickerDialog dateFromPickerDialog;
    DatePickerDialog dateToPickerDialog;
    LinearLayout lay_date;
    LinearLayout lay_month;
    LinearLayout lay_category;

    DailyReportViewAdapter dailyReportViewAdapter;
    YearlyReportViewAdapter yearlyReportViewAdapter;
    toDateCategoryReportViewAdapter toDateCategoryReportViewAdapter;

    private TransactionDB transactionDB;
    public CategoryDB categoryDB;
    CategoryListAdapter categoryViewAdapter;
    ImageView pdf_btn;
    ImageView xls_btn;
    Activity root;
    ProgressDialog progressBar;
    float clmWidth[] = new float[0];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_report);
        root = this;
        categoryDB = new CategoryDB(this.getActivity());
        ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecords();
        categoryViewAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        report_spr = (Spinner)root.findViewById(R.id.report_spr);
        category_spr = (Spinner)root.findViewById(R.id.category_spr);
        category_spr.setAdapter(categoryViewAdapter);
        lblYear = (TextView) root.findViewById(R.id.lbl_year);
        year_spr = (Spinner)root.findViewById(R.id.year_spr);
        String year = new SimpleDateFormat("yyyy").format(new Date());
        int positionOfYear = Utilities.getIndex(year_spr,year);
        year_spr.setSelection(positionOfYear);
        lblMonth = (TextView) root.findViewById(R.id.lbl_month);
        month_spr = (Spinner)root.findViewById(R.id.month_spnr);
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))-1;
        month_spr.setSelection(month);
        lay_category = (LinearLayout) root.findViewById(R.id.lay_category);
        progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage("Waiting.....");
        fromDate_txt = (TextView) root.findViewById(R.id.fromDate_txt);
        toDate_txt = (TextView) root.findViewById(R.id.toDate_txt);
        errorMsg_txt = (TextView) root.findViewById(R.id.error_msg);
        errorMsg_txt.setVisibility(View.GONE);
        fromDate_txt.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        toDate_txt.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        fromDate_btn = (ImageView) root.findViewById(R.id.fromDate_btn);
        toDate_btn = (ImageView) root.findViewById(R.id.toDate_btn);
        pdf_btn = (ImageView) root.findViewById(R.id.pdf_btn);
        pdf_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()){
                    ReportMasterBeans reportMasterBeans = null;
                    String report = (String) report_spr.getSelectedItem();
                    try{
                        progressBar.show();
                    }catch(Exception e){

                    }
                    reportMasterBeans = getReportSelected();
                    if (reportMasterBeans != null) {
                        ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList = new ArrayList<>();
                        ArrayList<PDFHeaderBeans> headerBeansList = new ArrayList<>();
                        genrateTableData(rowBeansList,headerBeansList,clmWidth,reportMasterBeans);
                        LoadWhatsPdf(false,report,clmWidth,reportMasterBeans.getReportInfoBeans(), headerBeansList, rowBeansList);
                    }


                }
            }
        });
        xls_btn = (ImageView) root.findViewById(R.id.xls_btn);
        xls_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()){
                    ReportMasterBeans reportMasterBeans = null;
                    String report = (String) report_spr.getSelectedItem();
                    try{
                        progressBar.show();
                    }catch(Exception e){

                    }
                    reportMasterBeans = getReportSelected();
                    if (reportMasterBeans != null) {
                        ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList = new ArrayList<>();
                        ArrayList<PDFHeaderBeans> headerBeansList = new ArrayList<>();
                        genrateTableData(rowBeansList,headerBeansList,clmWidth,reportMasterBeans);
                        LoadWhatsPdf(true,report,clmWidth,reportMasterBeans.getReportInfoBeans(), headerBeansList, rowBeansList);
                    }


                }
            }
        });
        pdf_btn.setVisibility(View.GONE);
        xls_btn.setVisibility(View.GONE);
        addBtn = (ImageView) root.findViewById(R.id.back_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    backToMore(new Intent(ReportFragment.this, MoreActivity.class));
            }
        });
        reportList_rec = (RecyclerView) root.findViewById(R.id.reportList);
        reportList_rec.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        reportList_rec.setLayoutManager(horizontalManager);

        lay_date = (LinearLayout) root.findViewById(R.id.lay_date);
        lay_month = (LinearLayout) root.findViewById(R.id.lay_month);
        lay_month.setVisibility(View.GONE);
        lay_category.setVisibility(View.GONE);
        fromDate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker("from");
            }
        });

        toDate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker("to");
            }
        });
        report_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String report = report_spr.getItemAtPosition(position).toString();
                if(report.equalsIgnoreCase("To dates Detail Report")){
                    lay_date.setVisibility(View.VISIBLE);
                    lay_month.setVisibility(View.GONE);
                    lay_category.setVisibility(View.GONE);
                }else if(report.equalsIgnoreCase("To dates Summary Report")){
                    lay_date.setVisibility(View.VISIBLE);
                    lay_month.setVisibility(View.GONE);
                    lay_category.setVisibility(View.GONE);
                }else if(report.equalsIgnoreCase("Yearly Report")){
                    lay_date.setVisibility(View.GONE);
                    lblMonth.setVisibility(View.GONE);
                    month_spr.setVisibility(View.GONE);
                    lay_month.setVisibility(View.VISIBLE);
                    lay_category.setVisibility(View.GONE);
                }else if(report.equalsIgnoreCase("To dates Category Detail Report")){
                    lay_date.setVisibility(View.VISIBLE);
                    lblMonth.setVisibility(View.GONE);
                    month_spr.setVisibility(View.GONE);
                    lay_month.setVisibility(View.GONE);
                    lay_category.setVisibility(View.VISIBLE);
                }else if(report.equalsIgnoreCase("To dates Category Summary Report")){
                    lay_date.setVisibility(View.VISIBLE);
                    lblMonth.setVisibility(View.GONE);
                    month_spr.setVisibility(View.GONE);
                    lay_month.setVisibility(View.GONE);
                    lay_category.setVisibility(View.VISIBLE);
                }else if(report.equalsIgnoreCase("Yearly Category Report")){
                    lay_date.setVisibility(View.GONE);
                    lblMonth.setVisibility(View.GONE);
                    month_spr.setVisibility(View.GONE);
                    lay_month.setVisibility(View.VISIBLE);
                    lay_category.setVisibility(View.VISIBLE);
                }else if(report.equalsIgnoreCase("Monthly Budget Report")){
                    lay_date.setVisibility(View.GONE);
                    lblMonth.setVisibility(View.VISIBLE);
                    month_spr.setVisibility(View.VISIBLE);
                    lay_month.setVisibility(View.VISIBLE);
                }else if(report.equalsIgnoreCase("Yearly Budget Report")){
                    lay_date.setVisibility(View.GONE);
                    lblMonth.setVisibility(View.GONE);
                    month_spr.setVisibility(View.GONE);
                    lay_month.setVisibility(View.VISIBLE);
                }

                LoadReportByList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        year_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadReportByList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        category_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadReportByList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        month_spr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadReportByList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        transactionDB = new TransactionDB(this.getActivity());

        //ads of Admob
        AdsInit();
    }

    public Activity getActivity(){
        return root;
    }
    public void AdsInit(){
//        mInterstitialAd = new InterstitialAd(this.getActivity());
//
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//
//                }
//            }
//        });
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        FrameLayout frameLayout =findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
    }
    InterstitialAd mInterstitialAd;
    public void DailyTransactionReport(){

        String fDate = fromDate_txt.getText().toString();
        String tDate = toDate_txt.getText().toString();

        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDates(fDate,tDate);
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        double opening = transactionDB.getTransactionByOpeningBalance(fDate);
        double balance = 0;
        double lastBalance = 0;
        TransactionBeans headerBeans = new TransactionBeans();
        headerBeans.setDescription("Descrption");
        headerBeans.setDate("Date");
        headerBeans.setOpening("Opening");
        headerBeans.setIncome("Income");
        headerBeans.setExpense("Expense");
        headerBeans.setBalance("Balance");
        transactionBeansList.add(headerBeans);
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beans = transactionBeans.get(index);
            double income = Double.parseDouble(beans.getIncome());
            double expense = Double.parseDouble(beans.getExpense());
            double balances = income - expense;
            if(index==0){
                balance+=balances+opening;
                lastBalance = balance;

                TransactionBeans headerBs = new TransactionBeans();
                headerBs.setDescription(beans.getDescription());
                headerBs.setDate(beans.getDate());
                headerBs.setOpening(new DecimalFormat("0.00").format(opening));
                headerBs.setIncome(beans.getIncome());
                headerBs.setExpense(beans.getExpense());
                headerBs.setBalance(new DecimalFormat("0.00").format(balance));
                transactionBeansList.add(headerBs);

            }else {
                balance=balances+lastBalance;
                TransactionBeans headerBs = new TransactionBeans();
                headerBs.setDescription(beans.getDescription());
                headerBs.setDate(beans.getDate());
                headerBs.setOpening(new DecimalFormat("0.00").format(lastBalance));
                headerBs.setIncome(beans.getIncome());
                headerBs.setExpense(beans.getExpense());
                headerBs.setBalance(new DecimalFormat("0.00").format(balance));
                transactionBeansList.add(headerBs);
                lastBalance+=balances;
            }
        }
        if(transactionBeansList.size()>1) {
            dailyReportViewAdapter = new DailyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.GONE);
        }else {
            transactionBeansList = new ArrayList<>();
            dailyReportViewAdapter = new DailyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.VISIBLE);
        }
        reportList_rec.setAdapter(dailyReportViewAdapter);
    }

    public void MonthlyTransactionReport(){

        String fDate = fromDate_txt.getText().toString();
        String tDate = toDate_txt.getText().toString();

        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsToDatesSummary(fDate,tDate);
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        double opening = transactionDB.getTransactionByOpeningBalance(fDate);
        double balance = 0;
        double lastBalance = 0;
        TransactionBeans headerBeans = new TransactionBeans();
        headerBeans.setDescription("Description");
        headerBeans.setDate("Date");
        headerBeans.setOpening("Opening");
        headerBeans.setIncome("Income");
        headerBeans.setExpense("Expense");
        headerBeans.setBalance("Balance");
        transactionBeansList.add(headerBeans);
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beans = transactionBeans.get(index);
            double income = Double.parseDouble(beans.getIncome());
            double expense = Double.parseDouble(beans.getExpense());
            double balances = income - expense;
            if(index==0){
                balance+=balances+opening;
                lastBalance = balance;

                TransactionBeans headerBs = new TransactionBeans();
                headerBs.setDescription(beans.getDescription());
                headerBs.setDate(beans.getDate());
                headerBs.setOpening(new DecimalFormat("0.00").format(opening));
                headerBs.setIncome(beans.getIncome());
                headerBs.setExpense(beans.getExpense());
                headerBs.setBalance(new DecimalFormat("0.00").format(balance));
                transactionBeansList.add(headerBs);

            }else {
                balance=balances+lastBalance;
                TransactionBeans headerBs = new TransactionBeans();
                headerBs.setDescription(beans.getDescription());
                headerBs.setDate(beans.getDate());
                headerBs.setOpening(new DecimalFormat("0.00").format(lastBalance));
                headerBs.setIncome(beans.getIncome());
                headerBs.setExpense(beans.getExpense());
                headerBs.setBalance(new DecimalFormat("0.00").format(balance));
                transactionBeansList.add(headerBs);
                lastBalance+=balances;
            }
        }
        if(transactionBeansList.size()>1) {
            dailyReportViewAdapter = new DailyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.GONE);
        }else {
            transactionBeansList = new ArrayList<>();
            dailyReportViewAdapter = new DailyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.VISIBLE);
        }
        reportList_rec.setAdapter(dailyReportViewAdapter);
    }

    String monthNumber[]={"Jan","Feb","Mar","Apr",
            "May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"};
    public void YearlyReport(){
        if(year_spr==null){
            return;
        }
        String year = year_spr.getSelectedItem().toString();
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        TransactionBeans headerBeans = new TransactionBeans();
        headerBeans.setDescription("Month");
        headerBeans.setIncome("Income");
        headerBeans.setExpense("Expense");
        headerBeans.setBalance("Balance");
        transactionBeansList.add(headerBeans);
        for(int index=0; index<monthNumber.length; index++){
            String month = monthNumber[index];
            TransactionBeans mTransaction = transactionDB.getTransactionRecordsYear(""+(Utilities.getIndexZero((index+1))),year);
            mTransaction.setDescription(year+","+month);
            transactionBeansList.add(mTransaction);
        }
        if(transactionBeansList.size()>1) {
            yearlyReportViewAdapter = new YearlyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.GONE);
        }else {
            transactionBeansList = new ArrayList<>();
            yearlyReportViewAdapter = new YearlyReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.VISIBLE);
        }
        reportList_rec.setAdapter(yearlyReportViewAdapter);
    }

    public void LoadToDateCategoryReport(String type){
        String fDate = fromDate_txt.getText().toString();
        String tDate = toDate_txt.getText().toString();
        CATEGORY beans = (CATEGORY) category_spr.getSelectedItem();
        if(beans==null){
            return;
        }
        String categoryId = beans.getId();
        ArrayList<TransactionBeans> transactionBeans = new ArrayList<>();
        if(type.equalsIgnoreCase("detail")){
            transactionBeans = transactionDB.getTransactionRecordsToDatesCategoryDetail(fDate,tDate,categoryId);
        }else if(type.equalsIgnoreCase("sum")){
            transactionBeans = transactionDB.getTransactionRecordsToDatesCategorySummary(fDate,tDate,categoryId);
        }
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        TransactionBeans headerBeans = new TransactionBeans();
        headerBeans.setDate("Date");
        headerBeans.setDescription("Description");
        headerBeans.setBalance("Amount");
        transactionBeansList.add(headerBeans);
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beansTr = transactionBeans.get(index);
            transactionBeansList.add(beansTr);
        }
        if(transactionBeansList.size()>1) {
            toDateCategoryReportViewAdapter = new toDateCategoryReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.GONE);
        }else {
            transactionBeansList = new ArrayList<>();
            toDateCategoryReportViewAdapter = new toDateCategoryReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.VISIBLE);
        }
        reportList_rec.setAdapter(toDateCategoryReportViewAdapter);
    }

    public void YearlyCategoryReport(){
        if(year_spr==null){
            return;
        }
        String year = year_spr.getSelectedItem().toString();
        CATEGORY beans = (CATEGORY) category_spr.getSelectedItem();
        if(beans==null){
            return;
        }
        String categoryId = beans.getId();
        String categoryName = beans.getName();
        List<TransactionBeans> transactionBeansList = new ArrayList<>();
        TransactionBeans headerBeans = new TransactionBeans();
        headerBeans.setDate("Month");
        headerBeans.setDescription("Description");
        headerBeans.setBalance("Amount");
        transactionBeansList.add(headerBeans);
        for(int index=0; index<monthNumber.length; index++){
            String month = monthNumber[index];
            TransactionBeans mTransaction = transactionDB.getTransactionRecordsYearCategory(""+(Utilities.getIndexZero((index+1))),year,categoryId);
            mTransaction.setDate(year+","+month);
            mTransaction.setDescription(categoryName);
            transactionBeansList.add(mTransaction);
        }
        if(transactionBeansList.size()>1) {
            toDateCategoryReportViewAdapter = new toDateCategoryReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.GONE);
        }else {
            transactionBeansList = new ArrayList<>();
            toDateCategoryReportViewAdapter = new toDateCategoryReportViewAdapter(this.getActivity(), transactionBeansList);
            errorMsg_txt.setVisibility(View.VISIBLE);
        }
        reportList_rec.setAdapter(toDateCategoryReportViewAdapter);
    }



    // when select report from drop down list call this method
    public void LoadReportByList(){
        reportList_rec.setAdapter(null);
        String report = report_spr.getSelectedItem().toString();
        if(report.equalsIgnoreCase("To dates Detail Report")){
            DailyTransactionReport();
        }else if(report.equalsIgnoreCase("To dates Summary Report")){
            MonthlyTransactionReport();
        }else if(report.equalsIgnoreCase("Yearly Report")){
            YearlyReport();
        }else if(report.equalsIgnoreCase("To dates Category Detail Report")){
            LoadToDateCategoryReport("detail");
        }else if(report.equalsIgnoreCase("To dates Category Summary Report")){
            LoadToDateCategoryReport("sum");
        }else if(report.equalsIgnoreCase("Yearly Category Report")){
            YearlyCategoryReport();
        }else if(report.equalsIgnoreCase("Monthly Budget Report")){

        }else if(report.equalsIgnoreCase("Yearly Budget Report")){

        }

    }


    // date picker
    Calendar calendar = null;
    int year,month,dayOfMonth;
    public void LoadDatePicker(String type){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if(type.equalsIgnoreCase("from")) {
            dateFromPickerDialog = new DatePickerDialog(ReportFragment.this.getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            fromDate_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                            LoadReportByList();
                        }
                    }, year, month, dayOfMonth);
            dateFromPickerDialog.show();
        }else if(type.equalsIgnoreCase("to")){
            dateToPickerDialog = new DatePickerDialog(ReportFragment.this.getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            toDate_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                            LoadReportByList();
                        }
                    }, year, month, dayOfMonth);
            dateToPickerDialog.show();
        }


    }



    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        LoadPdf();
                    } else {
                        Toast.makeText(this.getActivity(), "Some permissions are not granted ask again ",Toast.LENGTH_LONG).show();
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this.getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void LoadPdf() {

//        PdfView pdfView = new PdfView(this.getActivity());
//        pdfView.write("testPdf","");
//        try {
//            pdfView.imageToPDF("testPdf");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    PartiesBeans compRp = null;
    public ReportMasterBeans getReportSelected(){
        ReportMasterBeans reportMasterBeans = null;
        String fDate = fromDate_txt.getText().toString();
        String tDate = toDate_txt.getText().toString();
        String year = year_spr.getSelectedItem().toString();
        ReportForPdf reportForPdf = new ReportForPdf(ReportFragment.this.getActivity(),transactionDB);
        long position = report_spr.getSelectedItemId();
        String report = (String) report_spr.getSelectedItem();
        String prCode = "";
        String prName = "";
        if(position==0){
            clmWidth=new float[5];
            clmWidth[0]=50;
            clmWidth[1]=145;
            clmWidth[2]=75;
            clmWidth[3]=75;
            clmWidth[4]=75;

            reportMasterBeans = reportForPdf.getPartiesLedger(report,fDate,tDate);
        }else if(position==1){
            clmWidth=new float[5];
            clmWidth[0]=50;
            clmWidth[1]=145;
            clmWidth[2]=75;
            clmWidth[3]=75;
            clmWidth[4]=75;
            if (compRp != null) {
                prCode = compRp.getId();
                prName = compRp.getName();
            }
            reportMasterBeans = reportForPdf.getPartiesLedgerDetail(report,prCode,prName,fDate,tDate);
        }else if(position==2){
            clmWidth=new float[5];
            clmWidth[0]=60;
            clmWidth[1]=145;
            clmWidth[2]=75;
            clmWidth[3]=75;
            clmWidth[4]=75;
           // reportMasterBeans = reportForPdf.getAllCompanyPartiesLedger(report,fDate);

        }else if(position==3){
            clmWidth=new float[5];
            clmWidth[0]=50;
            clmWidth[1]=145;
            clmWidth[2]=75;
            clmWidth[3]=75;
            clmWidth[4]=75;
            if (compRp != null) {
                prCode = compRp.getId();
            }

          //  reportMasterBeans = reportForPdf.getCompanyLedgerDetail(report,prCode,prName,fDate,tDate);

        }
        return reportMasterBeans;
    }
    @SuppressLint("NewApi")
    public void genrateTableData(ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList, ArrayList<PDFHeaderBeans> headerBeansList, float colWidth[], ReportMasterBeans reportMasterBeans){







        for(int index=0; index<reportMasterBeans.getHeaderBeansArrayList().size(); index++){
            ReportHeaderBeans beans = reportMasterBeans.getHeaderBeansArrayList().get(index);
            if(index==0 || index==1){
                headerBeansList.add(getTableValues(beans.getName(), Element.ALIGN_LEFT, BaseColor.BLACK,BaseColor.LIGHT_GRAY,BaseColor.BLACK,13, Font.BOLD,beans.getColumnSize()));
            }else {
                headerBeansList.add(getTableValues(beans.getName(), Element.ALIGN_RIGHT,BaseColor.BLACK,BaseColor.LIGHT_GRAY,BaseColor.BLACK,13, Font.BOLD,beans.getColumnSize()));
            }
        }
        for(int index=0; index<reportMasterBeans.getListOfRow().size(); index++){
            ReportRowBeans beans = reportMasterBeans.getListOfRow().get(index);
            ArrayList<ReportHeaderBeans> hbeans = beans.getListOfStr();
            ArrayList<PDFHeaderBeans> pdfHeaderBeans = new ArrayList<>();
            for(int intd=0; intd<hbeans.size(); intd++) {
                ReportHeaderBeans nbBEans = hbeans.get(intd);
                ReportHeaderBeans nbBEans1 = hbeans.get(0);
                ReportHeaderBeans nbBEansTotal = hbeans.get(1);
                String name = nbBEans.getName();
                String name1 = nbBEans1.getName();
                String total = nbBEansTotal.getName();
                if(name1.equalsIgnoreCase("") && total.equalsIgnoreCase("")) {
                    if(!name.equalsIgnoreCase("")){
                        pdfHeaderBeans.add(getTableValues(getActivity().getResources().getString(R.string.company_name).toUpperCase()+" ("+name.toUpperCase()+")", Element.ALIGN_CENTER, new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorBlack)), new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorGreenlights)), new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorGreenlights)), 16, Font.BOLD, nbBEans.getColumnSize()));
                    }else {
                        pdfHeaderBeans.add(getTableValues("", Element.ALIGN_CENTER, new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorBlack)), new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorGreenlights)), new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorGreenlights)), 14, Font.BOLD, nbBEans.getColumnSize()));

                    }

                }else {
                    PDFHeaderBeans pdfHeaderBeans1 = null;
                    if (intd == 0 || intd == 1) {
                        try{
                            pdfHeaderBeans1 = getTableValues(name, Element.ALIGN_LEFT, BaseColor.BLACK, new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorLiteGraytran)),new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorLiteGraytran)), 12, Font.NORMAL, nbBEans.getColumnSize());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        pdfHeaderBeans1 = getTableValues(name, Element.ALIGN_RIGHT,BaseColor.BLACK,new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorLiteGraytran)),new BaseColor(ContextCompat.getColor(getActivity(), R.color.colorLiteGraytran)), 12, Font.NORMAL, nbBEans.getColumnSize());
                    }
                    pdfHeaderBeans.add(pdfHeaderBeans1);
                }
            }
            rowBeansList.add(pdfHeaderBeans);


        }

    }
    public PDFHeaderBeans getTableValues(String name, int align, BaseColor fColor, BaseColor bgColor, BaseColor brColor, int fontSize, int fontStyle,float columnSize) {
        PDFHeaderBeans beans = new PDFHeaderBeans();
        beans.setName(name);
        beans.setAlign(align);
        beans.setFontSize(fontSize);
        beans.setFontStyle(fontStyle);
        beans.setfColor(fColor);
        beans.setBgColor(bgColor);
        beans.setBrColor(brColor);
        beans.setColumnSize(columnSize);
        return beans;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void LoadWhatsPdf(boolean isSharedFile, String reportName, float[] colWidth, ReportInfoBeans reportInfoBeans, ArrayList<PDFHeaderBeans> headerBeansList, ArrayList<ArrayList<PDFHeaderBeans>> rowBeansList){
        PdfView pdfView = new PdfView(getActivity());
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime());
        String name = reportName+""+date+""+time;
        PDFViewPage pdfViewPage = new PDFViewPage(getActivity(),name,colWidth,reportInfoBeans,headerBeansList,rowBeansList);
        // boolean isCreated = pdfView.write("testPdf3",reportInfoBeans,beans,transactionBeans);
        boolean isCreated = pdfViewPage.isCreated();
        if(isCreated) {

            if(isSharedFile){
                pdfView.SharePdfToWhatsapp(name);
            }else {
                Toast.makeText(getActivity(),"Pdf Created...",Toast.LENGTH_LONG).show();
            }
        }
        progressBar.hide();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    String prodName = "MEDICINA";
    @Override
    public void onBackPressed() {
       // backToMore(new Intent(ReportFragment.this, MoreActivity.class));

    }

    public void backToMore(Intent intent){
        startActivity(intent);
        finish();
    }

}