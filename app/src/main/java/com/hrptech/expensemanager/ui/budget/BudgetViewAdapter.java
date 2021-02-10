package com.hrptech.expensemanager.ui.budget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.BudgetBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class BudgetViewAdapter extends RecyclerView.Adapter<BudgetViewAdapter.ViewHolder> {
    private Activity activity;
    private List<BudgetBeans> items;

    public BudgetViewAdapter(Activity activity, List<BudgetBeans> items) {
        for(int index=0; index<items.size(); index++){
            BudgetBeans beans = items.get(index);
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
        View view = inflater.inflate(R.layout.budgetlist_form, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BudgetBeans beans = items.get(position);
        viewHolder.txtName.setText(beans.getCat_name());
        viewHolder.textType.setText(beans.getCat_type());
        viewHolder.textAmount.setText(beans.getAmount());
        viewHolder.textYearMonth.setText(beans.getYear()+","+beans.getMonth());
        if(beans.getCat_type().equalsIgnoreCase("income")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        }else if(beans.getCat_type().equalsIgnoreCase("expense")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorRed));
        }

        viewHolder.deleteBtn.setTag(beans.getId());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BudgetFragment.getInit()!=null){
                    //Utilities.showDialogBudget(BudgetFragment.getCategoryFragment(),v.getTag().toString(),activity,"delete","Do you want to","delete record");
                    Utilities.showDialogForBudget(activity,"delete",v.getTag().toString());
                }
            }
        });
        viewHolder.itemView.setTag(beans.getId());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BudgetFragment.getInit()!=null){
                    BudgetFragment.getInit().ShowRecordOFBudgetForUpdate(v.getTag().toString());
                }
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
        private TextView txtName;
        private TextView textType;
        private TextView textAmount;
        private TextView textYearMonth;
        private ImageView deleteBtn;
        private LinearLayout adView_lay;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.name_txt);
            textType = (TextView)view.findViewById(R.id.type_txt);
            textAmount = (TextView)view.findViewById(R.id.amount_txt);
            textYearMonth = (TextView)view.findViewById(R.id.yearMonth_txt);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);

        }

    }
}
