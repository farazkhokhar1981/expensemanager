package com.hrptech.expensemanager.ui.budget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputEditText;
import com.hrptech.expensemanager.MoreActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.BudgetBeans;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.db.BudgetDB;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BudgetFragment extends Activity {

    CategoryListAdapter listAdapter;
    Spinner category_spnr;
    Spinner year_spnr;
    Spinner month_spnr;
    TextInputEditText budgetAmount_txt;
    LinearLayout saveBtn;
    ImageView addBtn;
    public CategoryDB categoryDB;
    public BudgetDB budgetDB;
    private RecyclerView budgetList;
    BudgetViewAdapter budgetViewAdapter;
    public static BudgetFragment budgetFragment;
    public static BudgetFragment getInit(){
        return budgetFragment;
    }
    Activity root;
    int onStartCount = 1;
    public Activity getActivity(){
        return root;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_budget);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        root = this;
        budgetFragment = this;
        category_spnr = (Spinner)root.findViewById(R.id.category_spnr);
        year_spnr = (Spinner)root.findViewById(R.id.year_spnr);
        String year = new SimpleDateFormat("yyyy").format(new Date());
        int positionOfYear = Utilities.getIndex(year_spnr,year);
        year_spnr.setSelection(positionOfYear);
        month_spnr = (Spinner)root.findViewById(R.id.month_spnr);
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))-1;
        month_spnr.setSelection(month);
        budgetAmount_txt = (TextInputEditText) root.findViewById(R.id.txtBudgetAmount);
        categoryDB = new CategoryDB(this.getActivity());
        budgetDB = new BudgetDB(this.getActivity());
        ArrayList<CATEGORY> categoryBeans = categoryDB.getCategoryRecordsExpenseOnly();
        listAdapter = new CategoryListAdapter(this.getActivity(),categoryBeans);
        category_spnr.setAdapter(listAdapter);
        budgetList = (RecyclerView) root.findViewById(R.id.budgetList);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(horizontalManager);
        ArrayList<BudgetBeans> budgetBeansArrayList = budgetDB.getBudgetRecords();
        budgetViewAdapter = new BudgetViewAdapter(this.getActivity(),budgetBeansArrayList);
        budgetList.setAdapter(budgetViewAdapter);
        saveBtn = (LinearLayout)root.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_Id.equalsIgnoreCase("")){
                    SaveRecord();
                }else {
                    Utilities.showDialogBudget(BudgetFragment.getInit(),"",BudgetFragment.getInit().getActivity(),"update","Do you want to","update record");
                }
            }
        });
        addBtn = (ImageView) root.findViewById(R.id.back_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMore(new Intent(BudgetFragment.this, MoreActivity.class));
            }
        });


        //ads of Admob
        AdsInit();
        //LoadBanner();
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
        FrameLayout frameLayout =root.
                findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(getActivity(),frameLayout);
    }
    InterstitialAd mInterstitialAd;



    public void RefreshRecord(){
        ArrayList<BudgetBeans> budgetBeansArrayList = budgetDB.getBudgetRecords();
        budgetViewAdapter = new BudgetViewAdapter(this.getActivity(),budgetBeansArrayList);
        budgetList.setAdapter(budgetViewAdapter);
    }

    public void ShowRecordOFBudgetForUpdate(String id){
        BudgetBeans budgetBeans = budgetDB.getBudgetRecordSingle(id);
        if(budgetBeans!=null){
            String cName = budgetBeans.getCat_name();
            String year = budgetBeans.getYear();
            String month = budgetBeans.getMonth();
            String amount = budgetBeans.getAmount();
            category_spnr.setSelection(Utilities.getIndexCategory(category_spnr,cName));
            year_spnr.setSelection(Utilities.getIndex(year_spnr,year));
            month_spnr.setSelection(Utilities.getIndex(month_spnr,month));
            budgetAmount_txt.setText(amount);
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
        Object year = (Object)year_spnr.getSelectedItem();
        Object month = (Object)month_spnr.getSelectedItem();
        if(selected_Id.equalsIgnoreCase("")) {
            if (budgetDB.isBudgetAlreadyExist(cid, year.toString(), month.toString())) {
                Toast.makeText(this.getActivity(), "Record of month already exist", Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            if (budgetDB.isBudgetAlreadyExist(selected_Id,cid, year.toString(), month.toString())) {
                Toast.makeText(this.getActivity(), "Record of month already exist in other record", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String amount = budgetAmount_txt.getText().toString();
        if(amount.equalsIgnoreCase("")){
            Toast.makeText(this.getActivity(), "Set Amount of budget", Toast.LENGTH_LONG).show();
            return;
        }
        BudgetBeans budgetBeans = new BudgetBeans();
        budgetBeans.setCid(cid);
        budgetBeans.setYear(year.toString());
        budgetBeans.setMonth(month.toString());
        budgetBeans.setAmount(amount);
        if(selected_Id.equalsIgnoreCase("")){
            budgetDB.InsertRecord(budgetBeans);
        }else {
            budgetDB.UpdateRecord(selected_Id,budgetBeans);
            selected_Id = "";
        }
        budgetAmount_txt.setText("");
        Utilities.hideKeyboardFrom(BudgetFragment.this.getActivity());
        RefreshRecord();

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
        backToMore(new Intent(BudgetFragment.this, HomeActivity.class));

    }

    public void backToMore(Intent intent){
        startActivity(intent);
        finish();
    }
}