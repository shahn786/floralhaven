package com.example.floralhaven.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floralhaven.OrderCartActivity;
import com.example.floralhaven.R;
import com.example.floralhaven.entities.Cart;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    List<Cart> ordersList;

    public OrderAdapter(Activity activity, Context context, List<Cart> ordersList){
        this.activity = activity;
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() { return ordersList.size(); }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Cart selectedCart = ordersList.get(position);
        holder.txtID.setText(String.valueOf(selectedCart.getCartId()));
        holder.txtTotalQuantity.setText("Quantity: " + selectedCart.getTotalQuantity());
        holder.txtTotalProducts.setText("Products: " + selectedCart.getTotalProducts());
        holder.txtTotalAmount.setText("Total Amount: " + selectedCart.getTotalAmount());
        holder.orderRowLayout.setOnClickListener(view -> {
            holder.orderRowLayout.setSelected(!holder.orderRowLayout.isSelected());
            if(holder.orderRowLayout.isSelected()) { holder.cardView.setCardBackgroundColor(R.color.light_orange); }
            else { holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF")); }
            Intent intent = new Intent(context, OrderCartActivity.class);
            intent.putExtra("id", holder.txtID.getText());
            intent.putExtra("total_products", holder.txtTotalProducts.getText());
            intent.putExtra("total_quantity", holder.txtTotalQuantity.getText());
            intent.putExtra("total_amount", holder.txtTotalAmount.getText());
            activity.startActivityForResult(intent, 1);
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtID, txtTotalAmount, txtTotalProducts, txtTotalQuantity;
        LinearLayout orderRowLayout;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            txtID = itemView.findViewById(R.id.txtID);
            txtTotalAmount = itemView.findViewById(R.id.txtTotalAmount);
            txtTotalProducts = itemView.findViewById(R.id.txtTotalProducts);
            txtTotalQuantity = itemView.findViewById(R.id.txtTotalQuantity);
            orderRowLayout = itemView.findViewById(R.id.orderRowLayout);
            orderRowLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.translate_anim));
        }
    }
}
