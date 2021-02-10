package com.hrptech.expensemanager.ui.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;

import java.util.List;


public class BudgetViewAdapter extends RecyclerView.Adapter<BudgetViewAdapter.ViewHolder> {
    private Activity activity;
    private List<BudgetBeans> items;

    public BudgetViewAdapter(Activity activity, List<BudgetBeans> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.budget_of_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BudgetBeans beans = items.get(position);
        viewHolder.txtName.setText(beans.getName());
        viewHolder.textAmount.setText(beans.getAmount());
        viewHolder.textExpense.setText(beans.getExpense());
        viewHolder.textBalance.setText(beans.getBalance());
        viewHolder.textProg.setText(beans.getPercentage());


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
        private TextView textAmount;
        private TextView textExpense;
        private TextView textBalance;
        private TextView textProg;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.names_txt);
            textAmount = (TextView)view.findViewById(R.id.budget_txt);
            textExpense = (TextView)view.findViewById(R.id.expense_txt);
            textBalance = (TextView)view.findViewById(R.id.balance_txt);
            textProg = (TextView)view.findViewById(R.id.per_txt);
        }

    }
}
