package com.example.spendit.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendit.data.Exdetail;
import com.example.spendit.databinding.LayoutexpensedetailBinding;

import java.util.List;

public class ExDetailAdapter extends RecyclerView.Adapter<ExDetailAdapter.ViewHolder> {

    List<Exdetail> exdetailList;

    public ExDetailAdapter(List<Exdetail> exdetailList) {
        this.exdetailList = exdetailList;
    }

    @NonNull
    @Override
    public ExDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutexpensedetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExDetailAdapter.ViewHolder holder, int position) {
        Exdetail exdetail = exdetailList.get(position);
        holder.binding.Date.setText(exdetail.getAmtDate());
        holder.binding.price.setText("₹ "+exdetail.getAmount());
    }

    @Override
    public int getItemCount() {
        return exdetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutexpensedetailBinding binding;

        public ViewHolder(@NonNull LayoutexpensedetailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
