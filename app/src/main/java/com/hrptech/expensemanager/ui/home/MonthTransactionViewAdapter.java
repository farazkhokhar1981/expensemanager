package com.hrptech.expensemanager.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class MonthTransactionViewAdapter extends RecyclerView.Adapter<MonthTransactionViewAdapter.ViewHolder> {
    private Activity activity;
    private List<DailyTransactionBeans> items;

    public MonthTransactionViewAdapter(Activity activity, List<DailyTransactionBeans> items) {
        for(int index=0; index<items.size(); index++){
            DailyTransactionBeans beans = items.get(index);
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
        View view = inflater.inflate(R.layout.monthlytransaction_layout, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        DailyTransactionBeans beans = items.get(position);
        viewHolder.txtDate.setText(beans.getDay());
        viewHolder.txtMonth.setText(beans.getMonth());
        viewHolder.txtOpening.setText(beans.getOpening());
        viewHolder.textIncome.setText(beans.getIncome());
        viewHolder.textExpense.setText(beans.getExpense());
        viewHolder.textBalance.setText(beans.getBalance());
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

        private TextView txtDate;
        private TextView txtMonth;
        private TextView txtOpening;
        private TextView textIncome;
        private TextView textExpense;
        private TextView textBalance;
        private LinearLayout adView_lay;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            txtDate = (TextView)view.findViewById(R.id.datenumber_txt);
            txtMonth = (TextView)view.findViewById(R.id.month_txt);
            txtOpening = (TextView)view.findViewById(R.id.openingTxt);
            textIncome = (TextView)view.findViewById(R.id.income_txt);
            textExpense = (TextView)view.findViewById(R.id.expense_txt);
            textBalance = (TextView)view.findViewById(R.id.balance_txt);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);
        }

    }
}
