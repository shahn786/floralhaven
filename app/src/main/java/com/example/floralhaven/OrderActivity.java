package com.example.floralhaven;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.floralhaven.adapters.OrderAdapter;
import com.example.floralhaven.dao.CartDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Cart;

import java.util.List;

public class OrderActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;
    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    RecyclerView orderRecyclerView;
    ImageView emptyImageview;
    TextView noData;
    OrderAdapter orderAdapter;
    private CartDAO cartDAO;
    List<Cart> ordersList;
    int currentUserID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserID = appSharedPreferences.getInt(KEY_USER_ID, 0);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        cartDAO = appDatabase.cartDAO();

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        emptyImageview = findViewById(R.id.emptyImageview);
        noData = findViewById(R.id.noData);

        loadOrdersList();

        orderAdapter = new OrderAdapter(OrderActivity.this,this, ordersList);
        orderRecyclerView.setAdapter(orderAdapter);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadOrdersList(){
        ordersList = cartDAO.getCartsByUserId(currentUserID);
        if(ordersList.size() == 0){
            emptyImageview.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
        else{
            emptyImageview.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
