package com.hrptech.expensemanager.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.ui.budget.CategoryListAdapter;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionFragment extends Fragment {
    CategoryListAdapter listAdapter;
    Spinner category_spnr;
    TextInputEditText budgetAmount_txt;

    EditText description_txt;
    LinearLayout saveBtn;
    public CategoryDB categoryDB;
    public BudgetDB budgetDB;
    private RecyclerView budgetList;
    TransactionViewAdapter transactionViewAdapter;
    public static TransactionFragment transactionFragment;
    public static TransactionFragment getTransactionFragment(){
        return transactionFragment;
    }
    DatePickerDialog datePickerDialog;
    ImageView calenderBtn;
    TextView date_txt;
    public TransactionDB transactionDB = null;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_transaction, container, false);
        transactionFragment = this;
        transactionDB = new TransactionDB(this.getActivity());
        category_spnr = (Spinner)root.findViewById(R.id.category_spnr);
        date_txt = (TextView)root.findViewById(R.id.date_txt);
        date_txt.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        budgetAmount_txt = (TextInputEditText) root.findViewById(R.id.txtBudgetAmount);
        description_txt = (EditText) root.findViewById(R.id.txtDescription);
        categoryDB = new CategoryDB(this.getActivity());
        budgetDB = new BudgetDB(this.getActivity());
        ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecords();
        listAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        listAdapter.notifyDataSetChanged();
        category_spnr.setAdapter(listAdapter);
        budgetList = (RecyclerView) root.findViewById(R.id.budgetList);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(horizontalManager);
        String date = date_txt.getText().toString();
        ArrayList<TransactionBeans> budgetBeansArrayList = transactionDB.getTransactionRecordsDate(date);
        transactionViewAdapter = new TransactionViewAdapter(this.getActivity(),budgetBeansArrayList,"");
        budgetList.setAdapter(transactionViewAdapter);
        saveBtn = (LinearLayout)root.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_Id.equalsIgnoreCase("")){
                    SaveRecord();
                }else {
                    Utilities.showDialogTransaction(TransactionFragment.getTransactionFragment(),"","",TransactionFragment.getTransactionFragment().getActivity(),"update","Do you want to","update record");
                }
            }
        });

        calenderBtn = (ImageView) root.findViewById(R.id.cander_btn);
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker();
            }
        });

        //ads of Admob
        mInterstitialAd = new InterstitialAd(this.getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        AdsInit();
        //LoadBanner();
        return root;
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
        datePickerDialog = new DatePickerDialog(TransactionFragment.this.getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                        RefreshRecord();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void RefreshRecord(){
        ArrayList<TransactionBeans> budgetBeansArrayList = transactionDB.getTransactionRecordsDate(date_txt.getText().toString());
        transactionViewAdapter = new TransactionViewAdapter(TransactionFragment.this.getActivity(),budgetBeansArrayList,"");
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

            if (type.equalsIgnoreCase("Income")) {
                transactionBeans.setIncome(amount);
                transactionBeans.setExpense("0");
                transactionBeans.setBalance(amount);
                transactionDB.InsertRecord(transactionBeans);
            } else if (type.equalsIgnoreCase("Expense")) {
                transactionBeans.setIncome("0");
                transactionBeans.setExpense(amount);
                transactionBeans.setBalance(amount);
                transactionDB.InsertRecord(transactionBeans);
            }
        }else {
            TransactionBeans transactionBeans = new TransactionBeans();
            transactionBeans.setCid(cid);
            transactionBeans.setDate(date);
            transactionBeans.setType(type);
            transactionBeans.setDescription(description);
            transactionBeans.setIncome("0");
            transactionBeans.setExpense(amount);
            transactionBeans.setBalance(amount);
            if (type.equalsIgnoreCase("Income")) {
                transactionBeans.setIncome(amount);
                transactionBeans.setExpense("0");
                transactionBeans.setBalance(amount);
                transactionDB.UpdateRecord(selected_Id,transactionBeans);
            } else if (type.equalsIgnoreCase("Expense")) {
                transactionBeans.setIncome("0");
                transactionBeans.setExpense(amount);
                transactionBeans.setBalance(amount);
                transactionDB.UpdateRecord(selected_Id,transactionBeans);
            }
            selected_Id="";
        }
        Utilities.hideKeyboardFrom(TransactionFragment.this.getContext());
        RefreshRecord();
        description_txt.setText("");
        budgetAmount_txt.setText("");
    }
}