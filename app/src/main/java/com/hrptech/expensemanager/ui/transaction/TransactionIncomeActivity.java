package com.hrptech.expensemanager.ui.transaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputEditText;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.BudgetDB;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.ModificationDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.localdb.db.GeneralDB;
import com.hrptech.expensemanager.ui.budget.CategoryListAdapter;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.utility.AddCategoryDialog;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionIncomeActivity extends Activity{
    CategoryListAdapter listAdapter;
    Spinner category_spnr;
    TextInputEditText budgetAmount_txt;

    EditText description_txt;
    LinearLayout saveBtn;
    ImageView backBtn;

    RadioButton rdoSortIncome_btn;
    RadioButton rdoSortExpense_btn;

    public CategoryDB categoryDB;
    public BudgetDB budgetDB;
    ModificationDB modificationDB;
    private RecyclerView budgetList;
    TransactionViewAdapter transactionViewAdapter;
    public static TransactionIncomeActivity transactionFragment;
    public static TransactionIncomeActivity getTransactionFragment(){
        return transactionFragment;
    }
    DatePickerDialog datePickerDialog;
    ImageView calenderBtn;
    ImageView addCategory_btn;
    TextView date_txt;
    public TransactionDB transactionDB = null;
    Activity root;
    String sortingType = "";
    String clValue = "";
    ArrayList<CATEGORY> categoryBeans = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_transaction);
        root = this;
        transactionFragment = this;

        Utilities.catNameList = CategoryDB.getCatList();

        transactionDB = new TransactionDB(this.getActivity());
        modificationDB = new ModificationDB(this.getActivity());
        category_spnr = (Spinner)root.findViewById(R.id.category_spnr);

        date_txt = (TextView)root.findViewById(R.id.date_txt);
        date_txt.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        budgetAmount_txt = (TextInputEditText) root.findViewById(R.id.txtBudgetAmount);
        description_txt = (EditText) root.findViewById(R.id.txtDescription);
        categoryDB = new CategoryDB(this.getActivity());
        budgetDB = new BudgetDB(this.getActivity());
        listAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        listAdapter.notifyDataSetChanged();
        category_spnr.setAdapter(listAdapter);
        budgetList = (RecyclerView) root.findViewById(R.id.budgetList);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(horizontalManager);
        clValue = Utilities.getValue(this,savedInstanceState,"className");
        if(clValue!=null){
            sortingType = Utilities.getValue(this,savedInstanceState,"trans");
            selected_Id = Utilities.getValue(this,savedInstanceState,"transactionId");

            if(sortingType.equalsIgnoreCase("Income")){
                loadCategory("Income");
            }
            else if(sortingType.equalsIgnoreCase("Expense")){
                loadCategory("Expense");
            }


            //Toast.makeText(this,"Token ID: "+selected_Id, Toast.LENGTH_LONG).show();

            if(!selected_Id.equalsIgnoreCase("")){
                Toast.makeText(this,"Token ID: "+selected_Id, Toast.LENGTH_LONG).show();
                ShowRecordOFBudgetForUpdate(selected_Id);
            }
        }



        saveBtn = (LinearLayout)root.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_Id.equalsIgnoreCase("")){
                    SaveRecord();
                }else {
                    Utilities.showDialogClose(TransactionIncomeActivity.this,"Update","Income",selected_Id,"");
                }
            }
        });
        //loadCategory(sortingType);

        rdoSortIncome_btn= (RadioButton) root.findViewById(R.id.income_rdo);
        rdoSortIncome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingType = "Income";
                loadCategory(sortingType);
            }
        });

        rdoSortExpense_btn = (RadioButton) root.findViewById(R.id.expense_rdo);
        rdoSortExpense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingType = "Expense";
                loadCategory(sortingType);
            }
        });
        if(sortingType.equalsIgnoreCase("Income")){
            rdoSortIncome_btn.setChecked(true);
            rdoSortExpense_btn.setChecked(false);
        }else if(sortingType.equalsIgnoreCase("Expense")){
            rdoSortIncome_btn.setChecked(false);
            rdoSortExpense_btn.setChecked(true);
        }



        backBtn = (ImageView) root.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToMain();
            }
        });

        calenderBtn = (ImageView) root.findViewById(R.id.cander_btn);
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker();
            }
        });

        addCategory_btn = (ImageView) root.findViewById(R.id.addCategory_btn);
        addCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog(TransactionIncomeActivity.this,"Income");
                addCategoryDialog.LoadDialog();
            }
        });

        //ads of Admob
        mInterstitialAd = new InterstitialAd(this.getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        AdsInit();
        //LoadBanner();

        //check id from intent get exptra value

    }
    String className = "";
    public void loadCategory(String type){

        categoryBeans = CategoryDB.getCatListWhere(sortingType);
        listAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        listAdapter.notifyDataSetChanged();
        category_spnr.setAdapter(listAdapter);
        String date = date_txt.getText().toString();
        ArrayList<TransactionBeans> budgetBeansArrayList = transactionDB.getTransactionRecordsDate(date,sortingType);
        transactionViewAdapter = new TransactionViewAdapter(this.getActivity(),budgetBeansArrayList,sortingType);
        budgetList.setAdapter(transactionViewAdapter);
    }

    public Activity getActivity(){
        return this;
    }
    public void AdsInit(){
        mInterstitialAd = new InterstitialAd(this.getActivity());

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
//        if(Utilities.isLoadAdsWhenOpened) {
//            mInterstitialAd.loadAd(adRequest);
//            mInterstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//
//                    }
//                }
//            });
//        }
        LoadBanner();
    }

    public void LoadBanner(){
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    InterstitialAd mInterstitialAd;



Calendar calendar = null;
    int year,month,dayOfMonth;
    public void LoadDatePicker(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(TransactionIncomeActivity.this.getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                        RefreshRecord();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void RefreshList(String name){
        ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecords(sortingType);
        listAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        listAdapter.notifyDataSetChanged();
        category_spnr.setAdapter(listAdapter);
        category_spnr.setSelection(getValuePosition(name,categoryBeans));
    }

    public int getValuePosition(String name,ArrayList<CATEGORY> categoryBeans){
        for(int index=0; index<categoryBeans.size(); index++){
            CATEGORY beans = categoryBeans.get(index);
            String nameStr = beans.getName();
            if(name.equalsIgnoreCase(nameStr)){
                return index;
            }
        }
        return -1;
    }

    public void RefreshRecord(){
        ArrayList<TransactionBeans> budgetBeansArrayList = transactionDB.getTransactionRecordsDate(date_txt.getText().toString(),sortingType);
        transactionViewAdapter = new TransactionViewAdapter(TransactionIncomeActivity.this.getActivity(),budgetBeansArrayList,sortingType);
        budgetList.setAdapter(transactionViewAdapter);
    }

    public void ShowRecordOFBudgetForUpdate(String id){
        TransactionBeans budgetBeans = transactionDB.getTransactionRecordSingle(id);
        if(budgetBeans!=null){
            String cName = budgetBeans.getName();
            String date = budgetBeans.getDate();
            String description = budgetBeans.getDescription();
            String amount = budgetBeans.getBalance();
            category_spnr.setSelection(Utilities.getIndexCategory(category_spnr,cName));
            budgetAmount_txt.setText(amount);
            description_txt.setText(description);
            date_txt.setText(date);
            selected_Id = id;
        }
    }
    public void Refresh(){
        selected_Id="";
        budgetAmount_txt.setText("");
    }
    String selected_Id = "";
    public void SaveRecord(){
        CATEGORY bean=(CATEGORY) category_spnr.getSelectedItem();
        if(bean==null){
            Toast.makeText(this.getActivity(), "Category not created", Toast.LENGTH_LONG).show();
            return;
        }
        String cid = bean.getId();
        String date = date_txt.getText().toString();
        String type = bean.getType();
        String amount = budgetAmount_txt.getText().toString();
        if(amount.equalsIgnoreCase("")){
            Toast.makeText(this.getActivity(),"Enter Amount greater then 0",Toast.LENGTH_LONG).show();
            return;
        }
        String description = description_txt.getText().toString();
        if(selected_Id.equalsIgnoreCase("")) {
            TransactionBeans transactionBeans = new TransactionBeans();
            transactionBeans.setCid(cid);
            transactionBeans.setDate(date);
            transactionBeans.setType(type);
            transactionBeans.setDescription(description);
            transactionBeans.setIncome("0");
            transactionBeans.setExpense(amount);
            transactionBeans.setBalance(amount);
            if(sortingType.equalsIgnoreCase("Income")){
                transactionBeans.setIncome(amount);
                transactionBeans.setExpense("0");
                transactionBeans.setBalance(amount);
                transactionDB.InsertRecord(transactionBeans);
            }else if(sortingType.equalsIgnoreCase("Expense")){
                transactionBeans.setIncome("0");
                transactionBeans.setExpense(amount);
                transactionBeans.setBalance(amount);
                transactionDB.InsertRecord(transactionBeans);
            }

        }else {
            String time = new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime());
            TransactionBeans transactionBeans = new TransactionBeans();
            transactionBeans.setCid(cid);
            transactionBeans.setDate(date);
            transactionBeans.setType(type);
            transactionBeans.setDescription(description);
            transactionBeans.setIncome("0");
            transactionBeans.setExpense(amount);
            transactionBeans.setBalance(amount);
            if(sortingType.equalsIgnoreCase("Income")) {
                transactionBeans.setIncome(amount);
                transactionBeans.setExpense("0");
                transactionBeans.setBalance(amount);
                transactionDB.UpdateRecord(selected_Id,transactionBeans);
            }else if(sortingType.equalsIgnoreCase("Expense")) {
                transactionBeans.setIncome("0");
                transactionBeans.setExpense(amount);
                transactionBeans.setBalance(amount);
                transactionDB.UpdateRecord(selected_Id, transactionBeans);
            }
            selected_Id="";
        }
            Utilities.lastChanges = amount+"";
            Utilities.lastType = sortingType;
            Utilities.lastName = bean.getName();
            Utilities.lastDate = date;

        Utilities.hideKeyboardFrom(TransactionIncomeActivity.this.getActivity());
        RefreshRecord();
        description_txt.setText("");
        budgetAmount_txt.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        BackToMain();

    }

    public void BackToMain(){
        Intent intent = null;
//        if(clValue.equalsIgnoreCase("home")){
            intent = new Intent(this, HomeActivity.class);
//        }else if(clValue.equalsIgnoreCase("dailyTran")){
//            intent = new Intent(this, DailyTransactionActivity.class);
//        }
        startActivity(intent);
        finish();
    }

}