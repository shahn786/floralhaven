package com.example.floralhaven;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floralhaven.adapters.CartAdapter;
import com.example.floralhaven.dao.CartDAO;
import com.example.floralhaven.dao.CartItemDAO;
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Cart;
import com.example.floralhaven.entities.CartItem;
import com.example.floralhaven.entities.Products;
import com.example.floralhaven.utilities.CommonMethods;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    SharedPreferences appSharedPreferences;
    private static final String SHARED_PREF_NAME = "app_shared_data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_IS_ADMIN = "is_admin";

    RecyclerView cartRecyclerView;
    ImageView emptyImageview;
    TextView noData, textViewWelcome;
    CartAdapter cartAdapter;
    private ProductsDAO productsDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    List<CartItem> cartItemsList;
    int currentUserID = 0;
    int totalQuantity = 0;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        appSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        currentUserID = appSharedPreferences.getInt(KEY_USER_ID, 0);

        if(currentUserID == 0) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        productsDAO = appDatabase.productsDAO();
        cartDAO = appDatabase.cartDAO();
        cartItemDAO = appDatabase.cartItemDAO();

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        emptyImageview = findViewById(R.id.emptyImageview);
        noData = findViewById(R.id.noData);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        loadCartItemsList();

        cartAdapter = new CartAdapter(CartActivity.this,this, cartItemsList);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadCartItemsList(){
        cartItemsList = CommonMethods.cartItemsList;
        if(cartItemsList.size() == 0){
            emptyImageview.setVisibility(View.VISIBLE);
            noData.setVisibility(View.VISIBLE);
            textViewWelcome.setVisibility(View.GONE);
        }
        else{
            emptyImageview.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            textViewWelcome.setVisibility(View.VISIBLE);
            for (CartItem currentCartItem : CommonMethods.cartItemsList) {
                totalAmount += (currentCartItem.getQuantity() * productsDAO.getProductByID(currentCartItem.getProductId()).getPrice());
                totalQuantity += currentCartItem.getQuantity();
            }
            textViewWelcome.setText("Total Purchases: " + totalAmount);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.emptyCart){ confirmDialog(); }
        else if(item.getItemId() == R.id.placeOrder){ placeOrder(); }
        return super.onOptionsItemSelected(item);
    }

    void placeOrder(){
        if(CommonMethods.cartItemsList.size() == 0) {
            Toast.makeText(this, "Cart empty to place order", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonMethods.cartItemsList.forEach(currentCartItem -> {
            Products searchProduct = productsDAO.getProductByID(currentCartItem.getProductId());
            totalQuantity = totalQuantity + currentCartItem.getQuantity();
            if(currentCartItem.getQuantity() > searchProduct.getInStock()) {
                Toast.makeText(this, "Order quantity more than available quantity for product '" + searchProduct.getName() + "'", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Place order?");
        builder.setMessage("Do you really want to place order for all cart items?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Cart newCart = new Cart(currentUserID, CommonMethods.cartItemsList.size(), totalQuantity, totalAmount);
            cartDAO.insert(newCart);
            int newCartID = cartDAO.getLastCartID();
            CommonMethods.cartItemsList.forEach(currentCartItem -> {
                cartItemDAO.insert(new CartItem(newCartID, currentCartItem.getProductId(), currentCartItem.getQuantity()));
                productsDAO.updateProductInStockByID(currentCartItem.getProductId(), currentCartItem.getQuantity());
            });
            Toast.makeText(this, "Order of cart items placed", Toast.LENGTH_SHORT).show();
            CommonMethods.cartItemsList = new ArrayList<>();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all cart items?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            CommonMethods.cartItemsList = new ArrayList<>();
            startActivity(new Intent(CartActivity.this, CartActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {});
        builder.create().show();
    }
}
