package com.hrptech.expensemanager.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hrptech.expensemanager.R;
import com.hrptech.expensemanager.ui.home.BudgetBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 12/5/17.
 */

public class PaginationBudgetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<BudgetBeans> categoryBeans;
    private Activity context;

    private boolean isLoadingAdded = false;

    public PaginationBudgetListAdapter(Activity context) {
        this.context = context;
        categoryBeans = new ArrayList<>();
    }

    public List<BudgetBeans> getMovies() {
        return categoryBeans;
    }

    public void setCategoryBeans(List<BudgetBeans> movieResults) {
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
        View v1 = inflater.inflate(R.layout.budget_of_item, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BudgetBeans beans = categoryBeans.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH viewHolder = (MovieVH) holder;

                String name = beans.getName().toUpperCase();
                String budget = beans.getAmount().toUpperCase();
                String expense = beans.getExpense().toUpperCase();
                String balance = beans.getBalance().toUpperCase();
                String per = beans.getPercentage().toUpperCase();
                String circle = beans.getName().toUpperCase().charAt(0)+"";
                viewHolder.name_txt.setText(name);
                viewHolder.circle_name.setText(circle);
                viewHolder.budget_txt.setText(budget);
                viewHolder.expense_txt.setText(expense);
                viewHolder.balance_txt.setText(balance);
                viewHolder.per_txt.setText(per);




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

    public void add(BudgetBeans r) {
        categoryBeans.add(r);
        notifyItemInserted(categoryBeans.size() - 1);
    }
String search = "";
    public void addAll(List<BudgetBeans> moveResults1, String search) {
            categoryBeans.clear();
        for (BudgetBeans result : moveResults1) {
            add(result);
        }
        this.search = search;
    }

    public void remove(BudgetBeans r) {
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
        add(new BudgetBeans());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = categoryBeans.size() - 1;
        BudgetBeans result = getItem(position);

        if (result != null) {
            categoryBeans.remove(position);
            notifyItemRemoved(position);
        }
    }

    public BudgetBeans getItem(int position) {
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
        private TextView budget_txt;
        private TextView expense_txt;
        private TextView balance_txt;
        private TextView per_txt;


        public MovieVH(View view) {
            super(view);

            circle_name = (TextView)view.findViewById(R.id.circle_name);
            name_txt = (TextView)view.findViewById(R.id.name_txt);
            budget_txt = (TextView)view.findViewById(R.id.budget_txt);
            expense_txt = (TextView)view.findViewById(R.id.expense_txt);
            balance_txt = (TextView)view.findViewById(R.id.balance_txt);
            per_txt = (TextView)view.findViewById(R.id.per_txt);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}