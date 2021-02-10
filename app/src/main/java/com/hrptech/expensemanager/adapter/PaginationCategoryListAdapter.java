package com.hrptech.expensemanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.beans.CATEGORY;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 12/5/17.
 */

public class PaginationCategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<CATEGORY> categoryBeans;
    private Activity context;

    private boolean isLoadingAdded = false;

    public PaginationCategoryListAdapter(Activity context) {
        this.context = context;
        categoryBeans = new ArrayList<>();
    }

    public List<CATEGORY> getMovies() {
        return categoryBeans;
    }

    public void setCategoryBeans(List<CATEGORY> movieResults) {
        this.categoryBeans = categoryBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                v2.setVisibility(View.VISIBLE);
                if(categoryBeans.size()>0){
                    v2.setVisibility(View.GONE);
                }
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_of_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CATEGORY beans = categoryBeans.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH viewHolder = (MovieVH) holder;

                String name = beans.getName().toUpperCase();
                String circle = beans.getName().toUpperCase().charAt(0)+"";
                viewHolder.name_txt.setText(name);
                viewHolder.circle_name.setText(circle);




                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return categoryBeans == null ? 0 : categoryBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == categoryBeans.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(CATEGORY r) {
        categoryBeans.add(r);
        notifyItemInserted(categoryBeans.size() - 1);
    }
String search = "";
    public void addAll(List<CATEGORY> moveResults1, String search) {
            categoryBeans.clear();

        for (CATEGORY result : moveResults1) {
            add(result);
        }
        this.search = search;
    }

    public void remove(CATEGORY r) {
        int position = categoryBeans.indexOf(r);
        if (position > -1) {
            categoryBeans.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new CATEGORY());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = categoryBeans.size() - 1;
        CATEGORY result = getItem(position);

        if (result != null) {
            categoryBeans.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CATEGORY getItem(int position) {
        return categoryBeans.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView name_txt;
        private TextView circle_name;

        public MovieVH(View view) {
            super(view);

            circle_name = (TextView)view.findViewById(R.id.circle_name);
            name_txt = (TextView)view.findViewById(R.id.name_txt);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}