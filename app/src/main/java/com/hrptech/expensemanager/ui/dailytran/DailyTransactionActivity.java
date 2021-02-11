package com.hrptech.expensemanager.ui.dailytran;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.adapter.DailyTransactionAdapter;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class DailyTransactionActivity extends Activity {


    DailyTransactionAdapter dailyTransactionAdapter = null;
    RecyclerView recyclerView = null;
    ImageView back_btn;
    ImageView sort_btn;
    SearchView searchView;
    LinearLayout date_lay;
    TextView fromDate_txt;
    TextView toDate_txt;
    public TransactionDB transactionDB;
    RadioButton rdoSortAll_btn;
    RadioButton rdoSortIncome_btn;
    RadioButton rdoSortExpense_btn;
    String sortingType = "all";
    private static DailyTransactionActivity instance;
    public static DailyTransactionActivity getInstance(){
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daily_transaction);
        init();

      }
      ArrayList<TransactionBeans> transactionBeansArrayList = null;
    String selectedCompanyName = "";
      public void init(){
          instance = this;
          calendar = Calendar.getInstance();
          yearFrom = calendar.get(Calendar.YEAR);
          monthFrom = calendar.get(Calendar.MONTH);
          dayOfMonthFrom = calendar.get(Calendar.DAY_OF_MONTH);


          yearTo = calendar.get(Calendar.YEAR);
          monthTo = calendar.get(Calendar.MONTH);
          dayOfMonthTo = calendar.get(Calendar.DAY_OF_MONTH);

        transactionDB = new TransactionDB(this);
            back_btn = (ImageView)findViewById(R.id.back_btn);
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackToMain();
                }
            });
            sort_btn = (ImageView)findViewById(R.id.sort_btn);
            sort_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(DailyTransactionActivity.this, sort_btn);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.dailytransaction);
                    //adding click listener
                    Menu menu = popup.getMenu();
                    menu.getItem(lastSelectedMenuNo).setChecked(true);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.sort_atoz_menu:
                                    item.setChecked(!item.isChecked());
                                    Collections.sort(transactionBeansArrayList, new Comparator<TransactionBeans>() {
                                        public int compare(TransactionBeans o1, TransactionBeans o2) {

                                            return o2.getName().compareTo(o1.getName());
                                        }

                                    });
                                    lastSelectedMenuNo =0;
                                    UpdateListBySortandSearch(transactionBeansArrayList,"");
                                    break;
                                case R.id.sort_ztoa_menu:
                                    item.setChecked(!item.isChecked());
                                    Collections.sort(transactionBeansArrayList, new Comparator<TransactionBeans>() {
                                        public int compare(TransactionBeans o1, TransactionBeans o2) {

                                            return o1.getName().compareTo(o2.getName());
                                        }

                                    });
                                    lastSelectedMenuNo = 1;
                                    UpdateListBySortandSearch(transactionBeansArrayList,"");
                                    break;
                                case R.id.sort_bydate_asc_menu:
                                    item.setChecked(!item.isChecked());
                                    Collections.sort(transactionBeansArrayList, new Comparator<TransactionBeans>() {
                                        public int compare(TransactionBeans o1, TransactionBeans o2) {

                                            return o2.getDate().compareTo(o1.getDate());
                                        }

                                    });
                                    lastSelectedMenuNo = 2;
                                    UpdateListBySortandSearch(transactionBeansArrayList,"");
                                    break;
                                case R.id.sort_bydate_desc_menu:
                                    item.setChecked(!item.isChecked());
                                    Collections.sort(transactionBeansArrayList, new Comparator<TransactionBeans>() {
                                        public int compare(TransactionBeans o1, TransactionBeans o2) {

                                            return o1.getDate().compareTo(o2.getDate());
                                        }

                                    });
                                    lastSelectedMenuNo = 3;
                                    UpdateListBySortandSearch(transactionBeansArrayList,"");
                                    break;
                                case R.id.debit_menu:
                                    item.setChecked(!item.isChecked());
                                    UpdateListBySortandSearch(getTransactionDR_CR("dr"),"");
                                    lastSelectedMenuNo = 4;
                                    break;
                                case R.id.credit_menu:
                                    item.setChecked(!item.isChecked());
                                    UpdateListBySortandSearch(getTransactionDR_CR("cr"),"");
                                    lastSelectedMenuNo = 5;
                                    break;

                            }
                            return true;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
          rdoSortAll_btn = (RadioButton) instance.findViewById(R.id.all_Sort_rdo_btn);
          rdoSortAll_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  sortingType = "all";
                  transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                  UpdateListBySortandSearch(transactionBeansArrayList,"");
              }
          });
          rdoSortIncome_btn= (RadioButton) instance.findViewById(R.id.income_Sort_rdo_btn);
          rdoSortIncome_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  sortingType = "Income";
                  transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                  UpdateListBySortandSearch(transactionBeansArrayList,"");
              }
          });
          rdoSortExpense_btn = (RadioButton) instance.findViewById(R.id.expense_Sort_rdo_btn);
          rdoSortExpense_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  sortingType = "Expense";
                  transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                  UpdateListBySortandSearch(transactionBeansArrayList,"");
              }
          });
          searchView = (SearchView) findViewById(R.id.searchView);
          SearchManager searchManager = (SearchManager) DailyTransactionActivity.this.getSystemService(Context.SEARCH_SERVICE);
          searchView.setSearchableInfo(searchManager.getSearchableInfo(DailyTransactionActivity.this.getComponentName()));
          searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  // Toast like print
                  //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                  if (!searchView.isIconified()) {
                      searchView.setIconified(true);
                  }
                  searchView.onActionViewCollapsed();
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String s) {
                  transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,s,fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                  UpdateListBySortandSearch(transactionBeansArrayList,s);
                  return false;
              }
          });

            date_lay = (LinearLayout)findViewById(R.id.date_lay);

            fromDate_txt = (TextView)findViewById(R.id.fromDate_txt);
            fromDate_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadDatePicker("from");
                }
            });
            toDate_txt = (TextView)findViewById(R.id.toDate_txt);
            toDate_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadDatePicker("to");
                }
            });

          recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
          recyclerView.setHasFixedSize(true);
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          recyclerView.setAdapter(null);
          transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
          dailyTransactionAdapter = new DailyTransactionAdapter(this,transactionBeansArrayList,"");
          recyclerView.setAdapter(dailyTransactionAdapter);
          if(transactionBeansArrayList.size()>1) {
              FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
              Utilities.refreshAd(this, frameLayout);
          }

          LoadBanner();

      }
