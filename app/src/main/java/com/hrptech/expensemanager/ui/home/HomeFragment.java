package com.hrptech.expensemanager.ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.BudgetDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.listviewitems.ChartItem;
import com.hrptech.expensemanager.ui.transaction.TransactionExpenseActivity;
import com.hrptech.expensemanager.ui.transaction.TransactionIncomeActivity;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    //hello
    private RecyclerView dailyTransactionList;
    private RecyclerView monthlyTransactionList;
    private RecyclerView yearlyTransactionList;
    private RecyclerView budgetList;
    private DailyTransactionViewAdapter dailyTransactionViewAdapter = null;
    private MonthTransactionViewAdapter monthlyTransactionViewAdapter = null;
    private YearlyTransactionViewAdapter yearlyTransactionViewAdapter = null;
    private BudgetViewAdapter budgetViewAdapter = null;
    private ListView listView = null;
    private TextView income_txt;
    private TextView expense_txt;
    private TextView balance_txt;
    BudgetDB budgetDB;
    TransactionDB transactionDB;
    DatePickerDialog datePickerDialog;
    LinearLayout calenderBtn;
    TextView date_txt;
    TextView month_txt;
    TextView year_txt;
    TextView openingFinal_txt;
    TextView incomeFina_txt;
    TextView expenseFinal_txt;
    TextView balanceFinal_txt;

    LinearLayout dailyTransaction_btn;
    LinearLayout addIncome_btn;
    LinearLayout addExpense_btn;

    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        dailyTransaction_btn = (LinearLayout) root.findViewById(R.id.daily_transaction_btn);
        dailyTransaction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), DailyTransactionActivity.class));
