package com.hrptech.expensemanager.ui.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.CATEGORYs;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.localdb.db.GeneralDB;
import com.hrptech.expensemanager.localdb.db.UtilitiesLocalDb;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class CategoryActivity extends Activity {


    ProgressDialog myDialog;

    RadioGroup rdo_group;
    TextInputEditText textName;
    LinearLayout saveRecord_btn;
    ImageView back_btn;
    public CategoryDB categoryDB;
    RadioButton rdo_btn;
    RadioButton rdoIncome_btn;
    RadioButton rdoExpense_btn;
    RadioButton rdoSortAll_btn;
    RadioButton rdoSortIncome_btn;
    RadioButton rdoSortExpense_btn;
    private RecyclerView catgeoryList;
    CategoryViewAdapter categoryViewAdapter;
    public static CategoryActivity categoryFragment;

    public static CategoryActivity getCategoryFragment() {
        return categoryFragment;
    }

    Activity root;
    int onStartCount = 1;
    String sortingType = "All";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_category);
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
        categoryFragment = this;
        categoryDB = new CategoryDB(this.getActivity());
        rdo_group = (RadioGroup) root.findViewById(R.id.rgDefault);
        rdoSortAll_btn = (RadioButton) root.findViewById(R.id.all_Sort_rdo_btn);
        rdoSortAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingType = "all";
                getFilter(sortingType);
            }
        });


        //Utilities.catNameList = CategoryDB.getCatList();

        //categoryDB.InsertRecord(getCategory("Business","Income"));
        /*categoryDB.InsertRecord(getCategory("Salary","Income"));
        categoryDB.InsertRecord(getCategory("Other","Income"));

        categoryDB.InsertRecord(getCategory("Utilities","Expense"));
        categoryDB.InsertRecord(getCategory("Food","Expense"));
        categoryDB.InsertRecord(getCategory("Eduction","Expense"));
        categoryDB.InsertRecord(getCategory("Travel","Expense"));
        categoryDB.InsertRecord(getCategory("Medical","Expense"));*/
        //categoryDB.InsertRecord(getCategory("Personal","Expense"));


        rdoSortIncome_btn = (RadioButton) root.findViewById(R.id.income_Sort_rdo_btn);
        rdoSortIncome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingType = "income";
                getFilter(sortingType);
            }
        });
        rdoSortExpense_btn = (RadioButton) root.findViewById(R.id.expense_Sort_rdo_btn);
        rdoSortExpense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingType = "expense";
                getFilter(sortingType);
            }
        });
        rdoIncome_btn = (RadioButton) root.findViewById(R.id.income_rdo);
        rdoExpense_btn = (RadioButton) root.findViewById(R.id.expense_rdo);
        textName = (TextInputEditText) root.findViewById(R.id.txtCategory);
        saveRecord_btn = (LinearLayout) root.findViewById(R.id.save_btn);
        saveRecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_Id.equalsIgnoreCase("")) {
                    saveRecord();
                } else {
                    Utilities.showDialogClose(CategoryActivity.this, "Update", "Category", selected_Id, "");
                }

            }
        });

        back_btn = (ImageView) root.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMore(new Intent(CategoryActivity.this, HomeActivity.class));
            }
        });
        catgeoryList = (RecyclerView) root.findViewById(R.id.categoryList);
        catgeoryList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        catgeoryList.setLayoutManager(horizontalManager);
        getFilter("All");
    }

    public void RefreshRecord() {

    }

    public void getFilter(String type) {
        ArrayList<CATEGORY> beansList = new ArrayList<>();
        beansList.removeAll(beansList);
        if (type.equalsIgnoreCase("All")) {
            beansList = Utilities.catNameList;
        } else {
            for (int index = 0; index < Utilities.catNameList.size(); index++) {
                CATEGORY beans = Utilities.catNameList.get(index);
                String type_ = beans.getType();
                if (type_.equalsIgnoreCase(type)) {
                    beansList.add(beans);
                }
            }
        }
        categoryViewAdapter = new CategoryViewAdapter(this.getActivity(), beansList);
        catgeoryList.setAdapter(null);
        catgeoryList.setAdapter(categoryViewAdapter);
    }

    public CATEGORY getCategory(String name, String type) {
        CATEGORY beans = new CATEGORY();
        beans.setId("");
        beans.setName(name);
        beans.setType(type);
        return beans;
    }


    String selected_Id = "";

    public void refresh() {
        selected_Id = "";
        textName.setText("");
    }

    public void ShowRecordOfCategory(String id) {
        CATEGORY CATEGORY = categoryDB.getCategoryRecordSingle(id);
        if (CATEGORY != null) {
            selected_Id = id;
            String name = CATEGORY.getName();
            String type = CATEGORY.getType();
            textName.setText(name);
            if (type.equalsIgnoreCase("Income")) {
                rdoIncome_btn.setChecked(true);
            } else {
                rdoExpense_btn.setChecked(true);
            }
        }
    }

    public boolean isCategoryExist(String name) {

        for (int index = 0; index < Utilities.catNameList.size(); index++) {
            if (name.equalsIgnoreCase(Utilities.catNameList.get(index).getName())) {
                return true;
            }
        }

        return false;
    }

    public boolean isCategoryExist(String userId, String name) {

        for (int index = 0; index < Utilities.catNameList.size(); index++) {
            if (userId.equalsIgnoreCase(Utilities.catNameList.get(index).getType()) && name.equalsIgnoreCase(Utilities.catNameList.get(index).getName())) {
                return true;
            }
        }

        return false;
    }

    public void saveRecord() {

        String name = textName.getText().toString();
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this.getActivity(), "Enter category", Toast.LENGTH_LONG).show();
            textName.requestFocus();
            return;
        }
        if (selected_Id.equalsIgnoreCase("")) {
            if (isCategoryExist(name)) {
                Toast.makeText(this.getActivity(), "Category already exist", Toast.LENGTH_LONG).show();
                textName.requestFocus();
                return;
            }
        } else {
            if (isCategoryExist(selected_Id, name)) {
                Toast.makeText(this.getActivity(), "Category already exist in other record", Toast.LENGTH_LONG).show();
                textName.requestFocus();
                return;
            }
        }



        int selectedId = rdo_group.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        rdo_btn = (RadioButton) root.findViewById(selectedId);
        String type = rdo_btn.getText().toString(); // rdo button type, cat type, income or expense for adding updating records from category
        int record = 0;
        CATEGORY category = new CATEGORY();
        category.setId("");
        category.setName(name);
        category.setType(type);

        if (selected_Id.equalsIgnoreCase("")) {
            myDialog = ProgressDialog.show(this, "","Please Wait...", true);
            record = categoryDB.InsertRecord(category);
            if (record > 0) {
                CategoryDB.getCatNameListToLocalDB();
                myDialog.dismiss();
                Toast.makeText(this.getActivity(), "Record Save Succussfuly....", Toast.LENGTH_LONG).show();
                textName.setText("");
                getFilter(sortingType);
                myDialog.dismiss();
            }
        } else {
            myDialog = ProgressDialog.show(this, "","Please Wait...", true);
            record = categoryDB.UpdateRecord(selected_Id, category);
            if (record > 0) {

                //GeneralDB.UpdateRecord(UtilitiesLocalDb.category_tbl,new String[]{UtilitiesLocalDb.category_name,UtilitiesLocalDb.category_type},new String[]{category.getName(),category.getType()},new String[]{UtilitiesLocalDb.category_id,selected_Id});
                //Utilities.catNameList = CategoryDB.getCatList();
                CategoryDB.getCatNameListToLocalDB();
                Toast.makeText(this.getActivity(), "Record Updated Succussfuly....", Toast.LENGTH_LONG).show();
                getFilter(sortingType);
                myDialog.dismiss();
                refresh();
            }
        }


        rdo_btn = (RadioButton) root.findViewById(selectedId);
        Utilities.hideKeyboardFrom(this.getActivity());

    }

    public Activity getActivity() {
        return root;
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
        backToMore(new Intent(CategoryActivity.this, HomeActivity.class));

    }

    public void backToMore(Intent intent) {
        startActivity(intent);
        finish();
    }
}