package com.hrptech.expensemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hrptech.expensemanager.moreapp.MoreAppsForm;
import com.hrptech.expensemanager.ui.backup.BackupRestoreFragment;
import com.hrptech.expensemanager.ui.budget.BudgetFragment;
import com.hrptech.expensemanager.ui.category.CategoryActivity;
import com.hrptech.expensemanager.ui.contact.ContactUsActivity;
import com.hrptech.expensemanager.ui.dailytran.DailyTransactionActivity;
import com.hrptech.expensemanager.ui.documents.DocumentsActivity;
import com.hrptech.expensemanager.ui.home.HomeActivity;
import com.hrptech.expensemanager.ui.report.ReportFragment;
import com.hrptech.expensemanager.ui.webserver.WebServerFragment;
import com.hrptech.expensemanager.utility.Utilities;

import java.util.Locale;

public class MoreActivity extends Activity {




    LinearLayout home_btn;
    LinearLayout add_category_btn;
    LinearLayout modification_btn;
    LinearLayout daily_transaction_btn;
    LinearLayout web_server_btn;
    LinearLayout report_btn;
    LinearLayout backup_btn;
    LinearLayout budget_btn;
    LinearLayout howtouse_btn;
    LinearLayout document_btn;
    LinearLayout feedback_btn;
    LinearLayout moreapp_btn;
    LinearLayout shareapp_btn;
    LinearLayout contactus_btn;
    LinearLayout quit_btn;




    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_more);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        init();
        Utilities.LoadBanner((AdView)findViewById(R.id.adView));
        Utilities.LoadBanner((AdView)findViewById(R.id.adView1));
        Utilities.LoadBanner((AdView)findViewById(R.id.adView2));
       // Utilities.LoadBanner((AdView)findViewById(R.id.adView3));
    }


    public void init(){
            home_btn = (LinearLayout)findViewById(R.id.home_btn);
            home_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this,HomeActivity.class));
                }
            });

            add_category_btn = (LinearLayout)findViewById(R.id.nav_category);
            add_category_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, CategoryActivity.class));
                }
            });

            modification_btn = (LinearLayout)findViewById(R.id.modification_btn);
            modification_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this,MainActivity.class));
                }
            });

            daily_transaction_btn = (LinearLayout)findViewById(R.id.daily_transaction_btn);
            daily_transaction_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, DailyTransactionActivity.class));
                }
            });

            web_server_btn = (LinearLayout)findViewById(R.id.nav_webserver);
            web_server_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, WebServerFragment.class));
                }
            });

            report_btn = (LinearLayout)findViewById(R.id.report_btn);
            report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, ReportFragment.class));
                }
            });

            backup_btn = (LinearLayout)findViewById(R.id.backup_btn);
            backup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, BackupRestoreFragment.class));
                }
            });

            budget_btn = (LinearLayout)findViewById(R.id.nav_budget);
            budget_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, BudgetFragment.class));
                }
            });

            howtouse_btn = (LinearLayout)findViewById(R.id.howtoplay_btn);
            howtouse_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this,YoutubeVideoLinkActivity.class));
                }
            });

            document_btn = (LinearLayout)findViewById(R.id.documents_btn);
            document_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, DocumentsActivity.class));
                }
            });

            feedback_btn = (LinearLayout)findViewById(R.id.feedback_btn);
            feedback_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GotoReview();
                }
            });

            moreapp_btn = (LinearLayout)findViewById(R.id.moreapps_btn);
            moreapp_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, MoreAppsForm.class));
                }
            });

            shareapp_btn = (LinearLayout)findViewById(R.id.share_btn);
            shareapp_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareApp();
                }
            });

            contactus_btn = (LinearLayout)findViewById(R.id.contactus_btn);
            contactus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadMoreActivity(new Intent(MoreActivity.this, ContactUsActivity.class));
                }
            });

            quit_btn = (LinearLayout)findViewById(R.id.quit_btn);
            quit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogClose();
                }
            });




        LoadBanner();
    }
    public void LoadBanner(){
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
                //loadMoreAppActivity(Uri.parse("https://play.google.com/store/apps/details?id=com.hrptech.dailyexampenseapp"));
            }
        });


        notebookBtn = (LinearLayout)dialog.findViewById(R.id.noteBook_btn);
        notebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadMoreAppActivity(Uri.parse("https://play.google.com/store/apps/details?id=notebook.com.notebook"));
            }
        });
        expenseManagerBtn.setVisibility(View.GONE);
        notebookBtn.setVisibility(View.GONE);
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

                String filePath = Utilities.exportDBZipReturn(MoreActivity.this,"","SystemCreatedBackup.DB");
                if(!filePath.equalsIgnoreCase("")){
                    Utilities.ShareImageToWhatsappBackup(MoreActivity.this,filePath);
                }
                //dialog.dismiss();
            }
        });
        FrameLayout frameLayout = dialog.findViewById(R.id.fl_adplaceholder);
        Utilities.refreshAd(this,frameLayout);
        dialog.show();

    }

    public void LoadLockActivity(String status){
        Intent intent = new Intent();
//        if(status.equalsIgnoreCase("change")){
//            intent.setClass(this,OldPinCodeActivity.class);
//            intent.putExtra("lock",status);
//        }else if(status.equalsIgnoreCase("delete") || status.equalsIgnoreCase("new")){
//            intent.setClass(this,PinCodeActivity.class);
//            intent.putExtra("lock",status);
//
//        }
        startActivity(intent);
        finish();
    }

    public void shareApp(){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.hrptech.dailyexampenseapp");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hrptech.dailyexampenseapp")));
        }
    }

    public void LoadMoreActivity(Intent intent){
//        SettingBeans settingBeans = settingDB.getSettings();
//        if(settingBeans!=null) {
//            String localeName = settingBeans.getLanguge();
//            myLocale = new Locale(localeName);
//            Configuration conf = new Configuration(getResources().getConfiguration());
//            conf.locale = myLocale;
//            if (Build.VERSION.SDK_INT >= 17) {
//                conf.setLayoutDirection(myLocale);
//            }
//            getBaseContext().getResources().updateConfiguration(conf,
//                    getBaseContext().getResources().getDisplayMetrics());
//
//        }
        startActivity(intent);
            finish();
    }


    public void GotoReview(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }
      //  Utilities.isLoadAdsWhenOpened = true;


        //updateUI(account);
    }


    @Override
    protected void onStop() {
        super.onStop();
      //  Utilities.isLoadAdsWhenOpened = false;
    }









    Locale myLocale;
    String currentLanguage = "en", currentLang;
    public void setLocale(String localeName) {
            myLocale = new Locale(localeName);
            currentLanguage = localeName;
//            UpdateSetting();
//            Refresh();
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);


    }



    public void setLocaleFirst(String localeName) {
            myLocale = new Locale(localeName);
            currentLanguage = localeName;
            //UpdateSetting();
        //Refresh();
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        //


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
                LoadMoreActivity(new Intent(MoreActivity.this, HomeActivity.class));

    }







}