//                getActivity().finish();
            }
        });
        addIncome_btn = (LinearLayout) root.findViewById(R.id.addIncome_btn);
        addIncome_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(new Intent(getActivity(), TransactionIncomeActivity.class));
            }
        });

        addExpense_btn = (LinearLayout) root.findViewById(R.id.addExpense_btn);
        addExpense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadActivity(new Intent(getActivity(), TransactionExpenseActivity.class));
            }
        });

        listView =(ListView)root.findViewById(R.id.listView1);
        date_txt = (TextView)root.findViewById(R.id.date_txt);
        month_txt = (TextView)root.findViewById(R.id.month_txt);
        year_txt = (TextView)root.findViewById(R.id.year_txt);
        openingFinal_txt = (TextView)root.findViewById(R.id.openingfinal_txt);
        incomeFina_txt = (TextView)root.findViewById(R.id.incomeFinal_txt);
        expenseFinal_txt = (TextView)root.findViewById(R.id.expensefinal_txt);
        balanceFinal_txt = (TextView)root.findViewById(R.id.closingfinal_txt);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String month =months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))-1];
        String year = new SimpleDateFormat("yyyy").format(new Date());
        date_txt.setText(date);
        month_txt.setText(month);
        year_txt.setText(year);
        budgetDB = new BudgetDB(this.getActivity());
        transactionDB = new TransactionDB(this.getActivity());
        income_txt = (TextView)root.findViewById(R.id.income_txt);
        expense_txt = (TextView)root.findViewById(R.id.expense_txt);
        balance_txt = (TextView)root.findViewById(R.id.balance_txt);
        dailyTransactionList = (RecyclerView) root.findViewById(R.id.dailyTransactionList);
        monthlyTransactionList = (RecyclerView) root.findViewById(R.id.monthlyTransactionList);
        yearlyTransactionList = (RecyclerView) root.findViewById(R.id.yearlyTransactionList);
        budgetList = (RecyclerView) root.findViewById(R.id.budgetList);
        dailyTransactionList.setHasFixedSize(true);
        monthlyTransactionList.setHasFixedSize(true);
        yearlyTransactionList.setHasFixedSize(true);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        dailyTransactionList.setLayoutManager(horizontalManager);
        LinearLayoutManager monthlyManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        yearlyTransactionList.setLayoutManager(layoutManager);
        monthlyTransactionList.setLayoutManager(monthlyManager);
        LinearLayoutManager budgetManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(budgetManager);


        calenderBtn = (LinearLayout) root.findViewById(R.id.cander_btn);
        calenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDatePicker();
            }
        });
        LoadAll();
        //ads of Admob
        AdsInit();
        //LoadBanner();
        return root;
    }

    public void LoadActivity(Intent intent){
        startActivity(intent);
        getActivity().finish();
    }
    public void AdsInit(){
        mInterstitialAd = new InterstitialAd(this.getActivity());

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
//        if(Utilities.isLoadAdsWhenOpened) {
//            mInterstitialAd.loadAd(adRequest);
//            mInterstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//
//                    }
//                }
//            });
//        }
        LoadBanner();
    }

    public void LoadBanner(){
//        AdView mAdView = (AdView) root.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }
    InterstitialAd mInterstitialAd;

    public void LoadAll(){
        MainTopTransaction();
        DailyTransaction();
        MonthlyTransaction();
        YearlyTransaction();
        BudgetTransaction();
        ChartTransaction();
        MainBottomTransaction();
    }
    public void MainTopTransaction(){
        String date = date_txt.getText().toString();
        TransactionBeans beansTransaction = transactionDB.getTransactionByDate(date);
        if(beansTransaction!=null){
            income_txt.setText(beansTransaction.getIncome());
            expense_txt.setText(beansTransaction.getExpense());
            balance_txt.setText(beansTransaction.getBalance());
        }
    }
    public void DailyTransaction(){
        String date = date_txt.getText().toString();
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsDate(date);
        dailyTransactionViewAdapter = new DailyTransactionViewAdapter(this.getActivity(),transactionBeans);
        dailyTransactionList.setAdapter(dailyTransactionViewAdapter);
    }
    String months[] = {"Jan","Feb","Mar","Apr",
            "May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"};


    public void MonthlyTransaction(){
        String year = year_txt.getText().toString();
        String month =Utilities.getMonth(months,month_txt.getText().toString());
        String date = year+"-"+month+"-01";
        double opening = transactionDB.getTransactionByOpeningBalance(date);
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsMonth(month,year);

        List<DailyTransactionBeans> beansMonthlyList = new ArrayList<>();
        double balance = 0;
        double lastBalance = 0;
        for(int index=0; index<transactionBeans.size(); index++){
            TransactionBeans beans = transactionBeans.get(index);
            String day = Utilities.getDayFromDate(beans.getDate(),"yyyy-MM-dd");
            if(index==0){
                balance+=Double.parseDouble(beans.getBalance())+opening;
                lastBalance = balance;
                beansMonthlyList.add(getMonthlyList(day,month_txt.getText().toString(),new DecimalFormat("0.00").format(opening),beans.getIncome(),beans.getExpense(),new DecimalFormat("0.00").format(balance)));
            }else {
                balance=Double.parseDouble(beans.getBalance())+lastBalance;
                beansMonthlyList.add(getMonthlyList(day,month_txt.getText().toString(),new DecimalFormat("0.00").format(lastBalance),beans.getIncome(),beans.getExpense(),new DecimalFormat("0.00").format(balance)));
                lastBalance+=Double.parseDouble(beans.getBalance());
            }

        }

        monthlyTransactionViewAdapter = new MonthTransactionViewAdapter(this.getActivity(),beansMonthlyList);
        monthlyTransactionList.setAdapter(monthlyTransactionViewAdapter);
    }

    public DailyTransactionBeans getMonthlyList(String day,String month,String opening,String income,String expense,String balance){
        DailyTransactionBeans beans = new DailyTransactionBeans();
        beans.setDay(day);
        beans.setMonth(month);
        beans.setOpening(opening);
        beans.setIncome(income);
        beans.setExpense(expense);
        beans.setBalance(balance);
        return beans;
    }

    public void YearlyTransaction(){
        String year = year_txt.getText().toString();
        List<DailyTransactionBeans> beansYearList = new ArrayList<>();
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("01",year),"Jan"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("02",year),"Feb"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("03",year),"Mar"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("04",year),"Apr"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("05",year),"May"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("06",year),"Jun"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("07",year),"Jul"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("08",year),"Aug"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("09",year),"Sep"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("10",year),"Oct"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("11",year),"Nov"));
        beansYearList.add(getDailyList(transactionDB.getTransactionRecordsYear("12",year),"Dec"));

        yearlyTransactionViewAdapter = new YearlyTransactionViewAdapter(this.getActivity(), beansYearList);
        yearlyTransactionList.setAdapter(yearlyTransactionViewAdapter);
    }

    public void BudgetTransaction(){
        String year = year_txt.getText().toString();
        String month = month_txt.getText().toString();
        String month1 =Utilities.getMonth(months,month_txt.getText().toString());
        ArrayList<TransactionBeans> listOfBudget = transactionDB.getBudgetRecordsMonth(month,year);
        List<BudgetBeans> budgetBeansList = new ArrayList<>();
        for(int index=0; index<listOfBudget.size(); index++){
            TransactionBeans beans = listOfBudget.get(index);
            String cid = beans.getCid();
            double budgetAmount = Double.parseDouble(beans.getExpense());
            double expense = transactionDB.getTransactionExpense(year,month1,cid);
            double remaining = budgetAmount-expense;
            double per = expense/budgetAmount*100;
            budgetBeansList.add(getBudgetList(beans.getName(),new DecimalFormat("0.00").format(budgetAmount),new DecimalFormat("0.00").format(expense),new DecimalFormat("0.00").format(remaining),new DecimalFormat("0.00").format(per)+"%",per));
        }

        budgetViewAdapter = new BudgetViewAdapter(this.getActivity(),budgetBeansList);
        budgetList.setAdapter(budgetViewAdapter);
    }



    public void ChartTransaction(){
        ArrayList<ChartItem> list = new ArrayList<>();

        // 30 items

                LineData lineData = generateDataLine();
             //   list.add(new LineChartItem(lineData, this.getActivity()));
             //   list.add(new BarChartItem(generateDataBar(), this.getActivity()));
               // list.add(new PieChartItem(generateDataPie(), this.getActivity()));



        ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(), list);
        listView.setAdapter(cda);
    }

    public void MainBottomTransaction(){
        String date = date_txt.getText().toString();
        TransactionBeans beansTransaction = transactionDB.getTransactionByDate(date);
        double balance = 0;

        double openingBalance = transactionDB.getTransactionByOpeningBalance(date);
        openingFinal_txt.setText(new DecimalFormat("0.00").format(openingBalance));

        if(beansTransaction!=null){
            incomeFina_txt.setText(beansTransaction.getIncome());
            expenseFinal_txt.setText(beansTransaction.getExpense());
            try{
                balance = Double.parseDouble(beansTransaction.getBalance());
            }catch(Exception e){
                try{
                    balance = Integer.parseInt(beansTransaction.getBalance());
                }catch (Exception ex){
                    balance = 0;
                }
            }
        }
        balance = balance+openingBalance;

        balanceFinal_txt.setText(new DecimalFormat("0.00").format(balance));
    }
    public DailyTransactionBeans getDailyList(TransactionBeans beansTransactionBeans,String month){
        DailyTransactionBeans beans = new DailyTransactionBeans();
        beans.setDay("");
        beans.setMonth(month);
        beans.setOpening("0");
        beans.setIncome("0.00");
        beans.setExpense("0.00");
        beans.setBalance("0.00");
        if(beansTransactionBeans!=null) {
            beans.setIncome(beansTransactionBeans.getIncome());
            beans.setExpense(beansTransactionBeans.getExpense());
            beans.setBalance(beansTransactionBeans.getBalance());
        }
        return beans;
    }

    public BudgetBeans getBudgetList(String name,String amount,String expense,String balance,String percentage,double per){
        BudgetBeans beans = new BudgetBeans();
        beans.setName(name);
        beans.setAmount(amount);
        beans.setExpense(expense);
        beans.setBalance(balance);
        beans.setPercentage(percentage);
        beans.setPer(per);
        return beans;
    }
    public String getDay(int day){
        if(day<10 && !(day+"").contains("0")){
            return "0"+day;
        }
        return day+"";
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
    private LineData generateDataLine() {
        String year = year_txt.getText().toString();
        String month1 =Utilities.getMonth(months,month_txt.getText().toString());
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsMonth(month1,year);
        for (int indexIncome = 0; indexIncome < transactionBeans.size(); indexIncome++) {
            values1.add(new Entry(indexIncome, (int)(Double.parseDouble(transactionBeans.get(indexIncome).getIncome()))));
        }

        LineDataSet d1 = new LineDataSet(values1, "Income, (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int indexIncome = 0; indexIncome < transactionBeans.size(); indexIncome++) {
            values2.add(new Entry(indexIncome, (int)(Double.parseDouble(transactionBeans.get(indexIncome).getExpense()))));
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

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateDataBar() {
        String year = year_txt.getText().toString();
        String month1 =Utilities.getMonth(months,month_txt.getText().toString());
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<TransactionBeans> transactionBeans = transactionDB.getTransactionRecordsMonth(month1,year);
        for (int i = 0; i < transactionBeans.size(); i++) {
            TransactionBeans beans = transactionBeans.get(i);
            entries.add(new BarEntry(i, (int) (Double.parseDouble(beans.getIncome()))));
        }

        BarDataSet d = new BarDataSet(entries, "");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        cd.setDrawValues(false);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Pie data
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        String year = year_txt.getText().toString();
        String month1 =Utilities.getMonth(months,month_txt.getText().toString());
        TransactionBeans transactionBeans = transactionDB.getTransactionRecordsYear(month1,year);
        if(transactionBeans!=null) {
            float income = 0;
            float expense = 0;
            try{
                income = (float) (Double.parseDouble(transactionBeans.getIncome()));
            }catch(Exception e){
                income = 0;
            }
            try{
                expense = (float) (Double.parseDouble(transactionBeans.getExpense()));
            }catch(Exception e){
                expense = 0;
            }
            entries.add(new PieEntry(income, "Income "));
            entries.add(new PieEntry(expense, "Expense "));
        }
        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new PieData(d);
    }

    Calendar calendar = null;
    int year,month,dayOfMonth;
    public void LoadDatePicker(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(HomeFragment.this.getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date_txt.setText(year + "-" + Utilities.getIndexZero((month + 1)) + "-" + Utilities.getIndexZero(day));
                        String month1 =months[month];
                        month_txt.setText(month1);
                        year_txt.setText(year+"");
                        LoadAll();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

}