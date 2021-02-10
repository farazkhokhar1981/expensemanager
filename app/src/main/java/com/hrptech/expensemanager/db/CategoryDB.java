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
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Iterator;

public class CategoryDB {


    Activity myActivity;
    FirebaseDatabase database =null;
    DatabaseReference databaseReference = null;
    public CategoryDB(Activity myActivity){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/CATEGORY");
        this.myActivity = myActivity;


    }


    public int InsertRecord(CATEGORY category) {
        int record = 0;
        try {
            String userId =  databaseReference.push().getKey();
            category.setId(userId);
            databaseReference.child(userId).setValue(category);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public void UpdateRecord(String userId,CATEGORY category) {
        try {
            category.setId(userId);
            databaseReference.child(userId).setValue(category);
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }

    public void DeleteRecord(String userId) {
        try {
            databaseReference.child(userId).removeValue();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    ArrayList<CATEGORY> beanList=new ArrayList<>();
    public ArrayList<CATEGORY> getCategoryRecords(String types){
        beanList.clear();
        try {
          //  mydb = managerDB.getDatabaseInit();
           // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
          //  Cursor allrows = mydb.rawQuery(sqlQuery, null);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()){
                        DataSnapshot object = dataSnapshotIterator.next();
                        CATEGORY users = object.getValue(CATEGORY.class);
                        if(users!=null){
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

    public ArrayList<CATEGORY> getCategoryRecords(){
        beanList.clear();
        try {
            //  mydb = managerDB.getDatabaseInit();
            // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
            //  Cursor allrows = mydb.rawQuery(sqlQuery, null);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()){
                        DataSnapshot object = dataSnapshotIterator.next();
                        CATEGORY users = object.getValue(CATEGORY.class);
                        if(users!=null){
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

    public ArrayList<CATEGORY> getCategoryRecordsExpenseOnly(){
      //  ArrayList<CATEGORY> beanList=new ArrayList<>();
      //  try {
           // mydb = managerDB.getDatabaseInit();
            //String sqlQuery = "SELECT * FROM " +Utilities.category_tbl+" where "+Utilities.category_type+"='Expense'";
        beanList.clear();
        try {
            //  mydb = managerDB.getDatabaseInit();
            // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
            //  Cursor allrows = mydb.rawQuery(sqlQuery, null);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    while (dataSnapshotIterator.hasNext()){
                        DataSnapshot object = dataSnapshotIterator.next();
                        CATEGORY users = object.getValue(CATEGORY.class);
                        if(users!=null){
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
    CATEGORY bean=null;
    public CATEGORY getCategoryRecordSingle(String userId){


        try {
            //  mydb = managerDB.getDatabaseInit();
            // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
            //  Cursor allrows = mydb.rawQuery(sqlQuery, null);
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bean = dataSnapshot.getValue(CATEGORY.class);
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

    public boolean isCategoryExist(String name){

        return true;
    }

    public boolean isCategoryExist(String id,String name){

        return true;
    }

}
