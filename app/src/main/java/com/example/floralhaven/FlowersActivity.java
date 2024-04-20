package com.example.floralhaven;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floralhaven.adapters.FlowerAdapter;
import com.example.floralhaven.crud.ProductCRUDActivity;
import com.example.floralhaven.dao.ProductsDAO;
import com.example.floralhaven.database.AppDatabase;
import com.example.floralhaven.entities.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FlowersActivity extends AppCompatActivity {

    RecyclerView flowersRecyclerView;
    ImageView emptyImageview;
    TextView noData;
    FlowerAdapter flowerAdapter;
    private ProductsDAO productsDAO;
    List<Products> productsList;

    private EditText editTextSearch;
    private Button buttonSearch, buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);

        AppDatabase appDatabase = AppDatabase.getDBInstance(this);
        productsDAO = appDatabase.productsDAO();

        flowersRecyclerView = findViewById(R.id.flowersRecyclerView);
        emptyImageview = findViewById(R.id.emptyImageview);
        noData = findViewById(R.id.noData);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonReset = findViewById(R.id.buttonReset);

        loadProductsList();
        reloadRecyclerView();

        buttonSearch.setOnClickListener(v -> {
            String searchText = editTextSearch.getText().toString().trim().toUpperCase();
            if(searchText.length() < 3) {
                Toast.makeText(this, "Enter atleast 3 characters to search", Toast.LENGTH_SHORT).show();
            }
            else {
                editTextSearch.setText("");
                List<Products> searchProductsList = new ArrayList<>();
                for (Products currentProduct : productsList) {
                    if(currentProduct.getName().contains(searchText)) { searchProductsList.add(currentProduct); }
                }
                productsList = searchProductsList;
                if(productsList.size() == 0){
                    emptyImageview.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
                else{
                    emptyImageview.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                }
                reloadRecyclerView();
            }
        });

        buttonReset.setOnClickListener(v -> {
            editTextSearch.setText("");
            loadProductsList();
            reloadRecyclerView();
        });
    }

    void reloadRecyclerView() {
        flowerAdapter = new FlowerAdapter(FlowersActivity.this,this, productsList);
        flowersRecyclerView.setAdapter(flowerAdapter);
        flowersRecyclerView.setLayoutManager(new LinearLayoutManager(FlowersActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){ recreate(); }
    }

    void loadProductsList(){
        productsList = productsDAO.getProducts();
        if(productsList.size() == 0){
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