public void RefreshData(){
    transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
    UpdateListBySortandSearch(transactionBeansArrayList,"");
}
    public void LoadBanner(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

      public int lastSelectedMenuNo = 0;


      public ArrayList<TransactionBeans> getTransactionDR_CR(String type){
          ArrayList<TransactionBeans> transactionBeans = new ArrayList<>();
          for(int index=0; index<transactionBeansArrayList.size(); index++){
              TransactionBeans tbeans = transactionBeansArrayList.get(index);
              double dr = Double.parseDouble(tbeans.getIncome());
              double cr = Double.parseDouble(tbeans.getExpense());
              if(type.equalsIgnoreCase("dr")) {
                  if (dr > 0) {
                    transactionBeans.add(tbeans);
                  }
              }else if(type.equalsIgnoreCase("cr")) {
                  if (cr > 0) {
                      transactionBeans.add(tbeans);
                  }
              }
          }
          return transactionBeans;
      }

      public void UpdateListBySortandSearch(ArrayList<TransactionBeans> list, String search){
          dailyTransactionAdapter = new DailyTransactionAdapter(this,list,search);
          recyclerView.setAdapter(dailyTransactionAdapter);
      }



      public void BackToMain(){
      //  startActivity(new Intent(DailyTransactionActivity.this,MoreActivity.class));
        finish();
      }



    Calendar calendar = null;
    public int yearFrom,monthFrom,dayOfMonthFrom;
    public int yearTo,monthTo,dayOfMonthTo;
    DatePickerDialog fromDatePickerDialog;
    DatePickerDialog toDatePickerDialog;
    public void LoadDatePicker(String type){

        if(type.equalsIgnoreCase("from")) {
            fromDatePickerDialog = new DatePickerDialog(DailyTransactionActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            yearFrom = year;
                            monthFrom = month;
                            dayOfMonthFrom = day;
                            fromDate_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                            transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                            UpdateListBySortandSearch(transactionBeansArrayList,"");

                        }
                    }, yearFrom, monthFrom, dayOfMonthFrom);

            fromDatePickerDialog.show();
        }else if(type.equalsIgnoreCase("to")) {
            toDatePickerDialog = new DatePickerDialog(DailyTransactionActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            yearTo = year;
                            monthTo = month;
                            dayOfMonthTo = day;
                            toDate_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                            transactionBeansArrayList = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,"",fromDate_txt.getText().toString(),toDate_txt.getText().toString(),sortingType);
                            UpdateListBySortandSearch(transactionBeansArrayList,"");

                        }
                    }, yearTo, monthTo, dayOfMonthTo);

            toDatePickerDialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
           // startActivity(new Intent(this, MoreActivity.class));
            finish();
        }
        return true;
    }


}
