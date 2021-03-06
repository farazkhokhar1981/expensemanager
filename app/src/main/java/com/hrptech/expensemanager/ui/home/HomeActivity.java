package com.hrptech.expensemanager.ui.home;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.SettingsActivity;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.chart.RoundedSlicesPieChartRenderer;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.listviewitems.ChartItem;
import com.hrptech.expensemanager.listviewitems.PieChartItem;
import com.hrptech.expensemanager.localdb.db.GeneralDB;
import com.hrptech.expensemanager.ui.budget.BudgetFragment;
import com.hrptech.expensemanager.ui.category.CategoryActivity;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {

    DatabaseReference getTotalIncomeExpense;

    String  selectedDateFormat = "";

    LinearLayout chart_id = null;
    LinearLayout budget_lay = null;
    ImageView settings_btn = null;
    TextView balance_txt;
    TextView income_txt;
    TextView expense_txt;
    TextView circle_txt;
    TextView type_txt;
    TextView amount_txt;
    TextView date_txt;
    private RecyclerView budgetList;
    TransactionDB transactionDB;
    CategoryDB categoryDB;
    SettingBeans settingBeans;
    SettingDB settingDB;
    private BudgetViewAdapter budgetViewAdapter = null;
    private ListView listView = null;
    public static String currency = "";
    String months[] = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_home);
        // initialiaze all objects here

        //// Setting Currency & date formate!
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("EXPENSEMANAGER/SETTINGS");
//        ref.addValueEventListener(new ValueEventListener() {
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                SettingBeans beans = dataSnapshot.getValue(SettingBeans.class);
//                currency = beans.getCurrency();
//                Toast.makeText(HomeActivity.this,"Currency: "+currency ,Toast.LENGTH_LONG).show();
//                selectedDateFormat = beans.getDateformat();
//            }
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
        //// -------------------------------

        decoViewCircle();
        notification();

        GeneralDB generalDB = new GeneralDB(this);
        CategoryDB.getCatNameList();
        TransactionDB.getTransactionList();

        getTotalIncomeExpense = FirebaseDatabase.getInstance().getReference().child("EXPENSEMANAGER/TRANSACTION");
        getTotalIncomeExpense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int income = 0;
                int expense = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    String income_ = map.get("income").toString();
                    String expense_ = map.get("expense").toString();
                    income += Integer.parseInt(income_);
                    expense += Integer.parseInt(expense_);
                    income_txt.setText(currency+""+income);
                    expense_txt.setText(currency+""+expense);
                    balance_txt.setText(currency+""+(income-expense));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        init();

        PieChart pieChart = new PieChart(this);
        pieChart.setRenderer(new RoundedSlicesPieChartRenderer(pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler()));

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(HomeActivity.this, R.style.CustomPopupTheme);
                PopupMenu popup = new PopupMenu(ctw, settings_btn);

                //inflating menu from xml resource
                //popup.inflate(R.menu.main);
                popup.inflate(R.menu.settings_doc);
                for (int index = 0; index < 7; index++) {
                    MenuItem menuItem = popup.getMenu().getItem(index);
                    insertMenuItemIcon(HomeActivity.this, menuItem);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int itemId = item.getItemId();
                        switch (itemId) {
                            case R.id.open_category:
                                LoadActivity(new Intent(HomeActivity.this, CategoryActivity.class), "");
                                break;

                            case R.id.open_addincome:
                                LoadActivity(new Intent(HomeActivity.this, TransactionIncomeActivity.class), "Income");
                                break;
                            case R.id.open_addexpense:
                                LoadActivity(new Intent(HomeActivity.this, TransactionIncomeActivity.class), "Expense");
                                break;
                            case R.id.open_Dashboard:
                                LoadActivity(new Intent(HomeActivity.this, DashBoardActivity.class), "");
                                break;
                            case R.id.open_Settings:
                                LoadActivity(new Intent(HomeActivity.this, SettingsActivity.class), "");
                                break;
                            case R.id.open_budgetform:
                                LoadActivity(new Intent(HomeActivity.this, BudgetFragment.class), "");
                                break;
                        }

                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1];
        String month_ = Utilities.getMonth(months, month);
