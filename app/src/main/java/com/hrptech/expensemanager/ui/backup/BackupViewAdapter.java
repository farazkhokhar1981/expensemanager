package com.hrptech.expensemanager.ui.backup;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.BudgetBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.File;
import java.util.List;


public class BackupViewAdapter extends RecyclerView.Adapter<BackupViewAdapter.ViewHolder> {
    private Activity activity;
    private List<BudgetBeans> items;

    public BackupViewAdapter(Activity activity, List<BudgetBeans> items) {
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
        View view = inflater.inflate(R.layout.backuprestorelist, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        BudgetBeans beans = items.get(position);
        viewHolder.txtName.setText(beans.getCat_type());
        viewHolder.view_btn.setTag(beans.getCat_name());
        viewHolder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(activity, viewHolder.view_btn);
                //inflating menu from xml resource
                popup.inflate(R.menu.settings);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = viewHolder.getAdapterPosition();
                        BudgetBeans beans = items.get(position);
                        switch (item.getItemId()) {
                            case R.id.menu_restore:

                                Utilities.importDB(activity,beans.getCat_name());

                                break;
                            case R.id.menu_delete:
                                String path = beans.getCat_name();
                                File fileOld = new File(path);
                                if(BackupRestoreFragment.getCategoryFragment()!=null){
                                    BackupRestoreFragment.getCategoryFragment().DeleteFile(fileOld);
                                }
                                break;
                            case R.id.menu_share:
                                if(BackupRestoreFragment.getCategoryFragment()!=null){
                                    BackupRestoreFragment.getCategoryFragment().ShareImageToWhatsapp(beans.getCat_name());
                                }
                                break;


                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
//
            }
        });
        viewHolder.deleteBtn.setTag(beans.getCat_name());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BackupRestoreFragment.getCategoryFragment()!=null){
                    BackupRestoreFragment.getCategoryFragment().importDB(activity,v.getTag().toString());
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

        private ImageView deleteBtn;
        private LinearLayout adView_lay;
        private AdView adView;
        private LinearLayout view_btn;

        public ViewHolder(View view) {
            super(view);
            txtName = (TextView)view.findViewById(R.id.backup_txt);
            deleteBtn = (ImageView) view.findViewById(R.id.deleteBtn);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);
            view_btn = (LinearLayout)view.findViewById(R.id.view_btn);


        }

    }
}
