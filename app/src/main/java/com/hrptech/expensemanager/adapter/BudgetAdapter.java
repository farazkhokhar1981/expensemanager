package com.hrptech.expensemanager.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {
    private Activity activity;
    private List<TransactionBeans> items;
    String search;
    public BudgetAdapter(Activity activity, List<TransactionBeans> items, String search) {
        this.activity = activity;
        for(int index=0; index<items.size(); index++){
            TransactionBeans beans = items.get(index);
            if((index+1)%4==0){
                beans.setMonth("ads");
            }else {
                beans.setMonth("");
            }
        }
        this.items = items;
        this.search = search;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailytransaction_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        TransactionBeans beans = items.get(position);
        String replacedWith = "<font color='red'>" + search + "</font>";
        String name = beans.getName();
        String modifiedString = name.replaceAll(search,replacedWith);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.txtName.setText(Html.fromHtml(modifiedString, Html.FROM_HTML_MODE_COMPACT));
        } else {
            viewHolder.txtName.setText(Html.fromHtml(modifiedString));
        }
        viewHolder.txtDescrption.setText(beans.getDescription());
        viewHolder.textDate.setText(beans.getDate());
        viewHolder.debit_txt.setText(beans.getIncome());
        viewHolder.credit_txt.setText(beans.getExpense());
        viewHolder.edit_btn.setTag(beans);

        viewHolder.edit_btn.setClickable(true);
        viewHolder.edit_image.setVisibility(View.VISIBLE);
        viewHolder.adView.setVisibility(View.GONE);
        viewHolder.adView_lay.setVisibility(View.GONE);

        String ads = beans.getMonth();
        if(ads.equalsIgnoreCase("ads")){
            viewHolder.adView.setVisibility(View.VISIBLE);
            viewHolder.adView_lay.setVisibility(View.VISIBLE);
            Utilities.LoadBanner(viewHolder.adView);
        }

        viewHolder.edit_btn.setTag(beans);
        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionBeans beans = (TransactionBeans)v.getTag();
                LoadTransactionFileFromHere(beans.getId());
            }
        });
        viewHolder.delete_btn.setTag(beans);
        viewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionBeans beans = (TransactionBeans)v.getTag();
                if(beans.getType().equalsIgnoreCase("income")){
                    Utilities.showDialogClose(activity,"delete","Income",beans.getId(),beans.getCid());
                }else if(beans.getType().equalsIgnoreCase("expense")){
                    Utilities.showDialogClose(activity,"delete","Expense",beans.getId(),beans.getCid());
                }
            }
        });
        viewHolder.viewRecord_btn.setTag(beans);
        viewHolder.viewRecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionBeans beans = (TransactionBeans)v.getTag();
                Utilities.showDialogCustomerTransactionVoucherDetail(beans,activity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtDescrption;
        private TextView textDate;
        private TextView debit_txt;
        private TextView credit_txt;
        private LinearLayout edit_btn;
        private LinearLayout delete_btn;
        private ImageView edit_image;
        private LinearLayout viewRecord_btn;
        private LinearLayout adView_lay;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.name_txt);
            txtDescrption = (TextView)view.findViewById(R.id.descr_txt);
            textDate = (TextView)view.findViewById(R.id.date_txt);
            debit_txt = (TextView)view.findViewById(R.id.debit_txt);
            credit_txt = (TextView)view.findViewById(R.id.credit_txt);
            edit_btn = (LinearLayout) view.findViewById(R.id.edit_btn);
            delete_btn = (LinearLayout) view.findViewById(R.id.delete_btn);
            edit_image = (ImageView) view.findViewById(R.id.edit_img);
            viewRecord_btn = (LinearLayout) view.findViewById(R.id.viewRecord_btn);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);

        }

    }
    TransactionDB transactionDB;
    public void LoadTransactionFileFromHere(String id){
        transactionDB = new TransactionDB(activity);
        TransactionBeans transactionBeans = transactionDB.getTransactionRecordSingle(id);
        Intent intent = new Intent(activity, TransactionIncomeActivity.class);
        intent.putExtra("trans",transactionBeans.getType());
        intent.putExtra("transactionId",id);
        intent.putExtra("className","dailyTran");
        activity.startActivity(intent);
        activity.finish();
    }
}
