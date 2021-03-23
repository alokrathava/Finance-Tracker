package com.example.spendit.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendit.data.BudgetData;
import com.example.spendit.databinding.LayoutbudgetBinding;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    List<BudgetData> budgetDataList;
    private BudgetInterface budgetInterface;

    public BudgetAdapter(List<BudgetData> budgetDataList, BudgetInterface budgetInterface) {
        this.budgetDataList = budgetDataList;
        this.budgetInterface = budgetInterface;
    }


    @NonNull
    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutbudgetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.ViewHolder holder, int position) {
        BudgetData budgetData = budgetDataList.get(position);
        int cnt = ++position;
        holder.binding.serialno.setText(String.valueOf(cnt));
        holder.binding.categoryName.setText(budgetData.getCategories_name());
        holder.binding.budget.setText("₹ " + budgetData.getAmountSet());
        holder.binding.deletebtn.setOnClickListener(v -> {
            budgetInterface.onDelete(budgetData);
        });
    }

    @Override
    public int getItemCount() {
        return budgetDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutbudgetBinding binding;

        public ViewHolder(@NonNull LayoutbudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface BudgetInterface {
        void onDelete(BudgetData budgetData);
    }
}
