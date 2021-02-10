package com.hrptech.expensemanager.ui.documents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.OpenPdfActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.ImageBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.File;
import java.util.List;


public class DocumentsViewAdapter extends RecyclerView.Adapter<DocumentsViewAdapter.ViewHolder> {
    private Activity activity;
    private List<ImageBeans> items;
    private boolean isParties = false;

    public DocumentsViewAdapter(Activity activity, List<ImageBeans> items, boolean isParties) {
        this.activity = activity;
        for(int index=0; index<items.size(); index++){
            ImageBeans beans = items.get(index);
            if((index+1)%4==0){
                beans.setAds("ads");
            }else {
                beans.setAds("");
            }
        }
        this.items = items;
        this.isParties = isParties;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.pdf_list, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ImageBeans beans = items.get(position);
        viewHolder.title_txt.setText(beans.getTitle());
        viewHolder.dateTime_txt.setText(beans.getDateTime());
        viewHolder.size_txt.setText(beans.getSize());
        viewHolder.settings_btn.setTag(beans.getTitle());
        viewHolder.settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, viewHolder.settings_btn);
                //inflating menu from xml resource
                popup.inflate(R.menu.settings_doc);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = viewHolder.getAdapterPosition();
                        ImageBeans beans = items.get(position);
                        switch (item.getItemId()) {
                            case R.id.open_addincome:


                                Utilities.imageFileName = beans.getPath();
//                                Intent intent = new Intent(activity, OpenPdfActivity.class);
                                LoadOpenPdf(Utilities.imageFileName,beans.getTitle());
                                break;
                            case R.id.open_addexpense:
                                Utilities.ShareImageToWhatsapp(activity,beans.getTitle());
                                break;
                            case R.id.open_Dashboard:
                                showDialogClose(beans.getPath());
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }

        });
        viewHolder.adView.setVisibility(View.GONE);
        viewHolder.adView_lay.setVisibility(View.GONE);

        String ads = beans.getAds();
        try {
            if(ads!=null) {
                if (ads.equalsIgnoreCase("ads")) {
                    viewHolder.adView.setVisibility(View.VISIBLE);
                    viewHolder.adView_lay.setVisibility(View.VISIBLE);
                    Utilities.LoadBanner(viewHolder.adView);
                }
            }
        }catch(Exception e){

        }


    }

    public void LoadOpenPdf(String path, String name){
        Intent intent = new Intent(activity, OpenPdfActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("name",name);
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_txt;
        private TextView dateTime_txt;
        private TextView size_txt;
        private ImageView settings_btn;
        private LinearLayout adView_lay;
        private AdView adView;

        public ViewHolder(View view) {
            super(view);
            title_txt = (TextView)view.findViewById(R.id.title_txt);
            dateTime_txt = (TextView)view.findViewById(R.id.dateTime_txt);
            size_txt = (TextView)view.findViewById(R.id.size_txt);
            settings_btn = (ImageView) view.findViewById(R.id.settings_btn);
            adView_lay = (LinearLayout) view.findViewById(R.id.adView_lay);
            adView = (AdView)view.findViewById(R.id.adView);
        }

    }
    public Dialog dialog = null;
    public Button no_btn= null;
    public Button yes_btn= null;
    public TextView confirm_txt= null;

    @SuppressLint("NewApi")
    public void showDialogClose(final String paths){
        dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.closedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirm_txt = (TextView) dialog.findViewById(R.id.confirm_txt);
        confirm_txt.setText(activity.getResources().getString(R.string.confirmDelete));
        no_btn = (Button)dialog.findViewById(R.id.no_btn);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        yes_btn = (Button)dialog.findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File fileOlds = new File(paths);
                if(DocumentsActivity.getInstance()!=null){
                    DocumentsActivity.getInstance().DeleteFile(fileOlds);
                }
                dialog.dismiss();
            }
        });
//        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
//        Utilities.refreshAd(activity,frameLayout);
        dialog.show();

    }

}
