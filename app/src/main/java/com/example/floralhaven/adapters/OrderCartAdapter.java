package com.example.floralhaven.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.CartItem;
import com.example.floralhaven.entities.Products;

import java.util.List;

public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<CartItem> cartItemsList;
    private ProductsDAO productsDAO;

    public OrderCartAdapter(Activity activity, Context context, List<CartItem> cartItemsList){
        this.activity = activity;
        this.context = context;
        this.cartItemsList = cartItemsList;

        AppDatabase appDatabase = AppDatabase.getDBInstance(context);
        productsDAO = appDatabase.productsDAO();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() { return cartItemsList.size(); }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        CartItem selectedCartItem = cartItemsList.get(position);
        holder.txtID.setText(String.valueOf(selectedCartItem.getProductId()));
        Products selectedProduct = productsDAO.getProductByID(selectedCartItem.getProductId());
        holder.txtQuantity.setText(String.valueOf(selectedCartItem.getQuantity()));
        holder.txtName.setText(selectedProduct.getName());
        holder.txtDescription.setText(selectedProduct.getDescription());
        holder.txtPrice.setText(String.valueOf(selectedProduct.getPrice()));
        holder.txtCategory.setText(selectedProduct.getCategory());
        holder.txtImageURL.setText(selectedProduct.getImageUrl());
        holder.txtTotal.setText(String.valueOf(selectedCartItem.getQuantity() * selectedProduct.getPrice()));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtName, txtDescription, txtPrice, txtCategory, txtImageURL, txtQuantity, txtTotal;
        LinearLayout cartItemRowLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtImageURL = itemView.findViewById(R.id.txtImageURL);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            cartItemRowLayout = itemView.findViewById(R.id.cartItemRowLayout);
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            cartItemRowLayout.setAnimation(translate_anim);
        }
    }
}
