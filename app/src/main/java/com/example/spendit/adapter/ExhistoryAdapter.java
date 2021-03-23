package com.example.spendit.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendit.R;
import com.example.spendit.data.Exphistory;
import com.example.spendit.databinding.LayoutexpenseBinding;

import java.util.List;

public class ExhistoryAdapter extends RecyclerView.Adapter<ExhistoryAdapter.ViewHolder> {

    private List<Exphistory> exphistories;
    private ExHistoryInterface exHistoryInterface;

    public ExhistoryAdapter(List<Exphistory> exphistories, ExHistoryInterface exHistoryInterface) {
        this.exphistories = exphistories;
        this.exHistoryInterface = exHistoryInterface;
    }


    @NonNull
    @Override
    public ExhistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutexpenseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExhistoryAdapter.ViewHolder holder, int position) {

        Exphistory exphistory = exphistories.get(position);
        int cnt = ++position;
        int amount = Integer.parseInt(exphistory.getAmount());
        int budget = Integer.parseInt(exphistory.getAmountSet());
        if (amount > budget) {
            holder.binding.amount.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.binding.amount.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            holder.binding.amountSet.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.binding.amountSet.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            holder.binding.serialno.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.binding.serialno.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            holder.binding.categoryName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
            holder.binding.categoryName.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            /*Value*/
            holder.binding.amount.setText("₹ " + exphistory.getAmount());
            holder.binding.amountSet.setText(exphistory.getAmountSet());
            holder.binding.serialno.setText(String.valueOf(cnt));
            holder.binding.categoryName.setText(exphistory.getCategoriesName());
        } else {
            holder.binding.serialno.setText(String.valueOf(cnt));
            holder.binding.categoryName.setText(exphistory.getCategoriesName());
            holder.binding.amount.setText("₹ " + exphistory.getAmount());
            holder.binding.amountSet.setText(exphistory.getAmountSet());
        }
        holder.binding.amount.setOnClickListener(v -> exHistoryInterface.onClick(exphistory));
    }

    @Override
    public int getItemCount() {
        return exphistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutexpenseBinding binding;

        public ViewHolder(@NonNull LayoutexpenseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ExHistoryInterface {
        void onClick(Exphistory exphistory);
    }
}
