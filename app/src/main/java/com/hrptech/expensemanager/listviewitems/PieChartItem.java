
package com.hrptech.expensemanager.listviewitems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.chart.RoundedSlicesPieChartRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PieChartItem extends ChartItem {

    private final Typeface mTf;
    private boolean isShowText = false;
    //private final SpannableString mCenterText;

    public PieChartItem(ChartData<?> cd, Context c,boolean isShowText) {
        super(cd);
        this.isShowText = isShowText;
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");

        //mCenterText = generateCenterText();
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setHoleRadius(55f);
        holder.chart.setTransparentCircleRadius(0f);
        String month = new SimpleDateFormat("MMM-yy").format(new Date());
        holder.chart.setCenterText(month);
        holder.chart.setCenterTextTypeface(mTf);
        holder.chart.setCenterTextSize(14f);
        holder.chart.setCenterTextColor(Color.BLUE);
        holder.chart.setEntryLabelColor(Color.WHITE);
        holder.chart.setDrawMarkers(true); // To remove markers when click
        holder.chart.setDrawEntryLabels(true); // To remove labels from piece of pie
        holder.chart.getDescription().setEnabled(false); // To remove description of pie


        holder.chart.getLegend().setEnabled(false);
        holder.chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        Legend l = holder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        holder.chart.setTouchEnabled(true);
        holder.chart.setDrawEntryLabels(false);
        holder.chart.setEntryLabelTextSize(10);
//        if(isShowText){
//            holder.chart.setEntryLabelTextSize(10);
//        }else {
//            holder.chart.setEntryLabelTextSize(0);
//        }

        holder.chart.setExtraOffsets(25.f, 2.f, 25.f, 2.f);
        holder.chart.setRenderer(new RoundedSlicesPieChartRenderer(holder.chart, holder.chart.getAnimator(), holder.chart.getViewPortHandler()));
        mChartData.setValueFormatter(new LargeValueFormatter());
        holder.chart.highlightValues(null);
        holder.chart.setUsePercentValues(true);
        holder.chart.setDragDecelerationFrictionCoef(0.95f);
        holder.chart.setHighlightPerTapEnabled(true);
        holder.chart.setDrawHoleEnabled(true);
        holder.chart.setEntryLabelColor(Color.BLACK);
        mChartData.setValueTypeface(mTf);
        mChartData.setValueTextSize(0f);
        mChartData.setValueTextColor(Color.BLUE);
        // set data

        holder.chart.setData((PieData) mChartData);



        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateY(900);



        return convertView;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Expanse Manager");
        s.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0);
        s.setSpan(new RelativeSizeSpan(.9f), 14, 25, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, 25, 0);
        s.setSpan(new RelativeSizeSpan(1.4f), 25, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 25, s.length(), 0);
        return s;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}
