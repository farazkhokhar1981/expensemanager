package com.hrptech.expensemanager.ui.backup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.BudgetBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupRestoreFragment extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    LinearLayout saveBtn;
    ImageView addBtn;
    private RecyclerView budgetList;
    BackupViewAdapter budgetViewAdapter;
    public static BackupRestoreFragment budgetFragment;
    public static BackupRestoreFragment getCategoryFragment(){
        return budgetFragment;
    }
    Activity root;
    int onStartCount = 1;
    public Activity getActivity(){
        return root;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_backupandrestore);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        root = this;
        budgetFragment = this;
        String year = new SimpleDateFormat("yyyy").format(new Date());
        budgetList = (RecyclerView) root.findViewById(R.id.backupList);
        budgetList.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        budgetList.setLayoutManager(horizontalManager);
        //read from sd card

//        ArrayList<BudgetBeans> budgetBeansArrayList = budgetDB.getBudgetRecords();
//        budgetViewAdapter = new BackupViewAdapter(this.getActivity(),budgetBeansArrayList);
//        budgetList.setAdapter(budgetViewAdapter);
        saveBtn = (LinearLayout) root.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()){
                    //exportDB(BackupRestoreFragment.this.getActivity());
                    String date = new SimpleDateFormat("yyyyMMdd").format(new Date())+""+new SimpleDateFormat("hhmm").format(Calendar.getInstance().getTime());
                    String filePath = Utilities.exportDBZipReturn(BackupRestoreFragment.this, "", date+"_SystemCreatedBackup.DB");
                    if(!filePath.equalsIgnoreCase("")){
                        if(checkAndRequestPermissions()){
                            RefreshRecord();
                        }
                    }
                }
            }
        });

        if(checkAndRequestPermissions()){
            RefreshRecord();
        }
        addBtn = (ImageView) root.findViewById(R.id.back_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     backToMore(new Intent(BackupRestoreFragment.this, MoreActivity.class));
            }
        });


        //ads of Admob
       // AdsInit();
        LoadBanner();
    }

    public void AdsInit(){
        mInterstitialAd = new InterstitialAd(this.getActivity());

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        if(Utilities.isLoadAdsWhenOpened) {
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                    }
                }
            });
        }
        LoadBanner();
    }

    public void LoadBanner(){
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        String path = this.getActivity().getExternalFilesDir(null).getAbsolutePath()+"\\hrptechExpense";
        ArrayList<BudgetBeans> budgetBeansArrayList = new ArrayList<>();
        List<File> files = getListFiles(new File(path));
        if(files.size()>2) {
            FrameLayout frameLayout = root.
                    findViewById(R.id.fl_adplaceholder);
            Utilities.refreshAd(getActivity(), frameLayout);
        }
    }
    InterstitialAd mInterstitialAd;



    public void RefreshRecord(){
       // String path = this.getActivity().getExternalFilesDir(null).getAbsolutePath()+"\\hrptechExpense";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+File.separator+"hrptechexpense");
        String path = newFile.getAbsolutePath();
        ArrayList<BudgetBeans> budgetBeansArrayList = new ArrayList<>();
        CreateDBToZipFiles(new File(path));
        List<File> files = getListFiles(new File(path));
        for(int index=0; index<files.size(); index++){
            File file = files.get(index);
            String paths = file.getAbsolutePath();
            BudgetBeans beans = new BudgetBeans();
            beans.setCat_name(paths);
            beans.setCat_type(file.getName());
            budgetBeansArrayList.add(beans);
        }

        budgetViewAdapter = new BackupViewAdapter(this.getActivity(),budgetBeansArrayList);
        budgetList.setAdapter(budgetViewAdapter);
    }

    public void ShowRecordOFBudgetForUpdate(String id){
//        BudgetBeans budgetBeans = budgetDB.getBudgetRecordSingle(id);
//        if(budgetBeans!=null){
//            String cName = budgetBeans.getCat_name();
//            String year = budgetBeans.getYear();
//            String month = budgetBeans.getMonth();
//            String amount = budgetBeans.getAmount();
//            selected_Id = id;
//        }
    }
    public void Refresh(){
        selected_Id="";

    }
    String selected_Id = "";


    //backup

    public void exportDB(Context context) {
        try {
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String time = new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime());
            String path = context.getExternalFilesDir(null).getAbsolutePath()+"\\hrptechExpense";
            File sd = new File(path);
            File data = Environment.getDataDirectory();

            if(!sd.mkdir()){
                sd.mkdirs();
            }
            if (sd.canWrite()) {
                String backupDBPath = String.format("%s.bak", Utilities.DBNAME);
                File currentDB = context.getDatabasePath(Utilities.DBNAME);
                File backupDB = new File(sd, date+"_"+time+"_"+backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(this.getActivity(),"Backup successfully created",Toast.LENGTH_LONG).show();
                RefreshRecord();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //restore

    public void importDB(Context context,String path) {
        try {
            File sd = new File(path);
            if (sd.canWrite()) {
                File backupDB = context.getDatabasePath(Utilities.DBNAME);

                FileChannel src = new FileInputStream(sd).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(this.getActivity(),"Backup successfully restored",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        RefreshRecord();
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

    //read
    private void CreateDBToZipFiles(File parentDir)
    {

        File[] files = parentDir.listFiles();
        try {
            for (File file : files) {

                //if (file.getName().endsWith(".txt"))

                if (file.isDirectory()) {

                } else {
                    if (file.getName().endsWith(".BAK")) {
                        String []filesTop = new String[1];
                        String zipFileName = "";
                        filesTop[0] = file.getAbsolutePath();
                        zipFileName = file.getAbsolutePath()+".zip";
                        Utilities.zip(filesTop,zipFileName);
                        // inFiles.add(file);
                    }
                }
            }
        }catch(Exception e){

        }

    }
    //read
    private List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        try {
            for (File file : files) {

                //if (file.getName().endsWith(".txt"))

                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".zip")) {
                        inFiles.add(file);
                    }
                }
            }
        }catch(Exception e){
            return inFiles;
        }
        return inFiles;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    String prodName = "MEDICINA";
    @Override
    public void onBackPressed() {
       // backToMore(new Intent(BackupRestoreFragment.this, MoreActivity.class));

    }

    public void backToMore(Intent intent){
        startActivity(intent);
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void DeleteFile(final File path){
        path.delete();
        RefreshRecord();
        budgetViewAdapter.notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ShareImageToWhatsapp(String fileName){
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+File.separator+"hrptechexpense");
        File filePath = new File(fileName);


        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(this, "com.hrptech.dailyexampenseapp", filePath);
        share.setDataAndType(uri, "application/x-wav");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(share);
    }


}