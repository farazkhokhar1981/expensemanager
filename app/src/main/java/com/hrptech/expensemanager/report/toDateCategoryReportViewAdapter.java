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


public class toDateCategoryReportViewAdapter extends RecyclerView.Adapter<toDateCategoryReportViewAdapter.ViewHolder> {
    private Activity activity;
    private List<TransactionBeans> items;

    public toDateCategoryReportViewAdapter(Activity activity, List<TransactionBeans> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.report_list_todate_category, parent, false);

        return new ViewHolder(view);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TransactionBeans beans = items.get(position);
        if(beans.getDate().equalsIgnoreCase("Date") || beans.getDate().equalsIgnoreCase("Month")){
            viewHolder.lay_Date.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Description.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));
            viewHolder.lay_Balance.setBackground(activity.getDrawable(R.drawable.textlayout_tran_hder));

            viewHolder.txtDate.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtDescription.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            viewHolder.txtAmount.setTextColor(activity.getResources().getColor(R.color.colorWhite));

        }else {
            viewHolder.lay_Date.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Description.setBackground(activity.getDrawable(R.drawable.textlayout_tran));
            viewHolder.lay_Balance.setBackground(activity.getDrawable(R.drawable.textlayout_tran));

            viewHolder.txtDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtDescription.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            viewHolder.txtAmount.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        }
        viewHolder.txtDate.setText(beans.getDate());
        viewHolder.txtDescription.setText(beans.getDescription());
        viewHolder.txtAmount.setText(beans.getBalance());

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
        private TextView txtAmount;

        private LinearLayout lay_Date;
        private LinearLayout lay_Description;
        private LinearLayout lay_Balance;

        public ViewHolder(View view) {
            super(view);
            txtDate = (TextView)view.findViewById(R.id.txtDate);
            txtDescription = (TextView)view.findViewById(R.id.txtDescription);
            txtAmount = (TextView)view.findViewById(R.id.amount_txt);

            lay_Date = (LinearLayout) view.findViewById(R.id.lay_date);
            lay_Description = (LinearLayout) view.findViewById(R.id.lay_description);
            lay_Balance = (LinearLayout) view.findViewById(R.id.lay_Amount);

        }

    }
}
