package com.hrptech.expensemanager.db;

import android.app.Activity;
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
import com.hrptech.expensemanager.ui.category.CategoryActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CategoryDB {
    CategoryDB categoryDB;
    static GeneralDB generalDB;
    Activity myActivity;

    int record = 0;

    DatabaseReference ref = null;
    FirebaseDatabase database =null;
    DatabaseReference checkCatExist = null;
    DatabaseReference databaseReference = null;
    public CategoryDB(Activity myActivity){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/CATEGORY");
        this.myActivity = myActivity;


    }



    public static  ArrayList<CATEGORY> getCatNameList(){

        Utilities.catNameList.clear();
        DatabaseReference checkCatExist = FirebaseDatabase.getInstance().getReference("EXPENSEMANAGER/CATEGORY");
        checkCatExist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    String name = map.get("name").toString();
                    String type = map.get("type").toString();
                    String id = map.get("id").toString();
                    CATEGORY beans1 = new CATEGORY();
                    beans1.setName(name);
                    beans1.setType(type);
                    beans1.setId(id);
                    Utilities.catNameList.add(beans1);

//                    if(!GeneralDB.checkCatExistInLocalDB(id,name,type)){
//                        GeneralDB.InsertRecord("Category",new String[]{id, name, type});
//                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return Utilities.catNameList;
    }



    public boolean isNameExist(String name,String type){
        for(int index=0; index<Utilities.catNameList.size(); index++){
            CATEGORY beans = Utilities.catNameList.get(index);
            String name_ = beans.getName();
            String type_ = beans.getType();
            if(name.equalsIgnoreCase(name_) && type.equalsIgnoreCase(type_)){
                return true;
            }
        }
        return false;
    }

    public int InsertRecord(CATEGORY category) {

        record = 0;
        String enteredCatName = category.getName();
        String enteredType = category.getType();
        try{
            if(!isNameExist(enteredCatName,enteredType)) {
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

    public int UpdateRecord(String userId,CATEGORY category) {
        record = 0;
        try {
            ref = database.getReference("EXPENSEMANAGER/CATEGORY");
            ref.child(userId).child("name").setValue(category.getName());
            ref.child(userId).child("type").setValue(category.getType());
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
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

    public ArrayList<CATEGORY> getCategoryRecords(String types){
        Utilities.catNameList.clear();
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
                                Utilities.catNameList.add(users);
                            }
                        }
                        if(CategoryActivity.getCategoryFragment()!=null){
                            CategoryActivity.getCategoryFragment().RefreshRecord();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




         /*   DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("EXPENSEMANAGER/CATEGORY").orderByChild("type").equalTo(types);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"

                          *//*  CATEGORY users = issue.getValue(CATEGORY.class);
                            if(users!=null){
                                beanList.add(users);
                            }*//*

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utilities.catNameList;
    }

    public boolean isCategoryExist(String name){
        return true;
    }

    public boolean isCategoryExist(String id,String name){
        return true;
    }

    public CATEGORY getCategoryRecordSingle(String id) {
        for(int index = 0; index < Utilities.catNameList.size(); index++){
            CATEGORY beans = Utilities.catNameList.get(index);
            String token = beans.getId();
            if(token.equalsIgnoreCase(id)){
                return beans;
            }
        }
        return  null;
    }
}
