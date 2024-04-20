package com.example.floralhaven;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floralhaven.adapters.OrderCartAdapter;
import com.example.floralhaven.dao.CartItemDAO;
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.CartItem;

import java.util.List;

public class OrderCartActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;
    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    RecyclerView orderCartRecyclerView;
    TextView textViewWelcome;
    OrderCartAdapter orderCartAdapter;
    private ProductsDAO productsDAO;
    private CartItemDAO cartItemDAO;
    List<CartItem> cartItemsList;
    int currentUserID = 0;
    double totalAmount = 0;

    String id, total_products, total_quantity, total_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserID = appSharedPreferences.getInt(KEY_USER_ID, 0);

        if(currentUserID == 0) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        productsDAO = appDatabase.productsDAO();
        cartItemDAO = appDatabase.cartItemDAO();

        orderCartRecyclerView = findViewById(R.id.orderCartRecyclerView);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        getAndSetIntentData();
        loadCartItemsList();

        orderCartAdapter = new OrderCartAdapter(OrderCartActivity.this,this, cartItemsList);
        orderCartRecyclerView.setAdapter(orderCartAdapter);
        orderCartRecyclerView.setLayoutManager(new LinearLayoutManager(OrderCartActivity.this));
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("total_products") && getIntent().hasExtra("total_quantity") && getIntent().hasExtra("total_amount")){
            id = getIntent().getStringExtra("id");
            total_products = getIntent().getStringExtra("total_products");
            total_quantity = getIntent().getStringExtra("total_quantity");
            total_amount = getIntent().getStringExtra("total_amount");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadCartItemsList(){
        cartItemsList = cartItemDAO.getCartItemsByCartId(Integer.parseInt(id));
        for (CartItem currentCartItem : cartItemsList) {
            totalAmount += (currentCartItem.getQuantity() * productsDAO.getProductByID(currentCartItem.getProductId()).getPrice());
        }
        textViewWelcome.setText("Total Purchases: " + totalAmount);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, OrderActivity.class));
        finish();
    }
}
