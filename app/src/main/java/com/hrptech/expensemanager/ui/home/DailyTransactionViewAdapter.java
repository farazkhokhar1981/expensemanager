package com.hrptech.expensemanager.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class DailyTransactionViewAdapter extends RecyclerView.Adapter<DailyTransactionViewAdapter.ViewHolder> {
    private Activity activity;
    private List<TransactionBeans> items;

    public DailyTransactionViewAdapter(Activity activity, List<TransactionBeans> items) {
        for(int index=0; index<items.size(); index++){
            TransactionBeans beans = items.get(index);
            if((index+1)%4==0){
                beans.setAds("ads");
            }else {
                beans.setAds("");
            }
        }
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dailytransaction_layout, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TransactionBeans beans = items.get(position);
        String description = beans.getDescription();
        if(description.equalsIgnoreCase("")){
            description = "----";
        }
        viewHolder.txtDescription.setText(description);
        viewHolder.txtName.setText(beans.getName());
        String type = beans.getType();
        viewHolder.textType.setText(type);
        if(type.equalsIgnoreCase("Income")){
            viewHolder.textType.setTextColor(ContextCompat.getColor(activity, R.color.colorGreens));
        }else if(type.equalsIgnoreCase("Expense")){
            viewHolder.textType.setTextColor(ContextCompat.getColor(activity, R.color.colorRed));
        }

        viewHolder.textIncome.setText(beans.getIncome());
        viewHolder.textExpense.setText(beans.getExpense());
        viewHolder.dailyTrans_btn.setTag(beans);
        viewHolder.dailyTrans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            TransactionBeans beans = (TransactionBeans)view.getTag();
            String id = beans.getId();
            String type = beans.getType();
                LoadActivity(new Intent(activity, TransactionIncomeActivity.class),id,"home",type);
            }
        });
        viewHolder.adView.setVisibility(View.GONE);
        viewHolder.adView_lay.setVisibility(View.GONE);
        String ads = beans.getAds();
        if(ads.equalsIgnoreCase("ads")){
            viewHolder.adView.setVisibility(View.VISIBLE);
            viewHolder.adView_lay.setVisibility(View.VISIBLE);
            Utilities.LoadBanner(viewHolder.adView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDescription;
        private TextView txtName;
        private TextView textType;
        private TextView textIncome;
        private TextView textExpense;
        private LinearLayout adView_lay;
        private LinearLayout dailyTrans_btn;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            txtDescription = (TextView)view.findViewById(R.id.description_txt);
            txtName = (TextView)view.findViewById(R.id.name_txt);
            textType = (TextView)view.findViewById(R.id.type_txt);
            textIncome = (TextView)view.findViewById(R.id.income_txt);
            textExpense = (TextView)view.findViewById(R.id.expense_txt);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            dailyTrans_btn = (LinearLayout) view.findViewById(R.id.dailyTran_lay_btn);
            adView = (AdView)view.findViewById(R.id.adView);
        }

    }
    public void LoadActivity(Intent intent, String id,String clValue,String value){
        intent.putExtra("className",clValue);
        intent.putExtra("trans",value);
        intent.putExtra("transactionId",id);
        activity.startActivity(intent);
        activity.finish();
    }
}
