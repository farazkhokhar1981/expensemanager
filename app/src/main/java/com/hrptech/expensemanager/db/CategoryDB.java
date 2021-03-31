package com.hrptech.expensemanager.db;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.lang.UProperty;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.CATEGORYs;
import com.hrptech.expensemanager.localdb.db.GeneralDB;
import com.hrptech.expensemanager.localdb.db.UtilitiesLocalDb;
import com.hrptech.expensemanager.ui.category.CategoryActivity;
import com.hrptech.expensemanager.ui.home.LoginActivity;
import com.hrptech.expensemanager.utility.Utilities;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class CategoryDB {
    CategoryDB categoryDB;
    static GeneralDB generalDB;
    Activity myActivity;
    static Activity myAct;
    static ProgressDialog myDialog;

    static int record = 0;

    DatabaseReference ref = null;
    FirebaseDatabase database = null;
    DatabaseReference checkCatExist = null;
    DatabaseReference databaseReference = null;

    public CategoryDB(Activity myActivity) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/CATEGORY");
        this.myActivity = myActivity;
        this.myAct = myActivity;
    }

    public static void getCatNameList() {
        //Utilities.catNameList.clear();
        //GeneralDB.DeleteRecord(UtilitiesLocalDb.category_tbl);
        DatabaseReference checkCatExist = FirebaseDatabase.getInstance().getReference("EXPENSEMANAGER/CATEGORY");
        checkCatExist.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilities.catNameList.removeAll(Utilities.catNameList);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    String name = map.get("name").toString();
                    String type = map.get("type").toString();
                    String id = map.get("id").toString();

                    if (!isNameExist(name, type)) {
                        CATEGORY category = new CATEGORY();
                        category.setId(id);
                        category.setType(type);
                        category.setName(name);
                        Utilities.catNameList.add(category);
                    }
                    //GeneralDB.InsertRecord(UtilitiesLocalDb.category_tbl, new String[]{UtilitiesLocalDb.category_id, UtilitiesLocalDb.category_name, UtilitiesLocalDb.category_type}, new String[]{id, name, type});
                    //record++;
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static ArrayList<CATEGORY> getCatList() {
        Utilities.catNameList.clear();
        ArrayList<String[]> recordList;
        recordList = GeneralDB.getRecords(UtilitiesLocalDb.category_tbl, new String[]{UtilitiesLocalDb.category_id, UtilitiesLocalDb.category_name, UtilitiesLocalDb.category_type});
        for (int index = 0; index < recordList.size(); index++) {
            CATEGORY category = new CATEGORY();
            category.setId(recordList.get(index)[0]);
            category.setName(recordList.get(index)[1]);
            category.setType(recordList.get(index)[2]);
            Utilities.catNameList.add(category);
        }
        return Utilities.catNameList;
    }

    public static ArrayList<CATEGORY> getCatListWhere(String catType) {
        //Utilities.catNameList.clear();
        //ArrayList<String[]> recordList;
        //recordList = GeneralDB.getRecordsWhere(UtilitiesLocalDb.category_tbl, new String[]{UtilitiesLocalDb.category_id, UtilitiesLocalDb.category_name, UtilitiesLocalDb.category_type},new String[]{UtilitiesLocalDb.category_type},new String[]{catType});

        ArrayList<CATEGORY> catList = new ArrayList<>();
        for (int index = 0; index <= Utilities.catNameList.size()-1; index++) {
            if(Utilities.catNameList.get(index).getType().equalsIgnoreCase(catType)){

                CATEGORY beans = new CATEGORY();

                beans.setId(Utilities.catNameList.get(index).getId());
                beans.setName(Utilities.catNameList.get(index).getName());
                beans.setType(Utilities.catNameList.get(index).getType());

                catList.add(beans);
            }
        }


//        for (int index = 0; index < recordList.size(); index++) {
//            CATEGORY category = new CATEGORY();
//            category.setId(recordList.get(index)[0]);
//            category.setName(recordList.get(index)[1]);
//            category.setType(recordList.get(index)[2]);
//            Utilities.catNameList.add(category);
//        }
        return catList;
    }


    public static boolean isNameExist(String name, String type) {
        for (int index = 0; index < Utilities.catNameList.size(); index++) {
            CATEGORY beans = Utilities.catNameList.get(index);
            String name_ = beans.getName();
            String type_ = beans.getType();
            if (name.equalsIgnoreCase(name_) && type.equalsIgnoreCase(type_)) {
                return true;
            }
        }
        return false;
    }

    public int InsertRecord(CATEGORY category) {
        record = 0;
        String enteredCatName = category.getName();
        String enteredCatType = category.getType();
        try {
            if (!isNameExist(enteredCatName, enteredCatType)) {
                String userId = databaseReference.push().getKey();
                category.setId(userId);
                databaseReference.child(userId).setValue(category);
                record = 1;
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public int InsertRecordDialog(CATEGORY category) {
        record = 0;
        String enteredCatName = category.getName();
        String enteredCatType = category.getType();
        try {
            if (!isNameExist(enteredCatName, enteredCatType)) {
                String userId = databaseReference.push().getKey();
                category.setId(userId);
                databaseReference.child(userId).setValue(category);
                record = 1;
                Utilities.catNameList.add(category);
            }
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public int UpdateRecord(String userId, CATEGORY category) {
        record = 0;
        try {
            ref = database.getReference("EXPENSEMANAGER/CATEGORY");
            ref.child(userId).child("name").setValue(category.getName());
            ref.child(userId).child("type").setValue(category.getType());
            record = 1;

        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in updating the the record", Toast.LENGTH_LONG);
        }
        return record;
    }


    public void DeleteRecord(String userId) {
        try {
            databaseReference.child(userId).removeValue();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }

    public ArrayList<CATEGORY> getCategoryRecords(String types) {

        ArrayList<CATEGORY> catList = new ArrayList<>();

        for(int index = 0; index < Utilities.catNameList.size(); index++){
            if(Utilities.catNameList.get(index).getType().equalsIgnoreCase(types)){
                CATEGORY bean = new CATEGORY();

                bean.setId(Utilities.catNameList.get(index).getId());
                bean.setName(Utilities.catNameList.get(index).getName());
                bean.setType(Utilities.catNameList.get(index).getType());

                catList.add(bean);
            }
        }

        return catList;
    }

    public boolean isCategoryExist(String name) {
        return true;
    }

    public boolean isCategoryExist(String id, String name) {
        return true;
    }

    public CATEGORY getCategoryRecordSingle(String id) {
        for (int index = 0; index < Utilities.catNameList.size(); index++) {
            CATEGORY beans = Utilities.catNameList.get(index);
            String token = beans.getId();
            if (token.equalsIgnoreCase(id)) {
                return beans;
            }
        }
        return null;
    }
}
