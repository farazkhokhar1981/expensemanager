package com.hrptech.expensemanager.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrptech.expensemanager.beans.BudgetBeans;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class SettingDB {

    FirebaseDatabase database =null;
    DatabaseReference databaseReference = null;
    Activity myActivity;
    public SettingDB(Activity myActivity){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/SETTINGS");
        this.myActivity = myActivity;

    }

    public int InsertRecord(SettingBeans settingBeans) {
        int record = 0;
        try {
            databaseReference.setValue(settingBeans);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public int UpdateRecord(String userId,SettingBeans settingBeans) {
        int record = 0;
        try {
            settingBeans.setId(userId);
            databaseReference.child(userId).setValue(settingBeans);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public void DeleteRecord() {
        try {
            databaseReference.removeValue();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    ArrayList<SettingBeans> beanList =new ArrayList<>();
    public ArrayList<SettingBeans> getSettingRecords(){
        try {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()) {
                        DataSnapshot object = dataSnapshotIterator.next();
                        SettingBeans users = object.getValue(SettingBeans.class);
                        if (users != null) {
                            beanList.add(users);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }
    SettingBeans bean=null;
    public SettingBeans getSettingRecordsSingle(){
        try {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bean = dataSnapshot.getValue(SettingBeans.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }





}
