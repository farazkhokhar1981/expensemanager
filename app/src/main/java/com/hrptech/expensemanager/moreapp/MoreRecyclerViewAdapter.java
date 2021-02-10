package com.hrptech.expensemanager.moreapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.hrptech.expensemanager.R;

import java.util.ArrayList;


/**
 * Created by hrptech on 4/9/2017.
 */

public class MoreRecyclerViewAdapter extends RecyclerView.Adapter<MoreRecyclerViewAdapter.ViewHolder> {

    private ArrayList<NoteBookBean> noteBookBeanArrayList=null;
    private LayoutInflater mInflater;
    MoreAppsForm mainScreen_=null;
    Activity context;

    // data is passed into the constructor
    public MoreRecyclerViewAdapter(Activity context, MoreAppsForm mainScreen_, ArrayList<NoteBookBean> noteBookBeanArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        try{
            this.noteBookBeanArrayList = noteBookBeanArrayList;
        }catch(Exception e){

        }
        this.mainScreen_=mainScreen_;

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.moreapplist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteBookBean proBean=noteBookBeanArrayList.get(position);


        holder.title_txt.setText(proBean.getTitle());
        holder.appsImage.setImageDrawable(context.getDrawable(proBean.getResImage()));
        holder.appsImage.setTag(proBean.getDescription());
        holder.appsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = view.getTag().toString(); // getPackageName() from Context or Activity object
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });





    }

    // total number of cells
    @Override
    public int getItemCount() {
        return noteBookBeanArrayList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_txt;
        public ImageView appsImage;





        public ViewHolder(View itemView) {
            super(itemView);
            title_txt = (TextView) itemView.findViewById(R.id.apps_name_txt);
            appsImage=(ImageView)itemView.findViewById(R.id.image_apps);
        }


    }

    // convenience method for getting data at click position
    public NoteBookBean getItem(int id) {
        return noteBookBeanArrayList.get(id);
    }




}
