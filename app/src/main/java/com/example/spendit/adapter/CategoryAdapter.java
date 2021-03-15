package com.example.spendit.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendit.data.Category;
import com.example.spendit.databinding.LayoutcategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Category> categoryList;
    private CategoryInterface categoryInterface;

    public CategoryAdapter(List<Category> categoryList, CategoryInterface categoryInterface) {
        this.categoryList = categoryList;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutcategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        int cnt = ++position;
        holder.binding.categoryName.setText(category.getCategoriesName());
        holder.binding.serialno.setText(String.valueOf(cnt));
        holder.binding.deletebtn.setOnClickListener(v -> {
            categoryInterface.onDelete(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutcategoryBinding binding;

        public ViewHolder(@NonNull LayoutcategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface CategoryInterface {
        void onDelete(Category category);
    }
}