//        Toast.makeText(this,"Year:  "+year,Toast.LENGTH_LONG).show();
//        Toast.makeText(this,"DATE:  "+month,Toast.LENGTH_LONG).show();
//        Toast.makeText(this,"DATE_:   "+month_,Toast.LENGTH_LONG).show();

        TransactionBeans beansTransaction = transactionDB.getTransactionRecordsYear(month_, year);
        if (beansTransaction != null) {
            Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
            income_txt.setText(currency + "" + beansTransaction.getIncome());
            expense_txt.setText(currency + "" + beansTransaction.getExpense());
            balance_txt.setText(currency + "" + beansTransaction.getBalance());
            TransactionBeans tBeans = transactionDB.getTransactionLast();
            if (tBeans != null) {
                circle_txt.setText(tBeans.getName().charAt(0) + "".toUpperCase());
                type_txt.setText(tBeans.getName());
                date_txt.setText(Utilities.getDayFromDate(tBeans.getDate(), settingBeans.getDateformat()));
                if (tBeans.getType().equalsIgnoreCase("Income")) {
                    amount_txt.setText(currency + "" + tBeans.getIncome());
                } else {
                    amount_txt.setText(currency + "" + tBeans.getExpense());
                }
            } else {
                circle_txt.setText("A");
                type_txt.setText("");
                date_txt.setText("");
                amount_txt.setText(currency + "0.0");
            }
        }

        BudgetTransaction();
        ChartTransaction();
    }

    private void decoViewCircle() {
        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcView);
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#0550F3"))
                .setRange(24, 100, 100)
                .setSeriesLabel(new SeriesLabel.Builder(" \u0020 \u0020 \u0020 \u0020 \u0020 \u0020 \u0020 \u0020 DEC")
                        .setColorBack(Color.argb(0, 0, 0, 0))
                        .setColorText(Color.argb(255, 0, 0, 0))
                        .setFontSize(16)
                        .build())
                .build();
        int series1Index = decoView.addSeries(seriesItem);

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.rgb(190, 224, 252))
                .setRange(0, 100, 20)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.1f))
                .build();
        int series2index = decoView.addSeries(seriesItem1);
    }

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Error";
                        }
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void init() {
        settings_btn = (ImageView) findViewById(R.id.settings_btn);
        balance_txt = (TextView) findViewById(R.id.balance_txt);
        income_txt = (TextView) findViewById(R.id.income_txt);
        expense_txt = (TextView) findViewById(R.id.expense_txt);
        circle_txt = (TextView) findViewById(R.id.circle_name);
        type_txt = (TextView) findViewById(R.id.names_txt);
        date_txt = (TextView) findViewById(R.id.date_txt);
        amount_txt = (TextView) findViewById(R.id.amount_txt);
//        chart_id = (LinearLayout)findViewById(R.id.chart_id);
        listView = (ListView) findViewById(R.id.listView1);
        //listView.setAdapter(null);
        budget_lay = (LinearLayout) findViewById(R.id.budget_lay);
        transactionDB = new TransactionDB(this);
        categoryDB = new CategoryDB(this);




        budgetList = (RecyclerView) findViewById(R.id.mRecyclerView);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager budgetManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(budgetManager);
        budgetList.setAdapter(null);


    }

    private static void insertMenuItemIcon(Context context, MenuItem menuItem) {
        Drawable icon = menuItem.getIcon();

        // If there's no icon, we insert a transparent one to keep the title aligned with the items
        // which do have icons.
        if (icon == null) icon = new ColorDrawable(Color.TRANSPARENT);

        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.menu_item_icon_size);
        icon.setBounds(0, 0, iconSize, iconSize);
        ImageSpan imageSpan = new ImageSpan(icon);

        // Add a space placeholder for the icon, before the title.
        SpannableStringBuilder ssb = new SpannableStringBuilder("       " + menuItem.getTitle());

        // Replace the space placeholder with the icon.
        ssb.setSpan(imageSpan, 1, 2, 0);
        menuItem.setTitle(ssb);
        // Set the icon to null just in case, on some weird devices, they've customized Android to display
        // the icon in the menu... we don't want two icons to appear.
        menuItem.setIcon(null);
    }

    public void LoadActivity(Intent intent, String value) {
        intent.putExtra("trans", value);
        intent.putExtra("transactionId", "");
        intent.putExtra("className", "home");
        startActivity(intent);
        getActivity().finish();
    }


    public Activity getActivity() {
        return this;
    }

    public void BudgetTransaction() {
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1];
        String month1 = Utilities.getMonth(months, month);
        ArrayList<TransactionBeans> listOfBudget = transactionDB.getBudgetRecordsMonth(month, year);
        List<BudgetBeans> budgetBeansList = new ArrayList<>();
        for (int index = 0; index < listOfBudget.size(); index++) {
            TransactionBeans beans = listOfBudget.get(index);
            String cid = beans.getCid();
            double budgetAmount = Double.parseDouble(beans.getExpense());
            double expense = transactionDB.getTransactionExpense(year, month1, cid);
            double remaining = budgetAmount - expense;
            double per = expense / budgetAmount * 100;
            budgetBeansList.add(getBudgetList(beans.getName(), new DecimalFormat("0.00").format(budgetAmount), new DecimalFormat("0.00").format(expense), new DecimalFormat("0.00").format(remaining), new DecimalFormat("0.00").format(per) + "%", per));
        }
        if (budgetBeansList.size() > 0) {
            budget_lay.setVisibility(View.VISIBLE);
        } else {
            budget_lay.setVisibility(View.GONE);
        }
        budgetViewAdapter = new BudgetViewAdapter(this.getActivity(), budgetBeansList);
        budgetList.setAdapter(budgetViewAdapter);
    }

    public BudgetBeans getBudgetList(String name, String amount, String expense, String balance, String percentage, double per) {
        BudgetBeans beans = new BudgetBeans();
        beans.setName(name);
        beans.setAmount(amount);
        beans.setExpense(expense);
        beans.setBalance(balance);
        beans.setPercentage(percentage);
        beans.setPer(per);
        return beans;
    }

    public void ChartTransaction() {
        ArrayList<ChartItem> list = new ArrayList<>();

        // 30 items

        LineData lineData = generateDataLine();
        //   list.add(new LineChartItem(lineData, this.getActivity()));
        //   list.add(new BarChartItem(generateDataBar(), this.getActivity()));
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1];
        String month1 = Utilities.getMonth(months, month);
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsMonth(month1, year);

        if (transactionBeans.size() > 0) {
            //list.add(new PieChartItem(generateDataPie(), this.getActivity(),true));
            list.add(new PieChartItem(generateDataPie(), this.getActivity(), true));

        } else {
            list.add(new PieChartItem(generateDataPie(), this.getActivity(), false));
        }


        ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(), list);
        //listView.setAdapter(cda);
    }

    private LineData generateDataLine() {
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1];
        String month1 = Utilities.getMonth(months, month);
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsMonth(month1, year);
        for (int indexIncome = 0; indexIncome < transactionBeans.size(); indexIncome++) {
            values1.add(new Entry(indexIncome, (int) (Double.parseDouble(transactionBeans.get(indexIncome).getIncome()))));
        }

        LineDataSet d1 = new LineDataSet(values1, "Income, (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int indexIncome = 0; indexIncome < transactionBeans.size(); indexIncome++) {
            values2.add(new Entry(indexIncome, (int) (Double.parseDouble(transactionBeans.get(indexIncome).getExpense()))));
        }

        LineDataSet d2 = new LineDataSet(values2, "Expense, (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }

    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1];
        String month1 = Utilities.getMonth(months, month);
        TransactionBeans transactionBeans = transactionDB.getTransactionRecordsYear(month1, year);
        float income = 0;
        float expense = 0;
        if (transactionBeans != null) {

            try {
                income = (float) (Double.parseDouble(transactionBeans.getIncome()));
            } catch (Exception e) {
                income = 0;
            }
            try {
                expense = (float) (Double.parseDouble(transactionBeans.getExpense()));
            } catch (Exception e) {
                expense = 0;
            }

        }

        if (income < 1 && expense < 1) {
            income = 0.1f;
            entries.add(new PieEntry(income, ""));
            entries.add(new PieEntry(expense, ""));

        } else {
            entries.add(new PieEntry(income, "Income"));
            entries.add(new PieEntry(expense, "Expense"));

        }
        PieDataSet dataSet = new PieDataSet(entries, "h");
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setValueLinePart1OffsetPercentage(100f); /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
//        dataSet.setValueLinePart1Length(0.6f); /** When valuePosition is OutsideSlice, indicates length of first half of the line */
//        dataSet.setValueLinePart2Length(0.6f); /** When valuePosition is OutsideSlice, indicates length of second half of the line */
//        dataSet.setSliceSpace(0f);
//        dataSet.setLabel("Hello");
//        PieData data = new PieData(dataSet);
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.BLACK);
//        data.setValueFormatter(new PercentFormatter());

        // space between slices
        dataSet.setSliceSpace(2f);

        dataSet.setColors(ColorTemplate.Custom2_COLORS);

        return new PieData(dataSet);
    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }
}