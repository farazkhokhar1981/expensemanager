package com.hrptech.expensemanager.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hrptech.expensemanager.beans.LockBeans;
import com.hrptech.expensemanager.utility.Utilities;

public class LockDB {

    DataBaseManager managerDB = null;
    private SQLiteDatabase mydb;
    Activity myActivity;
    public LockDB(Activity myActivity){
        managerDB = new DataBaseManager(myActivity);
        mydb = managerDB.getDatabaseInit();
        this.myActivity = myActivity;
        createTable();
    }
    public void createTable() {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("CREATE TABLE IF  NOT EXISTS " + Utilities.lock_tbl + " ("+Utilities.lock_password+" text,"+Utilities.lock_question+" text" +
                    ","+Utilities.lock_answer+" text,"+Utilities.lock_password_hint+" text,"+Utilities.lock_contact+" text)");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    public void UpdatePassword(String password) {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("UPDATE " + Utilities.lock_tbl + " set "+Utilities.lock_password+"='"+password+"'");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }

    public int InsertRecord(String password, String question, String answer, String passwordHint, String lockContact) {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("INSERT INTO " + Utilities.lock_tbl + "("+Utilities.lock_password+","+Utilities.lock_question+","+Utilities.lock_answer+","+Utilities.lock_password_hint+","+Utilities.lock_contact+")VALUES('"+password+"'," +
                    "'"+question+"','"+answer+"','"+passwordHint+"','"+lockContact+"')");
            mydb.close();
            return 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return 0;
    }


    public void DeleteRecord() {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("DELETE FROM " + Utilities.lock_tbl);
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }



    public LockBeans getLock(){
        LockBeans bean=null;
        try {
            mydb = managerDB.getDatabaseInit();
            Cursor allrows = mydb.rawQuery("SELECT * FROM " + Utilities.lock_tbl, null);
            if (allrows.moveToNext()) {
                bean=new LockBeans();
                String password=allrows.getString(allrows.getColumnIndex(Utilities.lock_password));
                String question=allrows.getString(allrows.getColumnIndex(Utilities.lock_question));
                String answer=allrows.getString(allrows.getColumnIndex(Utilities.lock_answer));
                String passwordHint=allrows.getString(allrows.getColumnIndex(Utilities.lock_password_hint));
                String contact=allrows.getString(allrows.getColumnIndex(Utilities.lock_contact));
                bean.setLock_password(password);
                bean.setLock_question(question);
                bean.setLock_password_hint(passwordHint);
                bean.setLock_contact(contact);
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


    public LockBeans loginAuthentication(String password){
        LockBeans bean=null;
        try {
            mydb = managerDB.getDatabaseInit();
            Cursor allrows = mydb.rawQuery("SELECT * FROM " + Utilities.lock_tbl+" where "+Utilities.lock_password+"='"+password+"'", null);
            if (allrows.moveToNext()) {
                bean=new LockBeans();
                String passwords=allrows.getString(allrows.getColumnIndex(Utilities.lock_password));
                String question=allrows.getString(allrows.getColumnIndex(Utilities.lock_question));
                String answer=allrows.getString(allrows.getColumnIndex(Utilities.lock_answer));
                String passwordHint=allrows.getString(allrows.getColumnIndex(Utilities.lock_password_hint));
                String contact=allrows.getString(allrows.getColumnIndex(Utilities.lock_contact));
                bean.setLock_password(passwords);
                bean.setLock_question(question);
                bean.setLock_password_hint(passwordHint);
                bean.setLock_contact(contact);
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

}
