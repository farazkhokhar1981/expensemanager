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
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransactionDB {

    FirebaseDatabase database =null;
    DatabaseReference databaseReference = null;
    Activity myActivity;
    public TransactionDB(Activity myActivity){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("EXPENSEMANAGER/TRANSACTION");
        this.myActivity = myActivity;
    }


    public int InsertRecord(TransactionBeans transactionBeans) {
        int record = 0;
        try {
            String userId =  databaseReference.push().getKey();
            transactionBeans.setId(userId);
            databaseReference.child(userId).setValue(transactionBeans);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in inserting into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public int UpdateRecord(String id,TransactionBeans transactionBeans) {
        int record = 0;
        try {
            transactionBeans.setId(id);
            DatabaseReference dbUpdateRecord = null;
            dbUpdateRecord.child("EXPENSEMANAGER/CATEGORY").child(id).setValue(transactionBeans);
            record = 1;
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in updating into table", Toast.LENGTH_LONG);
        }
        return record;
    }

    public void DeleteRecord(String id) {
        try {
            databaseReference.child(id).removeValue();
        } catch (Exception e) {
            Toast.makeText(myActivity, "Error in deleting from table", Toast.LENGTH_LONG);
        }
    }


    ArrayList<CATEGORY> categoryBeans = new ArrayList<>();

    TransactionBeans bean=null;
    public TransactionBeans getTransactionRecordSingle(String id){
        try {
            //  mydb = managerDB.getDatabaseInit();
            // String sqlQuery = "SELECT * FROM " + Utilities.category_tbl+" where "+Utilities.category_type+"='"+types+"'";
            //  Cursor allrows = mydb.rawQuery(sqlQuery, null);

            categoryBeans = CategoryDB.getCategoryRecords();



            for(int index = 0; index <= categoryBeans.size(); index++){
                if(Utilities.catNameList.get(index).getId().equalsIgnoreCase(id)){
                    bean.setId(Utilities.catNameList.get(index).getId());
                    bean.setName(Utilities.catNameList.get(index).getName());
                    bean.setType(Utilities.catNameList.get(index).getType());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bean;
    }


    public TransactionBeans getTransactionByDate(String date){
//        TransactionBeans bean=null;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+" where "+Utilities.transaction_date+"='"+date+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                bean=new TransactionBeans();
//                double income=allrows.getDouble(allrows.getColumnIndex("income"));
//                double expense=allrows.getDouble(allrows.getColumnIndex("expense"));
//                double balance=income-expense;
//                bean.setIncome(new DecimalFormat("0.00").format(income));
//                bean.setExpense(new DecimalFormat("0.00").format(expense));
//                bean.setBalance(new DecimalFormat("0.00").format(balance));
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return bean;
    }

    public double getTransactionByOpeningBalance(String date){
        double openingBalance=0;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+" where "+Utilities.transaction_date+"<'"+date+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                double income=allrows.getDouble(allrows.getColumnIndex("income"));
//                double expense=allrows.getDouble(allrows.getColumnIndex("expense"));
//                double balance=income-expense;
//                openingBalance = balance;
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return openingBalance;
    }


    public ArrayList<TransactionBeans> getTransactionRecords(){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String type=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String description=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                String balance=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                bean.setCid(cid);
//                bean.setType(type);
//                bean.setDescription(description);
//                bean.setDate(date);
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance(balance);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsDate(String dates){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+"='"+dates+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String id=allrows.getString(allrows.getColumnIndex(Utilities.transaction_id));
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String catType=allrows.getString(allrows.getColumnIndex(Utilities.category_type));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String description=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                bean.setId(id);
//                bean.setCid(cid);
//                bean.setName(catName);
//                bean.setType(catType);
//                bean.setDescription(description);
//                bean.setDate(date);
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance(amount);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsDate(String dates,String type){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+"='"+dates+"' and catbl."+Utilities.category_type+"='"+type+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String id=allrows.getString(allrows.getColumnIndex(Utilities.transaction_id));
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String catType=allrows.getString(allrows.getColumnIndex(Utilities.category_type));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String description=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                bean.setId(id);
//                bean.setCid(cid);
//                bean.setName(catName);
//                bean.setType(catType);
//                bean.setDescription(description);
//                bean.setDate(date);
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance(amount);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsToDates(String fromDate,String toDate){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+" between '"+fromDate+"' and '"+toDate+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                bean.setCid(cid);
//                bean.setDescription(catName);
//                bean.setDate(date);
//                bean.setOpening("");
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance("");
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsToDatesSummary(String fromDate,String toDate){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT "+Utilities.transaction_cid+","+Utilities.category_name+","+Utilities.transaction_date+","+Utilities.category_name+",sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+" between '"+fromDate+"' and '"+toDate+"' group by "+Utilities.transaction_date+","+Utilities.category_id+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String income=allrows.getString(allrows.getColumnIndex("income"));
//                String expense=allrows.getString(allrows.getColumnIndex("expense"));
//                bean.setCid(cid);
//                bean.setDescription(catName);
//                bean.setDate(date);
//                bean.setOpening("");
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance("");
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }


    public ArrayList<TransactionBeans> getTransactionRecordsToDatesCategoryDetail(String fromDate,String toDate,String categoryId){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+" between '"+fromDate+"' and '"+toDate+"' and "+Utilities.category_id+"='"+categoryId+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String balance=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                bean.setDescription(catName);
//                bean.setDate(date);
//                bean.setBalance(balance);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsToDatesCategorySummary(String fromDate,String toDate,String categoryId){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT "+Utilities.transaction_cid+","+Utilities.category_name+","+Utilities.transaction_date+","+Utilities.category_name+",sum("+Utilities.transaction_balance+") as balance FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+" between '"+fromDate+"' and '"+toDate+"' and catbl."+Utilities.category_id+"='"+categoryId+"' group by trtbl."+Utilities.transaction_date+",catbl."+Utilities.category_id+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String balance=allrows.getString(allrows.getColumnIndex("balance"));
//                bean.setCid(cid);
//                bean.setDescription(catName);
//                bean.setDate(date);
//                bean.setBalance(balance);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public BudgetBeans getBudgetRecordSingle(String id){
        BudgetBeans bean=null;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.budget_tbl+" as budtbl,"+Utilities.category_tbl+" as catbl where budtbl."+Utilities.budget_cid+"=catbl."+Utilities.category_id+" and budtbl."+Utilities.budget_id+"='"+id+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                bean=new BudgetBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.budget_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String catType=allrows.getString(allrows.getColumnIndex(Utilities.category_type));
//                String year=allrows.getString(allrows.getColumnIndex(Utilities.budget_year));
//                String month=allrows.getString(allrows.getColumnIndex(Utilities.budget_month));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.budget_amount));
//                bean.setId(id);
//                bean.setCid(cid);
//                bean.setCat_name(catName);
//                bean.setCat_type(catType);
//                bean.setYear(year);
//                bean.setMonth(month);
//                bean.setAmount(amount);
//
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return bean;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsMonth(String month,String year){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT "+Utilities.transaction_date+",sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+","+Utilities.category_tbl+" where "+Utilities.transaction_cid+"="+Utilities.category_id+" and strftime('%m',"+Utilities.transaction_date+")='"+month+"' and strftime('%Y',"+Utilities.transaction_date+")='"+year+"' group by "+Utilities.transaction_date+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                double income=allrows.getDouble(allrows.getColumnIndex("income"));
//                double expense=allrows.getDouble(allrows.getColumnIndex("expense"));
//                double balance=income-expense;
//                bean.setDate(date);
//                bean.setIncome(new DecimalFormat("0.00").format(income));
//                bean.setExpense(new DecimalFormat("0.00").format(expense));
//                bean.setBalance(new DecimalFormat("0.00").format(balance));
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }


    public ArrayList<TransactionBeans> getBudgetRecordsMonth(String month,String year){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.budget_tbl+","+Utilities.category_tbl+" where "+Utilities.budget_cid+"="+Utilities.category_id+" and "+Utilities.budget_month+"='"+month+"' and "+Utilities.budget_year+"='"+year+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.category_id));
//                String name=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.budget_amount));
//                bean.setCid(cid);
//                bean.setName(name);
//                bean.setExpense(amount);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public TransactionBeans getTransactionRecordsYear(String month,String year){
        TransactionBeans beanList=null;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT "+Utilities.transaction_date+",sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+","+Utilities.category_tbl+" where "+Utilities.transaction_cid+"="+Utilities.category_id+" and strftime('%m',"+Utilities.transaction_date+")='"+month+"' and strftime('%Y',"+Utilities.transaction_date+")='"+year+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                beanList=new TransactionBeans();
//                double income=allrows.getDouble(allrows.getColumnIndex("income"));
//                double expense=allrows.getDouble(allrows.getColumnIndex("expense"));
//                double balance=income-expense;
//                beanList.setIncome(new DecimalFormat("0.00").format(income));
//                beanList.setExpense(new DecimalFormat("0.00").format(expense));
//                beanList.setBalance(new DecimalFormat("0.00").format(balance));
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public TransactionBeans getTransactionLast(){
        TransactionBeans beanList=null;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+","+Utilities.category_tbl+" where "+Utilities.transaction_cid+"="+Utilities.category_id+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToLast()) {
//                beanList=new TransactionBeans();
//                String cId=allrows.getString(allrows.getColumnIndex(Utilities.category_id));
//                String name=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String type=allrows.getString(allrows.getColumnIndex(Utilities.category_type));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                double income=allrows.getDouble(allrows.getColumnIndex(Utilities.transaction_income));
//                double expense=allrows.getDouble(allrows.getColumnIndex(Utilities.transaction_expense));
//                beanList.setCid(cId);
//                beanList.setName(name);
//                beanList.setType(type);
//                beanList.setIncome(new DecimalFormat("0.00").format(income));
//                beanList.setExpense(new DecimalFormat("0.00").format(expense));
//                beanList.setDate(date);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public TransactionBeans getTransactionRecordsYearCategory(String month,String year,String categoryId){
        TransactionBeans beanList=null;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT "+Utilities.transaction_date+",sum("+Utilities.transaction_balance+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+","+Utilities.category_tbl+" where "+Utilities.transaction_cid+"="+Utilities.category_id+" and strftime('%m',"+Utilities.transaction_date+")='"+month+"' and strftime('%Y',"+Utilities.transaction_date+")='"+year+"' and "+Utilities.category_id+"='"+categoryId+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                beanList=new TransactionBeans();
//                double income=allrows.getDouble(allrows.getColumnIndex("income"));
//                double balance=income;
//                beanList.setBalance(new DecimalFormat("0.00").format(balance));
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public double getTransactionExpense(String year,String month,String cid){
        double openingBalance=0;
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT sum("+Utilities.transaction_income+") as income,sum("+Utilities.transaction_expense+") as expense FROM " + Utilities.transaction_tbl+" where strftime('%m',"+Utilities.transaction_date+")='"+month+"' and strftime('%Y',"+Utilities.transaction_date+")='"+year+"' and "+Utilities.transaction_cid+"='"+cid+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            if (allrows.moveToNext()) {
//                double expense=allrows.getDouble(allrows.getColumnIndex("expense"));
//                openingBalance = expense;
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return openingBalance;
    }
    public ArrayList<TransactionBeans> getTransactionRecordsDateAllRecords(String companyId,String search,String fromDate,String toDate){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "";
//            if(!fromDate.equalsIgnoreCase("Select From Date") && !toDate.equalsIgnoreCase("Select To Date")){
//                sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%' and strftime('%Y-%m-%d',trtbl."+Utilities.transaction_date+") between '"+fromDate+"' and '"+toDate+"'";
//            }else {
//                sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%'";
//            }
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String id=allrows.getString(allrows.getColumnIndex(Utilities.transaction_id));
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String description=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                String types=allrows.getString(allrows.getColumnIndex(Utilities.transaction_type));
//                //String cashType=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cashtype));
//                bean.setId(id);
//                bean.setCid(cid);
//                bean.setName(catName);
//                bean.setDescription(description);
//                bean.setDate(date);
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance(amount);
//                bean.setType(types);
//                //bean.setCashType(cashType);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }
    public ArrayList<TransactionBeans> getTransactionRecordsDateAllRecords(String companyId,String search,String fromDate,String toDate,String sortType){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "";
//            if(!fromDate.equalsIgnoreCase("Select From Date") && !toDate.equalsIgnoreCase("Select To Date")){
//                if(sortType.equalsIgnoreCase("all")){
//                    sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%' and strftime('%Y-%m-%d',trtbl."+Utilities.transaction_date+") between '"+fromDate+"' and '"+toDate+"'";
//                }else {
//                    sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%' and strftime('%Y-%m-%d',trtbl."+Utilities.transaction_date+") between '"+fromDate+"' and '"+toDate+"' and trtbl."+Utilities.transaction_type+"='"+sortType+"'";
//                }
//
//            }else {
//                if(sortType.equalsIgnoreCase("all")){
//                    sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%'";
//                }else {
//                    sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and "+Utilities.category_name+" like '%"+search+"%' and trtbl."+Utilities.transaction_type+"='"+sortType+"'";
//                }
//
//            }
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String id=allrows.getString(allrows.getColumnIndex(Utilities.transaction_id));
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.category_name));
//                String date=allrows.getString(allrows.getColumnIndex(Utilities.transaction_date));
//                String description=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                String income=allrows.getString(allrows.getColumnIndex(Utilities.transaction_income));
//                String expense=allrows.getString(allrows.getColumnIndex(Utilities.transaction_expense));
//                String amount=allrows.getString(allrows.getColumnIndex(Utilities.transaction_balance));
//                String types=allrows.getString(allrows.getColumnIndex(Utilities.transaction_type));
//                //String cashType=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cashtype));
//                bean.setId(id);
//                bean.setCid(cid);
//                bean.setName(catName);
//                bean.setDescription(description);
//                bean.setDate(date);
//                bean.setIncome(income);
//                bean.setExpense(expense);
//                bean.setBalance(amount);
//                bean.setType(types);
//                //bean.setCashType(cashType);
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

    public ArrayList<TransactionBeans> getTransactionRecordsDateAll(String dates,String tDate){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT trtbl."+Utilities.transaction_cid+" as cid,catbl."+Utilities.category_name+" as pname,sum(trtbl."+Utilities.transaction_income+"-trtbl."+Utilities.transaction_expense+") as total FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and trtbl."+Utilities.transaction_date+"<='"+dates+"' group by trtbl."+Utilities.transaction_cid+"";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            double dr=0;
//            double cr=0;
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex("cid"));
//                String name=allrows.getString(allrows.getColumnIndex("pname"));
//                String contact=allrows.getString(allrows.getColumnIndex("pcontact"));
//                double amount=allrows.getDouble(allrows.getColumnIndex("total"));
//                bean.setCid(cid);
//                bean.setName(name);
//                bean.setDescription(contact);
//
//                if(amount>0){
//                    bean.setIncome(new DecimalFormat("0.00").format(Math.abs(amount)));
//                    bean.setExpense("0.00");
//                }else {
//                    bean.setIncome("0.00");
//                    bean.setExpense(new DecimalFormat("0.00").format(Math.abs(amount)));
//                }
//
//
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }
    public ArrayList<TransactionBeans> getTransactionRecordsToDatesCategoryOpeningAndClosing(String date,String categoryId){
        ArrayList<TransactionBeans> beanList=new ArrayList<>();
//        try {
//            mydb = managerDB.getDatabaseInit();
//            String sqlQuery = "SELECT * FROM " + Utilities.transaction_tbl+" as trtbl,"+Utilities.category_tbl+" as catbl where trtbl."+Utilities.transaction_cid+"=catbl."+Utilities.category_id+" and strftime('%Y-%m-%d',trtbl."+Utilities.transaction_date+")<'"+date+"' and "+Utilities.category_id+"='"+categoryId+"'";
//            Cursor allrows = mydb.rawQuery(sqlQuery, null);
//            while (allrows.moveToNext()) {
//                TransactionBeans bean=new TransactionBeans();
//                String cid=allrows.getString(allrows.getColumnIndex(Utilities.transaction_cid));
//                String catName=allrows.getString(allrows.getColumnIndex(Utilities.transaction_description));
//                double income=allrows.getDouble(allrows.getColumnIndex(Utilities.transaction_income));
//                double expense=allrows.getDouble(allrows.getColumnIndex(Utilities.transaction_expense));
//                bean.setDescription(catName);
//                bean.setDate(date);
//                bean.setIncome(income+"");
//                bean.setExpense(expense+"");
//                bean.setBalance((income-expense)+"");
//                beanList.add(bean);
//            }
//            mydb.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return beanList;
    }

}
