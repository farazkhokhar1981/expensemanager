package com.hrptech.expensemanager.localdb.db;



public class UtilitiesLocalDb {
    public static boolean isLoadAdsWhenOpened = true;
    public static String seletedSortedTxt = "";
    public static String DBNAME="EXPENSEMANAGER.DB";

    public static String category_tbl = "CATEGORY_TBL";
    public static String category_id = "CATEGORY_ID";
    public static String category_type = "CATEGORY_TYPE";
    public static String category_name= "CATEGORY_NAME";

    public static String budget_tbl = "BUDGET_TBL";
    public static String budget_id = "BUDGET_ID";
    public static String budegt_category_id = "BUDGET_CATEGORY_ID";
    public static String budegt_category_name = "BUDGET_CATEGORY_NAME";
    public static String budegt_category_type = "BUDGET_CATEGORY_TYPE";
    public static String budegt_year = "BUDGET_YEAR";
    public static String budegt_month = "BUDGET_MONTH";
    public static String budegt_amount = "BUDGET_AMOUNT";

    public static String transaction_tbl = "TRANSACTION_TBL";
    public static String transaction_id = "TRANSACTION_ID";
    public static String transaction_category_id = "TRANSACTION_CATEGORY_ID";
    public static String transaction_name = "TRANSACTION_NAME";
    public static String transaction_date = "TRANSACTION_DATE";
    public static String transaction_month = "TRANSACTION_MONTH";
    public static String transaction_opening = "TRANSACTION_OPENING";
    public static String transaction_type = "TRANSACTION_TYPE";
    public static String transaction_description = "TRANSACTION_DESCRIPTION";
    public static String transaction_income = "TRANSACTION_INCOME";
    public static String transaction_expense = "TRANSACTION_EXPENSE";
    public static String transaction_balance = "TRANSACTION_BALANCE";
    public static String transaction_cash_type = "TRANSACTION_CASH_TYPE";




    public static String setting_tbl="SETTING_TBL";
    public static String setting_id="SETTING_ID";
    public static String setting_currency="SETTING_CURRENCY";
    public static String setting_language="SETTING_LANGUAGE";
    public static String setting_date="SETTING_DATE";
    public static String setting_companyid="SETTING_COMPANYID";


    public static String company_tbl="COMPANY_TBL";
    public static String company_id="COMPANY_ID";
    public static String company_name="COMPANY_NAME";
    public static String company_address="COMPANY_ADDRESS";
    public static String company_contact="COMPANY_CONTACT";
    public static String company_email="COMPANY_email";


    public static String lock_tbl="LOCK_TBL";
    public static String lock_password="LOCK_PASSWORD";
    public static String lock_question="LOCK_QUESTION";
    public static String lock_answer="LOCK_ANSWER";
    public static String lock_password_hint="LOCK_PASSWORD_HINT";
    public static String lock_contact="LOCK_CONTACT";


    public static String modification_tbl="MODIFICATION_TBL";
    public static String modification_id="MODIFICATION_ID";
    public static String modification_modi_tr_id="MODIFICATION_MODI_TR_ID"; // kis date key kam ko chara heay
    public static String modification_modi_date="MODIFICATION_MODI_DATE"; // kis date key kam ko chara heay
    public static String modification_modi_tr_date="MODIFICATION_MODI_TR_DATE"; // transaction date
    public static String modification_date="MODIFICATION_DATE";
    public static String modification_time="MODIFICATION_TIME";
    public static String modification_customer_id="MODIFICATION_CUSTOMER_ID";
    public static String modification_companyname_id="MODIFICATION_COMPANY_ID";
    public static String modification_description="MODIFICATION_DESCRIPTION"; // change
    public static String modification_last_amount="MODIFICATION_LAST_AMOUNT"; // change
    public static String modification_last_payment_type="MODIFICATION_PAYMENT_TYPE"; // change
    public static String modification_debit="MODIFICATION_DEBIT"; // change
    public static String modification_credit="MODIFICATION_CREDIT"; // change

}
