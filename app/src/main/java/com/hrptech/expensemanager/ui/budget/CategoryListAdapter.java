package com.hrptech.expensemanager.ui.budget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;

import java.util.ArrayList;


public class CategoryListAdapter  extends BaseAdapter {
    Activity context;
    ArrayList<CATEGORY> listItems;

    public CategoryListAdapter(Activity context, ArrayList<CATEGORY> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        CATEGORY proBean = listItems.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            // get layout from mobile.xml
            convertView = inflater.inflate(R.layout.categorylist_layout, null);

            holder.name_txt = (TextView) convertView.findViewById(R.id.name_txt);

            holder.type_txt = (TextView) convertView.findViewById(R.id.type_txt);
            holder.type_lay = (LinearLayout) convertView.findViewById(R.id.type_lay);
            holder.type_lay.setVisibility(View.GONE);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.name_txt.setText(proBean.getName());
        holder.type_txt.setText(proBean.getType());
        if (proBean.getType().equalsIgnoreCase("income")) {
            holder.type_txt.setTextColor(Color.GREEN);
        } else {
            holder.type_txt.setTextColor(Color.RED);
        }

        return convertView;

    }
    private class ViewHolder {

        protected TextView name_txt;
        private TextView type_txt;
        private LinearLayout type_lay;

    }
}