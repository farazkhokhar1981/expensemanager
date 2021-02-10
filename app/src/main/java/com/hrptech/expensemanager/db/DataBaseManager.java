package com.hrptech.expensemanager.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hrptech.expensemanager.utility.Utilities;

/**
 * Created by faraz on 5/5/2016.
 */
public class DataBaseManager {
    private SQLiteDatabase mydb;




    private Activity myActivity;
    public DataBaseManager(Activity myActivity){
        this.myActivity=myActivity;
     //   AlterNotebook(notebook_tbl_description_style,"text");


    }

    public SQLiteDatabase getDatabaseInit(){
        try {
            mydb = myActivity.openOrCreateDatabase(Utilities.DBNAME, Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in creating table", Toast.LENGTH_LONG);
        }
        return mydb;
    }


    public void AlterNotebook(String notebook_tbl,String columnName,String type) {
        try {
            mydb = myActivity.openOrCreateDatabase(Utilities.DBNAME, Context.MODE_PRIVATE, null);
            if(!existsColumnInTable(mydb,notebook_tbl,columnName)) {
                mydb.execSQL("alter table " + notebook_tbl + " add column " + columnName + " " + type + "");
                mydb.close();
            }
        } catch (Exception e) {

        }
    }

    private boolean existsColumnInTable(SQLiteDatabase inDatabase, String inTable, String columnToCheck) {
        Cursor mCursor = null;
        try {
            mCursor = inDatabase.rawQuery("SELECT * FROM " + inTable + " LIMIT 0", null);
            if (mCursor.getColumnIndex(columnToCheck) != -1)
                return true;
            else
                return false;

        } catch (Exception Exp) {
            return false;
        } finally {
            if (mCursor != null) mCursor.close();
        }
    }
}
