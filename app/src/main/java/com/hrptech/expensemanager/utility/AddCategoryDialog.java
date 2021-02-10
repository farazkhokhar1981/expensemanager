package com.hrptech.expensemanager.utility;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.ui.category.CategoryViewAdapter;
import com.hrptech.expensemanager.ui.transaction.TransactionExpenseActivity;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;

import java.util.ArrayList;


public class AddCategoryDialog {
    Activity activity;

    public CategoryDB categoryDB;

    String types = "";
    public AddCategoryDialog(Activity activity, String types){
        this.activity = activity;
        this.types = types;
        categoryDB = new CategoryDB(activity);

    }
    LinearLayout saveRecord_btn;
    ImageView back_btn;
    private RecyclerView catgeoryList;
    CategoryViewAdapter categoryViewAdapter;
    TextInputEditText textName;
    public Dialog dialog = null;

    public void LoadDialog(){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_category);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
        textName = (TextInputEditText)dialog.findViewById(R.id.txtCategory);
        saveRecord_btn = (LinearLayout) dialog.findViewById(R.id.save_btn);
        saveRecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    saveRecord();


            }
        });

        back_btn = (ImageView) dialog.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        catgeoryList = (RecyclerView) dialog.findViewById(R.id.categoryList);
        catgeoryList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        catgeoryList.setLayoutManager(horizontalManager);
        ArrayList<CATEGORY> CATEGORYArrayList = categoryDB.getCategoryRecords(types);
        categoryViewAdapter = new CategoryViewAdapter(activity, CATEGORYArrayList);
        catgeoryList.setAdapter(categoryViewAdapter);

        FrameLayout frameLayout = (FrameLayout)dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(activity,frameLayout);
        dialog.show();
    }


    String selected_Id = "";
    public void saveRecord(){
        String name = textName.getText().toString();
        if(name.equalsIgnoreCase("")){
            Toast.makeText(activity,"Enter category",Toast.LENGTH_LONG).show();
            textName.requestFocus();
            return;
        }
        if(selected_Id.equalsIgnoreCase("")) {
            if (categoryDB.isCategoryExist(name)) {
                Toast.makeText(activity, "Category already exist", Toast.LENGTH_LONG).show();
                textName.requestFocus();
                return;
            }
        }else {
            if (categoryDB.isCategoryExist(selected_Id,name)) {
                Toast.makeText(activity, "Category already exist in other record", Toast.LENGTH_LONG).show();
                textName.requestFocus();
                return;
            }
        }



        int record = 0;
        if(selected_Id.equalsIgnoreCase("")){
            record = categoryDB.InsertRecord(getCategory(name,types));
            if(record>0){
                Toast.makeText(activity,"Record Save Succussfuly....",Toast.LENGTH_LONG).show();

                if(types.equalsIgnoreCase("Income")){
                    if(TransactionIncomeActivity.getTransactionFragment()!=null){
                        TransactionIncomeActivity.getTransactionFragment().RefreshList(name);
                    }
                }else if(types.equalsIgnoreCase("Expense")){
                    if(TransactionExpenseActivity.getTransactionFragment()!=null){
                        TransactionExpenseActivity.getTransactionFragment().RefreshList(name);
                    }
                }
                textName.setText("");
                dialog.dismiss();
            }
        }else {
            categoryDB.UpdateRecord(selected_Id,getCategory(name,types));
            Toast.makeText(activity,"Record Save Succussfuly....",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }


    }
    public CATEGORY getCategory(String name,String type){
        CATEGORY beans = new CATEGORY();
        beans.setId("");
        beans.setName(name);
        beans.setType(type);
        return beans;
    }
}
