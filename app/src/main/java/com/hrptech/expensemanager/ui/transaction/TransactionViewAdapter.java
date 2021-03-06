package com.hrptech.expensemanager.ui.transaction;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.ViewHolder> {
    private Activity activity;
    private List<TransactionBeans> items;
    private String type = "";
    public TransactionViewAdapter(Activity activity, List<TransactionBeans> items,String type) {
        this.activity = activity;
        this.items = items;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.transactionlist_layout, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TransactionBeans beans = items.get(position);
        String name = beans.getName();
        final String type = beans.getType();
        String description = beans.getDescription();
        String date = beans.getDate();
        String balance = beans.getBalance();
        viewHolder.txtName.setText(name);
        viewHolder.textType.setText(type);
        viewHolder.textAmount.setText(balance);
        viewHolder.textDate.setText(date);
        viewHolder.textDescription.setText(description);
        if(type.equalsIgnoreCase("income")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        }else if(type.equalsIgnoreCase("expense")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorRed));
        }

        viewHolder.deleteBtn.setTag(beans);
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransactionBeans beans = (TransactionBeans)v.getTag();
                    if(type.equalsIgnoreCase("income")){
                        Utilities.showDialogClose(activity,"delete","Income",beans.getId(),beans.getCid());
                    }else if(type.equalsIgnoreCase("expense")){
                        Utilities.showDialogClose(activity,"delete","Expense",beans.getId(),beans.getCid());
                    }



            }
        });
        viewHolder.itemView.setTag(beans.getId());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(type.equalsIgnoreCase("income")){
                        TransactionIncomeActivity.getTransactionFragment().ShowRecordOFBudgetForUpdate(v.getTag().toString());
                    }else if(type.equalsIgnoreCase("expense")){
                        TransactionExpenseActivity.getTransactionFragment().ShowRecordOFBudgetForUpdate(v.getTag().toString());
                    }

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
        private TextView textType;
        private TextView textDate;
        private TextView textDescription;
        private TextView textAmount;
        private ImageView deleteBtn;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.txtName);
            textType = (TextView)view.findViewById(R.id.txtType);
            textAmount = (TextView)view.findViewById(R.id.amount_txt);
            textDate = (TextView)view.findViewById(R.id.date_txt);
            textDescription = (TextView)view.findViewById(R.id.txtDescription);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);

        }

    }
}
