package com.hrptech.expensemanager.ui.category;

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
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.List;


public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.ViewHolder> {
    private Activity activity;
    private List<CATEGORY> items;

    public CategoryViewAdapter(Activity activity, List<CATEGORY> items) {

        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.categorylist_form, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        CATEGORY beans = items.get(position);
        viewHolder.txtName.setText(beans.getName());
        viewHolder.textType.setText(beans.getType());
        if(beans.getType().equalsIgnoreCase("income")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        }else if(beans.getType().equalsIgnoreCase("expense")){
            viewHolder.textType.setTextColor(activity.getResources().getColor(R.color.colorRed));
        }
        viewHolder.deleteBtn.setTag(beans.getId());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CategoryActivity.getCategoryFragment()!=null){
                    Utilities.showDialogCategory(CategoryActivity.getCategoryFragment(),v.getTag().toString(),activity,"delete","Do you want to","delete category");
                }
            }
        });
        viewHolder.itemView.setTag(beans.getId());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CategoryActivity.getCategoryFragment()!=null){
                    CategoryActivity.getCategoryFragment().ShowRecordOfCategory(v.getTag().toString());
                }
            }
        });
        viewHolder.adView.setVisibility(View.GONE);
        viewHolder.adView_lay.setVisibility(View.GONE);

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
        private ImageView deleteBtn;
        private LinearLayout adView_lay;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.name_txt);
            textType = (TextView)view.findViewById(R.id.type_txt);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);
        }

    }
}
