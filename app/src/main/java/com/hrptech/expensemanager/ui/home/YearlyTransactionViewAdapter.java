package com.hrptech.expensemanager.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;

import java.util.List;


public class YearlyTransactionViewAdapter extends RecyclerView.Adapter<YearlyTransactionViewAdapter.ViewHolder> {
    private Activity activity;
    private List<DailyTransactionBeans> items;

    public YearlyTransactionViewAdapter(Activity activity, List<DailyTransactionBeans> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.yearlytransaction_layout, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        DailyTransactionBeans beans = items.get(position);
        viewHolder.textMonth.setText(beans.getMonth());
        viewHolder.textIncome.setText(beans.getIncome());
        viewHolder.textExpense.setText(beans.getExpense());
        viewHolder.textBalance.setText(beans.getBalance());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textMonth;
        private TextView textIncome;
        private TextView textExpense;
        private TextView textBalance;

        public ViewHolder(View view) {
            super(view);
            textMonth = (TextView)view.findViewById(R.id.month_txt);
            textIncome = (TextView)view.findViewById(R.id.income_txt);
            textExpense = (TextView)view.findViewById(R.id.expense_txt);
            textBalance = (TextView)view.findViewById(R.id.balance_txt);
        }

    }
}
