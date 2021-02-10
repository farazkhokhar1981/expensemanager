package com.hrptech.expensemanager.ui.documents;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hrptech.expensemanager.MoreActivity;
import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.ImageBeans;
import com.hrptech.expensemanager.utility.Utilities;

import java.io.File;
import java.util.ArrayList;


public class DocumentsActivity extends Activity {
    DocumentsViewAdapter documentsViewAdapter = null;
    RecyclerView recyclerView = null;
    private static DocumentsActivity initName;
    public static DocumentsActivity getInstance(){
        return initName;
    }
    Activity root;
    public Activity getActivity(){
        return this;
    }
    TextView noFoundTxt;
    ImageView back_btn;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_documents);

        root = this;
initName = this;
        noFoundTxt = (TextView) findViewById(R.id.noFoundTxt);
        back_btn = (ImageView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMainActivity(new Intent(DocumentsActivity.this, MoreActivity.class));
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(null);
        imageBeansArrayList = new ArrayList<>();
        LoadScannerImages();
        if(imageBeansArrayList.size()>0){
            noFoundTxt.setVisibility(View.GONE);
        }
        documentsViewAdapter = new DocumentsViewAdapter(this.getActivity(),imageBeansArrayList,false);
        recyclerView.setAdapter(documentsViewAdapter);
        if(imageBeansArrayList.size()>1) {
            FrameLayout frameLayout = root.
                    findViewById(R.id.fl_adplaceholder);
            Utilities.refreshAd(getActivity(), frameLayout);
        }
    }
    ArrayList<ImageBeans> imageBeansArrayList;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void LoadScannerImages(){
        if(imageBeansArrayList.size()>0){
            imageBeansArrayList.clear();
        }
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File newFile = new File(directory.getAbsolutePath()+""+ File.separator+"hrptechexpense"+ File.separator+"doc");
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        String[]list = newFile.list();
        File[] fList = newFile.listFiles();
        int nList = 0;
        int mList = 0;
        if(list!=null){
            for(int index=0; index<list.length; index++){
                ImageBeans beans = new ImageBeans();
                String name = list[index];
                String path = fList[index].getAbsolutePath();
                if(name.contains("pdf")) {
                    beans.setTitle(name);
                    beans.setDateTime("");
                    beans.setPath(path);
                    beans.setSize("");
                    imageBeansArrayList.add(beans);
                }
            }
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void DeleteFile(final File path){
        path.delete();
        LoadScannerImages();
        documentsViewAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        LoadMainActivity(new Intent(DocumentsActivity.this, MoreActivity.class));

    }

    public void LoadMainActivity(Intent intent){

            startActivity(intent);
            finish();

    }
}
