package com.hrptech.expensemanager.report;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;

import java.util.List;


public class DailyReportViewAdapter extends RecyclerView.Adapter<DailyReportViewAdapter.ViewHolder> {
    private Activity activity;
    private List<TransactionBeans> items;

    public DailyReportViewAdapter(Activity activity, List<TransactionBeans> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.report_list_daily, parent, false);

        return new ViewHolder(view);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TransactionBeans beans = items.get(position);
        if(beans.getDate().equalsIgnoreCase("Date")){
            viewHolder.lay_Date.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Description.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Opening.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Income.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Expense.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Balance.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));

            viewHolder.txtDate.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtDescription.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtOpening.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtIncome.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtExpense.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtBalance.setTextColor(activity.getResources().getColor(R.color.colorWhite));

        }else {
            viewHolder.lay_Date.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Description.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Opening.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Income.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Expense.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Balance.setBackground(activity.getDrawable(R.drawable.textlayout_tran));

            viewHolder.txtDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtDescription.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtOpening.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtIncome.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtExpense.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtBalance.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        }
        viewHolder.txtDate.setText(beans.getDate());
        viewHolder.txtDescription.setText(beans.getDescription());
        viewHolder.txtOpening.setText(beans.getOpening());
        viewHolder.txtIncome.setText(beans.getIncome());
        viewHolder.txtExpense.setText(beans.getExpense());
        viewHolder.txtBalance.setText(beans.getBalance());

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
        private TextView txtDescription;
        private TextView txtOpening;
        private TextView txtIncome;
        private TextView txtExpense;
        private TextView txtBalance;

        private LinearLayout lay_Date;
        private LinearLayout lay_Description;
        private LinearLayout lay_Opening;
        private LinearLayout lay_Income;
        private LinearLayout lay_Expense;
        private LinearLayout lay_Balance;

        public ViewHolder(View view) {
            super(view);
            txtDate = (TextView)view.findViewById(R.id.date_txt);
            txtDescription = (TextView)view.findViewById(R.id.txtDescription);
            txtOpening = (TextView)view.findViewById(R.id.opening_txt);
            txtIncome = (TextView)view.findViewById(R.id.income_txt);
            txtExpense = (TextView)view.findViewById(R.id.expense_txt);
            txtBalance = (TextView)view.findViewById(R.id.balance_txt);

            lay_Date = (LinearLayout) view.findViewById(R.id.lay_date);
            lay_Description = (LinearLayout) view.findViewById(R.id.lay_description);
            lay_Opening = (LinearLayout) view.findViewById(R.id.lay_opening);
            lay_Income = (LinearLayout) view.findViewById(R.id.lay_income);
            lay_Expense = (LinearLayout) view.findViewById(R.id.lay_expense);
            lay_Balance = (LinearLayout) view.findViewById(R.id.lay_balance);

        }

    }
}
