package com.hrptech.expensemanager.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.adapter.PaginationCategoryListAdapter;
import com.hrptech.expensemanager.beans.CATEGORY;
import com.hrptech.expensemanager.beans.SettingBeans;
import com.hrptech.expensemanager.beans.TransactionBeans;
import com.hrptech.expensemanager.db.CategoryDB;
import com.hrptech.expensemanager.db.SettingDB;
import com.hrptech.expensemanager.db.TransactionDB;
import com.hrptech.expensemanager.utility.PaginationScrollListener;
import com.hrptech.expensemanager.utility.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardActivity extends Activity {


    LinearLayout income_btn;
    LinearLayout expense_btn;
    TextView income_txt;
    TextView expense_txt;
    TextView balance_txt;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    PaginationCategoryListAdapter paginationCategoryListAdapter;
    ArrayList<CATEGORY> categoryBeans = new ArrayList<>();

    String months[] = {"Jan","Feb","Mar","Apr",
            "May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"};
    TransactionDB transactionDB;
    SettingDB settingDB;
    SettingBeans settingBeans;
    CategoryDB categoryDB;
    String currency;
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_dashboard);
        init();
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month =months[Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))-1];
        String month_ = Utilities.getMonth(months,month);
        TransactionBeans beansTransaction = transactionDB.getTransactionRecordsYear(month_,year);
        if(beansTransaction!=null){
            balance_txt.setText(currency+""+beansTransaction.getBalance());
        }
        changeButton("Income");

    }
boolean isLoading = false;
    int currentPage = 0;
    int TOTAL_PAGES = 0;
    boolean isLastPage = false;
    public void changeButton(final String type){
        if(type.equalsIgnoreCase("Income")){
            income_txt.setTextColor(getResources().getColor(R.color.colorWhite));
            income_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_fill_blue));
            expense_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            expense_btn.setBackground(null);
        }else {
            income_txt.setTextColor(getResources().getColor(R.color.colorBlack));
            income_btn.setBackground(null);
            expense_txt.setTextColor(getResources().getColor(R.color.colorWhite));
            expense_btn.setBackground(getResources().getDrawable(R.drawable.button_rounded_fill_blue));
        }
        expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButton("Expense");
            }
        });
        income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButton("Income");
            }
        });
        categoryBeans =categoryDB.getCategoryRecords(type);
        loadFirstPage(categoryBeans,"");

    }

    public void loadFirstPage(List<CATEGORY> transactionBeansArrayList1, String search) {


        //List<TransactionBeans> transactionBeansArrayList1 = transactionDB.getTransactionRecordsDateAllRecords(selectedCompanyName,search,fromDate_txt.getText().toString(),toDate_txt.getText().toString());
        paginationCategoryListAdapter.addAll(transactionBeansArrayList1,search);

        paginationCategoryListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(paginationCategoryListAdapter);
        if (currentPage <= TOTAL_PAGES) paginationCategoryListAdapter.addLoadingFooter();
        else isLastPage = true;
        progressBar.setVisibility(View.GONE);

    }

    private void loadNextPage(String search) {

        paginationCategoryListAdapter.removeLoadingFooter();
        isLoading = false;
        //progressBar.setVisibility(View.GONE);
        paginationCategoryListAdapter.addAll(categoryBeans,search);
        paginationCategoryListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(paginationCategoryListAdapter);
        if (currentPage != TOTAL_PAGES) paginationCategoryListAdapter.addLoadingFooter();
        else isLastPage = true;

    }

    public void init(){
        progressBar = (ProgressBar) findViewById(R.id.pro_bar);
        progressBar.setVisibility(View.VISIBLE);
        income_btn = (LinearLayout)findViewById(R.id.income_btn);
        expense_btn = (LinearLayout)findViewById(R.id.expense_btn);
        income_txt = (TextView) findViewById(R.id.income_txt);
        expense_txt = (TextView) findViewById(R.id.expense_txt);
        balance_txt = (TextView) findViewById(R.id.balance_txt);
        recyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        transactionDB = new TransactionDB(this);
        settingDB = new SettingDB(this);
        categoryDB = new CategoryDB(this);
        settingBeans = settingDB.getSettingRecordsSingle();
        if(settingBeans!=null){
            currency = settingBeans.getCurrency();
        }else {
            settingBeans = new SettingBeans();
            settingBeans.setLanguge("en");
            settingBeans.setCurrency("$");
            settingBeans.setDateformat("yyyy-MM-dd");
            settingDB.InsertRecord(settingBeans);
            currency = "$";
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(null);
        paginationCategoryListAdapter = new PaginationCategoryListAdapter(getActivity());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(paginationCategoryListAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage("");
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                categoryBeans =categoryDB.getCategoryRecords("Income");
                loadFirstPage(categoryBeans,"");
            }
        }, 1000);

    }


    public Activity getActivity(){
        return this;
    }
    public void LoadActivity(Intent intent,String value){
        intent.putExtra("trans",value);
        intent.putExtra("transactionId","");
        intent.putExtra("className","home");
        startActivity(intent);
        getActivity().finish();
    }

    String prodName = "MEDICINA";
    @Override
    public void onBackPressed() {
        showDialogClose();

    }


    public Dialog dialog = null;
    public Button no_btn= null;

    public Button yes_btn= null;
    public Button secure_btn= null;
    LinearLayout expenseManagerBtn;
    LinearLayout notebookBtn;

    @SuppressLint("NewApi")
    public void showDialogClose(){
        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.closedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        expenseManagerBtn = (LinearLayout) dialog.findViewById(R.id.expenseManager_btn);
        expenseManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(Uri.parse("https://play.google.com/store/apps/details?id=com.hrptech.khata"));
            }
        });


        notebookBtn = (LinearLayout)dialog.findViewById(R.id.noteBook_btn);
        notebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreAppActivity(Uri.parse("https://play.google.com/store/apps/details?id=notebook.com.notebook"));
            }
        });
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

                System.exit(0);
                dialog.dismiss();
            }
        });

        secure_btn = (Button)dialog.findViewById(R.id.secure_No_btn);
        secure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()) {
                    String filePath = Utilities.exportDBZipReturn(DashBoardActivity.this, "", "SystemCreatedBackup.DB");
                    if (!filePath.equalsIgnoreCase("")) {
                        Utilities.ShareImageToWhatsappBackup(DashBoardActivity.this, filePath);
                    }
                }
                //dialog.dismiss();
            }
        });
        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
        dialog.show();

    }

    public void loadMoreAppActivity(Uri uri){
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }




    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this.getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //RefreshRecord();
                    } else {
                        Toast.makeText(this.getActivity(), "Some permissions are not granted ask again ",Toast.LENGTH_LONG).show();
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this.getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}