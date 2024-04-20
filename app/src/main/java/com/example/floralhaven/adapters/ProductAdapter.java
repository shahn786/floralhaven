package com.example.floralhaven.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floralhaven.R;
import com.example.floralhaven.crud.ProductCRUDActivity;
import com.example.floralhaven.entities.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<Products> productsList;

    public ProductAdapter(Activity activity, Context context, List<Products> productsList){
        this.activity = activity;
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() { return productsList.size(); }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Products selectedProduct = productsList.get(position);
        holder.txtID.setText(String.valueOf(selectedProduct.getProductId()));
        holder.txtName.setText(selectedProduct.getName());
        holder.txtDescription.setText(selectedProduct.getDescription());
        holder.txtPrice.setText(String.valueOf(selectedProduct.getPrice()));
        holder.txtCategory.setText(selectedProduct.getCategory());
        holder.txtImageURL.setText(selectedProduct.getImageUrl());
        holder.txtInStock.setText(String.valueOf(selectedProduct.getInStock()));
        holder.productRowLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductCRUDActivity.class);
            intent.putExtra("id", String.valueOf(selectedProduct.getProductId()));
            intent.putExtra("name", selectedProduct.getName());
            intent.putExtra("description", selectedProduct.getDescription());
            intent.putExtra("price", String.valueOf(selectedProduct.getPrice()));
            intent.putExtra("image_url", selectedProduct.getImageUrl());
            intent.putExtra("category", selectedProduct.getCategory());
            intent.putExtra("in_stock", String.valueOf(selectedProduct.getInStock()));
            activity.startActivityForResult(intent, 1);
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtName, txtDescription, txtPrice, txtCategory, txtImageURL, txtInStock;
        LinearLayout productRowLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtImageURL = itemView.findViewById(R.id.txtImageURL);
            txtInStock = itemView.findViewById(R.id.txtInStock);
            productRowLayout = itemView.findViewById(R.id.productRowLayout);
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            productRowLayout.setAnimation(translate_anim);
        }
    }
}
