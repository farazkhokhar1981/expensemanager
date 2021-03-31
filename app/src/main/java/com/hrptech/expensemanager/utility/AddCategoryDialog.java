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
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.ui.category.CategoryViewAdapter;
import com.hrptech.expensemanager.ui.transaction.TransactionExpenseActivity;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;

import java.util.ArrayList;


public class AddCategoryDialog {
    static Activity activity;

    public static CategoryDB categoryDB;

    static String types = "";
    public AddCategoryDialog(Activity activity, String types){
        this.activity = activity;
        this.types = types;
        categoryDB = new CategoryDB(activity);

    }
    static LinearLayout saveRecord_btn;
    static ImageView back_btn;
    private static RecyclerView catgeoryList;
    static CategoryViewAdapter categoryViewAdapter;
    static TextInputEditText textName;
    public static Dialog dialog = null;

    public static void LoadDialog(){

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
        LinearLayoutManager horizontalManager_ = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        catgeoryList.setLayoutManager(horizontalManager_);
        ArrayList<CATEGORY> CATEGORYArrayList_ = categoryDB.getCategoryRecords(types);
        categoryViewAdapter = new CategoryViewAdapter(activity, CATEGORYArrayList_);
        catgeoryList.setAdapter(categoryViewAdapter);

        FrameLayout frameLayout = (FrameLayout)dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(activity,frameLayout);
        dialog.show();

        if (!selected_Id.equalsIgnoreCase("")) {
            //Toast.makeText(this, "Token ID: " + selected_Id, Toast.LENGTH_LONG).show();
            UpdateCategory(selected_Id);
        }
    }

    public static void UpdateCategory(String id) {
        CATEGORY category = categoryDB.getCategoryRecordSingle(id);
        if (category != null) {
            String cName = category.getName();
            String cType = category.getType();
            String cId = category.getId();
            textName.setText(category.getName());
            selected_Id = id;
        }
    }


    static String selected_Id = "";
    public static void saveRecord(){
        String name = textName.getText().toString();
        if(name.equalsIgnoreCase("")){
            Toast.makeText(activity,"Enter category",Toast.LENGTH_LONG).show();
            textName.requestFocus();
            return;
        }
        if(selected_Id.equalsIgnoreCase("")) {
            if(CategoryDB.isNameExist(name,types)){
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
            record = categoryDB.InsertRecordDialog(getCategory(name,types));
            if(record>0){
                Toast.makeText(activity,"Record Save Succussfuly....",Toast.LENGTH_LONG).show();

                    if(TransactionIncomeActivity.getTransactionFragment()!=null){
                        TransactionIncomeActivity.getTransactionFragment().RefreshList(name);
                    }
                textName.setText("");
                dialog.dismiss();
            }
        }else {
            categoryDB.UpdateRecord(selected_Id,getCategory(name,types));
            Toast.makeText(activity,"Record Updated Succussfuly....",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }


    }
    public static CATEGORY getCategory(String name, String type){
        CATEGORY beans = new CATEGORY();
        beans.setId("");
        beans.setName(name);
        beans.setType(type);
        return beans;
    }
}
