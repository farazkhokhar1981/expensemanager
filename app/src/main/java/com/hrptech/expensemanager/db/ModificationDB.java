package com.hrptech.expensemanager.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.hrptech.expensemanager.beans.ModificationBeans;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.ArrayList;

public class ModificationDB {

    DataBaseManager managerDB = null;
    private SQLiteDatabase mydb;
    Activity myActivity;
    public ModificationDB(Activity myActivity){
        managerDB = new DataBaseManager(myActivity);
        mydb = managerDB.getDatabaseInit();
        this.myActivity = myActivity;
        createTable();
        //Utilities.AlterNotebook(mydb,myActivity,Utilities.parties_company_id,Utilities.parties_tbl,"text",Utilities.parties_id,false);

    }

    public void createTable() {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("CREATE TABLE IF  NOT EXISTS " + Utilities.modification_tbl + " " +
                    "("+Utilities.modification_id+" integer primary key AUTOINCREMENT,"+Utilities.modification_modi_tr_id+" text,"+Utilities.modification_modi_date+" text,"+Utilities.modification_modi_tr_date+" text," +
                    " "+Utilities.modification_date+" text,"+Utilities.modification_time+" text,"+Utilities.modification_customer_id+" text,"+Utilities.modification_companyname_id+" text,"
                    +Utilities.modification_description+" text,"+Utilities.modification_last_amount+" text,"+Utilities.modification_last_payment_type+" text,"+Utilities.modification_debit+" text,"+Utilities.modification_credit+" text)");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    /*
        public static String modification_tbl="MODIFICATION_TBL";
       public static String modification_id="MODIFICATION_ID";
       public static String modification_modi_tr_id="MODIFICATION_MODI_TR_ID"; // kis date key kam ko chara heay
       public static String modification_modi_date="MODIFICATION_MODI_Date"; // kis date key kam ko chara heay
       public static String modification_modi_tr_date="MODIFICATION_MODI_RESONE_DATE"; // transaction date
       public static String modification_date="MODIFICATION_DATE";
       public static String modification_time="MODIFICATION_TIME";
       public static String modification_customer_id="MODIFICATION_CUSTOMER_ID";
       public static String modification_companyname_id="MODIFICATION_COMPANY_ID";
       public static String modification_description="MODIFICATION_DESCRIPTION"; // change
       public static String modification_last_amount="MODIFICATION_LAST_AMOUNT"; // change
       public static String modification_last_payment_type="MODIFICATION_PAYMENT_TYPE"; // change
       public static String modification_debit="MODIFICATION_DEBIT"; // change
       public static String modification_credit="MODIFICATION_CREDIT"; // change

        */
    public int InsertRecord(String modi_tr_id,String modi_date,String modi_tr_date,String date,String time,
                            String customer_id,String companyname_id,String description,String last_amount,
                            String last_payment_type,String debit,String credit) {
        int record = 0;
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("INSERT INTO " + Utilities.modification_tbl + "("+Utilities.modification_modi_tr_id+","+Utilities.modification_modi_date+","+Utilities.modification_modi_tr_date+","+Utilities.modification_date+","+Utilities.modification_time+"" +
                    ","+Utilities.modification_customer_id+","+Utilities.modification_companyname_id+","+Utilities.modification_description+","+Utilities.modification_last_amount+","+Utilities.modification_last_payment_type+","+Utilities.modification_debit+","+Utilities.modification_credit+")" +
                    "VALUES('"+modi_tr_id+"','"+modi_date+"','"+modi_tr_date+"','"+date+"','"+time+"'," +
                    "'"+customer_id+"','"+companyname_id+"','"+description+"','"+last_amount+"','"+last_payment_type+"','"+debit+"','"+credit+"')");
            mydb.close();
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }



    public void DeleteRecord(String id) {
        try {
            mydb = managerDB.getDatabaseInit();
            mydb.execSQL("DELETE FROM " + Utilities.modification_tbl + " where "+Utilities.modification_id+"='"+id+"'");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }

    public ArrayList<ModificationBeans> getModificationBeans(){
        ArrayList<ModificationBeans> beanList=new ArrayList<>();
        try {
            mydb = managerDB.getDatabaseInit();
            Cursor allrows = mydb.rawQuery("SELECT * FROM " + Utilities.modification_tbl, null);
            while (allrows.moveToNext()) {
                ModificationBeans bean=new ModificationBeans();

                String modification_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_id));
                String modification_modi_tr_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_tr_id));
                String modification_modi_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_date));
                String modification_modi_tr_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_tr_date));
                String modification_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_date));
                String modification_time=allrows.getString(allrows.getColumnIndex(Utilities.modification_time));
                String modification_customer_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_customer_id));
                String modification_companyname_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_companyname_id));
                String modification_description=allrows.getString(allrows.getColumnIndex(Utilities.modification_description));
                String modification_last_amount=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_amount));
                String modification_last_payment_type=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_payment_type));
                String modification_debit=allrows.getString(allrows.getColumnIndex(Utilities.modification_debit));
                String modification_credit=allrows.getString(allrows.getColumnIndex(Utilities.modification_credit));

                bean.setModification_id(modification_id);
                bean.setModification_modi_tr_id(modification_modi_tr_id);
                bean.setModification_modi_date(modification_modi_date);
                bean.setModification_modi_tr_date(modification_modi_tr_date);
                bean.setModification_date(modification_date);
                bean.setModification_time(modification_time);
                bean.setModification_customer_id(modification_customer_id);
                bean.setModification_companyname_id(modification_companyname_id);
                bean.setModification_description(modification_description);
                bean.setModification_last_amount(modification_last_amount);
                bean.setModification_last_payment_type(modification_last_payment_type);
                bean.setModification_debit(modification_debit);
                bean.setModification_credit(modification_credit);
                beanList.add(bean);
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }


    public ModificationBeans getModificationBeans(String id){
        ModificationBeans bean=null;
        try {
            mydb = managerDB.getDatabaseInit();
            Cursor allrows = mydb.rawQuery("SELECT * FROM " +Utilities.modification_tbl+" where "+Utilities.modification_id+"='"+id+"'", null);
            if (allrows.moveToNext()) {
                bean=new ModificationBeans();
                String modification_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_id));
                String modification_modi_tr_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_tr_id));
                String modification_modi_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_date));
                String modification_modi_tr_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_tr_date));
                String modification_date=allrows.getString(allrows.getColumnIndex(Utilities.modification_date));
                String modification_time=allrows.getString(allrows.getColumnIndex(Utilities.modification_time));
                String modification_customer_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_customer_id));
                String modification_companyname_id=allrows.getString(allrows.getColumnIndex(Utilities.modification_companyname_id));
                String modification_description=allrows.getString(allrows.getColumnIndex(Utilities.modification_description));
                String modification_last_amount=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_amount));
                String modification_last_payment_type=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_payment_type));
                String modification_debit=allrows.getString(allrows.getColumnIndex(Utilities.modification_debit));
                String modification_credit=allrows.getString(allrows.getColumnIndex(Utilities.modification_credit));

                bean.setModification_id(modification_id);
                bean.setModification_modi_tr_id(modification_modi_tr_id);
                bean.setModification_modi_date(modification_modi_date);
                bean.setModification_modi_tr_date(modification_modi_tr_date);
                bean.setModification_date(modification_date);
                bean.setModification_time(modification_time);
                bean.setModification_customer_id(modification_customer_id);
                bean.setModification_companyname_id(modification_companyname_id);
                bean.setModification_description(modification_description);
                bean.setModification_last_amount(modification_last_amount);
                bean.setModification_last_payment_type(modification_last_payment_type);
                bean.setModification_debit(modification_debit);
                bean.setModification_credit(modification_credit);
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public boolean isModifTransactionExist(String transaction_id){
        try {
            mydb = managerDB.getDatabaseInit();
            Cursor allrows = mydb.rawQuery("SELECT * FROM " +Utilities.modification_tbl+" where "+Utilities.modification_modi_tr_id+"='"+transaction_id+"'", null);
            if (allrows.moveToNext()) {
                    return true;
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsDateAllRecords(String companyId, String search, String fromDate, String toDate){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
        try {
            mydb = managerDB.getDatabaseInit();
            String sqlQuery = "";
            if(!fromDate.equalsIgnoreCase("Select From Date") && !toDate.equalsIgnoreCase("Select To Date")){
                sqlQuery = "SELECT * FROM " + Utilities.modification_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.modification_customer_id+"=catbl."+Utilities.category_id+" and "+Utilities.modification_companyname_id+"='"+companyId+"' and "+Utilities.category_name+" like '%"+search+"%' and strftime('%Y-%m-%d',trtbl."+Utilities.modification_modi_date+") between '"+fromDate+"' and '"+toDate+"'";
            }else {
                sqlQuery = "SELECT * FROM " + Utilities.modification_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.modification_customer_id+"=catbl."+Utilities.category_id+" and "+Utilities.modification_companyname_id+"='"+companyId+"' and "+Utilities.category_name+" like '%"+search+"%'";
            }
            Cursor allrows = mydb.rawQuery(sqlQuery, null);
            while (allrows.moveToNext()) {
                TransactionBeans bean=new TransactionBeans();
                String id=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_tr_id));
                String cid=allrows.getString(allrows.getColumnIndex(Utilities.modification_customer_id));
                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
                String date=allrows.getString(allrows.getColumnIndex(Utilities.modification_modi_date));
                String description=allrows.getString(allrows.getColumnIndex(Utilities.modification_description));
                String income=allrows.getString(allrows.getColumnIndex(Utilities.modification_debit));
                String expense=allrows.getString(allrows.getColumnIndex(Utilities.modification_credit));
                String amount=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_amount));
                String types=allrows.getString(allrows.getColumnIndex(Utilities.modification_last_payment_type));

                bean.setId(id);
                bean.setCid(cid);
                bean.setName(catName);
                bean.setDescription(description);
                bean.setDate(date);
                bean.setIncome(income);
                bean.setExpense(expense);
                bean.setBalance(amount);
                bean.setType(types);
                beanList.add(bean);
            }
            mydb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanList;
    }

}
