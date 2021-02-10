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
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class BudgetDB {


    FirebaseDatabase database =null;
    DatabaseReference databaseReference = null;
    Activity myActivity;
    public BudgetDB(Activity myActivity){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/BUDGET");
        this.myActivity = myActivity;

    }


    public int InsertRecord(BudgetBeans budgetBeans) {
        int record = 0;
        try {
            String userId =  databaseReference.push().getKey();
            budgetBeans.setId(userId);
            databaseReference.child(userId).setValue(budgetBeans);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public void UpdateRecord(String id,BudgetBeans budgetBeans) {
        try {
            budgetBeans.setId(id);
            databaseReference.child(id).setValue(budgetBeans);
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }

    public void DeleteRecord(String id) {
        try {
            databaseReference.child(id).removeValue();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    ArrayList<BudgetBeans> beanList=new ArrayList<>();
    public ArrayList<BudgetBeans> getBudgetRecords(){

        try {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()) {
                        DataSnapshot object = dataSnapshotIterator.next();
                        BudgetBeans users = object.getValue(BudgetBeans.class);
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


    public ArrayList<BudgetBeans> getBudgetRecordsTransaction(){
        try {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()) {
                        DataSnapshot object = dataSnapshotIterator.next();
                        BudgetBeans users = object.getValue(BudgetBeans.class);
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
    BudgetBeans bean = null;
    public BudgetBeans getBudgetRecordSingle(String id){
        try {
            //  mydb = managerDB.getDatabaseInit();
            // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
            //  Cursor allrows = mydb.rawQuery(sqlQuery, null);
            databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bean = dataSnapshot.getValue(BudgetBeans.class);
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

    public boolean isBudgetAlreadyExist(String cid,String year,String month){
        return true;
    }

    public boolean isBudgetAlreadyExist(String id,String cid,String year,String month){

        return true;
    }
}
